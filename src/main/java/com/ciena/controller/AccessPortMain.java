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

public class AccessPortMain {
    private Conexion.DBConnector dataBase;
    private static DBTable tableDicAccessPort;
    private static DBTable tableAccessPort;

    public AccessPortMain() throws SQLException, ClassNotFoundException {
        dataBase = new Conexion.DBConnector();
        tableAccessPort = dataBase.deleteTableIfExsist("exp_physical_device_access_port");
    }
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        AccessPortMain main = new AccessPortMain();
        main.analyzeInformationAccessPort("D:\\archivos\\objetociena.json","tapi-common:context",
                "tapi-equipment:physical-context","device","access-port");
    }
    public Boolean analyzeInformationAccessPort(String fileRoute, String tapiContext, String tapiTopology,
                                                String device, String accessPort) {
        boolean analyze = false;
        boolean insertDictionaryAccessPort = false;
        boolean insertMatrixAccessPort = false;
        System.out.println("-------------Processing Information From: " + accessPort + "------- \n");
        try {

            JSONObject totalObjects = Util.parseJSONFile(fileRoute);
            JSONObject objectsTopologyContext = Util.returnListPropertiesParentAssociatesChildNode(totalObjects,
                    tapiContext, tapiTopology);
            JSONArray deviceArray = objectsTopologyContext.getJSONArray(device);
            JSONObject accessContext = (JSONObject) deviceArray.get(0);
            if(accessContext.has(accessPort)){
                JSONArray listEquipment = accessContext.getJSONArray(accessPort);
            }

            List<String> columnList = Util.columnsNotFound(deviceArray, accessPort);

            String referenceTable = "exp_physical_device_access_port";
            String referenceColumn = "uuid";
            String columnName = "uuid_device";

            insertMatrixAccessPort = insertarMatrizAccessPort(columnList, dataBase, deviceArray,accessPort,referenceTable,referenceColumn,columnName);
            insertDictionaryAccessPort = insertDictionaryAccessPort(columnList, dataBase,referenceTable,referenceColumn,columnName);
            System.out.println("-------------Process executed successfully: " + insertDictionaryAccessPort  + "/ "+ insertMatrixAccessPort);
            analyze = insertDictionaryAccessPort && insertMatrixAccessPort ? true : false;
        } catch (Exception e) {
            analyze = false;
            System.out.println("-------------Buggy process: " + e.getMessage());
            e.printStackTrace();
        }
        return analyze;
    }

    private boolean insertarMatrizAccessPort(List<String> columnList, Conexion.DBConnector dataBase, JSONArray deviceArray, String accessPort, String referenceTable, String referenceColumn, String columnNameUuid) {
        Map<String, String> exp_accessport = new HashMap<>();
        for (String objects : columnList) {
            String columnName = objects.replaceAll("-", "_").replaceAll(":", "_");
            if (columnName.equals("uuid")) {
                exp_accessport.put(columnName, "varchar(50) primary key");
            } else {
                exp_accessport.put(columnName, "MEDIUMTEXT");
            }
        }
        try{
            exp_accessport.put(columnNameUuid,"varchar(250) , foreign key (uuid_device) references exp_physical_device(uuid)");
            tableAccessPort = Util.createTableMap(dataBase,"exp_physical_device_access_port", tableAccessPort,exp_accessport);
            DBRecord record = tableAccessPort.newRecord();
            for (Object objectsNode : deviceArray){
                JSONObject deviceUuid = (JSONObject) objectsNode;
                String columnUuid = deviceUuid.get(referenceColumn).toString();
                if(deviceUuid.has(accessPort)){
                    JSONArray listAccesPort = deviceUuid.getJSONArray(accessPort);
                    for (Object objectEvaluated : listAccesPort){
                        JSONObject objectColumn = (JSONObject) objectEvaluated;
                        record = tableAccessPort.newRecord();
                        insertInformation(record, objectColumn, columnList, columnUuid);
                    }
                }
            }
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean insertDictionaryAccessPort(List<String> columnList, Conexion.DBConnector dataBase, String referenceTable, String referenceColumn, String columnNameUuid) {
        String[][] dicAccessPort = new String[][] { { "id", "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY" },
                { "atribute_name", "varchar(250)" },{ "flag_fk","int(11)"},{"fk_foreign_object_name","varchar(250)"},
                {"fk_foreign_object_name_atribute","varchar(250)"} };
        columnList = columnList.stream().distinct().collect(Collectors.toList());
        try {
            String tableName = "dic_physical_device_access_port";
            System.out.println("	-------------Creating table: " + tableName);
            tableDicAccessPort = Util.createTableDictionary(dataBase, tableName, tableDicAccessPort, dicAccessPort);

            DBRecord record = tableDicAccessPort.newRecord();
            for (String objects : columnList) {
                record = tableDicAccessPort.newRecord();
                record.addField("atribute_name", objects);
                record.addField("flag_fk",0);
                tableDicAccessPort.insert(record);
            }
            record = tableDicAccessPort.newRecord();
            record.addField("atribute_name",columnNameUuid);
            record.addField("flag_fk", 1);
            record.addField("fk_foreign_object_name",referenceTable);
            record.addField("fk_foreign_object_name_atribute",referenceColumn);
            tableDicAccessPort.insert(record);
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    private void insertInformation(DBRecord record, JSONObject objects, List<String>columnList,
                                   String columnUuid){
        Map<String, Object> objectMap = objects.toMap();
        record = tableAccessPort.newRecord();
        for (Map.Entry<String, Object> entry : objectMap.entrySet()){
            if (columnList.stream().filter(x -> entry.getKey().equals(x)).findFirst().isPresent()){
                record.addField(entry.getKey().replaceAll("-","_").replaceAll(":","_"), entry.getValue().toString());
            } else {
                record.addField(entry.getKey().replaceAll("-","_").replaceAll(":","_"), null);
            }
        }
        try {
            record.addField("uuid_device", columnUuid);
            tableAccessPort.insert(record);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
