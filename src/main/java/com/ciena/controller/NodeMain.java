package com.ciena.controller;

import com.ciena.controller.dao.Conexion;
import com.ciena.controller.dao.DBRecord;
import com.ciena.controller.dao.DBTable;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Util;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NodeMain {
    private static Conexion.DBConnector dataBase;
    private static DBTable tablaNode;
    private static DBTable tablaDicNode;

    public static void main(String[] args) {
        NodeMain main = new NodeMain();
        main.analizarInformacionNode("D:\\archivos\\objetociena.json","tapi-common:context",
                "tapi-topology:topology-context","topology","node");
    }
    public Boolean analizarInformacionNode(String rutaDeArchivo, String tapiContext, String tapiTopology,
                                           String topology,String node) {
        boolean analizo = false;
        boolean insertoDiccionarioNode = false;
        boolean insertoMatrizNode = false;
        System.out.println("-------------Procesando informacion de: " + node + "------- \n");
        try {
            dataBase = new Conexion.DBConnector();
            JSONObject contenidoObjetosTotales = Util.parseJSONFile(rutaDeArchivo);
            JSONObject objetoTopologyContext = Util.retonarListaPropiedadesAsociadasNodoHijo(contenidoObjetosTotales,
                    tapiContext, tapiTopology);

            JSONArray topologyArray = objetoTopologyContext.getJSONArray(topology);
            JSONObject nodeContext = (JSONObject) topologyArray.get(0);
            JSONArray listaDeNode= nodeContext.getJSONArray(node);

            List<String> listaColumnas = Util.listaDeColumnasPadreArray(topologyArray, node);
            insertoDiccionarioNode = insertarDiccionarioNode(listaColumnas, dataBase);
            insertoMatrizNode = insertarMatrizNode(listaColumnas, dataBase, topologyArray,node);
            System.out.println("-------------Procesando ejecutado con exito: " + insertoDiccionarioNode  + "/ "+ insertoMatrizNode);
            analizo = insertoDiccionarioNode && insertoMatrizNode ? true : false;
        } catch (Exception e) {
            analizo = false;
            System.out.println("-------------Procesando con errores: " + e.getMessage());
            e.printStackTrace();
        }
        return analizo;
    }

    private boolean insertarMatrizNode(List<String> listaDeColumnas, Conexion.DBConnector dataBase, JSONArray evaluarANode, String node) {
        Map<String, String> exp_Node = new HashMap<>();
        for (String objectos : listaDeColumnas) {
            // INSERTANDO DATA
            String nombreColumna = objectos.replaceAll("-", "_").replaceAll(":", "_");
            if (nombreColumna.equals("uuid")) {
                exp_Node.put(nombreColumna, "varchar(50) primary key");
            } else {
                exp_Node.put(nombreColumna, "MEDIUMTEXT");
            }
        }
        exp_Node.put("uuid_topology", "varchar(250) , FOREIGN KEY  (uuid_topology) REFERENCES exp_topology(uuid)");
        try{
            tablaNode = Util.crearTablasGenericoMap(dataBase, "exp_topology_node", tablaNode, exp_Node);
            DBRecord record = tablaNode.newRecord();
            for (Object objetosNode : evaluarANode){
                JSONObject topologyUuid = (JSONObject) objetosNode;
                String columnaUuid = topologyUuid.get("uuid").toString();
                JSONArray listNode = topologyUuid.getJSONArray(node);
                for (Object objectEvaluado : listNode){
                    JSONObject objectEvaluadoDeJson = (JSONObject) objectEvaluado;
                    Map<String, Object> objectMap = objectEvaluadoDeJson.toMap();
                    record = tablaNode.newRecord();
                    for (Map.Entry<String, Object> entry : objectMap.entrySet()){
                        if (listaDeColumnas.stream().filter(x -> entry.getKey().equals(x)).findFirst().isPresent()){
                            record.addField(entry.getKey().replaceAll("-","_").replaceAll(":","_"), entry.getValue().toString());
                        } else {
                            record.addField(entry.getKey().replaceAll("-","_").replaceAll(":","_"), null);
                        }
                    }
                    try {
                        record.addField("uuid_topology", columnaUuid);
                        tablaNode.insert(record);

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

    private boolean insertarDiccionarioNode(List<String> listaDeColumnas, Conexion.DBConnector dataBase) {
        String[][] dicTopology = new String[][] { { "id", "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY" },
                { "atribute_name", "varchar(250)" } };
        listaDeColumnas = listaDeColumnas.stream().distinct().collect(Collectors.toList());
        try{
            String nombreTabla = "dic_topology_node";
            System.out.println("	-------------Creando tabla: " + nombreTabla);
            tablaDicNode = Util.crearTablasGenerico(dataBase, nombreTabla, tablaDicNode, dicTopology);
            DBRecord recorre = tablaDicNode.newRecord();
            for (String objetos : listaDeColumnas) {
                recorre = tablaDicNode.newRecord();
                recorre.addField("atribute_name", objetos);
                tablaDicNode.insert(recorre);
            }
        } catch (Exception e) {
            System.out.println("-------------Procesando con errores: " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

}
