package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PhotonicService {
    @JsonProperty("otsi-capability")
    private OtsiCapability otsi_capability;

    public PhotonicService(OtsiCapability otsi_capability) {
        this.otsi_capability = otsi_capability;
    }
    public PhotonicService(){
    }

    public OtsiCapability getOtsi_capability() {
        return otsi_capability;
    }

    public void setOtsi_capability(OtsiCapability otsi_capability) {
        this.otsi_capability = otsi_capability;
    }
}
