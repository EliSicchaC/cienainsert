package com.ciena.controller;

import com.ciena.controller.dao.Conexion;
import com.ciena.controller.dao.DBRecord;
import com.ciena.controller.dao.DBTable;
import org.json.JSONObject;
import util.Util;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PhysicalContextMain {
    private static Conexion.DBConnector dataBase;
    private static DBTable tablaDicPhysical;
    private static DBTable tablaExpPhysical;


    //  solo esta para probar mis metodos
    public static void main(final String[] args) throws IOException {
        PhysicalContextMain physical = new PhysicalContextMain();
        physical.analizarInformacionPhysicalContext("D:\\archivos\\objetociena.json","tapi-common:context",
                "tapi-equipment:physical-context");
    }
    public Boolean analizarInformacionPhysicalContext(String rutaDelArchivo,String tapiContext,String physicalContext){
        boolean analizo = false;
        boolean insertoDiccionarioPhysical = false;
        boolean insertoMatrizPhysical = false;
        System.out.println("-------------Procesando informacion de: " + physicalContext + "------- \n");
        try{
            dataBase = new Conexion.DBConnector();
            JSONObject contenidoObjetosTotales = Util.parseJSONFile(rutaDelArchivo);
            JSONObject objetoTopologyContext = Util.retonarListaPropiedadesAsociadasAPadre(contenidoObjetosTotales,
                    tapiContext);

            List<String> listaColumnas = Util.padreObject(objetoTopologyContext, physicalContext);
            insertoDiccionarioPhysical = insertarDiccionarioOwned(listaColumnas, dataBase);
            insertoMatrizPhysical = insertarMatrizOwnedNode(listaColumnas, dataBase, objetoTopologyContext, physicalContext);
            System.out.println("-------------Procesando ejecutado con exito: " + insertoDiccionarioPhysical  + "/ "+ insertoMatrizPhysical);
            analizo = insertoDiccionarioPhysical && insertoMatrizPhysical ? true : false;

            analizo = true;
        }catch (Exception e) {
            analizo = false;
            System.out.println("-------------Procesando con errores: " + e.getMessage());
            e.printStackTrace();
        }
        return analizo;
    }

    private boolean insertarMatrizOwnedNode(List<String> listaDeColumnas, Conexion.DBConnector dataBase,
                                            JSONObject objetoTopologyContext, String physicalContext) {
        Map<String, String> exp_Physical = new HashMap<>();
        for (String objectos : listaDeColumnas) {
            String nombreColumna = objectos.replaceAll("-", "_").replaceAll(":", "_");
            if (nombreColumna.equals("uuid")) {
                exp_Physical.put(nombreColumna, "varchar(50) primary key");
            } else {
                exp_Physical.put(nombreColumna, "MEDIUMTEXT");
            }
        }
        try{
            tablaExpPhysical = Util.crearTablasGenericoMap(dataBase, "exp_physical", tablaExpPhysical,
                    exp_Physical);
            DBRecord record = tablaExpPhysical.newRecord();
            JSONObject objetoTopology = objetoTopologyContext.getJSONObject(physicalContext);
            Map<String, Object> objetosMap = objetoTopology.toMap();
            record = tablaExpPhysical.newRecord();
            for (Map.Entry<String, Object> entry : objetosMap.entrySet()) {
                if (listaDeColumnas.stream().filter(x -> entry.getKey().equals(x)).findFirst().isPresent()) {
                    record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"), entry.getValue().toString());
                } else {
                    record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"), null);
                }
            }
            try {
                tablaExpPhysical.insert(record);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean insertarDiccionarioOwned(List<String> listaDeColumnas, Conexion.DBConnector dataBase) {
        String[][] dicPhysical = new String[][] { { "id", "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY" },
                { "atribute_name", "varchar(250)" }};
        listaDeColumnas = listaDeColumnas.stream().distinct().collect(Collectors.toList());
        try {
            String nombreTabla = "dic_physical";
            System.out.println("	-------------Creando tabla: " + nombreTabla);
            tablaDicPhysical = Util.crearTablasGenerico(dataBase, nombreTabla, tablaDicPhysical, dicPhysical);
            DBRecord recorre = tablaDicPhysical.newRecord();
            for (String objetos : listaDeColumnas) {
                recorre = tablaDicPhysical.newRecord();
                recorre.addField("atribute_name", objetos);
                tablaDicPhysical.insert(recorre);
            }
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    public void insertarPhysical(String lugarDelArchivo, String tapiContext, String physicalContext) throws SQLException, ClassNotFoundException {
        Map<String, String> exp_physical = new HashMap<>();
        List<String> listaDeColumnas = new ArrayList<>();
        JSONObject objectEvaluado = null;
        try {
            JSONObject json = Util.parseJSONFile(lugarDelArchivo);
            JSONObject identificaDevice = json.getJSONObject(tapiContext).
                    getJSONObject(physicalContext);
            objectEvaluado = identificaDevice;
            Map<String, Object> objectMap = objectEvaluado.toMap();
            for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
                listaDeColumnas.add(entry.getKey());
            }
        } catch (Exception exception) {
            System.out.println("error:: " + exception.getMessage());
        }
        String[][] dicPhysical = new String[][]{
                {
                        "id", "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY"
                },
                {
                        "atribute_name", "varchar(250)"
                }
        };
        listaDeColumnas = listaDeColumnas.stream().distinct().collect(Collectors.toList());
        for (String objectos : listaDeColumnas) {
            String nombreColumna = objectos.replaceAll("-", "_").replaceAll(":", "_");
            if (nombreColumna.equals("uuid")) {
                exp_physical.put(nombreColumna, "varchar(50) primary key ");
            } else {
                exp_physical.put(nombreColumna, "MEDIUMTEXT");
            }
        }
        dataBase = new Conexion.DBConnector();
        tablaDicPhysical = Util.crearTablasGenerico(dataBase, "dic_Physical", tablaDicPhysical, dicPhysical);
        tablaExpPhysical = Util.crearTablasGenericoMap(dataBase, "exp_physical_context", tablaExpPhysical, exp_physical);
        DBRecord recorre = tablaDicPhysical.newRecord();
        for (String objetos : listaDeColumnas) {
            recorre = tablaDicPhysical.newRecord();
            try {
                recorre.addField("atribute_name", objetos);
                tablaDicPhysical.insert(recorre);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        DBRecord record = tablaExpPhysical.newRecord();
        Map<String, Object> objetosMap = objectEvaluado.toMap();
        record = tablaExpPhysical.newRecord();
        for (Map.Entry<String, Object> entry : objetosMap.entrySet()) {
            if (listaDeColumnas.stream().filter(x -> entry.getKey().equals(x)).findFirst().isPresent()) {
                record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"), entry.getValue().toString());
            } else {
                record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"), null);
            }
        }
        try {
            tablaExpPhysical.insert(record);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}