package com.ciena.controller.entity;

import com.ciena.controller.dao.DBRecord;
import com.ciena.controller.dao.DBTable;
import com.fasterxml.jackson.annotation.JsonProperty;
import util.Util;

import java.sql.SQLException;
import java.util.List;

public class Topology {
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("node")
    private List<Node> node;
    @JsonProperty("name")
    private List<Name>name;
    @JsonProperty("link")
    private List<Link>link;
    @JsonProperty("layer-protocol-name")
    private List<String>layer_protocol_name;

    @Override
    public String toString() {
        return "Topology{" +
                "uuid='" + uuid + '\'' +
                ", node=" + node +
                ", name=" + name +
                ", link=" + link +
                ", layer_protocol_name=" + layer_protocol_name +
                '}';
    }

    public Topology(String uuid, List<Node> node, List<Name> name, List<Link> link, List<String> layer_protocol_name) {
        this.uuid = uuid;
        this.node = node;
        this.name = name;
        this.link = link;
        this.layer_protocol_name = layer_protocol_name;
    }
    public Topology(){
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<Node> getNode() {
        return node;
    }

    public void setNode(List<Node> node) {
        this.node = node;
    }

    public List<Name> getName() {
        return name;
    }

    public void setName(List<Name> name) {
        this.name = name;
    }

    public List<Link> getLink() {
        return link;
    }

    public void setLink(List<Link> link) {
        this.link = link;
    }

    public List<String> getLayer_protocol_name() {
        return layer_protocol_name;
    }

    public void setLayer_protocol_name(List<String> layer_protocol_name) {
        this.layer_protocol_name = layer_protocol_name;
    }

    public Boolean insertaTopology(List<Topology> topologies, DBTable tablaTopology){
        for(Topology topology : topologies){
            DBRecord record = tablaTopology.newRecord();
            record.addField("uuid", topology.getUuid());
            String nameStringValue = Util.generateNames(topology.getName());
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
        return null;
    }
}
