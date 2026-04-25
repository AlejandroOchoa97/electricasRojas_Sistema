/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidad;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una venta completa.
 * Guarda cliente, usuario, total y la lista de productos vendidos.
 */
public class Venta {

    private int idVenta;
    private LocalDateTime fecha;
    private BigDecimal total;
    private int idUsuario;
    private Integer idCliente;
    private List<DetalleVenta> detalles;

    public Venta() {
        detalles = new ArrayList<>();
    }

    public Venta(int idUsuario) {
        this.idUsuario = idUsuario;
        this.total = BigDecimal.ZERO;
        this.detalles = new ArrayList<>();
    }

    public Venta(int idUsuario, Integer idCliente) {
        this.idUsuario = idUsuario;
        this.idCliente = idCliente;
        this.total = BigDecimal.ZERO;
        this.detalles = new ArrayList<>();
    }

    public Venta(int idVenta, LocalDateTime fecha, BigDecimal total, int idUsuario) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.total = total;
        this.idUsuario = idUsuario;
        this.detalles = new ArrayList<>();
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

    public void agregarDetalle(DetalleVenta detalle) {
        this.detalles.add(detalle);
    }

    @Override
    public String toString() {
        return "Venta{" + "idVenta=" + idVenta + ", fecha=" + fecha + ", total=" + total + ", idUsuario=" + idUsuario + ", idCliente=" + idCliente + ", detalles=" + detalles + '}';
    }
}
