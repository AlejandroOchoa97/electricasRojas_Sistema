/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package pruebas;

import java.util.List;
import negocio.ReporteBO;
import persistencia.ConexionBD;

/**
 * Prueba rapida de reportes desde consola.
 * Imprime los reportes principales para revisar que las consultas regresen datos.
 */
public class MainPruebasReporte {

    public static void main(String[] args) {

        ReporteBO reporteBO = new ReporteBO();

        imprimirReporte("=== REPORTE INVENTARIO GENERAL ===", reporteBO.reporteInventarioGeneral());
        imprimirReporte("\n=== REPORTE PRODUCTOS BAJO STOCK ===", reporteBO.reporteProductosBajoStock(5));
        imprimirReporte("\n=== REPORTE VENTAS ===", reporteBO.reporteVentas());
        imprimirReporte("\n=== REPORTE PRODUCTOS MAS VENDIDOS ===", reporteBO.reporteProductosMasVendidos());
        imprimirReporte("\n=== REPORTE MERMAS ===", reporteBO.reporteMermas());

        System.out.println("\n=== PRUEBA FINALIZADA ===");
        ConexionBD.cerrarDriverMysql();
    }

    private static void imprimirReporte(String titulo, List<String> datos) {
        System.out.println(titulo);

        if (datos == null || datos.isEmpty()) {
            System.out.println("No hay datos para mostrar.");
            return;
        }

        for (String linea : datos) {
            System.out.println(linea);
        }
    }
}
