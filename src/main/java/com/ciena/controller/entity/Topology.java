package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Topology {
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("node")
    private List<Node> node;

    public Topology(String uuid, List<Node> node) {
        this.uuid = uuid;
        this.node = node;
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
}
