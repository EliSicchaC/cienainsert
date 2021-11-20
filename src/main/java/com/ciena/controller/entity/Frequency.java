package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Frequency {
    @JsonProperty("adjustment-granularity")
    private String adjustment_granularity;
    @JsonProperty("grid-type")
    private String grid_type;

    public Frequency(String adjustment_granularity, String grid_type) {
        this.adjustment_granularity = adjustment_granularity;
        this.grid_type = grid_type;
    }
    public Frequency(){
    }

    public String getAdjustment_granularity() {
        return adjustment_granularity;
    }

    public void setAdjustment_granularity(String adjustment_granularity) {
        this.adjustment_granularity = adjustment_granularity;
    }

    public String getGrid_type() {
        return grid_type;
    }

    public void setGrid_type(String grid_type) {
        this.grid_type = grid_type;
    }
}
