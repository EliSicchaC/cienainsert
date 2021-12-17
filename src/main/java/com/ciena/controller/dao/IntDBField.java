package com.ciena.controller.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IntDBField extends DBField {
    int value;

    public IntDBField(String name, int value) {
        this.name = name;
        this.value = value;
    }

    int getSQLValue() {
        return  value;
    }



    @Override
    public void setParam(int row, PreparedStatement stat) throws SQLException {
        stat.setInt(row, value);
    }
}
