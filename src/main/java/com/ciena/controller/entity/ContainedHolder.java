package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContainedHolder {
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("actual-holder")
    private ActualHolder actual_holder;
    @JsonProperty("expected-holder")
    private ActualHolder expected_holder;
    @JsonProperty("occupying-fru")
    private OccupyingFru occupying_fru;

    @Override
    public String toString() {
        return "ContainedHolder{" +
                "uuid='" + uuid + '\'' +
                ", actual_holder=" + actual_holder +
                ", expected_holder=" + expected_holder +
                ", occupying_fru=" + occupying_fru +
                '}';
    }

    public ContainedHolder(String uuid, ActualHolder actual_holder, ActualHolder expected_holder, OccupyingFru occupying_fru) {
        this.uuid = uuid;
        this.actual_holder = actual_holder;
        this.expected_holder = expected_holder;
        this.occupying_fru = occupying_fru;
    }
    public ContainedHolder(){

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public ActualHolder getActual_holder() {
        return actual_holder;
    }

    public void setActual_holder(ActualHolder actual_holder) {
        this.actual_holder = actual_holder;
    }

    public ActualHolder getExpected_holder() {
        return expected_holder;
    }

    public void setExpected_holder(ActualHolder expected_holder) {
        this.expected_holder = expected_holder;
    }

    public OccupyingFru getOccupying_fru() {
        return occupying_fru;
    }

    public void setOccupying_fru(OccupyingFru occupying_fru) {
        this.occupying_fru = occupying_fru;
    }
}
