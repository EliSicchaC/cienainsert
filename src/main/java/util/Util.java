package util;

import com.ciena.controller.dao.Conexion;
import com.ciena.controller.dao.DBTable;
import com.ciena.controller.entity.Name;
import com.ciena.controller.entity.mainObjects;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Util {
	public static mainObjects getObjectsMain(String fileRoute) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		// CONFIGURAMOS LA CLASE MAPPER CON ALGUNOS PARAMETROS PARA EVITAR ERRORES DE
		// SITAXIS
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		// FAIL_ON_UNKNOWN_PROPERTIES QUE FALLE SI NO ENCUENTRA PROPIEDADES ? FALSE --
		// NO, QUE SIGA
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// ACCEPT_SINGLE_VALUE_AS_ARRAY QUE ACEPTE ARREGLOS -- TRUE, QUE SI ACEPTE
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

		// LEER EL ARCHIVO JSON PARA ELLO USAMOS LA CLASE FILE QUE ME PIDE UNA RUTA
		// DONDE ESTA EL ARCHIVO
		File archivoJson = new File(fileRoute);

		// PARA PODER TRANSFORMAR EL ARCHIVO A OBJETOS USAMOS EL READVALUE DE MAPPER,
		// QUE ME PIDE EL ARCHIVO Y LA CLASE QUE ME SERVIRA PARA ALMACENAR LOS DATOS DEL
		// ARCHIVO
		mainObjects principal = mapper.readValue(archivoJson, mainObjects.class);
		return principal;
	}

	// REUTILIZANDO NAME
	public static String generateNames(List<Name> listNames) {
		String nameStringValue = "";
		for (Name name : listNames) {
			nameStringValue = nameStringValue + name.getValue_name() + " : " + name.getValue() + " \n";
		}
		return nameStringValue;
	}

	public static DBTable createTableDictionary(Conexion.DBConnector dataBase, String tableName, DBTable table,
												String[][] fields) throws SQLException, ClassNotFoundException {
		table = dataBase.deleteTableIfExsist(tableName);
		table.createTable(fields);
		return table;
	}

	public static DBTable createTableMap(Conexion.DBConnector dataBase, String tableName, DBTable table,
										 Map<String, String> fields) throws SQLException, ClassNotFoundException {
		table = dataBase.deleteTableIfExsist(tableName);
		table.createTableMap(fields);
		return table;
	}

	public static JSONObject parseJSONFile(String filename) throws IOException {
		String content = new String(Files.readAllBytes(Paths.get(filename)));
		return new JSONObject(content);
	}
	public static JSONObject returnListPropertiesParentAssociates(JSONObject archiveJson, String parentNode){
		JSONObject properties = null;
		try {
			properties = archiveJson.getJSONObject(parentNode);

		} catch (Exception exception) {
			System.out.println("error:: " + exception.getMessage());
		}
		return properties;
	}

	public static JSONObject returnListPropertiesParentAssociatesChildNode(JSONObject archiveJson, String parentNode,
																		   String childNodeFirstSegment) {
		JSONObject properties = null;
		try {
			properties = archiveJson.getJSONObject(parentNode).getJSONObject(childNodeFirstSegment);

		} catch (Exception exception) {
			System.out.println("error:: " + exception.getMessage());
		}
		return properties;
	}

	public static List<String> columnListParentArray(JSONArray nodeWithColumns, String node) {
		List<String> columnList = new ArrayList<>();
		for (Object objectsNode : nodeWithColumns) {
			JSONObject ownedNode = (JSONObject) objectsNode;
			JSONArray listEdgePoint = ownedNode.getJSONArray(node);
			for (Object objectEvaluated : listEdgePoint) {
				JSONObject objectEvaluatedJson = (JSONObject) objectEvaluated;
				Map<String, Object> objectMap = objectEvaluatedJson.toMap();
				for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
					columnList.add(entry.getKey());
				}
			}
		}
		return columnList;
	}
	public static List<String> columnsNotFound(JSONArray nodeWithColumns, String node) {
		List<String> columnList = new ArrayList<>();
		for (Object objectNode : nodeWithColumns) {
			JSONObject ownedNode = (JSONObject) objectNode;
			if(ownedNode.has(node)){
				JSONArray listEdgePoint = ownedNode.getJSONArray(node);
				for (Object objectEvaluated : listEdgePoint) {
					JSONObject objectEvaluatedJson = (JSONObject) objectEvaluated;
					Map<String, Object> objectMap = objectEvaluatedJson.toMap();
					for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
						columnList.add(entry.getKey());
					}
				}
			}
		}
		return columnList;
	}

	public static List<String> columnListParentObject(JSONObject nodeWithColumns, String node) {
		List<String> columnList = new ArrayList<>();
		JSONArray listEdgePoint = nodeWithColumns.getJSONArray(node);
		for (Object objectEvaluated : listEdgePoint) {
			JSONObject objectEvaluatedJson = (JSONObject) objectEvaluated;
			Map<String, Object> objectMap = objectEvaluatedJson.toMap();
			for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
				columnList.add(entry.getKey());
			}
		}
		return columnList;
	}
	public static List<String> parentObject(JSONObject nodeWithColumns, String node) {
		List<String> columnList = new ArrayList<>();
		JSONObject listEdgePoint = nodeWithColumns.getJSONObject(node);
		JSONObject objectEvaluatedJson = listEdgePoint;
		Map<String, Object> objectMap = objectEvaluatedJson.toMap();
		for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
			columnList.add(entry.getKey());
		}
		return columnList;
	}
}