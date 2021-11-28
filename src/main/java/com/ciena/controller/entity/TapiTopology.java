package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TapiTopology {
    @JsonProperty("nw-topology-service")
    private TopologyService nw_topology_service;
    @JsonProperty("topology")
    private List<Topology> topology;

    @Override
    public String toString() {
        return "TapiTopology{" +
                "nw_topology_service=" + nw_topology_service +
                ", topology=" + topology +
                '}';
    }

    public TapiTopology(TopologyService nw_topology_service, List<Topology> topology) {
        this.nw_topology_service = nw_topology_service;
        this.topology = topology;
    }
    public TapiTopology(){
    }

    public TopologyService getNw_topology_service() {
        return nw_topology_service;
    }

    public void setNw_topology_service(TopologyService nw_topology_service) {
        this.nw_topology_service = nw_topology_service;
    }

    public List<Topology> getTopology() {
        return topology;
    }

    public void setTopology(List<Topology> topology) {
        this.topology = topology;
    }
}
