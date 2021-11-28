package com.ciena.controller.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBRecord {
   List<DBField> fields = new ArrayList<>();

    String tableName;

    DBRecord(String tableName) {
        this.tableName = tableName;
    }

    public void addField(String name, String value) {
        fields.add(new StringDBField(name, value));
    }

    public void addField(String name, boolean value) {
       fields.add(new BooleanDBField(name, value));
    }

    public void addField(String name, int value) {
      //  fields.add(new IntDBField(name, value));
    }

    public int doTheInsertIncrement(Connection conn) throws SQLException {
        PreparedStatement stat = getPreparedStatement(conn);
        stat.executeUpdate();
        int autoIncKeyFromApi = -1;

        ResultSet rs = stat.getGeneratedKeys();

        if (rs.next()) {
            autoIncKeyFromApi = rs.getInt(1);
        }
        return autoIncKeyFromApi;
    }
    public void doTheInsert(Connection conn) throws SQLException {

        PreparedStatement stat = getPreparedStatement(conn);
        stat.executeUpdate();
    }

    private PreparedStatement getPreparedStatement(Connection conn) throws SQLException {
        StringBuffer fieldNames = new StringBuffer();
        StringBuffer values = new StringBuffer();
        String prefix = "";
        for (DBField field : fields) {
            fieldNames.append(prefix + field.getName());
            values.append(prefix + "?");

            prefix = ", ";
        }

        String sql = "INSERT INTO " + tableName + " (" + fieldNames.toString() + ") "
                + "\n VALUES (" + values.toString() + ")";
        PreparedStatement stat = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        int counter = 1;
        for (DBField field : fields) {
            field.setParam(counter, stat);
            counter++;
        }

        System.out.println("INSERT:: " + sql);
        return stat;
    }
}
