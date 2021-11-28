package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TopologyObject {
    @JsonProperty("topology-uuid")
    private String topology_uuid;

    @Override
    public String toString() {
        return "TopologyObject{" +
                "topology_uuid='" + topology_uuid + '\'' +
                '}';
    }

    public TopologyObject(String topology_uuid) {
        this.topology_uuid = topology_uuid;
    }
    public TopologyObject(){
    }

    public String getTopology_uuid() {
        return topology_uuid;
    }

    public void setTopology_uuid(String topology_uuid) {
        this.topology_uuid = topology_uuid;
    }
}
