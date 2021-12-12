package com.ciena.controller;

import com.ciena.controller.dao.Conexion;
import com.ciena.controller.dao.DBRecord;
import com.ciena.controller.dao.DBTable;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DeviceMain {
    private Conexion.DBConnector dataBase;
    private static DBTable tablaDictDevice;
    private static DBTable tablaDevice;


    public static void main(String[] args) {
        DeviceMain device = new DeviceMain();
        device.llamarADevice("D:\\archivos\\objetociena.json","tapi-common:context",
                "tapi-equipment:physical-context","device");
    }
    public void llamarADevice(String rutaDelArchivo,String tapiContext,String physicalContext,String device){
        DeviceMain deviceContext = new DeviceMain();
        try{
            deviceContext.diccionarioDevice(rutaDelArchivo,tapiContext,physicalContext,device);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public void diccionarioDevice(String lugarDelArchivo,String tapiContext,String physicalContext,String device) throws SQLException, ClassNotFoundException {
        Map<String, String> exp_device = new HashMap<>();
        List<String> listaDeColumnas = new ArrayList<>();
        JSONArray evaluarDevice = null;
        try{
            JSONObject json = Util.parseJSONFile(lugarDelArchivo);
            JSONObject identifica = json.getJSONObject(tapiContext).
                    getJSONObject(physicalContext);
            evaluarDevice = identifica.getJSONArray(device);
            JSONArray identificaElementos = identifica.getJSONArray(device);
            for(Object objetos : identificaElementos){
                JSONObject lineaDeElementos = (JSONObject) objetos;
                Map<String, Object> objectMap = lineaDeElementos.toMap();
                for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
                    listaDeColumnas.add(entry.getKey());
                }
            }
        } catch (Exception exception) {
            System.out.println("error:: " + exception.getMessage());
        }
        String[][] dicDevice = new String[][]{
                {
                        "id","int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY"
                },
                {
                        "atribute_name","varchar(250)"
                }
        };
        listaDeColumnas = listaDeColumnas.stream().distinct().collect(Collectors.toList());
        for (String objetos : listaDeColumnas){
            String nombreColumna = objetos.replaceAll("-","_").replaceAll(":","_");
            if(nombreColumna.equals("uuid")){
                exp_device.put(nombreColumna, "varchar(50) PRIMARY KEY ");
            }else{
                exp_device.put(nombreColumna, "MEDIUMTEXT");

            }
        }
        exp_device.put("uuid_physical_context","varchar(250) , FOREIGN KEY (uuid_physical_context) REFERENCES exp_physical_context(uuid)");
        dataBase = new Conexion.DBConnector();
        tablaDictDevice = Util.crearTablasGenerico(dataBase,"dic_device",tablaDictDevice,dicDevice);
        tablaDevice = Util.crearTablasGenericoMap(dataBase,"exp_device",tablaDevice,exp_device);
        DBRecord recorre = tablaDevice.newRecord();
        for(String objetos : listaDeColumnas){
            recorre = tablaDictDevice.newRecord();
            try {
                recorre.addField("atribute_name", objetos);
                tablaDictDevice.insert(recorre);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        DBRecord record = tablaDevice.newRecord();
        for (Object objetosDevice : evaluarDevice){
            JSONObject objetosEvaluadoDeJson = (JSONObject) objetosDevice;
            Map<String, Object> objetosMap = objetosEvaluadoDeJson.toMap();
            record = tablaDevice.newRecord();
            for (Map.Entry<String, Object> entry : objetosMap.entrySet()){
                if (listaDeColumnas.stream().filter(x -> entry.getKey().equals(x)).findFirst().isPresent()){
                    record.addField(entry.getKey().replaceAll("-","_").replaceAll(":","_"), entry.getValue().toString());
                } else {
                    record.addField(entry.getKey().replaceAll("-","_").replaceAll(":","_"), null);
                }
            }
            try {

                tablaDevice.insert(record);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }
}
