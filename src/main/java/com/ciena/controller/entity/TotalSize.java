package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TotalSize {
    @JsonProperty("unit")
    private String unit;
    @JsonProperty("value")
    private int value;

    public TotalSize(String unit, int value) {
        this.unit = unit;
        this.value = value;
    }
    public TotalSize(){
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
