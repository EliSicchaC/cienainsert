package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SupportedStream {
    @JsonProperty("supported-stream-type-uuid")
    private String supported_stream_type_uuid;

    public SupportedStream(String supported_stream_type_uuid) {
        this.supported_stream_type_uuid = supported_stream_type_uuid;
    }
    public SupportedStream(){
    }

    public String getSupported_stream_type_uuid() {
        return supported_stream_type_uuid;
    }

    public void setSupported_stream_type_uuid(String supported_stream_type_uuid) {
        this.supported_stream_type_uuid = supported_stream_type_uuid;
    }
}
