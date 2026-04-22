package persistencia;

import entidad.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public boolean insertarCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, telefono, correo, direccion) VALUES (?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getTelefono());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getDireccion());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al insertar cliente: " + e.getMessage());
            return false;
        }
    }

    public List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id_cliente, nombre, telefono, correo, direccion FROM clientes ORDER BY nombre";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }

        } catch (Exception e) {
            System.out.println("Error al listar clientes: " + e.getMessage());
        }

        return clientes;
    }

    public Cliente buscarClientePorId(int idCliente) {
        Cliente cliente = null;
        String sql = "SELECT id_cliente, nombre, telefono, correo, direccion FROM clientes WHERE id_cliente = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cliente = mapearCliente(rs);
                }
            }

        } catch (Exception e) {
            System.out.println("Error al buscar cliente por id: " + e.getMessage());
        }

        return cliente;
    }

    public List<Cliente> buscarClientePorNombre(String nombre) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id_cliente, nombre, telefono, correo, direccion FROM clientes WHERE nombre LIKE ? ORDER BY nombre";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clientes.add(mapearCliente(rs));
                }
            }

        } catch (Exception e) {
            System.out.println("Error al buscar cliente por nombre: " + e.getMessage());
        }

        return clientes;
    }

    public boolean actualizarCliente(Cliente cliente) {
        String sql = "UPDATE clientes SET nombre = ?, telefono = ?, correo = ?, direccion = ? WHERE id_cliente = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getTelefono());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getDireccion());
            ps.setInt(5, cliente.getIdCliente());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarCliente(int idCliente) {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }

    private Cliente mapearCliente(ResultSet rs) throws Exception {
        Cliente cliente = new Cliente();

        cliente.setIdCliente(rs.getInt("id_cliente"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setCorreo(rs.getString("correo"));
        cliente.setDireccion(rs.getString("direccion"));

        return cliente;
    }
}
