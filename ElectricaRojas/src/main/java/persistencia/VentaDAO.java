/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import entidad.DetalleVenta;
import entidad.Venta;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de ventas.
 * Registra la venta, sus detalles y el descuento de stock en una sola transaccion.
 */
public class VentaDAO {

    public boolean registrarVenta(Venta venta) {
        String sqlVenta = "INSERT INTO ventas (total, id_usuario, id_cliente) VALUES (?, ?, ?)";
        String sqlProducto = "SELECT precio, stock FROM productos WHERE id_producto = ? FOR UPDATE";
        String sqlDetalle = "INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio) VALUES (?, ?, ?, ?)";
        String sqlStock = "UPDATE productos SET stock = ? WHERE id_producto = ?";
        String sqlActualizarTotal = "UPDATE ventas SET total = ? WHERE id_venta = ?";
        Connection conexion = null;

        try {
            conexion = ConexionBD.obtenerConexion();
            conexion.setAutoCommit(false);

            try (PreparedStatement psVenta = conexion.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                psVenta.setBigDecimal(1, BigDecimal.ZERO);
                psVenta.setInt(2, venta.getIdUsuario());
                if (venta.getIdCliente() == null || venta.getIdCliente() <= 0) {
                    psVenta.setNull(3, java.sql.Types.INTEGER);
                } else {
                    psVenta.setInt(3, venta.getIdCliente());
                }
                psVenta.executeUpdate();

                try (ResultSet rs = psVenta.getGeneratedKeys()) {
                    if (rs.next()) {
                        venta.setIdVenta(rs.getInt(1));
                    } else {
                        conexion.rollback();
                        return false;
                    }
                }
            }

            BigDecimal total = BigDecimal.ZERO;

            for (DetalleVenta detalle : venta.getDetalles()) {
                BigDecimal precioProducto;
                int stockActual;

                try (PreparedStatement psProducto = conexion.prepareStatement(sqlProducto)) {
                    psProducto.setInt(1, detalle.getIdProducto());

                    try (ResultSet rs = psProducto.executeQuery()) {
                        if (rs.next()) {
                            precioProducto = rs.getBigDecimal("precio");
                            stockActual = rs.getInt("stock");
                        } else {
                            System.out.println("Producto no encontrado: " + detalle.getIdProducto());
                            conexion.rollback();
                            return false;
                        }
                    }
                }

                if (stockActual < detalle.getCantidad()) {
                    System.out.println("Stock insuficiente para el producto: " + detalle.getIdProducto());
                    conexion.rollback();
                    return false;
                }

                detalle.setIdVenta(venta.getIdVenta());
                detalle.setPrecio(precioProducto);

                try (PreparedStatement psDetalle = conexion.prepareStatement(sqlDetalle)) {
                    psDetalle.setInt(1, detalle.getIdVenta());
                    psDetalle.setInt(2, detalle.getIdProducto());
                    psDetalle.setInt(3, detalle.getCantidad());
                    psDetalle.setBigDecimal(4, detalle.getPrecio());
                    psDetalle.executeUpdate();
                }

                int nuevoStock = stockActual - detalle.getCantidad();

                try (PreparedStatement psStock = conexion.prepareStatement(sqlStock)) {
                    psStock.setInt(1, nuevoStock);
                    psStock.setInt(2, detalle.getIdProducto());
                    psStock.executeUpdate();
                }

                BigDecimal subtotal = precioProducto.multiply(new BigDecimal(detalle.getCantidad()));
                total = total.add(subtotal);
            }

            try (PreparedStatement psTotal = conexion.prepareStatement(sqlActualizarTotal)) {
                psTotal.setBigDecimal(1, total);
                psTotal.setInt(2, venta.getIdVenta());
                psTotal.executeUpdate();
            }

            venta.setTotal(total);
            conexion.commit();
            return true;

        } catch (Exception e) {
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (Exception ex) {
                    System.out.println("Error al revertir venta: " + ex.getMessage());
                }
            }
            System.out.println("Error al registrar venta: " + e.getMessage());
            return false;
        } finally {
            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (Exception e) {
                    System.out.println("Error al cerrar conexion de venta: " + e.getMessage());
                }
            }
        }
    }

    public List<Venta> listarVentas() {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT id_venta, fecha, total, id_usuario, id_cliente FROM ventas ORDER BY id_venta DESC";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Venta venta = new Venta();
                Timestamp fecha = rs.getTimestamp("fecha");

                venta.setIdVenta(rs.getInt("id_venta"));
                venta.setFecha(fecha.toLocalDateTime());
                venta.setTotal(rs.getBigDecimal("total"));
                venta.setIdUsuario(rs.getInt("id_usuario"));
                int idCliente = rs.getInt("id_cliente");
                if (rs.wasNull()) {
                    venta.setIdCliente(null);
                } else {
                    venta.setIdCliente(idCliente);
                }
                venta.setDetalles(listarDetallesPorVenta(venta.getIdVenta()));

                ventas.add(venta);
            }

        } catch (Exception e) {
            System.out.println("Error al listar ventas: " + e.getMessage());
        }

        return ventas;
    }

    public List<DetalleVenta> listarDetallesPorVenta(int idVenta) {
        List<DetalleVenta> detalles = new ArrayList<>();
        String sql = "SELECT id_detalle, id_venta, id_producto, cantidad, precio FROM detalle_venta WHERE id_venta = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idVenta);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetalleVenta detalle = new DetalleVenta();
                    detalle.setIdDetalle(rs.getInt("id_detalle"));
                    detalle.setIdVenta(rs.getInt("id_venta"));
                    detalle.setIdProducto(rs.getInt("id_producto"));
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setPrecio(rs.getBigDecimal("precio"));

                    detalles.add(detalle);
                }
            }

        } catch (Exception e) {
            System.out.println("Error al listar detalles de venta: " + e.getMessage());
        }

        return detalles;
    }
}
