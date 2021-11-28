package com.ciena.controller.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StringDBField extends DBField {
    String value;

    public StringDBField(String name, String value) {
        this.name = name;
        this.value = value;
    }

    String getSQLValue() {
        return  value;
    }



    @Override
    public void setParam(int row, PreparedStatement stat) throws SQLException {
        stat.setString(row, value);
    }
}
