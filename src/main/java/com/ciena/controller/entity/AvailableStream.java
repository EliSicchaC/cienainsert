package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AvailableStream {
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("supported-stream-type")
    private SupportedStream supported_stream_type;
    @JsonProperty("stream-state")
    private String stream_state;
    @JsonProperty("stream-id")
    private String stream_id;
    @JsonProperty("connection-protocol")
    private String connection_protocol;
    @JsonProperty("connection-address")
    private String connection_address;
    @JsonProperty("name")
    private List<Name> name;

    public AvailableStream(String uuid, SupportedStream supported_stream_type, String stream_state, String stream_id, String connection_protocol, String connection_address, List<Name> name) {
        this.uuid = uuid;
        this.supported_stream_type = supported_stream_type;
        this.stream_state = stream_state;
        this.stream_id = stream_id;
        this.connection_protocol = connection_protocol;
        this.connection_address = connection_address;
        this.name = name;
    }
    public AvailableStream(){
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public SupportedStream getSupported_stream_type() {
        return supported_stream_type;
    }

    public void setSupported_stream_type(SupportedStream supported_stream_type) {
        this.supported_stream_type = supported_stream_type;
    }

    public String getStream_state() {
        return stream_state;
    }

    public void setStream_state(String stream_state) {
        this.stream_state = stream_state;
    }

    public String getStream_id() {
        return stream_id;
    }

    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
    }

    public String getConnection_protocol() {
        return connection_protocol;
    }

    public void setConnection_protocol(String connection_protocol) {
        this.connection_protocol = connection_protocol;
    }

    public String getConnection_address() {
        return connection_address;
    }

    public void setConnection_address(String connection_address) {
        this.connection_address = connection_address;
    }

    public List<Name> getName() {
        return name;
    }

    public void setName(List<Name> name) {
        this.name = name;
    }
}
