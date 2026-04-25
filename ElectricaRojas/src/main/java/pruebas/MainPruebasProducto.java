/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package pruebas;

import entidad.Producto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import negocio.ProductoBO;
import persistencia.ConexionBD;

/**
 * Prueba rapida del modulo productos desde consola.
 * Inserta un producto de prueba, lo busca y actualiza ese mismo registro.
 */
public class MainPruebasProducto {

    public static void main(String[] args) {

        ProductoBO productoBO = new ProductoBO();

        System.out.println("=== PRUEBA INSERTAR PRODUCTO ===");
        
        String nombrePrueba = "Producto prueba " + LocalDateTime.now();

        Producto productoNuevo = new Producto(
                nombrePrueba,
                "Producto creado desde prueba de consola",
                new BigDecimal("15.50"),
                100,
                1
        );

        boolean insertado = productoBO.insertarProducto(productoNuevo);

        if (insertado) {
            System.out.println("Producto insertado correctamente.");
        } else {
            System.out.println("No se pudo insertar el producto.");
        }

        System.out.println("\n=== PRUEBA LISTAR PRODUCTOS ===");

        List<Producto> productos = productoBO.listarProductos();

        for (Producto producto : productos) {
            System.out.println(producto);
        }

        System.out.println("\n=== PRUEBA BUSCAR PRODUCTO POR NOMBRE ===");

        List<Producto> productosEncontrados = productoBO.buscarProductoPorNombre("Cable");

        for (Producto producto : productosEncontrados) {
            System.out.println(producto);
        }

        System.out.println("\n=== PRUEBA ACTUALIZAR PRODUCTO ===");

        List<Producto> productosPrueba = productoBO.buscarProductoPorNombre(nombrePrueba);

        if (!productosPrueba.isEmpty()) {
            Producto productoActualizar = productosPrueba.get(0);

            productoActualizar.setNombre(nombrePrueba + " actualizado");
            productoActualizar.setDescripcion("Producto actualizado desde prueba de consola");
            productoActualizar.setPrecio(new BigDecimal("18.75"));
            productoActualizar.setStock(80);
            productoActualizar.setIdCategoria(1);

            boolean actualizado = productoBO.actualizarProducto(productoActualizar);

            if (actualizado) {
                System.out.println("Producto actualizado correctamente.");
            } else {
                System.out.println("No se pudo actualizar el producto.");
            }
        } else {
            System.out.println("No se encontro el producto de prueba para actualizar.");
        }

        System.out.println("\n=== PRUEBA ELIMINAR PRODUCTO ===");
        System.out.println("Prueba de eliminar no ejecutada automaticamente.");
        System.out.println("Para eliminar, usa productoBO.eliminarProducto(idProducto) con un id de prueba.");

        System.out.println("\n=== PRUEBA FINALIZADA ===");
        ConexionBD.cerrarDriverMysql();
    }
}
