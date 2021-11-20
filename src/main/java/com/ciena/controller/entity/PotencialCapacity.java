package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PotencialCapacity {
    @JsonProperty("total-size")
    private TotalSize total_size;

    public PotencialCapacity(TotalSize total_size) {
        this.total_size = total_size;
    }
    public PotencialCapacity(){

    }

    public TotalSize getTotal_size() {
        return total_size;
    }

    public void setTotal_size(TotalSize total_size) {
        this.total_size = total_size;
    }
}
