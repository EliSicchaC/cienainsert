package com.ciena.controller.entity;

import com.ciena.controller.dao.Conexion;
import com.ciena.controller.dao.DBRecord;
import com.ciena.controller.dao.DBTable;
import util.Util;
import java.io.IOException;
import java.sql.SQLException;

public class MainTopology{
    private static Conexion.DBConnector dataBase;
    private static DBTable tablaTopology;
    private static DBTable tablaNode;
    private static DBTable tablaOwnedNode;
    private static DBTable tablaAccessPort;

    public static void main(final String[] args) throws IOException {
        MainTopology mainTopology = new MainTopology();
        mainTopology.analizarInformacionTopoloyContext("D:\\archivos\\objetociena.json");
    }
    private void analizarInformacionTopoloyContext(String s) throws IOException {
        ObjetosPrincipales principales = Util.getObjetosPrincipales(s);
        System.out.println("INFORMACION: " + principales.getTapi_common_context().getUuid());
        System.out.println("--------------------IMPRIMIR topology-------- \n");
        MainTopology mainTopology = new MainTopology();
        try{
            crearTablas();
            //mainTopology.insertarTopology(principales.getTapi_common_context().getTapi_topology_topology_context());
            //mainTopology.insertarNode(principales.getTapi_common_context().getTapi_topology_topology_context());
            //mainTopology.insertarOwnedNode(principales.getTapi_common_context().getTapi_topology_topology_context());
            mainTopology.insertarAccessPort(principales.getTapi_common_context().getTapi_topology_topology_context());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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
                    "name", "varchar(250)"
                },
                {
                    "layer_protocol_name", "mediumtext"
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
                    "name", "varchar(250)"
                },
                {
                    "operational_state", "varchar(250)"
                }
        };
        tablaNode = Util.crearTablasGenerico(dataBase,"exp_node",tablaNode,node);
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
                    "name","varchar(250)"
                },
                {
                    "operational_state","varchar(250)"
                },
                {
                    "supported_cep_layer_protocol_qualifier","mediumtext"
                },
                {
                    "administrative_state","varchar(250)"
                }
        };
        tablaOwnedNode = Util.crearTablasGenerico(dataBase,"exp_ownednode",tablaOwnedNode,ownednode);
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
        tablaAccessPort = Util.crearTablasGenerico(dataBase,"exp_accessport",tablaAccessPort,accessport);
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
                    if(null != ownedNode.getTapi_equipment_supporting_access_port().getAccess_port().getAccess_port_uuid()){
                        record.addField("access_port_uuid",ownedNode.getTapi_equipment_supporting_access_port().getAccess_port().getAccess_port_uuid());
                    }
                    if(null != ownedNode.getTapi_equipment_supporting_access_port().getAccess_port().getDevice_uuid()){
                        record.addField("device_uuid",ownedNode.getTapi_equipment_supporting_access_port().getAccess_port().getDevice_uuid());
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

}