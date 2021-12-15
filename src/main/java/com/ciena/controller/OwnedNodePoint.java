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

public class OwnedNodePoint {
	private static Conexion.DBConnector dataBase;
	private static DBTable tablaOwnedNode;
	private static DBTable tablaDiccionario;

	// Se crea un metodo main, para poder ejecutarlo y tambien conectarnos al json
	public static void main(final String[] args) throws IOException {
		OwnedNodePoint mainTopology = new OwnedNodePoint();
		mainTopology.analizarInformacionOwned("D:\\archivos\\objetociena.json", "tapi-common:context",
				"tapi-topology:topology-context", "topology", "node", "owned-node-edge-point");
	}

	public Boolean analizarInformacionOwned(String rutaDeArchivo, String tapiContext, String tapiTopology,
                                            String topology, String node, String ownedPoint) {
		boolean analizo = false;
		boolean insertoDiccionarioOwned = false;
		boolean insertoMatrizOwned = false;
		System.out.println("-------------Procesando informacion de: " + ownedPoint + "------- \n");
		try {
			dataBase = new Conexion.DBConnector();
			JSONObject contenidoObjetosTotales = Util.parseJSONFile(rutaDeArchivo);
			JSONObject objetoTopologyContext = Util.retonarListaPropiedadesAsociadasNodoHijo(contenidoObjetosTotales,
					tapiContext, tapiTopology);
			JSONArray topologyArray = objetoTopologyContext.getJSONArray(topology);
			JSONObject ownedNode = (JSONObject) topologyArray.get(0);
			JSONArray listaDeNode = ownedNode.getJSONArray(node);

			List<String> listaColumnas = Util.listaDeColumnasPadreArray(listaDeNode, ownedPoint);
			insertoDiccionarioOwned = insertarDiccionarioOwned(listaColumnas, dataBase);
			insertoMatrizOwned = insertarMatrizOwnedNode(listaColumnas, dataBase, listaDeNode, ownedPoint);
			System.out.println("-------------Procesando ejecutado con exito: " + insertoDiccionarioOwned  + "/ "+ insertoMatrizOwned);
			analizo = insertoDiccionarioOwned && insertoMatrizOwned ? true : false;
		} catch (Exception e) {
			analizo = false;
			System.out.println("-------------Procesando con errores: " + e.getMessage());
			e.printStackTrace();
		}

		return analizo;
	}

	private Boolean insertarDiccionarioOwned(List<String> listaDeColumnas, Conexion.DBConnector dataBase) {
		String[][] dicOwnedpoint = new String[][] { { "id", "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY" },
				{ "atribute_name", "varchar(250)" }};
		listaDeColumnas = listaDeColumnas.stream().distinct().collect(Collectors.toList());
		try {
			String nombreTabla = "dic_topology_onep";
			System.out.println("	-------------Creando tabla: " + nombreTabla);
			tablaDiccionario = Util.crearTablasGenerico(dataBase, nombreTabla, tablaDiccionario, dicOwnedpoint);
			DBRecord recorre = tablaDiccionario.newRecord();
			for (String objetos : listaDeColumnas) {
				recorre = tablaDiccionario.newRecord();
				recorre.addField("atribute_name", objetos);
				tablaDiccionario.insert(recorre);

			}
		} catch (SQLException | ClassNotFoundException e) {
			return false;
		}
		return true;
	}

	private Boolean insertarMatrizOwnedNode(List<String> listaDeColumnas, Conexion.DBConnector dataBase,
			JSONArray nodoEvaluar, String nodoHijoInsertarData) {
		Map<String, String> tablaOwned = new HashMap<String, String>();
		for (String objectos : listaDeColumnas) {
			// INSERTANDO DATA
			String nombreColumna = objectos.replaceAll("-", "_").replaceAll(":", "_");
			if (nombreColumna.equals("uuid")) {
				tablaOwned.put(nombreColumna, "varchar(50) primary key");
			} else {
				tablaOwned.put(nombreColumna, "MEDIUMTEXT");
			}
		}
		tablaOwned.put("uuid_node", "varchar(250) , FOREIGN KEY  (uuid_node) REFERENCES exp_topology_node(uuid)");
		try {
			tablaOwnedNode = Util.crearTablasGenericoMap(dataBase, "exp_topology_owned_node_edgepoint", tablaOwnedNode,
					tablaOwned);
			DBRecord record = tablaOwnedNode.newRecord();
			for (Object objetosNode : nodoEvaluar) {
				// JSONOBJECT ES PARA TRAER UN OBJETO DEL JSON
				JSONObject ownedNode = (JSONObject) objetosNode;
				// QUIERO ATRAER EL UUID DE OWNEDNODE PARA LUEGO IMPLEMENTARLO EN LA BD
				String columnaUuid = ownedNode.get("uuid").toString();
				JSONArray listEdgePoint = ownedNode.getJSONArray(nodoHijoInsertarData);
				for (Object objectEvaluado : listEdgePoint) {
					JSONObject objetosEvaluadoDeJson = (JSONObject) objectEvaluado;
					Map<String, Object> objetosMap = objetosEvaluadoDeJson.toMap();
					record = tablaOwnedNode.newRecord();
					for (Map.Entry<String, Object> entry : objetosMap.entrySet()) {
						if (listaDeColumnas.stream().filter(x -> entry.getKey().equals(x)).findFirst().isPresent()) {
							record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"),
									entry.getValue().toString());
						} else {
							record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"), null);
						}
					}
					record.addField("uuid_node", columnaUuid);
					tablaOwnedNode.insert(record);

				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}