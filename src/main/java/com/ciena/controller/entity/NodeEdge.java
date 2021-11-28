package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NodeEdge {
    @JsonProperty("topology-uuid")
    private String topology_uuid;
    @JsonProperty("node-uuid")
    private String node_uuid;
    @JsonProperty("node-edge-point-uuid")
    private String node_edge_point_uuid;

    public NodeEdge(String topology_uuid, String node_uuid, String node_edge_point_uuid) {
        this.topology_uuid = topology_uuid;
        this.node_uuid = node_uuid;
        this.node_edge_point_uuid = node_edge_point_uuid;
    }
    public NodeEdge(){
    }

    public String getTopology_uuid() {
        return topology_uuid;
    }

    public void setTopology_uuid(String topology_uuid) {
        this.topology_uuid = topology_uuid;
    }

    public String getNode_uuid() {
        return node_uuid;
    }

    public void setNode_uuid(String node_uuid) {
        this.node_uuid = node_uuid;
    }

    public String getNode_edge_point_uuid() {
        return node_edge_point_uuid;
    }

    public void setNode_edge_point_uuid(String node_edge_point_uuid) {
        this.node_edge_point_uuid = node_edge_point_uuid;
    }
}
