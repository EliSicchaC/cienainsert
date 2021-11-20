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

    public Device(String uuid, List<Equipment> equipment, List<Name> name) {
        this.uuid = uuid;
        this.equipment = equipment;
        this.name = name;
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
}
