package com.cordi.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConexionBD {

    // credenciales por defecto de xampp
    private static final String URL = "jdbc:mysql://localhost:3306/abarrotes_cordi";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";

    public static Connection obtenerConexion() {
        Connection conexion = null;
        try {
            // indicamos driver de mysql
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (Exception e) {
            System.out.println("ERROR critico al conectar a la BD: " + e.getMessage());
        }
        return conexion;
    }

    // metodo apoyo para ejecutar instrucciones (INSERT, UPDATE, DELETE)
    public static boolean ejecutarInstruccion(String sql) {
        try (Connection con = obtenerConexion(); Statement stmt = con.createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (Exception e) {
            System.out.println("ERROR al ejecutar instruccion SQL: " + e.getMessage());
            return false;
        }
    }
}