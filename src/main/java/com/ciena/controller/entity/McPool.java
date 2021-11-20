package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class McPool {
    @JsonProperty("supportable-spectrum")
    private List<Spectrum> supportable_spectrum;
    @JsonProperty("available-spectrum")
    private List<Spectrum> available_spectrum;

    public McPool(List<Spectrum> supportable_spectrum, List<Spectrum> available_spectrum) {
        this.supportable_spectrum = supportable_spectrum;
        this.available_spectrum = available_spectrum;
    }
    public McPool(){
    }

    public List<Spectrum> getSupportable_spectrum() {
        return supportable_spectrum;
    }

    public void setSupportable_spectrum(List<Spectrum> supportable_spectrum) {
        this.supportable_spectrum = supportable_spectrum;
    }

    public List<Spectrum> getAvailable_spectrum() {
        return available_spectrum;
    }

    public void setAvailable_spectrum(List<Spectrum> available_spectrum) {
        this.available_spectrum = available_spectrum;
    }
}
