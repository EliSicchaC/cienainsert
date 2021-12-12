package com.ciena.controller;

import com.ciena.controller.dao.Conexion;
import com.ciena.controller.dao.DBRecord;
import com.ciena.controller.dao.DBTable;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Util;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LinkMain {
	private static Conexion.DBConnector dataBase;
	private static DBTable tablaLink;
	private static DBTable tablaDicLink;

	public static void main(final String[] args) throws IOException {
		LinkMain linkMain = new LinkMain();
		linkMain.llamarAlMetodo("D:\\archivos\\objetociena.json", "tapi-common:context",
				"tapi-topology:topology-context", "topology", "link","node-edge-point");
	}

	public void llamarAlMetodo(String rutaDeArchivo, String tapiContext, String tapiTopology, String topology,
			String link,String nodeEdgePoint) throws IOException {
		LinkMain linkMain = new LinkMain();
		try {
			linkMain.diccionarioLink(rutaDeArchivo, tapiContext, tapiTopology, topology, link, nodeEdgePoint);

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void diccionarioLink(String lugarDelArchivo, String tapiContext, String tapiTopology, String topology,
			String link,String nodeEdgePoint) throws SQLException, ClassNotFoundException {

		Map<String, String> exp_Link = new HashMap<>();
		List<String> listaDeColumnas = new ArrayList<>();
		JSONArray evaluarALink = null;
		try {
			JSONObject json = Util.parseJSONFile(lugarDelArchivo);
			JSONObject identifica = json.getJSONObject(tapiContext).getJSONObject(tapiTopology);
			evaluarALink = identifica.getJSONArray(topology);
			JSONArray identificaElementos = identifica.getJSONArray(topology);
			for (Object objetos : identificaElementos) {
				JSONObject lineaDeElementos = (JSONObject) objetos;
				for (Object objetosLink : lineaDeElementos.getJSONArray(link)) {
					JSONObject objectEvaluado = (JSONObject) objetosLink;
					Map<String, Object> objectMap = objectEvaluado.toMap();
					for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
						listaDeColumnas.add(entry.getKey());
					}
				}
			}
		} catch (Exception exception) {
			System.out.println("error:: " + exception.getMessage());
		}
		String[][] dicLink = new String[][] { { "id", "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY" },
				{ "atribute_name", "varchar(250)" } };
		listaDeColumnas = listaDeColumnas.stream().distinct().collect(Collectors.toList());
		for (String objectos : listaDeColumnas) {
			String nombreColumna = objectos.replaceAll("-", "_").replaceAll(":", "_");
			/*
			 * SOLO SIRVE PARA PRIMERY KEY PORQUE LO QUE QUEREMOS ES VALIDAR SI AQUELLA
			 * COLUMNA EXISTE EN LA TABLA
			 */
			if (nombreColumna.equals("uuid")) {
				exp_Link.put(nombreColumna, "varchar(50)");
			} else {
				exp_Link.put(nombreColumna, "MEDIUMTEXT");
			}
		}
		// FOREIGN KEY (NOMBRE) REFERENCES NOMBREDELATABLA(QUEQUIEREDELATABLA)
		exp_Link.put("uuid_topology", "varchar(250) , FOREIGN KEY  (uuid_topology) REFERENCES exp_topology(uuid) ");
		exp_Link.put("uuid_ownedNodePoint", "varchar(250)");
		dataBase = new Conexion.DBConnector();
		// APARTIR DE AQUI SE CREA LA TABLA, SI NO ENTONCES NO FUNCIONARA EL FOREIGN KEY
		tablaDicLink = Util.crearTablasGenerico(dataBase, "dic_topology_link", tablaDicLink, dicLink);
		tablaLink = Util.crearTablasGenericoMap(dataBase, "exp_topology_link", tablaLink, exp_Link);
		DBRecord recorre = tablaDicLink.newRecord();
		for (String objetos : listaDeColumnas) {
			recorre = tablaDicLink.newRecord();
			try {
				recorre.addField("atribute_name", objetos);
				tablaDicLink.insert(recorre);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		DBRecord record = tablaLink.newRecord();
		for (Object objetosLink : evaluarALink) {
			JSONObject topologyLink = (JSONObject) objetosLink;
			String columnaUuid = topologyLink.get("uuid").toString();
			JSONArray listLink = topologyLink.getJSONArray(link);
			for (Object objectEvaluado : listLink) {
				JSONObject objetos = (JSONObject) objectEvaluado;
				//VALIDO SI TODOS TIENEN EL OBJETO NODEEDGEPOINT, Y SI LO TIENE QUE ME LO TRAIGA
				if (objetos.has(nodeEdgePoint)) {
					// ME POSICIONO EN EL NODE EDGE POINT
					JSONArray node = objetos.getJSONArray(nodeEdgePoint);
					// RECORRO TODO LO QUE TIENE NODE EDGE POINT
					for (Object objectNode : node) {
						record = tablaLink.newRecord();
						JSONObject objetosEvaluadoDeJson = (JSONObject) objectNode;
						record.addField("uuid_ownedNodePoint",
								objetosEvaluadoDeJson.get("node-edge-point-uuid").toString());
						//QUE ME TRAIGA TODAS LAS PARTES DE LINK
						insertarInformacion(record, objetos, listaDeColumnas, columnaUuid);
					}
					//SI NO LO TIENE ENTONCES QUE ME TRAIGA TODO LO QUE TIENE LINK
				}else {
					record = tablaLink.newRecord();
					insertarInformacion(record, objetos, listaDeColumnas, columnaUuid);
				}

			}
		}
	}
	//REFACTORICE LO QUE TRAE TODO LINK PARA USARLO ARRIBA
	private void insertarInformacion(DBRecord record, JSONObject objetos, List<String> listaDeColumnas,
			String columnaUuid) {
		Map<String, Object> objetosMap = objetos.toMap();
		for (Map.Entry<String, Object> entry : objetosMap.entrySet()) {
			if (listaDeColumnas.stream().filter(x -> entry.getKey().equals(x)).findFirst().isPresent()) {
				record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"), entry.getValue().toString());
			} else {
				record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"), null);
			}
		}
		try {
			record.addField("uuid_topology", columnaUuid);
			tablaLink.insert(record);

		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

}
