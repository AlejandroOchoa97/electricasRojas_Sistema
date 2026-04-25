/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de reportes.
 * Arma las consultas que alimentan las tablas y resumenes del modulo Reportes.
 */
public class ReporteDAO {

    public List<Object[]> reporteInventarioGeneralTabla() {
        List<Object[]> reporte = new ArrayList<>();
        String sql = "SELECT p.nombre, c.nombre AS categoria, p.descripcion, p.precio, p.stock "
                + "FROM productos p "
                + "INNER JOIN categorias c ON p.id_categoria = c.id_categoria "
                + "ORDER BY p.nombre";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                reporte.add(new Object[]{
                    rs.getString("nombre"),
                    rs.getString("categoria"),
                    rs.getString("descripcion"),
                    rs.getBigDecimal("precio"),
                    rs.getInt("stock")
                });
            }

        } catch (Exception e) {
            System.out.println("Error al generar reporte de inventario en tabla: " + e.getMessage());
        }

        return reporte;
    }

    public List<Object[]> reporteProductosBajoStockTabla(int stockMinimo) {
        List<Object[]> reporte = new ArrayList<>();
        String sql = "SELECT p.nombre, c.nombre AS categoria, p.stock "
                + "FROM productos p "
                + "INNER JOIN categorias c ON p.id_categoria = c.id_categoria "
                + "WHERE p.stock <= ? "
                + "ORDER BY p.stock ASC";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, stockMinimo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reporte.add(new Object[]{
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getInt("stock")
                    });
                }
            }

        } catch (Exception e) {
            System.out.println("Error al generar reporte de bajo stock en tabla: " + e.getMessage());
        }

        return reporte;
    }

    public List<Object[]> reporteVentasTabla() {
        return reporteVentasTabla(null, null);
    }

    public List<Object[]> reporteVentasTabla(String fechaInicio, String fechaFin) {
        List<Object[]> reporte = new ArrayList<>();
        String sql = "SELECT v.fecha, COALESCE(c.nombre, 'Publico general') AS cliente, "
                + "u.nombre AS usuario, v.total "
                + "FROM ventas v "
                + "INNER JOIN usuarios u ON v.id_usuario = u.id_usuario "
                + "LEFT JOIN clientes c ON v.id_cliente = c.id_cliente "
                + construirCondicionFecha("v.fecha", fechaInicio, fechaFin)
                + "ORDER BY v.id_venta DESC";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            prepararFechas(ps, fechaInicio, fechaFin);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reporte.add(new Object[]{
                        rs.getTimestamp("fecha"),
                        rs.getString("cliente"),
                        rs.getString("usuario"),
                        rs.getBigDecimal("total")
                    });
                }
            }

        } catch (Exception e) {
            System.out.println("Error al generar reporte de ventas en tabla: " + e.getMessage());
        }

        return reporte;
    }

    public List<Object[]> reporteProductosMasVendidosTabla() {
        return reporteProductosMasVendidosTabla(null, null);
    }

    public List<Object[]> reporteProductosMasVendidosTabla(String fechaInicio, String fechaFin) {
        List<Object[]> reporte = new ArrayList<>();
        String sql = "SELECT p.nombre, SUM(d.cantidad) AS cantidad_vendida, "
                + "SUM(d.cantidad * d.precio) AS total_vendido "
                + "FROM detalle_venta d "
                + "INNER JOIN ventas v ON d.id_venta = v.id_venta "
                + "INNER JOIN productos p ON d.id_producto = p.id_producto "
                + construirCondicionFecha("v.fecha", fechaInicio, fechaFin)
                + "GROUP BY p.id_producto, p.nombre "
                + "ORDER BY cantidad_vendida DESC";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            prepararFechas(ps, fechaInicio, fechaFin);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reporte.add(new Object[]{
                        rs.getString("nombre"),
                        rs.getInt("cantidad_vendida"),
                        rs.getBigDecimal("total_vendido")
                    });
                }
            }

        } catch (Exception e) {
            System.out.println("Error al generar reporte de productos mas vendidos en tabla: " + e.getMessage());
        }

        return reporte;
    }

    public List<Object[]> reporteMermasTabla() {
        return reporteMermasTabla(null, null);
    }

    public List<Object[]> reporteMermasTabla(String fechaInicio, String fechaFin) {
        List<Object[]> reporte = new ArrayList<>();
        String sql = "SELECT m.fecha, p.nombre, m.cantidad, m.motivo "
                + "FROM mermas m "
                + "INNER JOIN productos p ON m.id_producto = p.id_producto "
                + construirCondicionFecha("m.fecha", fechaInicio, fechaFin)
                + "ORDER BY m.id_merma DESC";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            prepararFechas(ps, fechaInicio, fechaFin);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reporte.add(new Object[]{
                        rs.getTimestamp("fecha"),
                        rs.getString("nombre"),
                        rs.getInt("cantidad"),
                        rs.getString("motivo")
                    });
                }
            }

        } catch (Exception e) {
            System.out.println("Error al generar reporte de mermas en tabla: " + e.getMessage());
        }

        return reporte;
    }

    private String construirCondicionFecha(String campoFecha, String fechaInicio, String fechaFin) {
        boolean tieneInicio = fechaInicio != null && !fechaInicio.trim().isEmpty();
        boolean tieneFin = fechaFin != null && !fechaFin.trim().isEmpty();

        if (tieneInicio && tieneFin) {
            return "WHERE DATE(" + campoFecha + ") BETWEEN ? AND ? ";
        }

        if (tieneInicio) {
            return "WHERE DATE(" + campoFecha + ") >= ? ";
        }

        if (tieneFin) {
            return "WHERE DATE(" + campoFecha + ") <= ? ";
        }

        return "";
    }

    private void prepararFechas(PreparedStatement ps, String fechaInicio, String fechaFin) throws Exception {
        int indice = 1;

        if (fechaInicio != null && !fechaInicio.trim().isEmpty()) {
            ps.setString(indice++, fechaInicio);
        }

        if (fechaFin != null && !fechaFin.trim().isEmpty()) {
            ps.setString(indice, fechaFin);
        }
    }

    public List<String> reporteInventarioGeneral() {
        List<String> reporte = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre, p.descripcion, p.precio, p.stock, c.nombre AS categoria "
                + "FROM productos p "
                + "INNER JOIN categorias c ON p.id_categoria = c.id_categoria "
                + "ORDER BY p.nombre";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String linea = "ID: " + rs.getInt("id_producto")
                        + " | Producto: " + rs.getString("nombre")
                        + " | Categoria: " + rs.getString("categoria")
                        + " | Descripcion: " + rs.getString("descripcion")
                        + " | Precio: $" + rs.getBigDecimal("precio")
                        + " | Stock: " + rs.getInt("stock");

                reporte.add(linea);
            }

        } catch (Exception e) {
            System.out.println("Error al generar reporte de inventario: " + e.getMessage());
        }

        return reporte;
    }

    public List<String> reporteProductosBajoStock(int stockMinimo) {
        List<String> reporte = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre, p.stock, c.nombre AS categoria "
                + "FROM productos p "
                + "INNER JOIN categorias c ON p.id_categoria = c.id_categoria "
                + "WHERE p.stock <= ? "
                + "ORDER BY p.stock ASC";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, stockMinimo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String linea = "ID: " + rs.getInt("id_producto")
                            + " | Producto: " + rs.getString("nombre")
                            + " | Categoria: " + rs.getString("categoria")
                            + " | Stock: " + rs.getInt("stock");

                    reporte.add(linea);
                }
            }

        } catch (Exception e) {
            System.out.println("Error al generar reporte de bajo stock: " + e.getMessage());
        }

        return reporte;
    }

    public List<String> reporteVentas() {
        List<String> reporte = new ArrayList<>();
        String sql = "SELECT v.id_venta, v.fecha, v.total, u.nombre AS usuario, "
                + "COALESCE(c.nombre, 'Publico general') AS cliente "
                + "FROM ventas v "
                + "INNER JOIN usuarios u ON v.id_usuario = u.id_usuario "
                + "LEFT JOIN clientes c ON v.id_cliente = c.id_cliente "
                + "ORDER BY v.id_venta DESC";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String linea = "Venta: " + rs.getInt("id_venta")
                        + " | Fecha: " + rs.getTimestamp("fecha")
                        + " | Total: $" + rs.getBigDecimal("total")
                        + " | Usuario: " + rs.getString("usuario")
                        + " | Cliente: " + rs.getString("cliente");

                reporte.add(linea);
            }

        } catch (Exception e) {
            System.out.println("Error al generar reporte de ventas: " + e.getMessage());
        }

        return reporte;
    }

    public List<String> reporteProductosMasVendidos() {
        List<String> reporte = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre, SUM(d.cantidad) AS cantidad_vendida, "
                + "SUM(d.cantidad * d.precio) AS total_vendido "
                + "FROM detalle_venta d "
                + "INNER JOIN productos p ON d.id_producto = p.id_producto "
                + "GROUP BY p.id_producto, p.nombre "
                + "ORDER BY cantidad_vendida DESC";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String linea = "ID: " + rs.getInt("id_producto")
                        + " | Producto: " + rs.getString("nombre")
                        + " | Cantidad vendida: " + rs.getInt("cantidad_vendida")
                        + " | Total vendido: $" + rs.getBigDecimal("total_vendido");

                reporte.add(linea);
            }

        } catch (Exception e) {
            System.out.println("Error al generar reporte de productos mas vendidos: " + e.getMessage());
        }

        return reporte;
    }

    public List<String> reporteMermas() {
        List<String> reporte = new ArrayList<>();
        String sql = "SELECT m.id_merma, m.fecha, p.nombre, m.cantidad, m.motivo "
                + "FROM mermas m "
                + "INNER JOIN productos p ON m.id_producto = p.id_producto "
                + "ORDER BY m.id_merma DESC";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String linea = "Merma: " + rs.getInt("id_merma")
                        + " | Fecha: " + rs.getTimestamp("fecha")
                        + " | Producto: " + rs.getString("nombre")
                        + " | Cantidad: " + rs.getInt("cantidad")
                        + " | Motivo: " + rs.getString("motivo");

                reporte.add(linea);
            }

        } catch (Exception e) {
            System.out.println("Error al generar reporte de mermas: " + e.getMessage());
        }

        return reporte;
    }
}
