package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
@JsonInclude(JsonInclude.Include.ALWAYS)
public class TapiContext {
    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("service-interface-point")
    private List<ServiceInterface> service_interface_point;


    @JsonProperty("tapi-equipment:physical-context")
    private TapiEquipment tapi_equipment_physical_context;

    @JsonProperty("name")
    public List<Name> getName;
    @JsonProperty("tapi-streaming:stream-context")
    private TapiStreaming tapi_streaming_stream_context;
    @JsonProperty("tapi-topology:topology-context")
    private TapiTopology tapi_topology_topology_context;

    public TapiContext(List<Name> getName, String uuid, List<ServiceInterface> service_interface_point, TapiEquipment tapi_equipment_physical_context, TapiStreaming tapi_streaming_stream_context, TapiTopology tapi_topology_topology_context) {
        this.getName = getName;
        this.uuid = uuid;
        this.service_interface_point = service_interface_point;
        this.tapi_equipment_physical_context = tapi_equipment_physical_context;
        this.tapi_streaming_stream_context = tapi_streaming_stream_context;
        this.tapi_topology_topology_context = tapi_topology_topology_context;
    }
    public TapiContext(){
    }

    public List<Name> getGetName() {
        return getName;
    }

    public void setGetName(List<Name> getName) {
        this.getName = getName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<ServiceInterface> getService_interface_point() {
        return service_interface_point;
    }

    public void setService_interface_point(List<ServiceInterface> service_interface_point) {
        this.service_interface_point = service_interface_point;
    }

    public TapiEquipment getTapi_equipment_physical_context() {
        return tapi_equipment_physical_context;
    }

    public void setTapi_equipment_physical_context(TapiEquipment tapi_equipment_physical_context) {
        this.tapi_equipment_physical_context = tapi_equipment_physical_context;
    }

    public TapiStreaming getTapi_streaming_stream_context() {
        return tapi_streaming_stream_context;
    }

    public void setTapi_streaming_stream_context(TapiStreaming tapi_streaming_stream_context) {
        this.tapi_streaming_stream_context = tapi_streaming_stream_context;
    }

    public TapiTopology getTapi_topology_topology_context() {
        return tapi_topology_topology_context;
    }

    public void setTapi_topology_topology_context(TapiTopology tapi_topology_topology_context) {
        this.tapi_topology_topology_context = tapi_topology_topology_context;
    }
}