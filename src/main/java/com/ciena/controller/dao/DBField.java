package com.ciena.controller.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class DBField {
    String name;

    public String getName() {
        return name;
    }

    public abstract void setParam(int row, PreparedStatement stat) throws SQLException;
}
