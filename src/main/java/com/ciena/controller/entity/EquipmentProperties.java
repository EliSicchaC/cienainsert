package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EquipmentProperties {
    @JsonProperty("equipment-type-version")
    private String equipment_type_version;

    @JsonProperty("manufacturer-name")
    private String manufacturer_name;

    @JsonProperty("equipment-type-name")
    private String equipment_type_name;

    public EquipmentProperties(String equipment_type_version, String manufacturer_name, String equipment_type_name) {
        this.equipment_type_version = equipment_type_version;
        this.manufacturer_name = manufacturer_name;
        this.equipment_type_name = equipment_type_name;
    }
    public EquipmentProperties(){
    }

    public String getEquipment_type_version() {
        return equipment_type_version;
    }

    public void setEquipment_type_version(String equipment_type_version) {
        this.equipment_type_version = equipment_type_version;
    }

    public String getManufacturer_name() {
        return manufacturer_name;
    }

    public void setManufacturer_name(String manufacturer_name) {
        this.manufacturer_name = manufacturer_name;
    }

    public String getEquipment_type_name() {
        return equipment_type_name;
    }

    public void setEquipment_type_name(String equipment_type_name) {
        this.equipment_type_name = equipment_type_name;
    }
}
