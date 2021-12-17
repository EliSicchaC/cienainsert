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

public class ServiceInterface {
    private static Conexion.DBConnector dataBase;
    private static DBTable tablaService;
    private static DBTable tablaDicService;

    public ServiceInterface() throws SQLException, ClassNotFoundException {
        dataBase = new Conexion.DBConnector();
        tablaService = dataBase.deleteTableIfExsist("exp_service_interface");
    }
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ServiceInterface serviceInterface = new ServiceInterface();
        serviceInterface.analizarInformacionConecctivity("D:\\archivos\\nuevoJSON.json","tapi-common:context",
                "service-interface-point");
    }
    public Boolean analizarInformacionConecctivity(String rutaDeArchivo,String tapiContext,String serviceInterface){
        boolean analizo = false;
        boolean insertoDiccionarioService = false;
        boolean insertoMatrizService = false;
        System.out.println("-------------Procesando informacion de: " + serviceInterface + "------- \n");
        try{
            JSONObject contenidoObjetosJSON = Util.parseJSONFile(rutaDeArchivo);
            JSONObject objetoService = Util.retonarListaPropiedadesAsociadasAPadre(contenidoObjetosJSON,tapiContext);

            List<String> listaColumnas = Util.listaDeColumnasPadreObject(objetoService, serviceInterface);

            insertoDiccionarioService = insertoDiccionarioService(listaColumnas, dataBase);
            insertoMatrizService = insertoMatrizService(listaColumnas, dataBase, objetoService, serviceInterface);

            System.out.println("-------------Procesando ejecutado con exito: " + insertoDiccionarioService + "/ "
                    + insertoMatrizService);
            analizo = insertoDiccionarioService && insertoMatrizService ? true : false;

        } catch (Exception e) {
            analizo = false;
            System.out.println("-------------Procesando con errores: " + e.getMessage());
            e.printStackTrace();
        }
        return analizo;
    }

    private boolean insertoMatrizService(List<String> listaDeColumnas, Conexion.DBConnector dataBase, JSONObject objetoService, String serviceInterface) {
        Map<String, String> exp_device_interface = new HashMap<>();
        for (String objectos : listaDeColumnas) {
            String nombreColumna = objectos.replaceAll("-", "_").replaceAll(":", "_");
            if (nombreColumna.equals("uuid")) {
                exp_device_interface.put(nombreColumna, "varchar(50) primary key");
            } else {
                exp_device_interface.put(nombreColumna, "MEDIUMTEXT");
            }
        }
        try{
            tablaService = Util.crearTablasGenericoMap(dataBase, "exp_service_interface", tablaService,
                    exp_device_interface);
            DBRecord record = tablaService.newRecord();
            JSONArray evaluarAService = objetoService.getJSONArray(serviceInterface);
            for (Object objectEvaluado : evaluarAService) {
                JSONObject objetosEvaluadoDeJson = (JSONObject) objectEvaluado;
                Map<String, Object> objetosMap = objetosEvaluadoDeJson.toMap();
                record = tablaService.newRecord();
                for (Map.Entry<String, Object> entry : objetosMap.entrySet()) {
                    if (listaDeColumnas.stream().filter(x -> entry.getKey().equals(x)).findFirst().isPresent()) {
                        record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"),
                                entry.getValue().toString());
                    } else {
                        record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"), null);
                    }
                }
                try {
                    tablaService.insert(record);
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

    private boolean insertoDiccionarioService(List<String> listaDeColumnas, Conexion.DBConnector dataBase) {
        String[][] dicService = new String[][] { { "id", "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY" },
                { "atribute_name", "varchar(250)" } };
        listaDeColumnas = listaDeColumnas.stream().distinct().collect(Collectors.toList());
        try{
            String nombreTabla = "dic_service_interface";
            System.out.println("	-------------Creando tabla: " + nombreTabla);
            tablaDicService = Util.crearTablasGenerico(dataBase, nombreTabla, tablaDicService, dicService);

            DBRecord recorre = tablaDicService.newRecord();
            for (String objetos : listaDeColumnas) {
                recorre = tablaDicService.newRecord();
                recorre.addField("atribute_name", objetos);
                tablaDicService.insert(recorre);
            }
        }catch (SQLException | ClassNotFoundException e) {
            return false;
        }
        return true;
    }
}
