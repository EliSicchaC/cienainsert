package com.ciena.controller;

import com.ciena.controller.dao.Conexion;
import com.ciena.controller.dao.DBRecord;
import com.ciena.controller.dao.DBTable;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Util;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConnectivityMain {
    private static Conexion.DBConnector dataBase;
    private static DBTable tablaConectivity;
    private static DBTable tablaDicConectivity;

    public ConnectivityMain() throws SQLException, ClassNotFoundException {
        dataBase = new Conexion.DBConnector();
        tablaConectivity = dataBase.deleteTableIfExsist("exp_connectivity_service");
    }
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ConnectivityMain connectivityMain = new ConnectivityMain();
        connectivityMain.analizarInformacionConecctivity("D:\\archivos\\nuevoJSON.json","tapi-common:context",
                "connectivity-context","connectivity-service");
    }
    public Boolean analizarInformacionConecctivity(String rutaDeArchivo,String tapiContext,String connectivityContext,
                                                   String connectivityService){
        boolean analizo = false;
        boolean insertoDiccionarioConectivity = false;
        boolean insertoMatrizConecctivity = false;
        System.out.println("-------------Procesando informacion de: " + connectivityService + "------- \n");
        try{
            JSONObject contenidoObjetosJSON = Util.parseJSONFile(rutaDeArchivo);
            JSONObject objetoConectivityContext = Util.retonarListaPropiedadesAsociadasNodoHijo(contenidoObjetosJSON,
                    tapiContext,connectivityContext);

            List<String> listaColumnas = Util.listaDeColumnasPadreObject(objetoConectivityContext, connectivityService);

            insertoDiccionarioConectivity = insertoDiccionarioConectivity(listaColumnas, dataBase);
            insertoMatrizConecctivity = insertoMatrizConecctivity(listaColumnas, dataBase, objetoConectivityContext, connectivityService);

            System.out.println("-------------Procesando ejecutado con exito: " + insertoDiccionarioConectivity + "/ "
                    + insertoMatrizConecctivity);
            analizo = insertoDiccionarioConectivity && insertoMatrizConecctivity ? true : false;
        } catch (Exception e) {
            analizo = false;
            System.out.println("-------------Procesando con errores: " + e.getMessage());
            e.printStackTrace();
        }
        return analizo;
    }

    private boolean insertoMatrizConecctivity(List<String> listaDeColumnas, Conexion.DBConnector dataBase, JSONObject objetoConectivityContext, String connectivityService) {
        Map<String, String> exp_connectivity = new HashMap<>();
        for (String objectos : listaDeColumnas) {
            String nombreColumna = objectos.replaceAll("-", "_").replaceAll(":", "_");
            if (nombreColumna.equals("uuid")) {
                exp_connectivity.put(nombreColumna, "varchar(50) primary key");
            } else {
                exp_connectivity.put(nombreColumna, "MEDIUMTEXT");
            }
        }
        try{
            tablaConectivity = Util.crearTablasGenericoMap(dataBase, "exp_connectivity_service", tablaConectivity,
                    exp_connectivity);
            DBRecord record = tablaConectivity.newRecord();
            JSONArray evaluarAConnectivityContext = objetoConectivityContext.getJSONArray(connectivityService);
            for (Object objectEvaluado : evaluarAConnectivityContext) {
                JSONObject objetosEvaluadoDeJson = (JSONObject) objectEvaluado;
                Map<String, Object> objetosMap = objetosEvaluadoDeJson.toMap();
                record = tablaConectivity.newRecord();
                for (Map.Entry<String, Object> entry : objetosMap.entrySet()) {
                    if (listaDeColumnas.stream().filter(x -> entry.getKey().equals(x)).findFirst().isPresent()) {
                        record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"),
                                entry.getValue().toString());
                    } else {
                        record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"), null);
                    }
                }
                try {
                    tablaConectivity.insert(record);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }

        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean insertoDiccionarioConectivity(List<String> listaDeColumnas, Conexion.DBConnector dataBase) {
        String[][] dicConectivity = new String[][] { { "id", "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY" },
                { "atribute_name", "varchar(250)" } };
        listaDeColumnas = listaDeColumnas.stream().distinct().collect(Collectors.toList());
        try {
            String nombreTabla = "dic_connectivity_service";
            System.out.println("	-------------Creando tabla: " + nombreTabla);
            tablaDicConectivity = Util.crearTablasGenerico(dataBase, nombreTabla, tablaDicConectivity, dicConectivity);

            DBRecord recorre = tablaDicConectivity.newRecord();
            for (String objetos : listaDeColumnas) {
                recorre = tablaDicConectivity.newRecord();
                recorre.addField("atribute_name", objetos);
                tablaDicConectivity.insert(recorre);
            }
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }
        return true;
    }
}
