package com.ciena.controller.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTable {
    String tableName;
    Connection conn;
    final short recreateTables = 1;

    protected DBTable(String tableName, Connection conn) throws SQLException {
        this.tableName = tableName;
        this.conn = conn;

        Statement stmt = conn.createStatement();
        String sql = "DROP TABLE IF EXISTS " + tableName;

        if (recreateTables == 1) {
            stmt.execute(sql);
        }

    }

    public void createTable(String[][] fields) throws SQLException {
        StringBuffer sql = new StringBuffer("CREATE TABLE " + tableName + " (");
        String prefix = "   ";
        for (String[] field : fields) {
            System.out.println(field[0] + ":" + field[1]);
            sql.append("\n" + prefix + field[0] + " " + field[1]);
            prefix = "  ,";
        }
        sql.append("\n)");
        System.out.println("sql: " + sql);
        Statement stmt = conn.createStatement();

        if (recreateTables == 1) {
            stmt.execute(sql.toString());
        }
    }

    public DBRecord newRecord() {
        return new DBRecord(tableName);
    }

    public void insert(DBRecord rec) throws SQLException {
        rec.doTheInsert(conn);
    }

    public int insertIncrement(DBRecord rec) throws SQLException {
        return rec.doTheInsertIncrement(conn);
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public short getRecreateTables() {
        return recreateTables;
    }
}
