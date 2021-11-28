package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExpectedEquipment {
    @JsonProperty("common-equipment-properties")
    private EquipmentProperties common_equipment_properties;

    @Override
    public String toString() {
        return "ExpectedEquipment{" +
                "common_equipment_properties=" + common_equipment_properties +
                '}';
    }

    public ExpectedEquipment(EquipmentProperties common_equipment_properties) {
        this.common_equipment_properties = common_equipment_properties;
    }
    public ExpectedEquipment(){
    }

    public EquipmentProperties getCommon_equipment_properties() {
        return common_equipment_properties;
    }

    public void setCommon_equipment_properties(EquipmentProperties common_equipment_properties) {
        this.common_equipment_properties = common_equipment_properties;
    }
}
