package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OwnedNode {
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("termination-state")
    private String termination_state;
    @JsonProperty("termination-direction")
    private String termination_direction;
    @JsonProperty("layer-protocol-name")
    private String layer_protocol_name;
    @JsonProperty("lifecycle-state")
    private String lifecycle_state;
    @JsonProperty("name")
    private List<Name> name;
    @JsonProperty("operational-state")
    private String operational_state;
    @JsonProperty("tapi-equipment:supporting-access-port")
    private SupportingAcces tapi_equipment_supporting_access_port;
    @JsonProperty("supported-cep-layer-protocol-qualifier")
    private List<String>supported_cep_layer_protocol_qualifier;
    @JsonProperty("administrative-state")
    private String administrative_state;
    @JsonProperty("tapi-photonic-media:media-channel-node-edge-point-spec")
    private TapiPhotonic tapi_photonic_media_media_channel_node_edge_point_spec;

    public OwnedNode(String uuid, String termination_state, String termination_direction, String layer_protocol_name, String lifecycle_state, List<Name> name, String operational_state, SupportingAcces tapi_equipment_supporting_access_port, List<String> supported_cep_layer_protocol_qualifier, String administrative_state, TapiPhotonic tapi_photonic_media_media_channel_node_edge_point_spec) {
        this.uuid = uuid;
        this.termination_state = termination_state;
        this.termination_direction = termination_direction;
        this.layer_protocol_name = layer_protocol_name;
        this.lifecycle_state = lifecycle_state;
        this.name = name;
        this.operational_state = operational_state;
        this.tapi_equipment_supporting_access_port = tapi_equipment_supporting_access_port;
        this.supported_cep_layer_protocol_qualifier = supported_cep_layer_protocol_qualifier;
        this.administrative_state = administrative_state;
        this.tapi_photonic_media_media_channel_node_edge_point_spec = tapi_photonic_media_media_channel_node_edge_point_spec;
    }
    public OwnedNode(){
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTermination_state() {
        return termination_state;
    }

    public void setTermination_state(String termination_state) {
        this.termination_state = termination_state;
    }

    public String getTermination_direction() {
        return termination_direction;
    }

    public void setTermination_direction(String termination_direction) {
        this.termination_direction = termination_direction;
    }

    public String getLayer_protocol_name() {
        return layer_protocol_name;
    }

    public void setLayer_protocol_name(String layer_protocol_name) {
        this.layer_protocol_name = layer_protocol_name;
    }

    public String getLifecycle_state() {
        return lifecycle_state;
    }

    public void setLifecycle_state(String lifecycle_state) {
        this.lifecycle_state = lifecycle_state;
    }

    public List<Name> getName() {
        return name;
    }

    public void setName(List<Name> name) {
        this.name = name;
    }

    public String getOperational_state() {
        return operational_state;
    }

    public void setOperational_state(String operational_state) {
        this.operational_state = operational_state;
    }

    public SupportingAcces getTapi_equipment_supporting_access_port() {
        return tapi_equipment_supporting_access_port;
    }

    public void setTapi_equipment_supporting_access_port(SupportingAcces tapi_equipment_supporting_access_port) {
        this.tapi_equipment_supporting_access_port = tapi_equipment_supporting_access_port;
    }

    public List<String> getSupported_cep_layer_protocol_qualifier() {
        return supported_cep_layer_protocol_qualifier;
    }

    public void setSupported_cep_layer_protocol_qualifier(List<String> supported_cep_layer_protocol_qualifier) {
        this.supported_cep_layer_protocol_qualifier = supported_cep_layer_protocol_qualifier;
    }

    public String getAdministrative_state() {
        return administrative_state;
    }

    public void setAdministrative_state(String administrative_state) {
        this.administrative_state = administrative_state;
    }

    public TapiPhotonic getTapi_photonic_media_media_channel_node_edge_point_spec() {
        return tapi_photonic_media_media_channel_node_edge_point_spec;
    }

    public void setTapi_photonic_media_media_channel_node_edge_point_spec(TapiPhotonic tapi_photonic_media_media_channel_node_edge_point_spec) {
        this.tapi_photonic_media_media_channel_node_edge_point_spec = tapi_photonic_media_media_channel_node_edge_point_spec;
    }
}
