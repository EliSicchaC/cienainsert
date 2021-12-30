package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Device {
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("equipment")
    private List<Equipment> equipment;
    @JsonProperty("name")
    private List<Name> name;
    @JsonProperty("access-port")
    private List<AccessPortONEP>access_port;

    @Override
    public String toString() {
        return "Device{" +
                "uuid='" + uuid + '\'' +
                ", equipment=" + equipment +
                ", name=" + name +
                ", access_port=" + access_port +
                '}';
    }

    public Device(String uuid, List<Equipment> equipment, List<Name> name, List<AccessPortONEP> access_port) {
        this.uuid = uuid;
        this.equipment = equipment;
        this.name = name;
        this.access_port = access_port;
    }
    public Device(){
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Equipment> equipment) {
        this.equipment = equipment;
    }

    public List<Name> getName() {
        return name;
    }

    public void setName(List<Name> name) {
        this.name = name;
    }

    public List<AccessPortONEP> getAccess_port() {
        return access_port;
    }

    public void setAccess_port(List<AccessPortONEP> access_port) {
        this.access_port = access_port;
    }
}
