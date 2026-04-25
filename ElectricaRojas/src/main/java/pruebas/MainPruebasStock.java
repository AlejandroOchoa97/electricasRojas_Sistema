/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package pruebas;

import entidad.Producto;
import negocio.ProductoBO;
import persistencia.ConexionBD;

/**
 * Prueba rapida para movimientos de stock.
 * Descuenta y vuelve a aumentar stock para comprobar ambos metodos.
 */
public class MainPruebasStock {

    public static void main(String[] args) {

        ProductoBO productoBO = new ProductoBO();

        int idProducto = 1;
        int cantidad = 2;

        System.out.println("=== PRUEBA BUSCAR PRODUCTO POR ID ===");

        Producto producto = productoBO.buscarProductoPorId(idProducto);

        if (producto != null) {
            System.out.println(producto);
        } else {
            System.out.println("Producto no encontrado.");
            return;
        }

        System.out.println("\n=== PRUEBA DESCONTAR STOCK ===");

        boolean descontado = productoBO.descontarStock(idProducto, cantidad);

        if (descontado) {
            System.out.println("Stock descontado correctamente.");
            System.out.println(productoBO.buscarProductoPorId(idProducto));
        } else {
            System.out.println("No se pudo descontar stock.");
        }

        System.out.println("\n=== PRUEBA AUMENTAR STOCK ===");

        boolean aumentado = productoBO.aumentarStock(idProducto, cantidad);

        if (aumentado) {
            System.out.println("Stock aumentado correctamente.");
            System.out.println(productoBO.buscarProductoPorId(idProducto));
        } else {
            System.out.println("No se pudo aumentar stock.");
        }

        System.out.println("\n=== PRUEBA FINALIZADA ===");
        ConexionBD.cerrarDriverMysql();
    }
}
