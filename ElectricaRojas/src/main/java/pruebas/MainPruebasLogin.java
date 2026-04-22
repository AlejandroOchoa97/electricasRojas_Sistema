package pruebas;

import entidad.Usuario;
import negocio.UsuarioBO;

public class MainPruebasLogin {

    public static void main(String[] args) {

        UsuarioBO usuarioBO = new UsuarioBO();

        Usuario usuario = usuarioBO.iniciarSesion("admin", "1234");

        if (usuario != null) {
            System.out.println("Conexion correcta");
            System.out.println("Bienvenido: " + usuario.getNombre());
            System.out.println("Usuario: " + usuario.getUsuario());
            System.out.println("Rol: " + usuario.getRol());
        } else {
            System.out.println("Usuario o contrasena incorrectos");
        }

        System.out.println("\n=== PRUEBA LOGIN VENDEDORA ===");

        Usuario vendedora = usuarioBO.iniciarSesion("vendedora", "1234");

        if (vendedora != null) {
            System.out.println("Conexion correcta");
            System.out.println("Bienvenido: " + vendedora.getNombre());
            System.out.println("Usuario: " + vendedora.getUsuario());
            System.out.println("Rol: " + vendedora.getRol());
        } else {
            System.out.println("Usuario o contrasena incorrectos");
        }
    }
}
