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
    private static DBTable tableConectivity;
    private static DBTable tableDicConectivity;

    public ConnectivityMain() throws SQLException, ClassNotFoundException {
        dataBase = new Conexion.DBConnector();
        tableConectivity = dataBase.deleteTableIfExsist("exp_connectivity_service");
    }
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ConnectivityMain connectivityMain = new ConnectivityMain();
        connectivityMain.analyzeInformationConecctivity("D:\\archivos\\nuevoJSON.json","tapi-common:context",
                "connectivity-context","connectivity-service");
    }
    public Boolean analyzeInformationConecctivity(String fileRoute, String tapiContext, String connectivityContext,
                                                  String connectivityService){
        boolean analyze = false;
        boolean insertDictionaryConectivity = false;
        boolean insertMatrixConecctivity = false;
        System.out.println("-------------Processing Information From: " + connectivityService + "------- \n");
        try{
            JSONObject totalObjectsJSON = Util.parseJSONFile(fileRoute);
            JSONObject objectsConectivityContext = Util.returnListPropertiesParentAssociatesChildNode(totalObjectsJSON,
                    tapiContext,connectivityContext);

            List<String> columnList = Util.columnListParentObject(objectsConectivityContext, connectivityService);

            insertDictionaryConectivity = insertDictionaryConectivity(columnList, dataBase);
            insertMatrixConecctivity = insertoMatrizConecctivity(columnList, dataBase, objectsConectivityContext, connectivityService);

            System.out.println("-------------Process executed successfully: " + insertDictionaryConectivity + "/ "
                    + insertMatrixConecctivity);
            analyze = insertDictionaryConectivity && insertMatrixConecctivity ? true : false;
        } catch (Exception e) {
            analyze = false;
            System.out.println("-------------Buggy process: " + e.getMessage());
            e.printStackTrace();
        }
        return analyze;
    }

    private boolean insertoMatrizConecctivity(List<String> columnList, Conexion.DBConnector dataBase, JSONObject objectConectivityContext, String connectivityService) {
        Map<String, String> exp_connectivity = new HashMap<>();
        for (String objects : columnList) {
            String columnName = objects.replaceAll("-", "_").replaceAll(":", "_");
            if (columnName.equals("uuid")) {
                exp_connectivity.put(columnName, "varchar(50) primary key");
            } else {
                exp_connectivity.put(columnName, "MEDIUMTEXT");
            }
        }
        try{
            tableConectivity = Util.createTableMap(dataBase, "exp_connectivity_service", tableConectivity,
                    exp_connectivity);
            DBRecord record = tableConectivity.newRecord();
            JSONArray evaluateAConnectivityContext = objectConectivityContext.getJSONArray(connectivityService);
            for (Object objectEvaluated : evaluateAConnectivityContext) {
                JSONObject objectEvaluatedJson = (JSONObject) objectEvaluated;
                Map<String, Object> objectsMap = objectEvaluatedJson.toMap();
                record = tableConectivity.newRecord();
                for (Map.Entry<String, Object> entry : objectsMap.entrySet()) {
                    if (columnList.stream().filter(x -> entry.getKey().equals(x)).findFirst().isPresent()) {
                        record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"),
                                entry.getValue().toString());
                    } else {
                        record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"), null);
                    }
                }
                try {
                    tableConectivity.insert(record);
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

    private boolean insertDictionaryConectivity(List<String> columnList, Conexion.DBConnector dataBase) {
        String[][] dicConectivity = new String[][] { { "id", "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY" },
                { "atribute_name", "varchar(250)" } };
        columnList = columnList.stream().distinct().collect(Collectors.toList());
        try {
            String tablaName = "dic_connectivity_service";
            System.out.println("	-------------Creating table: " + tablaName);
            tableDicConectivity = Util.createTableDictionary(dataBase, tablaName, tableDicConectivity, dicConectivity);

            DBRecord record = tableDicConectivity.newRecord();
            for (String objects : columnList) {
                record = tableDicConectivity.newRecord();
                record.addField("atribute_name", objects);
                tableDicConectivity.insert(record);
            }
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }
        return true;
    }
}
