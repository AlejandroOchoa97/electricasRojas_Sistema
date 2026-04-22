package negocio;

import entidad.Cliente;
import java.util.List;
import persistencia.ClienteDAO;

public class ClienteBO {

    private ClienteDAO clienteDAO;

    public ClienteBO() {
        clienteDAO = new ClienteDAO();
    }

    public boolean insertarCliente(Cliente cliente) {
        if (!validarCliente(cliente)) {
            return false;
        }

        return clienteDAO.insertarCliente(cliente);
    }

    public List<Cliente> listarClientes() {
        return clienteDAO.listarClientes();
    }

    public Cliente buscarClientePorId(int idCliente) {
        if (idCliente <= 0) {
            System.out.println("El id del cliente no es valido.");
            return null;
        }

        return clienteDAO.buscarClientePorId(idCliente);
    }

    public List<Cliente> buscarClientePorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return clienteDAO.listarClientes();
        }

        return clienteDAO.buscarClientePorNombre(nombre);
    }

    public boolean actualizarCliente(Cliente cliente) {
        if (cliente == null || cliente.getIdCliente() <= 0) {
            System.out.println("El id del cliente no es valido.");
            return false;
        }

        if (!validarCliente(cliente)) {
            return false;
        }

        return clienteDAO.actualizarCliente(cliente);
    }

    public boolean eliminarCliente(int idCliente) {
        if (idCliente <= 0) {
            System.out.println("El id del cliente no es valido.");
            return false;
        }

        return clienteDAO.eliminarCliente(idCliente);
    }

    private boolean validarCliente(Cliente cliente) {
        if (cliente == null) {
            System.out.println("El cliente no puede ser nulo.");
            return false;
        }

        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            System.out.println("El nombre del cliente es obligatorio.");
            return false;
        }

        return true;
    }
}
