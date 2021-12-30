package com.ciena.controller.entity;

import com.ciena.controller.dao.DBRecord;
import com.ciena.controller.dao.DBTable;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.SQLException;

public class AccessPortDevice {
    @JsonProperty("access-port-uuid")
    private String access_port_uuid;
    @JsonProperty("device-uuid")
    private String device_uuid;

    @Override
    public String toString() {
        return "AccesPort{" +
                "access_port_uuid='" + access_port_uuid + '\'' +
                ", device_uuid='" + device_uuid + '\'' +
                '}';
    }

    public AccessPortDevice(String access_port_uuid, String device_uuid) {
        this.access_port_uuid = access_port_uuid;
        this.device_uuid = device_uuid;
    }
    public AccessPortDevice(){
    }

    public String getAcces_port_uuid() {
        return access_port_uuid;
    }

    public void setAccess_port_uuid(String access_port_uuid) {
        this.access_port_uuid = access_port_uuid;
    }

    public String getDevice_uuid() {
        return device_uuid;
    }

    public void setDevice_uuid(String device_uuid) {
        this.device_uuid = device_uuid;
    }

    public Boolean analizoAccsessPort(DBTable tablaAccessPort,String access_port_uuid,String device_uuid){
        DBRecord record = tablaAccessPort.newRecord();
        if(null != access_port_uuid){
            record.addField("access_port_uuid", access_port_uuid);
        }
        if(null != device_uuid){
            record.addField("device_uuid",device_uuid);
        }


        try {
            tablaAccessPort.insert(record);
        } catch (SQLException e) {
            System.out.println("Ex: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
