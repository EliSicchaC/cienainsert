package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConnectorPin {
    @JsonProperty("connector-identification")
    private String connector_identification;
    @JsonProperty("pin-identification")
    private String pin_identification;
    @JsonProperty("equipment-uuid")
    private String equipment_uuid;

    @Override
    public String toString() {
        return "ConnectorPin{" +
                "connector_identification='" + connector_identification + '\'' +
                ", pin_identification='" + pin_identification + '\'' +
                ", equipment_uuid='" + equipment_uuid + '\'' +
                '}';
    }

    public ConnectorPin(String connector_identification, String pin_identification, String equipment_uuid) {
        this.connector_identification = connector_identification;
        this.pin_identification = pin_identification;
        this.equipment_uuid = equipment_uuid;
    }
    public ConnectorPin(){
    }


    public String getConnector_identification() {
        return connector_identification;
    }

    public void setConnector_identification(String connector_identification) {
        this.connector_identification = connector_identification;
    }

    public String getPin_identification() {
        return pin_identification;
    }

    public void setPin_identification(String pin_identification) {
        this.pin_identification = pin_identification;
    }

    public String getEquipment_uuid() {
        return equipment_uuid;
    }

    public void setEquipment_uuid(String equipment_uuid) {
        this.equipment_uuid = equipment_uuid;
    }
}
