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

public class DeviceMain {
    private Conexion.DBConnector dataBase;
    private static DBTable tablaDicDevice;
    private static DBTable tablaDevice;

    public static void main(String[] args) {
        DeviceMain device = new DeviceMain();
        device.analizarInformacionDevice("D:\\archivos\\objetociena.json","tapi-common:context",
                "tapi-equipment:physical-context","device");
    }
    public Boolean analizarInformacionDevice(String rutaDeArchivo, String tapiContext, String tapiPhysical,
                                              String device) {
        boolean analizo = false;
        boolean insertoDiccionarioDevice = false;
        boolean insertoMatrizDevice = false;
        System.out.println("-------------Procesando informacion de: " + device + "------- \n");
        try {
            dataBase = new Conexion.DBConnector();

            JSONObject contenidoObjetosTotales = Util.parseJSONFile(rutaDeArchivo);
            JSONObject objetoTapiPhysical = Util.retonarListaPropiedadesAsociadasNodoHijo(contenidoObjetosTotales,
                    tapiContext, tapiPhysical);

            List<String> listaColumnas = Util.listaDeColumnasPadreObject(objetoTapiPhysical, device);
            insertoDiccionarioDevice = insertarDiccionarioDevice(listaColumnas, dataBase);
            insertoMatrizDevice = insertarMatrizDevice(listaColumnas, dataBase, objetoTapiPhysical, device);
            System.out.println("-------------Procesando ejecutado con exito: " + insertoDiccionarioDevice + "/ "
                    + insertoMatrizDevice);
            analizo = insertoDiccionarioDevice && insertoMatrizDevice ? true : false;
        } catch (Exception e) {
            analizo = false;
            System.out.println("-------------Procesando con errores: " + e.getMessage());
            e.printStackTrace();
        }

        return analizo;
    }

    private boolean insertarMatrizDevice(List<String> listaDeColumnas, Conexion.DBConnector dataBase, JSONObject evaluarADevice, String device) {
        Map<String, String> exp_Device = new HashMap<String, String>();
        for (String objectos : listaDeColumnas) {
            String nombreColumna = objectos.replaceAll("-", "_").replaceAll(":", "_");
            if (nombreColumna.equals("uuid")) {
                exp_Device.put(nombreColumna, "varchar(50) primary key");
            } else {
                exp_Device.put(nombreColumna, "MEDIUMTEXT");
            }
        }
        exp_Device.put("uuid_physical_context","varchar(250) , FOREIGN KEY (uuid_physical_context) REFERENCES exp_physical(uuid)");
        try {
            tablaDevice = Util.crearTablasGenericoMap(dataBase, "exp_physical_device", tablaDevice,
                    exp_Device);
            DBRecord record = tablaDevice.newRecord();
            JSONArray evaluarATopologyContext = evaluarADevice.getJSONArray(device);
            for (Object objectEvaluado : evaluarATopologyContext) {
                JSONObject objetosEvaluadoDeJson = (JSONObject) objectEvaluado;
                Map<String, Object> objetosMap = objetosEvaluadoDeJson.toMap();
                record = tablaDevice.newRecord();
                for (Map.Entry<String, Object> entry : objetosMap.entrySet()) {
                    if (listaDeColumnas.stream().filter(x -> entry.getKey().equals(x)).findFirst().isPresent()) {
                        record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"),
                                entry.getValue().toString());
                    } else {
                        record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"), null);
                    }
                }
                try {
                    record.addField("uuid_physical_context",evaluarADevice.getString("uuid"));
                    tablaDevice.insert(record);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean insertarDiccionarioDevice(List<String> listaDeColumnas, Conexion.DBConnector dataBase) {
        String[][] dicDevice = new String[][] { { "id", "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY" },
                { "atribute_name", "varchar(250)" } };
        listaDeColumnas = listaDeColumnas.stream().distinct().collect(Collectors.toList());
        try {
            String nombreTabla = "dic_physical_device";
            System.out.println("	-------------Creando tabla: " + nombreTabla);
            tablaDicDevice = Util.crearTablasGenerico(dataBase, nombreTabla, tablaDicDevice, dicDevice);

            DBRecord recorre = tablaDicDevice.newRecord();
            for (String objetos : listaDeColumnas) {
                recorre = tablaDicDevice.newRecord();
                recorre.addField("atribute_name", objetos);
                tablaDicDevice.insert(recorre);

            }
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }
        return true;
    }

}
