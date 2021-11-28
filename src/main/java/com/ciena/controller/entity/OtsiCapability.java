package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OtsiCapability {
    @JsonProperty("supportable-central-frequency-spectrum-band")
    private List<SupportableCentral> supportable_central_frequency_spectrum_band;

    public OtsiCapability(List<SupportableCentral> supportable_central_frequency_spectrum_band) {
        this.supportable_central_frequency_spectrum_band = supportable_central_frequency_spectrum_band;
    }
    public OtsiCapability(){
    }

    public List<SupportableCentral> getSupportable_central_frequency_spectrum_band() {
        return supportable_central_frequency_spectrum_band;
    }

    public void setSupportable_central_frequency_spectrum_band(List<SupportableCentral> supportable_central_frequency_spectrum_band) {
        this.supportable_central_frequency_spectrum_band = supportable_central_frequency_spectrum_band;
    }
}
