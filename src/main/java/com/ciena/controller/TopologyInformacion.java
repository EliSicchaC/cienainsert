package com.ciena.controller;

import com.ciena.controller.dao.Conexion;
import com.ciena.controller.dao.DBRecord;
import com.ciena.controller.dao.DBTable;
import com.ciena.controller.entity.*;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Util;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TopologyInformacion {
    private static Conexion.DBConnector dataBase;
    private static DBTable tablaTopology;
    private static DBTable tablaNode;
    private static DBTable tablaOwnedNode;
    private static DBTable tablaAccessPort;
    private static DBTable tablaMcPool;
    private static DBTable tablaLink;
    private static DBTable tablaDiccionario;

    //Se crea un metodo main, para poder ejecutarlo y tambien conectarnos al json
    public static void main(final String[] args) throws IOException {
        TopologyInformacion mainTopology = new TopologyInformacion();
        mainTopology.analizarInformacionTopoloyContext("D:\\archivos\\objetociena.json","tapi-common:context","tapi-topology:topology-context",
                "topology","node","owned-node-edge-point");
    }
    public Boolean analizarInformacionTopoloyContext(String rutaDeArchivo,String tapiContext,String tapiTopology,
                                                     String topology, String node,String ownedPoint) throws IOException {
        Boolean analizo = false;
        System.out.println("--------------------IMPRIMIR topology-------- \n");
        TopologyInformacion mainTopology = new TopologyInformacion();
        try {
            dataBase = new Conexion.DBConnector();
            JSONObject contenidoObjetosTotales = Util.parseJSONFile(rutaDeArchivo);
            JSONObject objetoTopologyContext = Util.retonarListaPropiedadesAsociadasNodoHijo(contenidoObjetosTotales,tapiContext,tapiTopology);


            JSONArray topologyArray = objetoTopologyContext.getJSONArray(topology);

            JSONObject ownedNode = (JSONObject) topologyArray.get(0);
            JSONArray listaDeNode = ownedNode.getJSONArray(node);

            List<String> listaColumnas = Util.listaDeColumnasPadreArray(listaDeNode,ownedPoint);
            boolean insertoDiccionarioOwned = insertarDiccionarioOwned(listaColumnas,dataBase);
            boolean insertoMatrizOwned = insertarMatrizOwnedNode(listaColumnas,dataBase,listaDeNode,ownedPoint);
            analizo = true;
        } catch (Exception e) {
            analizo = false;
            e.printStackTrace();
        }
        return analizo;
    }
    private void crearTablas() throws SQLException, ClassNotFoundException {
        //SE HACE UNA CONEXION A LA BASE DE DATOS
        dataBase = new Conexion.DBConnector();
        //SE COLOCA LOS PARAMETROS QUE IRA EN TU TABLA
        String[][] topology = new String[][]{
                {
                    "id", "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY"
                },
                {
                  "uuid", "varchar(250)"
                },
                {
                    "name", "mediumtext"
                },
                {
                    "layer_protocol_name", "mediumtext"
                },
                {
                    "uuid_topology_context","varchar(250)"
                }
        };
        //SE CREA TU TABLA (CONEXION,NOMBRE DE COMO QUIERES QUE SE LLAME TU TABLA,,NOMBRE QUE LE QUIERES LLAMAR DESPUES DE TU TIPO DE DATO
        tablaTopology = Util.crearTablasGenerico(dataBase,"exp_topology",tablaTopology,topology);
        String[][] node = new String[][]{
                {
                    "id", "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY"
                },
                {
                    "uuid","varchar(250)"
                },
                {
                    "lifecycle_state", "varchar(250)"
                },
                {
                    "name", "mediumtext"
                },
                {
                    "operational_state", "varchar(250)"
                },
                {
                    "uuid_topology","varchar(250)"
                }
        };
        tablaNode = Util.crearTablasGenerico(dataBase,"exp_topology_node",tablaNode,node);
        String[][] ownednode = new String[][]{
                {
                    "id", "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY"
                },
                {
                    "uuid","varchar(250)"
                },
                {
                    "termination_state","varchar(250)"
                },
                {
                    "termination_direction","varchar(250)"
                },
                {
                    "layer_protocol_name","varchar(250)"
                },
                {
                    "lifecycle_state","varchar(250)"
                },
                {
                    "name","mediumtext"
                },
                {
                    "operational_state","varchar(250)"
                },
                {
                    "supported_cep_layer_protocol_qualifier","mediumtext"
                },
                {
                    "administrative_state","varchar(250)"
                },
                {
                    "uuid_topology","varchar(250)"
                },
                {
                    "uuid_node","varchar(250)"
                },
                {
                    "uuid_link","varchar(250)"
                }
        };
        tablaOwnedNode = Util.crearTablasGenerico(dataBase,"exp_topology_owned_node_edgepoint",tablaOwnedNode,ownednode);
        String[][] accessport = new String[][]{
                {
                    "id", "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY"
                },
                {
                    "access_port_uuid","varchar(250)"
                },
                {
                    "device_uuid","varchar(250)"
                }
        };
        tablaAccessPort = Util.crearTablasGenerico(dataBase,"exp_onep_access_port",tablaAccessPort,accessport);
        String[][] mcpool = new String[][]{
                {
                        "id","int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY"
                },
                {
                        "uuid","varchar(250)"
                },
                {
                        "supportable_spectrum","mediumtext"
                },
                {
                        "available_spectrum","mediumtext"
                }
        };
        tablaMcPool = Util.crearTablasGenerico(dataBase, "exp_onep_mc_pool",tablaMcPool,mcpool);
        String[][] link = new String[][]{
                {
                    "id", "int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY"
                },
                {
                    "uuid", "varchar(250)"
                },
                {
                    "node_edge_point", "mediumtext"
                },
                {
                    "lifecycle_state", "varchar(250)"
                },
                {
                    "name", "mediumtext"
                },
                {
                    "operational_state", "varchar(250)"
                },
                {
                    "administrative_state", "varchar(250)"
                },
                {
                    "direction", "varchar(250)"
                },
                {
                    "layer_protocol_name", "mediumtext"
                },
                {
                    "uuid_topology","varchar(250)"
                }
        };
        tablaLink = Util.crearTablasGenerico(dataBase,"exp_topology_link",tablaLink,link);
    }

    private void insertarTopology(TapiTopology topologycontext){
        //SE CREA UN NUEVO OBJETO Y SE COLOGA LOS PARAMETROS RESPECTIVOS
        //EN LOS PARAMETROS NO SE COLOCA EL TIPO DE DATO, SOLO LO QUE QUIERES TRAER
        Topology topology = new Topology();
        topology.insertaTopology(topologycontext.getTopology(),tablaTopology);
    }
    private void insertarNode(TapiTopology nodecontext){
        for(Topology topology : nodecontext.getTopology()){
            Node node = new Node();
            node.insertaNode(topology.getNode(),tablaNode,topology.getUuid());
        }
    }
    private void insertarOwnedNode(TapiTopology ownednode){
        for(Topology topology : ownednode.getTopology()){
            DBRecord record = tablaOwnedNode.newRecord();
            for(Node node : topology.getNode()){
                record = tablaOwnedNode.newRecord();
                OwnedNode ownedNode = new OwnedNode();
                ownedNode.analizarOwnedNode(node.getOwned_node_edge_point(),tablaOwnedNode,topology.getUuid(),node.getUuid());
            }
        }
    }
    private void insertarAccessPort(TapiTopology accessportcontext){
        for(Topology topology : accessportcontext.getTopology()){
            DBRecord record = tablaAccessPort.newRecord();
            for(Node node : topology.getNode()){
                record = tablaAccessPort.newRecord();
                for(OwnedNode ownedNode : node.getOwned_node_edge_point()){
                    record = tablaAccessPort.newRecord();
                    AccesPort accesPort = new AccesPort();
                    if (null != ownedNode.getTapi_equipment_supporting_access_port() && null != ownedNode.getTapi_equipment_supporting_access_port().getAcces_port()){
                        accesPort.analizoAccsessPort(tablaAccessPort,ownedNode.getTapi_equipment_supporting_access_port().getAcces_port().getAcces_port_uuid(),ownedNode.getTapi_equipment_supporting_access_port().getAcces_port().getDevice_uuid());
                    }
                }
            }
        }
    }
    private void insertarMcPool(TapiTopology mcpool){
        for (Topology topology : mcpool.getTopology()){
            DBRecord record = tablaMcPool.newRecord();
            for (Node node : topology.getNode()){
                record = tablaMcPool.newRecord();
                for (OwnedNode ownedNode : node.getOwned_node_edge_point()){
                    record = tablaMcPool.newRecord();
                    McPool mcPool = new McPool();
                    if (null != ownedNode.getTapi_photonic_media_media_channel_node_edge_point_spec()){
                        mcPool.analizaMcPool(tablaMcPool,ownedNode.getUuid(),ownedNode.getTapi_photonic_media_media_channel_node_edge_point_spec().getMc_pool().getSupportable_spectrum(),
                                ownedNode.getTapi_photonic_media_media_channel_node_edge_point_spec().getMc_pool().getAvailable_spectrum());
                    }
                }
            }
        }
    }
    private void insertarLink(TapiTopology linkcontext){
        for (Topology topology : linkcontext.getTopology()){
            DBRecord record = tablaLink.newRecord();
            Link link = new Link();
            link.analizoLink(topology.getLink(),tablaLink,topology.getUuid());
        }
    }

    private Boolean insertarDiccionarioOwned(List<String> listaDeColumnas, Conexion.DBConnector dataBase){
        String[][] ownedpoint = new String[][]{
                {
                        "id","int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY"
                },
                {
                        "atribute_name","varchar(250)"
                },
                {
                        "sample_reference_object_uuid","varchar(250)"
                },
                {
                        "sample_value","MEDIUMTEXT"
                },
                {
                        "sample_reference_onep_layer_protocol_qualifier","MEDIUMTEXT"
                }
        };
        listaDeColumnas = listaDeColumnas.stream().distinct().collect(Collectors.toList());
        try {
            tablaDiccionario = Util.crearTablasGenerico(dataBase,"dic_topology_onep",tablaDiccionario,ownedpoint);
            DBRecord recorre = tablaDiccionario.newRecord();
            for(String objetos : listaDeColumnas){
                recorre = tablaDiccionario.newRecord();
                try {
                    recorre.addField("atribute_name", objetos);
                    tablaDiccionario.insert(recorre);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }
        return true;

    }
    private Boolean insertarMatrizOwnedNode(List < String > listaDeColumnas, Conexion.DBConnector dataBase, JSONArray nodoEvaluar, String nodoHijoInsertarData) {
        Map < String, String > tablaOwned = new HashMap();
        for (String objectos: listaDeColumnas) {
            //INSERTANDO DATA
            tablaOwned.put(objectos.replaceAll("-", "_").replaceAll(":", "_"), "MEDIUMTEXT");
        }
        tablaOwned.put("uuid_node", "varchar(250)");
        try {
            tablaOwnedNode = Util.crearTablasGenericoMap(dataBase, "exp_topology_owned_node_edgepoint", tablaOwnedNode, tablaOwned);
            DBRecord record = tablaOwnedNode.newRecord();
            for (Object objetosNode: nodoEvaluar) {
                //JSONOBJECT ES PARA TRAER UN OBJETO DEL JSON
                JSONObject ownedNode = (JSONObject) objetosNode;
                //QUIERO ATRAER EL UUID DE OWNEDNODE PARA LUEGO IMPLEMENTARLO EN LA BD
                String columnaUuid = ownedNode.get("uuid").toString();
                JSONArray listEdgePoint = ownedNode.getJSONArray(nodoHijoInsertarData);
                for (Object objectEvaluado: listEdgePoint) {
                    JSONObject objetosEvaluadoDeJson = (JSONObject) objectEvaluado;
                    Map < String, Object > objetosMap = objetosEvaluadoDeJson.toMap();
                    record = tablaOwnedNode.newRecord();
                    for (Map.Entry < String, Object > entry: objetosMap.entrySet()) {
                        if (listaDeColumnas.stream().filter(x -> entry.getKey().equals(x)).findFirst().isPresent()) {
                            record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"), entry.getValue().toString());
                        } else {
                            record.addField(entry.getKey().replaceAll("-", "_").replaceAll(":", "_"), null);
                        }
                    }
                    record.addField("uuid_node", columnaUuid);
                    tablaOwnedNode.insert(record);

                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }

        return true;
    }

    public void diccionarioOwned(String lugarDelArchivo, String tapiContext, String tapiTopology,
                                 String topology, String node, String ownedPoint) throws SQLException, ClassNotFoundException {
        TopologyInformacion mainTopology = new TopologyInformacion();
        Map<String, String> tablaOwned = new HashMap();
        List<String> listaDeColumnas = new ArrayList<>();
        JSONArray EvaluarANode = null;
        try {
            //CONECTO CON EL JSON
            JSONObject json = Util.parseJSONFile(lugarDelArchivo);
            //ME COLOCO EN EL TAPICONTEXT Y LUEGO AL TOPOLOGY
            JSONObject identifica = json.getJSONObject(tapiContext)
                    .getJSONObject(tapiTopology);
            //COMO EL TOPOLOGY ES UN ARRAY SE COLOCA JSONARRAY Y QUE ME IDENTIFIQUE LOS OBJETOS
            JSONArray identificaElementos = identifica.getJSONArray(topology);
            for (Object objetos : identificaElementos) {
                JSONObject lineaDeElementos = (JSONObject) objetos;
                //DENTRO DE TOPOLOGY NOS PASAMOS A NODE
                EvaluarANode = lineaDeElementos.getJSONArray(node);
                for (Object objetosNode : EvaluarANode) {
                    JSONObject ownedNode = (JSONObject) objetosNode;
                    JSONArray listaDeEdgePoint = ownedNode.getJSONArray(ownedPoint);
                    for (Object objetoEvaluado : listaDeEdgePoint) {
                        JSONObject objetoEvaluadoJson = (JSONObject) objetoEvaluado;
                        Map<String, Object> objectMap = objetoEvaluadoJson.toMap();
                        for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
                            //QUIERO QUE LAS COLUMNAS ESTEN SU KEY Y SU VALOR
                            listaDeColumnas.add(entry.getKey());
                        }
                    }
                }

            }
        } catch (Exception exception) {
            System.out.println("error:: " + exception.getMessage());
        }
        //ARMAR EL DICCIONARIO
        // CONFIGURAR LA TABLA
        String[][] ownedpoint = new String[][]{
                {
                    "id","int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY"
                },
                {
                    "atribute_name","varchar(250)"
                },
                {
                    "sample_reference_object_uuid","varchar(250)"
                },
                {
                    "sample_value","MEDIUMTEXT"
                },
                {
                    "sample_reference_onep_layer_protocol_qualifier","MEDIUMTEXT"
                }
        };
        listaDeColumnas = listaDeColumnas.stream().distinct().collect(Collectors.toList());
        for (String objectos : listaDeColumnas){
            //INSERTANDO DATA
            tablaOwned.put(objectos.replaceAll("-","_").replaceAll(":","_"), "MEDIUMTEXT");

        }
        //PUT SIGNIFICA METER, EN ESTE CASO METO EL UUID_NODE A LA BD
        tablaOwned.put("uuid_node","varchar(250)");
        dataBase = new Conexion.DBConnector();
        tablaDiccionario = Util.crearTablasGenerico(dataBase,"dic_topology_onep",tablaDiccionario,ownedpoint);
        tablaOwnedNode = Util.crearTablasGenericoMap(dataBase, "exp_topology_owned_node_edgepoint", tablaOwnedNode, tablaOwned);
        //DBRECORD TIENE QUE ESTAR ORDENADO PARA NO TENER PROBLEMAS DE REDUNDANTE
        DBRecord recorre = tablaDiccionario.newRecord();
        for(String objetos : listaDeColumnas){
            recorre = tablaDiccionario.newRecord();
            try {
                recorre.addField("atribute_name", objetos);
                tablaDiccionario.insert(recorre);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        DBRecord record = tablaOwnedNode.newRecord();
        for (Object objetosNode : EvaluarANode) {
            //JSONOBJECT ES PARA TRAER UN OBJETO DEL JSON
            JSONObject ownedNode = (JSONObject) objetosNode;
            //QUIERO ATRAER EL UUID DE OWNEDNODE PARA LUEGO IMPLEMENTARLO EN LA BD
            String columnaUuid = ownedNode.get("uuid").toString();
            JSONArray listEdgePoint = ownedNode.getJSONArray(ownedPoint);
            for (Object objectEvaluado : listEdgePoint) {
                JSONObject objetosEvaluadoDeJson = (JSONObject) objectEvaluado;
                Map<String, Object> objetosMap = objetosEvaluadoDeJson.toMap();
                record = tablaOwnedNode.newRecord();
                for (Map.Entry<String, Object> entry : objetosMap.entrySet()){
                    if (listaDeColumnas.stream().filter(x -> entry.getKey().equals(x)).findFirst().isPresent()){
                        record.addField(entry.getKey().replaceAll("-","_").replaceAll(":","_"), entry.getValue().toString());
                    } else {
                        record.addField(entry.getKey().replaceAll("-","_").replaceAll(":","_"), null);
                    }
                }
                try {
                    //RECORD.ADDFIELD SIRVE PARA IMPLEMENTARLO EN LA BD
                    record.addField("uuid_node", columnaUuid);
                    tablaOwnedNode.insert(record);

                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

}