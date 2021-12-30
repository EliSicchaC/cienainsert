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
    private static DBTable tableDicDevice;
    private static DBTable tableDevice;

    public DeviceMain() throws SQLException, ClassNotFoundException {
        dataBase = new Conexion.DBConnector();
        tableDevice = dataBase.deleteTableIfExsist("exp_physical_device");
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DeviceMain device = new DeviceMain();
        device.analyzeInformationDevice("D:\\archivos\\objetociena.json","tapi-common:context",
                "tapi-equipment:physical-context","device");
    }
    public Boolean analyzeInformationDevice(String fileRoute, String tapiContext, String tapiPhysical,
                                            String device) {
        boolean analyze = false;
        boolean insertDictionaryDevice = false;
        boolean insertMatrixDevice = false;
        System.out.println("-------------Processing Information From: " + device + "------- \n");
        try {

            JSONObject totalObjects = Util.parseJSONFile(fileRoute);
            JSONObject objectTapiPhysical = Util.returnListPropertiesParentAssociatesChildNode(totalObjects,
                    tapiContext, tapiPhysical);

            List<String> columnList = Util.columnListParentObject(objectTapiPhysical, device);

            String referenceTable = "exp_physical";
            String referenceColumn = "uuid";
            String columnName = "uuid_physical_context";

            insertMatrixDevice = insertarMatrizDevice(columnList, dataBase, objectTapiPhysical, device,referenceTable,referenceColumn,
                    columnName);
            insertDictionaryDevice = insertDictionaryDevice(columnList, dataBase,referenceTable,referenceColumn,
                    columnName);
            System.out.println("-------------Process executed successfully: " + insertDictionaryDevice + "/ "
                    + insertMatrixDevice);
            analyze = insertDictionaryDevice && insertMatrixDevice ? true : false;
        } catch (Exception e) {
            analyze = false;
            System.out.println("-------------Buggy process: " + e.getMessage());
            e.printStackTrace();
        }

        return analyze;
    }

    private boolean insertarMatrizDevice(List<String> columnsList, Conexion.DBConnector dataBase, JSONObject evaluateDevice, String device, String referenceTable, String referenceColumn, String columnNameUuid) {
        Map<String, String> exp_Device = new HashMap<String, String>();
        for (String objects : columnsList) {
            String columnName = objects.replaceAll("-", "_").replaceAll(":", "_");
            if (columnName.equals("uuid")) {
                exp_Device.put(columnName, "varchar(50) primary key");
            } else {
                exp_Device.put(columnName, "MEDIUMTEXT");
            }
        }
        exp_Device.put(columnNameUuid,"varchar(250) , FOREIGN KEY (uuid_physical_context) REFERENCES exp_physical(uuid)");
        try {
            tableDevice = Util.createTableMap(dataBase, "exp_physical_device", tableDevice,
                    exp_Device);
            DBRecord record = tableDevice.newRecord();
            JSONArray evaluateTopologyContext = evaluateDevice.getJSONArray(device);
            for (Object objectEvaluate : evaluateTopologyContext) {
                JSONObject objectsEvaluateJson = (JSONObject) objectEvaluate;
                Map<String, Object> objectsMap = objectsEvaluateJson.toMap();
                record = tableDevice.newRecord();
                for (Map.Entry<String, Object> entry : objectsMap.entrySet()) {
                    if (columnsList.stream().filter(x -> entry.getKey().equals(x)).findFirst().isPresent()) {
                        record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"),
                                entry.getValue().toString());
                    } else {
                        record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"), null);
                    }
                }
                try {
                    record.addField(columnNameUuid,evaluateDevice.getString(referenceColumn));
                    tableDevice.insert(record);
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

    private boolean insertDictionaryDevice(List<String> columnList, Conexion.DBConnector dataBase, String referenceTable, String referenceColumn, String columnNameUuid) {
        String[][] dicDevice = new String[][] { { "id", "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY" },
                { "atribute_name", "varchar(250)" },{ "flag_fk","int(11)"},{"fk_foreign_object_name","varchar(250)"},
                {"fk_foreign_object_name_atribute","varchar(250)"}};
        columnList = columnList.stream().distinct().collect(Collectors.toList());
        try {
            String tableName = "dic_physical_device";
            System.out.println("	-------------Creating table: " + tableName);
            tableDicDevice = Util.createTableDictionary(dataBase, tableName, tableDicDevice, dicDevice);

            DBRecord record = tableDicDevice.newRecord();
            for (String objects : columnList) {
                record = tableDicDevice.newRecord();
                record.addField("atribute_name", objects);
                record.addField("flag_fk",0);
                tableDicDevice.insert(record);

            }
            record = tableDicDevice.newRecord();
            record.addField("atribute_name",columnNameUuid);
            record.addField("flag_fk", 1);
            record.addField("fk_foreign_object_name",referenceTable);
            record.addField("fk_foreign_object_name_atribute",referenceColumn);
            tableDicDevice.insert(record);
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }
        return true;
    }

}
