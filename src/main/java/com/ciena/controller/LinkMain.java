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
		linkMain.analizarInformacionLink("D:\\archivos\\objetociena.json", "tapi-common:context",
				"tapi-topology:topology-context", "topology", "link","node-edge-point");
	}
	public Boolean analizarInformacionLink(String rutaDeArchivo, String tapiContext, String tapiTopology,
										   String topology,String link,String nodeEdgePoint) {
		boolean analizo = false;
		boolean insertoDiccionarioLink = false;
		boolean insertoMatrizLink = false;
		System.out.println("-------------Procesando informacion de: " + link + "------- \n");
		try {
			dataBase = new Conexion.DBConnector();
			//AQUI ME ESTOY POSICIONANDO EN TAPITOPOLOGY
			JSONObject contenidoObjetosTotales = Util.parseJSONFile(rutaDeArchivo);
			JSONObject objetoTopologyContext = Util.retonarListaPropiedadesAsociadasNodoHijo(contenidoObjetosTotales,
					tapiContext, tapiTopology);
			//QUIERO LO QUE VIEN EN TOPOLOGY POR ESO TOPOLOGY ES EL PADRE
			JSONArray topologyArray = objetoTopologyContext.getJSONArray(topology);
			JSONObject linkContext = (JSONObject) topologyArray.get(0);
			JSONArray listaDeLink = linkContext.getJSONArray(link);
			//PADRE TOPOLOGY Y HIJO LINK
			List<String> listaColumnas = Util.listaDeColumnasPadreArray(topologyArray, link);
			insertoDiccionarioLink = insertarDiccionarioLink(listaColumnas, dataBase);
			insertoMatrizLink = insertarMatrizLink(listaColumnas, dataBase, topologyArray,link,nodeEdgePoint);
			System.out.println("-------------Procesando ejecutado con exito: " + insertoDiccionarioLink  + "/ "+ insertoMatrizLink);
			analizo = insertoDiccionarioLink && insertoMatrizLink ? true : false;
		} catch (Exception e) {
			analizo = false;
			System.out.println("-------------Procesando con errores: " + e.getMessage());
			e.printStackTrace();
		}
		return analizo;
	}

	private boolean insertarMatrizLink(List<String> listaDeColumnas, Conexion.DBConnector dataBase,
									   JSONArray evaluarALink, String link,String nodeEdgePoint) {
		Map<String, String> exp_Link = new HashMap<>();
		for (String objectos : listaDeColumnas) {
			// INSERTANDO DATA
			String nombreColumna = objectos.replaceAll("-", "_").replaceAll(":", "_");
			if (nombreColumna.equals("uuid")) {
				exp_Link.put(nombreColumna, "varchar(50)");
			} else {
				exp_Link.put(nombreColumna, "MEDIUMTEXT");
			}
		}
		exp_Link.put("uuid_topology", "varchar(250) , FOREIGN KEY  (uuid_topology) REFERENCES exp_topology(uuid)");
		exp_Link.put("uuid_ownedNodePoint", "varchar(250) , foreign key (uuid_ownedNodePoint) references exp_topology_owned_node_edgepoint(uuid)");
		try{
			tablaLink = Util.crearTablasGenericoMap(dataBase, "exp_topology_link", tablaLink, exp_Link);
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
		}catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean insertarDiccionarioLink(List<String> listaDeColumnas, Conexion.DBConnector dataBase) {
		String[][] dicTopology = new String[][] { { "id", "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY" },
				{ "atribute_name", "varchar(250)" } };
		listaDeColumnas = listaDeColumnas.stream().distinct().collect(Collectors.toList());
		try{
			String nombreTabla = "dic_topology_link";
			System.out.println("	-------------Creando tabla: " + nombreTabla);
			tablaDicLink = Util.crearTablasGenerico(dataBase, nombreTabla, tablaDicLink, dicTopology);
			DBRecord recorre = tablaDicLink.newRecord();
			for (String objetos : listaDeColumnas) {
				recorre = tablaDicLink.newRecord();
				recorre.addField("atribute_name", objetos);
				tablaDicLink.insert(recorre);
			}
		} catch (Exception e) {
			System.out.println("-------------Procesando con errores: " + e.getMessage());
			e.printStackTrace();
		}
		return true;
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
