package com.ciena.controller.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

// JSON INCLUDE ME VA PERMITIR HACER UNA RELACION DE NOMBRES ENTRE MI ARCHIVO CON MIS CLASES
@JsonInclude(JsonInclude.Include.ALWAYS)
public class mainObjects {
    // POR CADA PROPIEDADQUE QUIERO RELACIONAR DEBO PONER EL NOMBRE TAL CUAL ESTA EN EL ARCHIVO COMO ES EL CASO DE TAPI-COMMON:CONTEXT
    // ESTO DEBE HACERSE CON TODAS LAS CLASES
    @JsonProperty("tapi-common:context")
    private TapiContext tapi_common_context;

    public mainObjects(TapiContext tapi_common_context) {
        this.tapi_common_context = tapi_common_context;
    }
    public mainObjects() {
    }
    public TapiContext getTapi_common_context() {
        return tapi_common_context;
    }

    public void setTapi_common_context(TapiContext tapi_common_context) {
        this.tapi_common_context = tapi_common_context;
    }
}



