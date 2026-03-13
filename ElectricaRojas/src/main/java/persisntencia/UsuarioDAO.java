/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persisntencia;

import entidad.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author aleja
 */
public class UsuarioDAO {
    
       public Usuario login(String usuario, String password) {

        Usuario us = null;

        try {

            Connection conexion = ConexionBD.obtenerConexion();

            String sql = "SELECT * FROM usuarios WHERE usuario=? AND password=?";

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, usuario);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                us = new Usuario();

                us.setIdUsuario(rs.getInt("id_usuario"));
                us.setNombre(rs.getString("nombre"));
                us.setUsuario(rs.getString("usuario"));
                us.setPassword(rs.getString("password"));

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return us;
    }
}
