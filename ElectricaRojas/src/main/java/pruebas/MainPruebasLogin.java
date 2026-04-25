/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pruebas;

import entidad.Usuario;
import negocio.UsuarioBO;
import persistencia.ConexionBD;

/**
 * Prueba rapida del login desde consola.
 * Revisa que admin y vendedora puedan iniciar sesion con su rol correcto.
 */
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

        ConexionBD.cerrarDriverMysql();
    }
}
