package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ActualHolder {
    @JsonProperty("common-holder-properties")
    private CommonHolder common_holder_properties;

    @Override
    public String toString() {
        return "ActualHolder{" +
                "common_holder_properties=" + common_holder_properties +
                '}';
    }

    public ActualHolder(CommonHolder common_holder_properties) {
        this.common_holder_properties = common_holder_properties;
    }
    public ActualHolder(){
    }

    public CommonHolder getCommon_holder_properties() {
        return common_holder_properties;
    }

    public void setCommon_holder_properties(CommonHolder common_holder_properties) {
        this.common_holder_properties = common_holder_properties;
    }
}
