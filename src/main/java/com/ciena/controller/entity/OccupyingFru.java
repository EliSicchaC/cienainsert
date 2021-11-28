package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OccupyingFru {
    @JsonProperty("equipment-uuid")
    private String equipment_uuid;
    @JsonProperty("device-uuid")
    private String device_uuid;

    @Override
    public String toString() {
        return "OccupyingFru{" +
                "equipment_uuid='" + equipment_uuid + '\'' +
                ", device_uuid='" + device_uuid + '\'' +
                '}';
    }

    public OccupyingFru(String equipment_uuid, String device_uuid) {
        this.equipment_uuid = equipment_uuid;
        this.device_uuid = device_uuid;
    }
    public OccupyingFru(){

    }

    public String getEquipment_uuid() {
        return equipment_uuid;
    }

    public void setEquipment_uuid(String equipment_uuid) {
        this.equipment_uuid = equipment_uuid;
    }

    public String getDevice_uuid() {
        return device_uuid;
    }

    public void setDevice_uuid(String device_uuid) {
        this.device_uuid = device_uuid;
    }
}
