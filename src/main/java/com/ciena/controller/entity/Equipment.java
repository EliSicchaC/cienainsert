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
    private ActualEquipment actual_equipment;
    @JsonProperty("contained-holder")
    private List<ContainedHolder>contained_holder;
    @JsonProperty("geographical-location")
    private String geographical_location;
    @JsonProperty("equipment-location")
    private String equipment_location;
    @JsonProperty("category")
    private String category;
    @JsonProperty("is-expected-actual-mismatch")
    private boolean is_expected_actual_mismatch  ;

    @Override
    public String toString() {
        return "Equipment{" +
                "uuid='" + uuid + '\'' +
                ", name=" + name +
                ", expected_equipment=" + expected_equipment +
                ", actual_equipment=" + actual_equipment +
                ", contained_holder=" + contained_holder +
                ", geographical_location='" + geographical_location + '\'' +
                ", equipment_location='" + equipment_location + '\'' +
                ", category='" + category + '\'' +
                ", is_expected_actual_mismatch=" + is_expected_actual_mismatch +
                '}';
    }

    public Equipment(String uuid, List<Name> name, List<ExpectedEquipment> expected_equipment, ActualEquipment actual_equipment, List<ContainedHolder> contained_holder, String geographical_location, String equipment_location, String category, boolean is_expected_actual_mismatch) {
        this.uuid = uuid;
        this.name = name;
        this.expected_equipment = expected_equipment;
        this.actual_equipment = actual_equipment;
        this.contained_holder = contained_holder;
        this.geographical_location = geographical_location;
        this.equipment_location = equipment_location;
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

    public ActualEquipment getActual_equipment() {
        return actual_equipment;
    }

    public void setActual_equipment(ActualEquipment actual_equipment) {
        this.actual_equipment = actual_equipment;
    }

    public List<ContainedHolder> getContained_holder() {
        return contained_holder;
    }

    public void setContained_holder(List<ContainedHolder> contained_holder) {
        this.contained_holder = contained_holder;
    }

    public String getGeographical_location() {
        return geographical_location;
    }

    public void setGeographical_location(String geographical_location) {
        this.geographical_location = geographical_location;
    }

    public String getEquipment_location() {
        return equipment_location;
    }

    public void setEquipment_location(String equipment_location) {
        this.equipment_location = equipment_location;
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
