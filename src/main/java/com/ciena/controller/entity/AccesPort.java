package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccesPort {
    @JsonProperty("access-port-uuid")
    private String access_port_uuid;
    @JsonProperty("device-uuid")
    private String device_uuid;

    public AccesPort(String access_port_uuid, String device_uuid) {
        this.access_port_uuid = access_port_uuid;
        this.device_uuid = device_uuid;
    }
    public AccesPort(){
    }

    public String getAccess_port_uuid() {
        return access_port_uuid;
    }

    public void setAccess_port_uuid(String access_port_uuid) {
        this.access_port_uuid = access_port_uuid;
    }

    public String getDevice_uuid() {
        return device_uuid;
    }

    public void setDevice_uuid(String device_uuid) {
        this.device_uuid = device_uuid;
    }
}
