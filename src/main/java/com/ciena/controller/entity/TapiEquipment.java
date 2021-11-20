package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TapiEquipment {
    @JsonProperty("device")
    private List<Device> device;
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("name")
    private List<Name>name;

    public TapiEquipment(List<Device> device, String uuid, List<Name> name) {
        this.device = device;
        this.uuid = uuid;
        this.name = name;
    }
    public TapiEquipment(){
    }

    public List<Device> getDevice() {
        return device;
    }

    public void setDevice(List<Device> device) {
        this.device = device;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<Name> getName() {
        return name;
    }

    public void setName(List<Name> name) {
        this.name = name;
    }
}
