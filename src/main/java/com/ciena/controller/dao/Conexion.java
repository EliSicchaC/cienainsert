package com.ciena.controller.dao;

import java.sql.*;

public class Conexion {
    public static class DBConnector {
        final static String dbAddress = "190.232.112.5";  //todo: move them to properties
        //final static String dbName = "db_read_corba";
        final static String dbName = "onf-tapi_model";
        final static String dbUser = "esiccha";
        final static String dbPassword = "siccha";
        Connection conn;

        public DBConnector() throws SQLException, ClassNotFoundException {
            System.out.println("Writing to database");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Mysql driver registered");
            final String dbUrl = "jdbc:mysql://" + dbAddress + "/" + dbName + "?serverTimezone=UTC";

            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        }


        public DBTable deleteTableIfExsist(String tableName) throws SQLException {
            return new DBTable(tableName, conn);
        }


        public void update(String query) throws SQLException {

            PreparedStatement stat = conn.prepareStatement(query);
            stat.executeUpdate();
        }

        public Connection getConexion() {
            return conn;
        }

        public void cerrarConexion() throws SQLException {
            conn.close();
        }
    }
}


