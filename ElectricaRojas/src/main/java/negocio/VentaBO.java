/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import entidad.DetalleVenta;
import entidad.Venta;
import java.util.List;
import persistencia.VentaDAO;

/**
 *
 * @author aleja
 */
public class VentaBO {

    private VentaDAO ventaDAO;

    public VentaBO() {
        ventaDAO = new VentaDAO();
    }

    public boolean registrarVenta(Venta venta) {
        if (!validarVenta(venta)) {
            return false;
        }

        return ventaDAO.registrarVenta(venta);
    }

    public List<Venta> listarVentas() {
        return ventaDAO.listarVentas();
    }

    public List<DetalleVenta> listarDetallesPorVenta(int idVenta) {
        if (idVenta <= 0) {
            System.out.println("El id de la venta no es valido.");
            return null;
        }

        return ventaDAO.listarDetallesPorVenta(idVenta);
    }

    private boolean validarVenta(Venta venta) {
        if (venta == null) {
            System.out.println("La venta no puede ser nula.");
            return false;
        }

        if (venta.getIdUsuario() <= 0) {
            System.out.println("El usuario de la venta no es valido.");
            return false;
        }

        if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
            System.out.println("La venta debe tener al menos un producto.");
            return false;
        }

        for (DetalleVenta detalle : venta.getDetalles()) {
            if (detalle.getIdProducto() <= 0) {
                System.out.println("El producto del detalle no es valido.");
                return false;
            }

            if (detalle.getCantidad() <= 0) {
                System.out.println("La cantidad debe ser mayor a cero.");
                return false;
            }
        }

        return true;
    }
}
