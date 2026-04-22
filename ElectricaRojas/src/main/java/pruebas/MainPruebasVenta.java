package pruebas;

import entidad.DetalleVenta;
import entidad.Venta;
import java.util.List;
import negocio.VentaBO;

public class MainPruebasVenta {

    public static void main(String[] args) {

        VentaBO ventaBO = new VentaBO();

        System.out.println("=== PRUEBA REGISTRAR VENTA ===");

        Venta venta = new Venta(1, 1);
        venta.agregarDetalle(new DetalleVenta(1, 1));

        boolean registrada = ventaBO.registrarVenta(venta);

        if (registrada) {
            System.out.println("Venta registrada correctamente.");
            System.out.println(venta);
        } else {
            System.out.println("No se pudo registrar la venta.");
        }

        System.out.println("\n=== PRUEBA LISTAR VENTAS ===");

        List<Venta> ventas = ventaBO.listarVentas();

        for (Venta ventaLista : ventas) {
            System.out.println(ventaLista);
        }

        System.out.println("\n=== PRUEBA FINALIZADA ===");
    }
}
