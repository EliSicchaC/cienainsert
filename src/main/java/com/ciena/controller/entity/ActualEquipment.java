package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ActualEquipment {
    @JsonProperty("common-equipment-properties")
    private EquipmentProperties actual_equipment;

    @Override
    public String toString() {
        return "ActualEquipment{" +
                "actual_equipment=" + actual_equipment +
                '}';
    }

    public ActualEquipment(EquipmentProperties actual_equipment) {
        this.actual_equipment = actual_equipment;
    }
    public ActualEquipment(){
    }

    public EquipmentProperties getActual_equipment() {
        return actual_equipment;
    }

    public void setActual_equipment(EquipmentProperties actual_equipment) {
        this.actual_equipment = actual_equipment;
    }
}
