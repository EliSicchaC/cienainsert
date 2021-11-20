package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TapiPhotonic {
    @JsonProperty("mc-pool")
    private McPool mc_pool;

    public TapiPhotonic(McPool mc_pool) {
        this.mc_pool = mc_pool;
    }
    public TapiPhotonic(){
    }

    public McPool getMc_pool() {
        return mc_pool;
    }

    public void setMc_pool(McPool mc_pool) {
        this.mc_pool = mc_pool;
    }
}
