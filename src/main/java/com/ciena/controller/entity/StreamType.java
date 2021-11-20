package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StreamType {
    @JsonProperty("uudi")
    private String uudi;
    @JsonProperty("record-retention")
    private String record_retention;
    @JsonProperty("stream-type-name")
    private String stream_type_name;
    @JsonProperty("log-storage-strategy")
    private String log_storage_strategy;
    @JsonProperty("segment-size")
    private String segment_size;
    @JsonProperty("log-record-strategy")
    private String log_record_strategy;
    @JsonProperty("record-content")
    private List<String> record_content;
    @JsonProperty("name")
    private List<Name>name;
    @JsonProperty("connection-protocol-details")
    private ConnectionProtocol connection_protocol_details;

    public StreamType(String uudi, String record_retention, String stream_type_name, String log_storage_strategy, String segment_size, String log_record_strategy, List<String> record_content, List<Name> name, ConnectionProtocol connection_protocol_details) {
        this.uudi = uudi;
        this.record_retention = record_retention;
        this.stream_type_name = stream_type_name;
        this.log_storage_strategy = log_storage_strategy;
        this.segment_size = segment_size;
        this.log_record_strategy = log_record_strategy;
        this.record_content = record_content;
        this.name = name;
        this.connection_protocol_details = connection_protocol_details;
    }
    public StreamType(){
    }

    public String getUudi() {
        return uudi;
    }

    public void setUudi(String uudi) {
        this.uudi = uudi;
    }

    public String getRecord_retention() {
        return record_retention;
    }

    public void setRecord_retention(String record_retention) {
        this.record_retention = record_retention;
    }

    public String getStream_type_name() {
        return stream_type_name;
    }

    public void setStream_type_name(String stream_type_name) {
        this.stream_type_name = stream_type_name;
    }

    public String getLog_storage_strategy() {
        return log_storage_strategy;
    }

    public void setLog_storage_strategy(String log_storage_strategy) {
        this.log_storage_strategy = log_storage_strategy;
    }

    public String getSegment_size() {
        return segment_size;
    }

    public void setSegment_size(String segment_size) {
        this.segment_size = segment_size;
    }

    public String getLog_record_strategy() {
        return log_record_strategy;
    }

    public void setLog_record_strategy(String log_record_strategy) {
        this.log_record_strategy = log_record_strategy;
    }

    public List<String> getRecord_content() {
        return record_content;
    }

    public void setRecord_content(List<String> record_content) {
        this.record_content = record_content;
    }

    public List<Name> getName() {
        return name;
    }

    public void setName(List<Name> name) {
        this.name = name;
    }

    public ConnectionProtocol getConnection_protocol_details() {
        return connection_protocol_details;
    }

    public void setConnection_protocol_details(ConnectionProtocol connection_protocol_details) {
        this.connection_protocol_details = connection_protocol_details;
    }
}
