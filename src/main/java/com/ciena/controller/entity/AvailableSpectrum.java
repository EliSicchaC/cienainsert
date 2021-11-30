package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AvailableSpectrum {
    @JsonProperty("upper-frequency")
    private int upper_frequency;
    @JsonProperty("lower-frequency")
    private int lower_frequency;
    @JsonProperty("frequency-constraint")
    private Frequency frequency_constraint;

    @Override
    public String toString() {
        return "AvailableSpectrum{" +
                "upper_frequency=" + upper_frequency +
                ", lower_frequency=" + lower_frequency +
                ", frequency_constraint=" + frequency_constraint +
                '}';
    }

    public AvailableSpectrum(int upper_frequency, int lower_frequency, Frequency frequency_constraint) {
        this.upper_frequency = upper_frequency;
        this.lower_frequency = lower_frequency;
        this.frequency_constraint = frequency_constraint;
    }
    public AvailableSpectrum(){
    }

    public int getUpper_frequency() {
        return upper_frequency;
    }

    public void setUpper_frequency(int upper_frequency) {
        this.upper_frequency = upper_frequency;
    }

    public int getLower_frequency() {
        return lower_frequency;
    }

    public void setLower_frequency(int lower_frequency) {
        this.lower_frequency = lower_frequency;
    }

    public Frequency getFrequency_constraint() {
        return frequency_constraint;
    }

    public void setFrequency_constraint(Frequency frequency_constraint) {
        this.frequency_constraint = frequency_constraint;
    }
}
