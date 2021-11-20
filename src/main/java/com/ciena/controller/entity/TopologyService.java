package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TopologyService {
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("topology")
    private List<TopologyObject> topology;
    @JsonProperty("name")
    private List <Name>name;

    public TopologyService(String uuid, List<TopologyObject> topology, List<Name> name) {
        this.uuid = uuid;
        this.topology = topology;
        this.name = name;
    }
    public TopologyService(){
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<TopologyObject> getTopology() {
        return topology;
    }

    public void setTopology(List<TopologyObject> topology) {
        this.topology = topology;
    }

    public List<Name> getName() {
        return name;
    }

    public void setName(List<Name> name) {
        this.name = name;
    }
}
