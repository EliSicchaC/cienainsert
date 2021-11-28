package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Node {
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("lifecycle-state")
    private String lifecycle_state;
    @JsonProperty("name")
    private List<Name> name;
    @JsonProperty("operational-state")
    private String operational_state;
    @JsonProperty("owned-node-edge-point")
    private List<OwnedNode>owned_node_edge_point;

    @Override
    public String toString() {
        return "Node{" +
                "uuid='" + uuid + '\'' +
                ", lifecycle_state='" + lifecycle_state + '\'' +
                ", name=" + name +
                ", operational_state='" + operational_state + '\'' +
                ", owned_node_edge_point=" + owned_node_edge_point +
                '}';
    }

    public Node(String uuid, String lifecycle_state, List<Name> name, String operational_state, List<OwnedNode> owned_node_edge_point) {
        this.uuid = uuid;
        this.lifecycle_state = lifecycle_state;
        this.name = name;
        this.operational_state = operational_state;
        this.owned_node_edge_point = owned_node_edge_point;
    }
    public Node(){
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public List<OwnedNode> getOwned_node_edge_point() {
        return owned_node_edge_point;
    }

    public void setOwned_node_edge_point(List<OwnedNode> owned_node_edge_point) {
        this.owned_node_edge_point = owned_node_edge_point;
    }
}
