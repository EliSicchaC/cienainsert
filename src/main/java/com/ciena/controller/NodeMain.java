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
        main.llamarAlMetodo("D:\\archivos\\objetociena.json","tapi-common:context",
                "tapi-topology:topology-context","topology","node");
    }
    public void llamarAlMetodo(String rutaDelArchivo,String tapiContext,String tapiTopology,String topology,String node){
        NodeMain main = new NodeMain();
        try{
            main.insertarNode(rutaDelArchivo,tapiContext,tapiTopology,topology,node);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public void insertarNode(String lugarDelArchivo, String tapiContext, String tapiTopology, String topology, String node) throws SQLException, ClassNotFoundException {

        Map<String, String> exp_node = new HashMap<>();
        List<String> listaDeColumnas = new ArrayList<>();
        JSONArray evaluarANode = null;
        try{
            JSONObject json = Util.parseJSONFile(lugarDelArchivo);
            JSONObject identifica = json.getJSONObject(tapiContext).
                    getJSONObject(tapiTopology);
            evaluarANode = identifica.getJSONArray(topology);
            JSONArray identifcaElementos = identifica.getJSONArray(topology);
            for(Object objetos : identifcaElementos){
                JSONObject lineaDeElementos = (JSONObject) objetos;
                //evaluarANode = lineaDeElementos.getJSONArray(node);
                for(Object objetosNode : lineaDeElementos.getJSONArray(node)){
                    JSONObject objectEvaluado = (JSONObject) objetosNode;
                    Map<String, Object> objectMap = objectEvaluado.toMap();
                    for (Map.Entry<String, Object> entry : objectMap.entrySet()){
                        listaDeColumnas.add(entry.getKey());
                    }
                }
            }
        } catch (Exception exception) {
            System.out.println("error:: " + exception.getMessage());
        }
        String[][] dicNode = new String[][]{
                {
                        "id","int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY"
                },
                {
                        "atribute_name","varchar(250)"
                }
        };
        listaDeColumnas = listaDeColumnas.stream().distinct().collect(Collectors.toList());
        for (String objectos : listaDeColumnas){
            String nombreColumna = objectos.replaceAll("-","_").replaceAll(":","_");
            if(nombreColumna.equals("uuid")){
                exp_node.put(nombreColumna, "varchar(50) primary key ");
            }else{
                exp_node.put(nombreColumna,"MEDIUMTEXT");
            }
        }
        exp_node.put("uuid_topology","varchar(250) , FOREIGN KEY  (uuid_topology) REFERENCES exp_topology(uuid)");
        dataBase = new Conexion.DBConnector();
        tablaDicNode = Util.crearTablasGenerico(dataBase,"dic_topology_node",tablaDicNode,dicNode);
        tablaNode = Util.crearTablasGenericoMap(dataBase,"exp_topology_node",tablaNode,exp_node);
        DBRecord recorre = tablaNode.newRecord();
        for(String objetos : listaDeColumnas){
            recorre = tablaDicNode.newRecord();
            try {
                recorre.addField("atribute_name", objetos);
                tablaDicNode.insert(recorre);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
    }
}
