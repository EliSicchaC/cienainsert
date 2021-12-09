package com.ciena.controller.entity;

import com.ciena.controller.dao.DBRecord;
import com.ciena.controller.dao.DBTable;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.SQLException;
import java.util.List;

public class Link {
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("node-edge-point")
    private List<NodeEdge> node_edge_point;
    @JsonProperty("lifecycle-state")
    private String lifecycle_state;
    @JsonProperty("name")
    private List<Name>name;
    @JsonProperty("operational-state")
    private String operational_state;
    @JsonProperty("administrative-state")
    private String administrative_state;
    @JsonProperty("direction")
    private String direction;
    @JsonProperty("layer-protocol-name")
    private List<String>layer_protocol_name;

    @Override
    public String toString() {
        return "Link{" +
                "uuid='" + uuid + '\'' +
                ", node_edge_point=" + node_edge_point +
                ", lifecycle_state='" + lifecycle_state + '\'' +
                ", name=" + name +
                ", operational_state='" + operational_state + '\'' +
                ", administrative_state='" + administrative_state + '\'' +
                ", direction='" + direction + '\'' +
                ", layer_protocol_name=" + layer_protocol_name +
                '}';
    }

    public Link(String uuid, List<NodeEdge> node_edge_point, String lifecycle_state, List<Name> name, String operational_state, String administrative_state, String direction, List<String> layer_protocol_name) {
        this.uuid = uuid;
        this.node_edge_point = node_edge_point;
        this.lifecycle_state = lifecycle_state;
        this.name = name;
        this.operational_state = operational_state;
        this.administrative_state = administrative_state;
        this.direction = direction;
        this.layer_protocol_name = layer_protocol_name;
    }
    public Link(){
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<NodeEdge> getNode_edge_point() {
        return node_edge_point;
    }

    public void setNode_edge_point(List<NodeEdge> node_edge_point) {
        this.node_edge_point = node_edge_point;
    }

    public String getLifecycle_state() {
        return lifecycle_state;
    }

    public void setLifecycle_state(String lifecycle_state) {
        this.lifecycle_state = lifecycle_state;
    }

    public List<Name> getName() {
        return name;
    }

    public void setName(List<Name> name) {
        this.name = name;
    }

    public String getOperational_state() {
        return operational_state;
    }

    public void setOperational_state(String operational_state) {
        this.operational_state = operational_state;
    }

    public String getAdministrative_state() {
        return administrative_state;
    }

    public void setAdministrative_state(String administrative_state) {
        this.administrative_state = administrative_state;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public List<String> getLayer_protocol_name() {
        return layer_protocol_name;
    }

    public void setLayer_protocol_name(List<String> layer_protocol_name) {
        this.layer_protocol_name = layer_protocol_name;
    }
    public Boolean analizoLink(List<Link>links, DBTable tablaLink,String topology_uuid){
        DBRecord record = tablaLink.newRecord();
        for (Link link : links){
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
            record.addField("uuid_topology",topology_uuid);
            try {
                tablaLink.insert(record);
            } catch (SQLException e) {
                System.out.println("Ex: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }
}
