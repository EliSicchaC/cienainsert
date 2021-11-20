package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ConnectionProtocol {
    @JsonProperty("allowed-connection-protocols")
    private List<String> allowed_connection_protocols;

    public ConnectionProtocol(List<String> allowed_connection_protocols) {
        this.allowed_connection_protocols = allowed_connection_protocols;
    }
    public ConnectionProtocol(){
    }

    public List<String> getAllowed_connection_protocols() {
        return allowed_connection_protocols;
    }

    public void setAllowed_connection_protocols(List<String> allowed_connection_protocols) {
        this.allowed_connection_protocols = allowed_connection_protocols;
    }
}
