package com.ciena.controller;

import com.ciena.controller.dao.Conexion;
import com.ciena.controller.dao.Conexion.DBConnector;
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

public class TopologyMain {
	private static Conexion.DBConnector dataBase;
	private static DBTable tablaTopology;
	private static DBTable tablaDicTopology;

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		TopologyMain main = new TopologyMain();
		main.analizarInformacionTopoloy("D:\\archivos\\objetociena.json", "tapi-common:context", "tapi-topology:topology-context",
				"topology");
	}
	public TopologyMain() throws SQLException, ClassNotFoundException {
		dataBase = new Conexion.DBConnector();
		tablaTopology = dataBase.deleteTableIfExsist("exp_topology");
	}
	public Boolean analizarInformacionTopoloy(String rutaDeArchivo, String tapiContext, String tapiTopology,
			String topology) {
		boolean analizo = false;
		boolean insertoDiccionarioTopology = false;
		boolean insertoMatrizTopology = false;
		System.out.println("-------------Procesando informacion de: " + topology + "------- \n");
		try {

			//AQUI ME ESTOY POSICIONANDO EN TAPITOPOLOGY
			JSONObject contenidoObjetosTotales = Util.parseJSONFile(rutaDeArchivo);
			JSONObject objetoTopologyContext = Util.returnListPropertiesParentAssociatesChildNode(contenidoObjetosTotales,
					tapiContext, tapiTopology);

			//AQUI ESTOY TRAYENDO LOS OBJETOS DE TOPOLOGY, POR ESO ME ESTOY POSICIONANDO EN ÉL
			List<String> listaColumnas = Util.columnListParentObject(objetoTopologyContext, topology);

			insertoDiccionarioTopology = insertarDiccionarioTopology(listaColumnas, dataBase);

			insertoMatrizTopology = insertarMatrizTopology(listaColumnas, dataBase, objetoTopologyContext, topology);
			System.out.println("-------------Procesando ejecutado con exito: " + insertoDiccionarioTopology + "/ "
					+ insertoMatrizTopology);
			analizo = insertoDiccionarioTopology && insertoMatrizTopology ? true : false;
		} catch (Exception e) {
			analizo = false;
			System.out.println("-------------Procesando con errores: " + e.getMessage());
			e.printStackTrace();
		}

		return analizo;
	}

	private boolean insertarMatrizTopology(List<String> listaDeColumnas, DBConnector dataBase,JSONObject evaluarATopology,
										   String topology){
		Map<String, String> exp_topology = new HashMap<String, String>();
		for (String objectos : listaDeColumnas) {
			String nombreColumna = objectos.replaceAll("-", "_").replaceAll(":", "_");
			if (nombreColumna.equals("uuid")) {
				exp_topology.put(nombreColumna, "varchar(50) primary key");
			} else {
				exp_topology.put(nombreColumna, "MEDIUMTEXT");
			}
		}
		exp_topology.put("uuid_topology_context", "varchar(250)");
		try {
			tablaTopology = Util.createTableMap(dataBase, "exp_topology", tablaTopology,
					exp_topology);
			DBRecord record = tablaTopology.newRecord();
			//MI NODO PADRE ES UN OBJETO Y YO QUIERO MI NODO HIJO, LO QUE TENDRIA QUE HACER ES POSICIONARME
			//DEBERIA HACER MI NODO PADRE UN ARREGLO Y TRAER A MI HIJO QUE EN ESTE CASO SERIA TOPOLOGYCONTEXT
			JSONArray evaluarATopologyContext = evaluarATopology.getJSONArray(topology);
			for (Object objectEvaluado : evaluarATopologyContext) {
				JSONObject objetosEvaluadoDeJson = (JSONObject) objectEvaluado;
				Map<String, Object> objetosMap = objetosEvaluadoDeJson.toMap();
				record = tablaTopology.newRecord();
				for (Map.Entry<String, Object> entry : objetosMap.entrySet()) {
					if (listaDeColumnas.stream().filter(x -> entry.getKey().equals(x)).findFirst().isPresent()) {
						record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"),
								entry.getValue().toString());
					} else {
						record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"), null);
					}
				}
				try {
					tablaTopology.insert(record);
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

	private boolean insertarDiccionarioTopology(List<String> listaDeColumnas, DBConnector dataBase) {
		String[][] dicTopology = new String[][] { { "id", "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY" },
				{ "atribute_name", "varchar(250)" } };
		listaDeColumnas = listaDeColumnas.stream().distinct().collect(Collectors.toList());
		try {
			String nombreTabla = "dic_topology";
			System.out.println("	-------------Creando tabla: " + nombreTabla);
			tablaDicTopology = Util.createTableDictionary(dataBase, nombreTabla, tablaDicTopology, dicTopology);

			DBRecord recorre = tablaDicTopology.newRecord();
			for (String objetos : listaDeColumnas) {
				recorre = tablaDicTopology.newRecord();
				recorre.addField("atribute_name", objetos);
				tablaDicTopology.insert(recorre);
			}
		} catch (SQLException | ClassNotFoundException e) {
			return false;
		}
		return true;
	}
}
