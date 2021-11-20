package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Equipment {
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("name")
    private List<Name>name;
    @JsonProperty("expected-equipment")
    private List<ExpectedEquipment>expected_equipment;
    @JsonProperty("actual-equipment")
    private ExpectedEquipment actual_equipment;
    @JsonProperty("category")
    private String category;
    @JsonProperty("is-expected-actual-mismatch")
    private boolean is_expected_actual_mismatch;

    public Equipment(String uuid, List<Name> name, List<ExpectedEquipment> expected_equipment, ExpectedEquipment actual_equipment, String category, boolean is_expected_actual_mismatch) {
        this.uuid = uuid;
        this.name = name;
        this.expected_equipment = expected_equipment;
        this.actual_equipment = actual_equipment;
        this.category = category;
        this.is_expected_actual_mismatch = is_expected_actual_mismatch;
    }
    public Equipment(){
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

    public List<ExpectedEquipment> getExpected_equipment() {
        return expected_equipment;
    }

    public void setExpected_equipment(List<ExpectedEquipment> expected_equipment) {
        this.expected_equipment = expected_equipment;
    }

    public ExpectedEquipment getActual_equipment() {
        return actual_equipment;
    }

    public void setActual_equipment(ExpectedEquipment actual_equipment) {
        this.actual_equipment = actual_equipment;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isIs_expected_actual_mismatch() {
        return is_expected_actual_mismatch;
    }

    public void setIs_expected_actual_mismatch(boolean is_expected_actual_mismatch) {
        this.is_expected_actual_mismatch = is_expected_actual_mismatch;
    }
}
