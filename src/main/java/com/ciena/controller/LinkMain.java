package com.ciena.controller;

import com.ciena.controller.dao.Conexion;
import com.ciena.controller.dao.DBRecord;
import com.ciena.controller.dao.DBTable;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Util;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LinkMain {
	private static Conexion.DBConnector dataBase;
	private static DBTable tableLink;
	private static DBTable tableDicLink;

	public LinkMain() throws SQLException, ClassNotFoundException {
		dataBase = new Conexion.DBConnector();
		tableLink = dataBase.deleteTableIfExsist("exp_topology_link");
	}

	public static void main(final String[] args) throws IOException, SQLException, ClassNotFoundException {
		LinkMain linkMain = new LinkMain();
		linkMain.analyzeInformationLink("D:\\archivos\\objetociena.json", "tapi-common:context",
				"tapi-topology:topology-context", "topology", "link","node-edge-point");
	}

	public Boolean analyzeInformationLink(String fileRoute, String tapiContext, String tapiTopology,
										  String topology, String link, String nodeEdgePoint) {
		boolean analyze = false;
		boolean insertDictionaryEquipment = false;
		boolean insertMatrixLink = false;
		System.out.println("-------------Processing Information From: " + link + "------- \n");
		try {

			//AQUI ME ESTOY POSICIONANDO EN TAPITOPOLOGY
			JSONObject totalObjects = Util.parseJSONFile(fileRoute);
			JSONObject objectTopologyContext = Util.returnListPropertiesParentAssociatesChildNode(totalObjects,
					tapiContext, tapiTopology);
			//QUIERO LO QUE VIEN EN TOPOLOGY POR ESO TOPOLOGY ES EL PADRE
			JSONArray topologyArray = objectTopologyContext.getJSONArray(topology);

			//PADRE TOPOLOGY Y HIJO LINK
			List<String> columnList = Util.columnListParentArray(topologyArray, link);

			String referenceTable = "exp_topology";
			String referenceColumn = "uuid";
			String columnName = "uuid_topology";
			String column = "uuid_ownedNodePoint";
			String reference = "exp_topology_node_onep";
			String referenceUuid = "uuid";

			insertMatrixLink = insertarMatrizLink(columnList, dataBase, topologyArray,link,nodeEdgePoint,referenceTable,referenceColumn,columnName,
					column,reference,referenceUuid);
			insertDictionaryEquipment = insertDictionaryLink(columnList, dataBase,referenceTable,referenceColumn,columnName,column,
					reference,referenceUuid);
			System.out.println("-------------Process executed successfully: " + insertDictionaryEquipment  + "/ "+ insertMatrixLink);
			analyze = insertDictionaryEquipment && insertMatrixLink ? true : false;
		} catch (Exception e) {
			analyze = false;
			System.out.println("-------------Buggy process: " + e.getMessage());
			e.printStackTrace();
		}
		return analyze;
	}

	private boolean insertarMatrizLink(List<String> columnsList, Conexion.DBConnector dataBase,
									   JSONArray evaluateLink, String link, String nodeEdgePoint, String referenceTable, String referenceColumn, String columnNameUuid, String column, String reference, String referenceUuid) {
		Map<String, String> exp_Link = new HashMap<>();
		for (String objectos : columnsList) {
			// INSERTANDO DATA
			String columnName = objectos.replaceAll("-", "_").replaceAll(":", "_");
			if (columnName.equals("uuid")) {
				exp_Link.put(columnName, "varchar(50)");
			} else {
				exp_Link.put(columnName, "MEDIUMTEXT");
			}
		}
		exp_Link.put("uuid_topology", "varchar(250) , foreign key (uuid_topology) references exp_topology(uuid)");
		exp_Link.put("uuid_ownedNodePoint", "varchar(250) , foreign key (uuid_ownedNodePoint) references exp_topology_node_onep(uuid)");
		try{
			tableLink = Util.createTableMap(dataBase, "exp_topology_link", tableLink, exp_Link);
			DBRecord record = tableLink.newRecord();
			for (Object objectsLink : evaluateLink) {
				JSONObject topologyLink = (JSONObject) objectsLink;
				String columnUuid = topologyLink.get(referenceColumn).toString();
				JSONArray listLink = topologyLink.getJSONArray(link);
				for (Object objectEvaluate : listLink) {
					JSONObject objects = (JSONObject) objectEvaluate;
					//VALIDO SI TODOS TIENEN EL OBJETO NODEEDGEPOINT, Y SI LO TIENE QUE ME LO TRAIGA
					if (objects.has(nodeEdgePoint)) {
						// ME POSICIONO EN EL NODE EDGE POINT
						JSONArray node = objects.getJSONArray(nodeEdgePoint);
						// RECORRO TODO LO QUE TIENE NODE EDGE POINT
						for (Object objectNode : node) {
							record = tableLink.newRecord();
							JSONObject objectsEvaluateJson = (JSONObject) objectNode;
							record.addField(column,
									objectsEvaluateJson.get("node-edge-point-uuid").toString());
							//QUE ME TRAIGA TODAS LAS PARTES DE LINK
							insertInformation(record, objects, columnsList, columnUuid);
						}
						//SI NO LO TIENE ENTONCES QUE ME TRAIGA TODO LO QUE TIENE LINK
					}else {
						record = tableLink.newRecord();
						insertInformation(record, objects, columnsList, columnUuid);
					}
				}
			}
		}catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean insertDictionaryLink(List<String> columnsList, Conexion.DBConnector dataBase, String referenceTable, String referenceColumn, String columnNameUuid, String column, String reference, String referenceUuid) {
		String[][] dicTopology = new String[][] { { "id", "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY" },
				{ "atribute_name", "varchar(250)" },{ "flag_fk","int(11)"},{"fk_foreing_object_name","varchar(250)"},
				{"fk_foreing_object_name_atribute","varchar(250)"}};
		columnsList = columnsList.stream().distinct().collect(Collectors.toList());
		try{
			String tableName = "dic_topology_link";
			System.out.println("	-------------Creating table: " + tableName);
			tableDicLink = Util.createTableDictionary(dataBase, tableName, tableDicLink, dicTopology);
			DBRecord record = tableDicLink.newRecord();
			for (String objects : columnsList) {
				record = tableDicLink.newRecord();
				record.addField("atribute_name", objects);
				record.addField("flag_fk",0);
				tableDicLink.insert(record);
			}
			record = tableDicLink.newRecord();
			record.addField("atribute_name",columnNameUuid);
			record.addField("flag_fk", 1);
			record.addField("fk_foreing_object_name",referenceTable);
			record.addField("fk_foreing_object_name_atribute",referenceColumn);
			tableDicLink.insert(record);
			record = tableDicLink.newRecord();
			record.addField("atribute_name",column);
			record.addField("flag_fk", 1);
			record.addField("fk_foreing_object_name",reference);
			record.addField("fk_foreing_object_name_atribute",referenceUuid);
			tableDicLink.insert(record);

		} catch (Exception e) {
			System.out.println("-------------Buggy process: " + e.getMessage());
			e.printStackTrace();
		}
		return true;
	}

	//REFACTORICE LO QUE TRAE TODO LINK PARA USARLO ARRIBA
	private void insertInformation(DBRecord record, JSONObject objects, List<String> columnsList,
								   String columnUuid) {
		Map<String, Object> objectsMap = objects.toMap();
		for (Map.Entry<String, Object> entry : objectsMap.entrySet()) {
			if (columnsList.stream().filter(x -> entry.getKey().equals(x)).findFirst().isPresent()) {
				record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"), entry.getValue().toString());
			} else {
				record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"), null);
			}
		}
		try {
			record.addField("uuid_topology", columnUuid);
			tableLink.insert(record);

		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

}
