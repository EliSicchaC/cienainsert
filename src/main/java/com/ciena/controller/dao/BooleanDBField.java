package com.ciena.controller.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BooleanDBField extends DBField {
    Boolean value;

    public BooleanDBField(String name, Boolean value) {
        this.name = name;
        this.value = value;
    }

    Boolean getSQLValue() {
        return  value;
    }


    @Override
    public void setParam(int row, PreparedStatement stat) throws SQLException {
        stat.setBoolean(row, value);
    }
}
