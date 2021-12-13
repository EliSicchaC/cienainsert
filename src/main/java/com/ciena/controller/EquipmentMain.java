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

public class EquipmentMain {
    private Conexion.DBConnector dataBase;
    private static DBTable tablaDicEquipment;
    private static DBTable tablaEquipment;


    public static void main(String[] args) {
        EquipmentMain equipmentMain = new EquipmentMain();
        equipmentMain.llamarAEquipment("D:\\archivos\\objetociena.json","tapi-common:context",
                "tapi-equipment:physical-context","device","equipment");

    }
    public void llamarAEquipment(String rutaDelArchivo,String tapiContext,
                                 String physicalContext,String device, String equipment){
        EquipmentMain equipmentMain = new EquipmentMain();
        try{
            equipmentMain.diccionarioEquipment(rutaDelArchivo,tapiContext,physicalContext,device,equipment);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public void diccionarioEquipment(String lugarDelArchivo,String tapiContext,
                                     String physicalContext,String device,String equipment) throws SQLException, ClassNotFoundException {
        Map<String, String> exp_equipment = new HashMap<>();
        List<String> listaDeColumnas = new ArrayList<>();
        JSONArray evaluarEquipment = null;
        try{
            JSONObject json = Util.parseJSONFile(lugarDelArchivo);
            JSONObject identifica = json.getJSONObject(tapiContext).
                    getJSONObject(physicalContext);
            evaluarEquipment = identifica.getJSONArray(device);
            JSONArray identificaElementos = identifica.getJSONArray(device);
            for(Object objetos : identificaElementos){
                JSONObject lineaDeElementos = (JSONObject) objetos;
                for(Object objetosNode : lineaDeElementos.getJSONArray(equipment)){
                    JSONObject objectEvaluado = (JSONObject) objetosNode;
                    Map<String, Object> objectMap = objectEvaluado.toMap();
                    for (Map.Entry<String, Object> entry : objectMap.entrySet()){
                        listaDeColumnas.add(entry.getKey());
                    }
                }
            }
        }catch (Exception exception) {
            System.out.println("error:: " + exception.getMessage());
        }
        String[][] dicEquipment = new String[][]{
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
                exp_equipment.put(nombreColumna, "varchar(50) PRIMARY KEY ");
            }else{
                exp_equipment.put(nombreColumna, "MEDIUMTEXT");

            }
        }
        exp_equipment.put("uuid_device","varchar(250) , FOREIGN KEY (uuid_device) REFERENCES exp_physical_device(uuid)");
        dataBase = new Conexion.DBConnector();
        tablaDicEquipment = Util.crearTablasGenerico(dataBase,"dic_equipment",tablaDicEquipment,dicEquipment);
        tablaEquipment = Util.crearTablasGenericoMap(dataBase,"exp_physical_equipment",tablaEquipment,exp_equipment);
        DBRecord recorre = tablaEquipment.newRecord();
        for(String objetos : listaDeColumnas){
            recorre = tablaDicEquipment.newRecord();
            try {
                recorre.addField("atribute_name", objetos);
                tablaEquipment.insert(recorre);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        DBRecord record = tablaEquipment.newRecord();
        for (Object objetosNode : evaluarEquipment){
            JSONObject DeviceUuid = (JSONObject) objetosNode;
            String columnaUuid = DeviceUuid.get("uuid").toString();
            JSONArray listEquipment = DeviceUuid.getJSONArray(equipment);
            for (Object objectEvaluado : listEquipment){
                JSONObject objectEvaluadoDeJson = (JSONObject) objectEvaluado;
                Map<String, Object> objectMap = objectEvaluadoDeJson.toMap();
                record = tablaEquipment.newRecord();
                for (Map.Entry<String, Object> entry : objectMap.entrySet()){
                    if (listaDeColumnas.stream().filter(x -> entry.getKey().equals(x)).findFirst().isPresent()){
                        record.addField(entry.getKey().replaceAll("-","_").replaceAll(":","_"), entry.getValue().toString());
                    } else {
                        record.addField(entry.getKey().replaceAll("-","_").replaceAll(":","_"), null);
                    }
                }
                try {
                    record.addField("uuid_device", columnaUuid);
                    tablaEquipment.insert(record);

                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
}
