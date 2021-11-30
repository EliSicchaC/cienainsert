package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SupportingAcces {
    @JsonProperty("access-port")
    private AccesPort access_port;

    @Override
    public String toString() {
        return "SupportingAcces{" +
                "access_port=" + access_port +
                '}';
    }

    public SupportingAcces(AccesPort access_port) {
        this.access_port = access_port;
    }
    public SupportingAcces(){
    }

    public AccesPort getAcces_port() {
        return access_port;
    }

    public void setAccess_port(AccesPort access_port) {
        this.access_port = access_port;
    }
}
