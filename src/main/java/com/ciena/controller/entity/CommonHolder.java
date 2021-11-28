package com.ciena.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommonHolder {
    @JsonProperty("holder-category")
    private String holder_category;
    @JsonProperty("is-guided")
    private boolean is_guided;
    @JsonProperty("holder-location")
    private String holder_location;

    @Override
    public String toString() {
        return "CommonHolder{" +
                "holder_category='" + holder_category + '\'' +
                ", is_guided=" + is_guided +
                ", holder_location='" + holder_location + '\'' +
                '}';
    }

    public CommonHolder(String holder_category, boolean is_guided, String holder_location) {
        this.holder_category = holder_category;
        this.is_guided = is_guided;
        this.holder_location = holder_location;
    }
    public CommonHolder(){
    }

    public String getHolder_category() {
        return holder_category;
    }

    public void setHolder_category(String holder_category) {
        this.holder_category = holder_category;
    }

    public boolean isIs_guided() {
        return is_guided;
    }

    public void setIs_guided(boolean is_guided) {
        this.is_guided = is_guided;
    }

    public String getHolder_location() {
        return holder_location;
    }

    public void setHolder_location(String holder_location) {
        this.holder_location = holder_location;
    }
}
