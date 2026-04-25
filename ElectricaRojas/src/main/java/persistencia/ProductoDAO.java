/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import entidad.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de productos.
 * Contiene las consultas para el inventario: insertar, listar, buscar, editar y eliminar.
 */
public class ProductoDAO {

    public boolean insertarProducto(Producto producto) {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, stock, id_categoria) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setBigDecimal(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            ps.setInt(5, producto.getIdCategoria());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }

    public List<Producto> listarProductos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre, p.descripcion, p.precio, p.stock, "
                + "p.id_categoria, c.nombre AS nombre_categoria "
                + "FROM productos p "
                + "INNER JOIN categorias c ON p.id_categoria = c.id_categoria "
                + "ORDER BY p.nombre";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }

        } catch (Exception e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }

        return productos;
    }

    public List<Producto> buscarProductoPorNombre(String nombre) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre, p.descripcion, p.precio, p.stock, "
                + "p.id_categoria, c.nombre AS nombre_categoria "
                + "FROM productos p "
                + "INNER JOIN categorias c ON p.id_categoria = c.id_categoria "
                + "WHERE p.nombre LIKE ? "
                + "ORDER BY p.nombre";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapearProducto(rs));
                }
            }

        } catch (Exception e) {
            System.out.println("Error al buscar producto por nombre: " + e.getMessage());
        }

        return productos;
    }

    public List<Producto> listarProductosPorCategoria(int idCategoria) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre, p.descripcion, p.precio, p.stock, "
                + "p.id_categoria, c.nombre AS nombre_categoria "
                + "FROM productos p "
                + "INNER JOIN categorias c ON p.id_categoria = c.id_categoria "
                + "WHERE p.id_categoria = ? "
                + "ORDER BY p.nombre";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idCategoria);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapearProducto(rs));
                }
            }

        } catch (Exception e) {
            System.out.println("Error al listar productos por categoria: " + e.getMessage());
        }

        return productos;
    }

    public Producto buscarProductoPorId(int idProducto) {
        Producto producto = null;
        String sql = "SELECT p.id_producto, p.nombre, p.descripcion, p.precio, p.stock, "
                + "p.id_categoria, c.nombre AS nombre_categoria "
                + "FROM productos p "
                + "INNER JOIN categorias c ON p.id_categoria = c.id_categoria "
                + "WHERE p.id_producto = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    producto = mapearProducto(rs);
                }
            }

        } catch (Exception e) {
            System.out.println("Error al buscar producto por id: " + e.getMessage());
        }

        return producto;
    }

    public boolean actualizarProducto(Producto producto) {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, stock = ?, id_categoria = ? "
                   + "WHERE id_producto = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setBigDecimal(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            ps.setInt(5, producto.getIdCategoria());
            ps.setInt(6, producto.getIdProducto());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarStock(int idProducto, int nuevoStock) {
        String sql = "UPDATE productos SET stock = ? WHERE id_producto = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, nuevoStock);
            ps.setInt(2, idProducto);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al actualizar stock: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarProducto(int idProducto) {
        String sql = "DELETE FROM productos WHERE id_producto = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idProducto);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    private Producto mapearProducto(ResultSet rs) throws Exception {
        Producto producto = new Producto();

        producto.setIdProducto(rs.getInt("id_producto"));
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setPrecio(rs.getBigDecimal("precio"));
        producto.setStock(rs.getInt("stock"));
        producto.setIdCategoria(rs.getInt("id_categoria"));
        producto.setNombreCategoria(rs.getString("nombre_categoria"));

        return producto;
    }
}
