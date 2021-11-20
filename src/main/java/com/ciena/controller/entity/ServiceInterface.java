package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class ServiceInterface {

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("supported-layer-protocol-qualifier")
    private List<String> supported_layer_protocol_qualifier;
    @JsonProperty("lifecycle-state")
    private String lifecycle_state;
    @JsonProperty("total-potential-capacity")
    private PotencialCapacity total_potential_capacity;
    @JsonProperty("layer-protocol-name")
    private String layer_protocol_name;
    @JsonProperty("administrative-state")
    private String administrative_state;
    @JsonProperty("available-capacity")
    private PotencialCapacity available_capacity;
    @JsonProperty("direction")
    private String direction;
    @JsonProperty("operational-state")
    private String operational_state;
    @JsonProperty("name")
    private List<Name> name;

    public ServiceInterface(){
    }
    public ServiceInterface(String uuid, List<String> supported_layer_protocol_qualifier,
                            String lifecycle_state, PotencialCapacity total_potential_capacity,
                            String layer_protocol_name, String administrative_state,
                            PotencialCapacity available_capacity, String direction,
                            String operational_state, List<Name> name) {
        this.uuid = uuid;
        this.supported_layer_protocol_qualifier = supported_layer_protocol_qualifier;
        this.lifecycle_state = lifecycle_state;
        this.total_potential_capacity = total_potential_capacity;
        this.layer_protocol_name = layer_protocol_name;
        this.administrative_state = administrative_state;
        this.available_capacity = available_capacity;
        this.direction = direction;
        this.operational_state = operational_state;
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<String> getSupported_layer_protocol_qualifier() {
        return supported_layer_protocol_qualifier;
    }

    public void setSupported_layer_protocol_qualifier(List<String> supported_layer_protocol_qualifier) {
        this.supported_layer_protocol_qualifier = supported_layer_protocol_qualifier;
    }

    public String getLifecycle_state() {
        return lifecycle_state;
    }

    public void setLifecycle_state(String lifecycle_state) {
        this.lifecycle_state = lifecycle_state;
    }

    public PotencialCapacity getTotal_potential_capacity() {
        return total_potential_capacity;
    }

    public void setTotal_potential_capacity(PotencialCapacity total_potential_capacity) {
        this.total_potential_capacity = total_potential_capacity;
    }

    public String getLayer_protocol_name() {
        return layer_protocol_name;
    }

    public void setLayer_protocol_name(String layer_protocol_name) {
        this.layer_protocol_name = layer_protocol_name;
    }

    public String getAdministrative_state() {
        return administrative_state;
    }

    public void setAdministrative_state(String administrative_state) {
        this.administrative_state = administrative_state;
    }

    public PotencialCapacity getAvailable_capacity() {
        return available_capacity;
    }

    public void setAvailable_capacity(PotencialCapacity available_capacity) {
        this.available_capacity = available_capacity;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getOperational_state() {
        return operational_state;
    }

    public void setOperational_state(String operational_state) {
        this.operational_state = operational_state;
    }

    public List<Name> getName() {
        return name;
    }

    public void setName(List<Name> name) {
        this.name = name;
    }
}

