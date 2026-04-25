/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package negocio;

import java.util.List;
import persistencia.ReporteDAO;

/**
 * Capa de negocio para reportes.
 * Pide la informacion al DAO y la deja lista para mostrarla en la pantalla.
 */
public class ReporteBO {

    private ReporteDAO reporteDAO;

    public ReporteBO() {
        reporteDAO = new ReporteDAO();
    }

    public List<String> reporteInventarioGeneral() {
        return reporteDAO.reporteInventarioGeneral();
    }

    public List<Object[]> reporteInventarioGeneralTabla() {
        return reporteDAO.reporteInventarioGeneralTabla();
    }

    public List<String> reporteProductosBajoStock(int stockMinimo) {
        if (stockMinimo < 0) {
            System.out.println("El stock minimo no puede ser negativo.");
            stockMinimo = 0;
        }

        return reporteDAO.reporteProductosBajoStock(stockMinimo);
    }

    public List<Object[]> reporteProductosBajoStockTabla(int stockMinimo) {
        if (stockMinimo < 0) {
            System.out.println("El stock minimo no puede ser negativo.");
            stockMinimo = 0;
        }

        return reporteDAO.reporteProductosBajoStockTabla(stockMinimo);
    }

    public List<String> reporteVentas() {
        return reporteDAO.reporteVentas();
    }

    public List<Object[]> reporteVentasTabla() {
        return reporteDAO.reporteVentasTabla();
    }

    public List<Object[]> reporteVentasTabla(String fechaInicio, String fechaFin) {
        return reporteDAO.reporteVentasTabla(fechaInicio, fechaFin);
    }

    public List<String> reporteProductosMasVendidos() {
        return reporteDAO.reporteProductosMasVendidos();
    }

    public List<Object[]> reporteProductosMasVendidosTabla() {
        return reporteDAO.reporteProductosMasVendidosTabla();
    }

    public List<Object[]> reporteProductosMasVendidosTabla(String fechaInicio, String fechaFin) {
        return reporteDAO.reporteProductosMasVendidosTabla(fechaInicio, fechaFin);
    }

    public List<String> reporteMermas() {
        return reporteDAO.reporteMermas();
    }

    public List<Object[]> reporteMermasTabla() {
        return reporteDAO.reporteMermasTabla();
    }

    public List<Object[]> reporteMermasTabla(String fechaInicio, String fechaFin) {
        return reporteDAO.reporteMermasTabla(fechaInicio, fechaFin);
    }
}
