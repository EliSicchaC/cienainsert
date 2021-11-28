package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Name {
    @JsonProperty("value-name")
    private String value_name;
    @JsonProperty("value")
    private String value;

    @Override
    public String toString() {
        return "Name{" +
                "value_name='" + value_name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public Name(String value_name, String value) {
        this.value_name = value_name;
        this.value = value;
    }
    public Name(){
    }

    public String getValue_name() {
        return value_name;
    }

    public void setValue_name(String value_name) {
        this.value_name = value_name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
