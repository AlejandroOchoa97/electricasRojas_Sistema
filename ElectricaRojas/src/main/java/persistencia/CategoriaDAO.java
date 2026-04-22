package persistencia;

import entidad.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public boolean insertarCategoria(Categoria categoria) {
        String sql = "INSERT INTO categorias (nombre) VALUES (?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, categoria.getNombre());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al insertar categoria: " + e.getMessage());
            return false;
        }
    }

    public List<Categoria> listarCategorias() {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre FROM categorias ORDER BY nombre";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(rs.getInt("id_categoria"));
                categoria.setNombre(rs.getString("nombre"));

                categorias.add(categoria);
            }

        } catch (Exception e) {
            System.out.println("Error al listar categorias: " + e.getMessage());
        }

        return categorias;
    }

    public Categoria buscarCategoriaPorId(int idCategoria) {
        Categoria categoria = null;
        String sql = "SELECT id_categoria, nombre FROM categorias WHERE id_categoria = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idCategoria);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    categoria = new Categoria();
                    categoria.setIdCategoria(rs.getInt("id_categoria"));
                    categoria.setNombre(rs.getString("nombre"));
                }
            }

        } catch (Exception e) {
            System.out.println("Error al buscar categoria por id: " + e.getMessage());
        }

        return categoria;
    }

    public List<Categoria> buscarCategoriaPorNombre(String nombre) {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre FROM categorias WHERE nombre LIKE ? ORDER BY nombre";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Categoria categoria = new Categoria();
                    categoria.setIdCategoria(rs.getInt("id_categoria"));
                    categoria.setNombre(rs.getString("nombre"));

                    categorias.add(categoria);
                }
            }

        } catch (Exception e) {
            System.out.println("Error al buscar categoria por nombre: " + e.getMessage());
        }

        return categorias;
    }

    public boolean actualizarCategoria(Categoria categoria) {
        String sql = "UPDATE categorias SET nombre = ? WHERE id_categoria = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, categoria.getNombre());
            ps.setInt(2, categoria.getIdCategoria());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al actualizar categoria: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarCategoria(int idCategoria) {
        String sql = "DELETE FROM categorias WHERE id_categoria = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idCategoria);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al eliminar categoria: " + e.getMessage());
            return false;
        }
    }
}
