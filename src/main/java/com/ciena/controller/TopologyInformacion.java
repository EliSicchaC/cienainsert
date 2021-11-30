package com.ciena.controller;

import com.ciena.controller.dao.Conexion;
import com.ciena.controller.dao.DBRecord;
import com.ciena.controller.dao.DBTable;
import com.ciena.controller.entity.*;
import util.Util;
import java.io.IOException;
import java.sql.SQLException;

public class TopologyInformacion {
    private static Conexion.DBConnector dataBase;
    private static DBTable tablaTopology;
    private static DBTable tablaNode;
    private static DBTable tablaOwnedNode;
    private static DBTable tablaAccessPort;
    private static DBTable tablaMcPool;
    private static DBTable tablaLink;

    public static void main(final String[] args) throws IOException {
        TopologyInformacion mainTopology = new TopologyInformacion();
        mainTopology.analizarInformacionTopoloyContext("D:\\archivos\\objetociena.json");
    }
    public Boolean analizarInformacionTopoloyContext(String s) throws IOException {
        Boolean analizo = false;
        ObjetosPrincipales principales = Util.getObjetosPrincipales(s);
        System.out.println("INFORMACION: " + principales.getTapi_common_context().getUuid());
        System.out.println("--------------------IMPRIMIR topology-------- \n");
        TopologyInformacion mainTopology = new TopologyInformacion();
        try {
            crearTablas();
            mainTopology.insertarTopology(principales.getTapi_common_context().getTapi_topology_topology_context());
            mainTopology.insertarNode(principales.getTapi_common_context().getTapi_topology_topology_context());
            mainTopology.insertarOwnedNode(principales.getTapi_common_context().getTapi_topology_topology_context());
            mainTopology.insertarAccessPort(principales.getTapi_common_context().getTapi_topology_topology_context());
            mainTopology.insertarMcPool(principales.getTapi_common_context().getTapi_topology_topology_context());
            mainTopology.insertarLink(principales.getTapi_common_context().getTapi_topology_topology_context());

            analizo = true;
        } catch (SQLException | ClassNotFoundException e) {
            analizo = false;
            e.printStackTrace();
        }
        return analizo;
    }

    private void crearTablas() throws SQLException, ClassNotFoundException {
        dataBase = new Conexion.DBConnector();
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
        for(Topology topology : topologycontext.getTopology()){
            DBRecord record = tablaTopology.newRecord();
            record.addField("uuid", topology.getUuid());
            String nameStringValue = Util.generarNames(topology.getName());
            System.out.println("nameStringValue: " + nameStringValue);
            record.addField("name", nameStringValue);
            record.addField("layer_protocol_name", topology.getLayer_protocol_name().toString());
            try {
                tablaTopology.insert(record);
            } catch (SQLException e) {
                System.out.println("Ex: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void insertarNode(TapiTopology nodecontext){
        for(Topology topology : nodecontext.getTopology()){
            DBRecord record = tablaNode.newRecord();
            for(Node node : topology.getNode()){
                record = tablaNode.newRecord();
                record.addField("uuid", node.getUuid());
                record.addField("lifecycle_state",node.getLifecycle_state());
                String nameStringValue = Util.generarNames(node.getName());
                System.out.println("nameStringValue: " + nameStringValue);
                record.addField("name", nameStringValue);
                record.addField("operational_state",node.getOperational_state());
                record.addField("uuid_topology",topology.getUuid());
                try {
                    tablaNode.insert(record);
                } catch (SQLException e) {
                    System.out.println("Ex: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private void insertarOwnedNode(TapiTopology ownednode){
        for(Topology topology : ownednode.getTopology()){
            DBRecord record = tablaOwnedNode.newRecord();
            for(Node node : topology.getNode()){
                record = tablaOwnedNode.newRecord();
                for(OwnedNode ownedNode : node.getOwned_node_edge_point()){
                    record = tablaOwnedNode.newRecord();
                    record.addField("uuid",ownedNode.getUuid());
                    record.addField("termination_state",ownedNode.getTermination_state());
                    record.addField("termination_direction",ownedNode.getTermination_direction());
                    record.addField("layer_protocol_name",ownedNode.getLayer_protocol_name());
                    record.addField("lifecycle_state",ownedNode.getLifecycle_state());
                    String nameStringValue = Util.generarNames(ownedNode.getName());
                    System.out.println("nameStringValue: " + nameStringValue);
                    record.addField("name", nameStringValue);
                    record.addField("operational_state",ownedNode.getOperational_state());
                    record.addField("supported_cep_layer_protocol_qualifier",ownedNode.getSupported_cep_layer_protocol_qualifier().toString());
                    record.addField("administrative_state",ownedNode.getAdministrative_state());
                    record.addField("uuid_topology",topology.getUuid());
                    record.addField("uuid_node",node.getUuid());
                    try {
                        tablaOwnedNode.insert(record);
                    } catch (SQLException e) {
                        System.out.println("Ex: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
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
                    if(null != ownedNode.getTapi_equipment_supporting_access_port() && null != ownedNode.getTapi_equipment_supporting_access_port().getAcces_port()){
                        record.addField("access_port_uuid", ownedNode.getTapi_equipment_supporting_access_port().getAcces_port().getAcces_port_uuid());
                        record.addField("device_uuid",ownedNode.getTapi_equipment_supporting_access_port().getAcces_port().getDevice_uuid());
                    }
                    try {
                        tablaAccessPort.insert(record);
                    } catch (SQLException e) {
                        System.out.println("Ex: " + e.getMessage());
                        e.printStackTrace();
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
                    record.addField("uuid", ownedNode.getUuid());
                    if(null != ownedNode.getTapi_photonic_media_media_channel_node_edge_point_spec()){
                        record.addField("supportable_spectrum", ownedNode.getTapi_photonic_media_media_channel_node_edge_point_spec().getMc_pool().getSupportable_spectrum().toString());
                    }
                    if(null != ownedNode.getTapi_photonic_media_media_channel_node_edge_point_spec()){
                        record.addField("available_spectrum", ownedNode.getTapi_photonic_media_media_channel_node_edge_point_spec().getMc_pool().getAvailable_spectrum().toString());
                    }
                    try {
                        tablaMcPool.insert(record);
                    } catch (SQLException e) {
                        System.out.println("Ex: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void insertarLink(TapiTopology linkcontext){
        for (Topology topology : linkcontext.getTopology()){
            DBRecord record = tablaLink.newRecord();
            for (Link link : topology.getLink()){
                record = tablaLink.newRecord();
                record.addField("uuid", link.getUuid());
                if(null != link && null != link.getNode_edge_point()){
                    record.addField("node_edge_point", link.getNode_edge_point().toString());
                }
                record.addField("lifecycle_state", link.getLifecycle_state());
                record.addField("name", link.getName().toString());
                record.addField("operational_state", link.getOperational_state());
                record.addField("administrative_state", link.getAdministrative_state());
                record.addField("direction", link.getDirection());
                record.addField("layer_protocol_name", link.getLayer_protocol_name().toString());
                record.addField("uuid_topology",topology.getUuid());
                try {
                    tablaLink.insert(record);
                } catch (SQLException e) {
                    System.out.println("Ex: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}