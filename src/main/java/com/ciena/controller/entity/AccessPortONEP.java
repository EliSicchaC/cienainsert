package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AccessPortONEP {
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("connector-pin")
    private List<ConnectorPin>connector_pin;
    @JsonProperty("name")
    private List<Name>name;

    @Override
    public String toString() {
        return "AccessPort{" +
                "uuid='" + uuid + '\'' +
                ", connector_pin=" + connector_pin +
                ", name=" + name +
                '}';
    }

    public AccessPortONEP(String uuid, List<ConnectorPin> connector_pin, List<Name> name) {
        this.uuid = uuid;
        this.connector_pin = connector_pin;
        this.name = name;
    }
    public AccessPortONEP(){
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<ConnectorPin> getConnector_pin() {
        return connector_pin;
    }

    public void setConnector_pin(List<ConnectorPin> connector_pin) {
        this.connector_pin = connector_pin;
    }

    public List<Name> getName() {
        return name;
    }

    public void setName(List<Name> name) {
        this.name = name;
    }
}
