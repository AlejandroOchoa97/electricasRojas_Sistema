/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import entidad.Merma;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aleja
 */
public class MermaDAO {

    public boolean registrarMerma(Merma merma) {
        String sqlProducto = "SELECT stock FROM productos WHERE id_producto = ? FOR UPDATE";
        String sqlMerma = "INSERT INTO mermas (id_producto, cantidad, motivo) VALUES (?, ?, ?)";
        String sqlStock = "UPDATE productos SET stock = ? WHERE id_producto = ?";
        Connection conexion = null;

        try {
            conexion = ConexionBD.obtenerConexion();
            conexion.setAutoCommit(false);

            int stockActual;

            try (PreparedStatement psProducto = conexion.prepareStatement(sqlProducto)) {
                psProducto.setInt(1, merma.getIdProducto());

                try (ResultSet rs = psProducto.executeQuery()) {
                    if (rs.next()) {
                        stockActual = rs.getInt("stock");
                    } else {
                        System.out.println("Producto no encontrado.");
                        conexion.rollback();
                        return false;
                    }
                }
            }

            if (stockActual < merma.getCantidad()) {
                System.out.println("Stock insuficiente para registrar la merma.");
                conexion.rollback();
                return false;
            }

            try (PreparedStatement psMerma = conexion.prepareStatement(sqlMerma, Statement.RETURN_GENERATED_KEYS)) {
                psMerma.setInt(1, merma.getIdProducto());
                psMerma.setInt(2, merma.getCantidad());
                psMerma.setString(3, merma.getMotivo());
                psMerma.executeUpdate();

                try (ResultSet rs = psMerma.getGeneratedKeys()) {
                    if (rs.next()) {
                        merma.setIdMerma(rs.getInt(1));
                    }
                }
            }

            int nuevoStock = stockActual - merma.getCantidad();

            try (PreparedStatement psStock = conexion.prepareStatement(sqlStock)) {
                psStock.setInt(1, nuevoStock);
                psStock.setInt(2, merma.getIdProducto());
                psStock.executeUpdate();
            }

            conexion.commit();
            return true;

        } catch (Exception e) {
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (Exception ex) {
                    System.out.println("Error al revertir merma: " + ex.getMessage());
                }
            }
            System.out.println("Error al registrar merma: " + e.getMessage());
            return false;
        } finally {
            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (Exception e) {
                    System.out.println("Error al cerrar conexion de merma: " + e.getMessage());
                }
            }
        }
    }

    public List<Merma> listarMermas() {
        List<Merma> mermas = new ArrayList<>();
        String sql = "SELECT id_merma, id_producto, cantidad, motivo, fecha FROM mermas ORDER BY id_merma DESC";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Merma merma = new Merma();
                Timestamp fecha = rs.getTimestamp("fecha");

                merma.setIdMerma(rs.getInt("id_merma"));
                merma.setIdProducto(rs.getInt("id_producto"));
                merma.setCantidad(rs.getInt("cantidad"));
                merma.setMotivo(rs.getString("motivo"));
                merma.setFecha(fecha.toLocalDateTime());

                mermas.add(merma);
            }

        } catch (Exception e) {
            System.out.println("Error al listar mermas: " + e.getMessage());
        }

        return mermas;
    }
}
