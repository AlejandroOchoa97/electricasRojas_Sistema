package pruebas;

import entidad.Producto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import negocio.ProductoBO;

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

        if (!productos.isEmpty()) {
            Producto productoActualizar = productos.get(productos.size() - 1);

            productoActualizar.setNombre(productoActualizar.getNombre() + " actualizado");
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
            System.out.println("No hay productos para actualizar.");
        }

        System.out.println("\n=== PRUEBA ELIMINAR PRODUCTO ===");
        System.out.println("Prueba de eliminar no ejecutada automaticamente.");
        System.out.println("Para eliminar, usa productoBO.eliminarProducto(idProducto) con un id de prueba.");

        System.out.println("\n=== PRUEBA FINALIZADA ===");
    }
}
