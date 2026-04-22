package negocio;

import entidad.Usuario;
import java.util.List;
import persistencia.UsuarioDAO;

public class UsuarioBO {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario iniciarSesion(String usuario, String password) {
        return usuarioDAO.login(usuario, password);
    }

    public boolean insertarUsuario(Usuario usuario) {
        if (!validarUsuario(usuario)) {
            return false;
        }

        return usuarioDAO.insertarUsuario(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioDAO.listarUsuarios();
    }

    public Usuario buscarUsuarioPorId(int idUsuario) {
        if (idUsuario <= 0) {
            System.out.println("El id del usuario no es valido.");
            return null;
        }

        return usuarioDAO.buscarUsuarioPorId(idUsuario);
    }

    public List<Usuario> buscarUsuarioPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return usuarioDAO.listarUsuarios();
        }

        return usuarioDAO.buscarUsuarioPorNombre(nombre);
    }

    public boolean actualizarUsuario(Usuario usuario) {
        if (usuario == null || usuario.getIdUsuario() <= 0) {
            System.out.println("El id del usuario no es valido.");
            return false;
        }

        if (!validarUsuario(usuario)) {
            return false;
        }

        return usuarioDAO.actualizarUsuario(usuario);
    }

    public boolean eliminarUsuario(int idUsuario) {
        if (idUsuario <= 0) {
            System.out.println("El id del usuario no es valido.");
            return false;
        }

        return usuarioDAO.eliminarUsuario(idUsuario);
    }

    private boolean validarUsuario(Usuario usuario) {
        if (usuario == null) {
            System.out.println("El usuario no puede ser nulo.");
            return false;
        }

        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            System.out.println("El nombre del usuario es obligatorio.");
            return false;
        }

        if (usuario.getUsuario() == null || usuario.getUsuario().trim().isEmpty()) {
            System.out.println("El usuario de acceso es obligatorio.");
            return false;
        }

        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            System.out.println("El password es obligatorio.");
            return false;
        }

        if (!"ADMIN".equals(usuario.getRol()) && !"VENDEDOR".equals(usuario.getRol())) {
            System.out.println("El rol debe ser ADMIN o VENDEDOR.");
            return false;
        }

        return true;
    }
}
