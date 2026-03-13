/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persisntencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author aleja
 */
public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/electrica_rojas";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection obtenerConexion() throws SQLException {

        return DriverManager.getConnection(URL, USER, PASSWORD);

    }
}
