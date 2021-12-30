package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SupportingAcces {
    @JsonProperty("access-port")
    private AccessPortDevice access_port;

    @Override
    public String toString() {
        return "SupportingAcces{" +
                "access_port=" + access_port +
                '}';
    }

    public SupportingAcces(AccessPortDevice access_port) {
        this.access_port = access_port;
    }
    public SupportingAcces(){
    }

    public AccessPortDevice getAcces_port() {
        return access_port;
    }

    public void setAccess_port(AccessPortDevice access_port) {
        this.access_port = access_port;
    }
}
