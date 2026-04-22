package persistencia;

import entidad.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario login(String usuario, String password) {

        Usuario us = null;
        String sql = "SELECT id_usuario, nombre, usuario, password, rol FROM usuarios WHERE usuario = ? AND password = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    us = mapearUsuario(rs);
                }
            }

        } catch (Exception e) {
            System.out.println("Error al iniciar sesion: " + e.getMessage());
        }

        return us;
    }

    public boolean insertarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, usuario, password, rol) VALUES (?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getUsuario());
            ps.setString(3, usuario.getPassword());
            ps.setString(4, usuario.getRol());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    public List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id_usuario, nombre, usuario, password, rol FROM usuarios ORDER BY nombre";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }

        } catch (Exception e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }

        return usuarios;
    }

    public Usuario buscarUsuarioPorId(int idUsuario) {
        Usuario usuario = null;
        String sql = "SELECT id_usuario, nombre, usuario, password, rol FROM usuarios WHERE id_usuario = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = mapearUsuario(rs);
                }
            }

        } catch (Exception e) {
            System.out.println("Error al buscar usuario por id: " + e.getMessage());
        }

        return usuario;
    }

    public List<Usuario> buscarUsuarioPorNombre(String nombre) {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id_usuario, nombre, usuario, password, rol FROM usuarios WHERE nombre LIKE ? OR usuario LIKE ? ORDER BY nombre";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + nombre + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapearUsuario(rs));
                }
            }

        } catch (Exception e) {
            System.out.println("Error al buscar usuario: " + e.getMessage());
        }

        return usuarios;
    }

    public boolean actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, usuario = ?, password = ?, rol = ? WHERE id_usuario = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getUsuario());
            ps.setString(3, usuario.getPassword());
            ps.setString(4, usuario.getRol());
            ps.setInt(5, usuario.getIdUsuario());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarUsuario(int idUsuario) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws Exception {
        Usuario usuario = new Usuario();

        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setUsuario(rs.getString("usuario"));
        usuario.setPassword(rs.getString("password"));
        usuario.setRol(rs.getString("rol"));

        return usuario;
    }
}
