package com.ciena.controller.entity;

public class SupportingAcces {
    private AccesPort access_port;

    public SupportingAcces(AccesPort access_port) {
        this.access_port = access_port;
    }
    public SupportingAcces(){
    }

    public AccesPort getAccess_port() {
        return access_port;
    }

    public void setAccess_port(AccesPort access_port) {
        this.access_port = access_port;
    }
}
