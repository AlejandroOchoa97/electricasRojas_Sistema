package pruebas;

import entidad.Cliente;
import java.time.LocalDateTime;
import java.util.List;
import negocio.ClienteBO;
import persistencia.ConexionBD;

/**
 * Prueba rapida del modulo clientes desde consola.
 * Inserta, lista y busca clientes para revisar que la logica este funcionando.
 */
public class MainPruebasCliente {

    public static void main(String[] args) {

        ClienteBO clienteBO = new ClienteBO();

        System.out.println("=== PRUEBA INSERTAR CLIENTE ===");

        Cliente clienteNuevo = new Cliente(
                "Cliente prueba " + LocalDateTime.now(),
                "6620000000",
                "cliente@correo.com",
                "Direccion de prueba"
        );

        boolean insertado = clienteBO.insertarCliente(clienteNuevo);

        if (insertado) {
            System.out.println("Cliente insertado correctamente.");
        } else {
            System.out.println("No se pudo insertar el cliente.");
        }

        System.out.println("\n=== PRUEBA LISTAR CLIENTES ===");

        List<Cliente> clientes = clienteBO.listarClientes();

        for (Cliente cliente : clientes) {
            System.out.println(cliente);
        }

        System.out.println("\n=== PRUEBA BUSCAR CLIENTE POR NOMBRE ===");

        List<Cliente> clientesEncontrados = clienteBO.buscarClientePorNombre("Cliente");

        for (Cliente cliente : clientesEncontrados) {
            System.out.println(cliente);
        }

        System.out.println("\n=== PRUEBA FINALIZADA ===");
        ConexionBD.cerrarDriverMysql();
    }
}
