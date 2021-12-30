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

public class EquipmentMain {
    private Conexion.DBConnector dataBase;
    private static DBTable tableDicEquipment;
    private static DBTable tableEquipment;

    public EquipmentMain() throws SQLException, ClassNotFoundException {
        dataBase = new Conexion.DBConnector();
        tableEquipment = dataBase.deleteTableIfExsist("exp_physical_device_equipment");
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        EquipmentMain equipmentMain = new EquipmentMain();
        equipmentMain.analyzeInformationEquipment("D:\\archivos\\objetociena.json","tapi-common:context",
                "tapi-equipment:physical-context","device","equipment");

    }
    public Boolean analyzeInformationEquipment(String fileRoute, String tapiContext, String tapiPhysical,
                                               String device, String equipment) {
        boolean analyze = false;
        boolean insertDictionaryEquipment = false;
        boolean insertMatrixEquipment = false;
        System.out.println("-------------Processing Information From: " + equipment + "------- \n");
        try {

            JSONObject totalObjects = Util.parseJSONFile(fileRoute);
            JSONObject objectTapiPhysical = Util.returnListPropertiesParentAssociatesChildNode(totalObjects,
                    tapiContext, tapiPhysical);
            JSONArray deviceArray = objectTapiPhysical.getJSONArray(device);
            JSONObject nodeContext = (JSONObject) deviceArray.get(0);
            JSONArray listEquipment = nodeContext.getJSONArray(equipment);

            List<String> columnList = Util.columnListParentArray(deviceArray, equipment);

            String referenceTable = "exp_physical_device";
            String referenceColumn = "uuid";
            String columnName = "uuid_device";

            insertMatrixEquipment = insertarMatrizEquipment(columnList, dataBase, deviceArray,equipment,referenceTable,referenceColumn,columnName);
            insertDictionaryEquipment = insertDictionaryEquipment(columnList, dataBase,referenceTable,referenceColumn,columnName);
            System.out.println("-------------Process executed successfully: " + insertDictionaryEquipment  + "/ "+ insertMatrixEquipment);
            analyze = insertDictionaryEquipment && insertMatrixEquipment ? true : false;
        } catch (Exception e) {
            analyze = false;
            System.out.println("-------------Buggy process: " + e.getMessage());
            e.printStackTrace();
        }
        return analyze;
    }

    private boolean insertarMatrizEquipment(List<String> columnsList, Conexion.DBConnector dataBase, JSONArray deviceArray, String equipment, String referenceTable, String referenceColumn, String columnNameUuid) {
        Map<String, String> exp_equipment = new HashMap<>();
        for (String objects : columnsList) {
            String columnName = objects.replaceAll("-", "_").replaceAll(":", "_");
            if (columnName.equals("uuid")) {
                exp_equipment.put(columnName, "varchar(50) primary key");
            } else {
                exp_equipment.put(columnName, "MEDIUMTEXT");
            }
        }
        try{
            exp_equipment.put(columnNameUuid,"varchar(250) , FOREIGN KEY (uuid_device) REFERENCES exp_physical_device(uuid)");
            tableEquipment = Util.createTableMap(dataBase,"exp_physical_device_equipment", tableEquipment,exp_equipment);
            DBRecord record = tableEquipment.newRecord();
            for (Object objectsDevice : deviceArray){
                JSONObject device = (JSONObject) objectsDevice;
                String columnUuid = device.get(referenceColumn).toString();
                JSONArray listEquipment = device.getJSONArray(equipment);
                for (Object objectEvaluate : listEquipment){
                    JSONObject objectEvaluateJson = (JSONObject) objectEvaluate;
                    Map<String, Object> objectMap = objectEvaluateJson.toMap();
                    record = tableEquipment.newRecord();
                    for (Map.Entry<String, Object> entry : objectMap.entrySet()){
                        if (columnsList.stream().filter(x -> entry.getKey().equals(x)).findFirst().isPresent()){
                            record.addField(entry.getKey().replaceAll("-","_").replaceAll(":","_"), entry.getValue().toString());
                        } else {
                            record.addField(entry.getKey().replaceAll("-","_").replaceAll(":","_"), null);
                        }
                    }
                    try {
                        record.addField(columnNameUuid, columnUuid);
                        tableEquipment.insert(record);

                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean insertDictionaryEquipment(List<String> columnList, Conexion.DBConnector dataBase, String referenceTable, String referenceColumn, String columnNameUuid) {
        String[][] dicDevice = new String[][] { { "id", "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY" },
                { "atribute_name", "varchar(250)" },{ "flag_fk","int(11)"},{"fk_foreign_object_name","varchar(250)"},
                {"fk_foreign_object_name_atribute","varchar(250)"} };
        columnList = columnList.stream().distinct().collect(Collectors.toList());
        try {
            String tableName = "dic_physical_device_equipment";
            System.out.println("	-------------Creating table: " + tableName);
            tableDicEquipment = Util.createTableDictionary(dataBase, tableName, tableDicEquipment, dicDevice);

            DBRecord record = tableDicEquipment.newRecord();
            for (String objetos : columnList) {
                record = tableDicEquipment.newRecord();
                record.addField("atribute_name", objetos);
                record.addField("flag_fk",0);
                tableDicEquipment.insert(record);

            }
            record = tableDicEquipment.newRecord();
            record.addField("atribute_name",columnNameUuid);
            record.addField("flag_fk", 1);
            record.addField("fk_foreign_object_name",referenceTable);
            record.addField("fk_foreign_object_name_atribute",referenceColumn);
            tableDicEquipment.insert(record);
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }
        return true;
    }

}
