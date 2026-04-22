/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidad;

import java.time.LocalDateTime;

/**
 *
 * @author aleja
 */
public class Merma {

    private int idMerma;
    private int idProducto;
    private int cantidad;
    private String motivo;
    private LocalDateTime fecha;

    public Merma() {
    }

    public Merma(int idProducto, int cantidad, String motivo) {
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.motivo = motivo;
    }

    public Merma(int idMerma, int idProducto, int cantidad, String motivo, LocalDateTime fecha) {
        this.idMerma = idMerma;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.motivo = motivo;
        this.fecha = fecha;
    }

    public int getIdMerma() {
        return idMerma;
    }

    public void setIdMerma(int idMerma) {
        this.idMerma = idMerma;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Merma{" + "idMerma=" + idMerma + ", idProducto=" + idProducto + ", cantidad=" + cantidad + ", motivo=" + motivo + ", fecha=" + fecha + '}';
    }
}
