package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SupportableCentral {
    @JsonProperty("lower-central-frequency")
    private int lower_central_frequency;
    @JsonProperty("upper-central-frequency")
    private int upper_central_frequency;
    @JsonProperty("frequency-constraint")
    private FrequencyConstraint frequency_constraint;

    public SupportableCentral(int lower_central_frequency, int upper_central_frequency, FrequencyConstraint frequency_constraint) {
        this.lower_central_frequency = lower_central_frequency;
        this.upper_central_frequency = upper_central_frequency;
        this.frequency_constraint = frequency_constraint;
    }
    public SupportableCentral(){
    }

    public int getLower_central_frequency() {
        return lower_central_frequency;
    }

    public void setLower_central_frequency(int lower_central_frequency) {
        this.lower_central_frequency = lower_central_frequency;
    }

    public int getUpper_central_frequency() {
        return upper_central_frequency;
    }

    public void setUpper_central_frequency(int upper_central_frequency) {
        this.upper_central_frequency = upper_central_frequency;
    }

    public FrequencyConstraint getFrequency_constraint() {
        return frequency_constraint;
    }

    public void setFrequency_constraint(FrequencyConstraint frequency_constraint) {
        this.frequency_constraint = frequency_constraint;
    }
}
