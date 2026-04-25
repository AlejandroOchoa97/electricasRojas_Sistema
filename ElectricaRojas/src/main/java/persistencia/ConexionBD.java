/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase central para abrir la conexion con MySQL.
 * Asi todas las clases DAO usan el mismo punto de conexion.
 */
public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/electrica_rojas";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection obtenerConexion() throws SQLException {

        return DriverManager.getConnection(URL, USER, PASSWORD);

    }

    public static void cerrarDriverMysql() {
        try {
            AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Exception e) {
            System.out.println("Error al cerrar driver MySQL: " + e.getMessage());
        }
    }
}
