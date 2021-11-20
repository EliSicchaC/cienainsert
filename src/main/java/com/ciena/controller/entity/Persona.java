package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Persona {
   // @JsonPropertyOrder({ "name", "id" })


    @JsonProperty("nombre:t")
    private String nombre;

    @JsonProperty("edad:t")
    private int edad;
    @JsonProperty("tapi-common:context")
    private TapiContext tapi_common_context;

    public Persona(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }
    public Persona() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public TapiContext getTapi_common_context() {
        return tapi_common_context;
    }

    public void setTapi_common_context(TapiContext tapi_common_context) {
        this.tapi_common_context = tapi_common_context;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
}
