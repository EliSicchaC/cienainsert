package com.ciena.controller.entity;

import com.ciena.controller.dao.DBRecord;
import com.ciena.controller.dao.DBTable;
import com.fasterxml.jackson.annotation.JsonProperty;
import util.Util;

import java.sql.SQLException;
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

    @Override
    public String toString() {
        return "OwnedNode{" +
                "uuid='" + uuid + '\'' +
                ", termination_state='" + termination_state + '\'' +
                ", termination_direction='" + termination_direction + '\'' +
                ", layer_protocol_name='" + layer_protocol_name + '\'' +
                ", lifecycle_state='" + lifecycle_state + '\'' +
                ", name=" + name +
                ", operational_state='" + operational_state + '\'' +
                ", tapi_equipment_supporting_access_port=" + tapi_equipment_supporting_access_port +
                ", supported_cep_layer_protocol_qualifier=" + supported_cep_layer_protocol_qualifier +
                ", administrative_state='" + administrative_state + '\'' +
                ", tapi_photonic_media_media_channel_node_edge_point_spec=" + tapi_photonic_media_media_channel_node_edge_point_spec +
                '}';
    }

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
    public Boolean analizarOwnedNode(List<OwnedNode>ownedNodeList, DBTable tablaOwnedNode,String topology_uuid,String node_uuid){
        DBRecord record = tablaOwnedNode.newRecord();
        for(OwnedNode ownedNode : ownedNodeList){
            record = tablaOwnedNode.newRecord();
            record.addField("uuid",ownedNode.getUuid());
            record.addField("termination_state",ownedNode.getTermination_state());
            record.addField("termination_direction",ownedNode.getTermination_direction());
            record.addField("layer_protocol_name",ownedNode.getLayer_protocol_name());
            record.addField("lifecycle_state",ownedNode.getLifecycle_state());
            String nameStringValue = Util.generateNames(ownedNode.getName());
            System.out.println("nameStringValue: " + nameStringValue);
            record.addField("name", nameStringValue);
            record.addField("operational_state",ownedNode.getOperational_state());
            record.addField("supported_cep_layer_protocol_qualifier",ownedNode.getSupported_cep_layer_protocol_qualifier().toString());
            record.addField("administrative_state",ownedNode.getAdministrative_state());
            record.addField("uuid_topology",topology_uuid);
            record.addField("uuid_node",node_uuid);
            try {
                tablaOwnedNode.insert(record);
            } catch (SQLException e) {
                System.out.println("Ex: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }
}
