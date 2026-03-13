/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pruebas;

import entidad.Usuario;
import negocio.UsuarioBO;

/**
 *
 * @author aleja
 */
public class MainPruebas {

    public static void main(String[] args) {

        UsuarioBO usuarioBO = new UsuarioBO();

        Usuario usuario = usuarioBO.iniciarSesion("admin", "1234");

        if (usuario != null) {

            System.out.println("Conexion correcta");
            System.out.println("Bienvenido: " + usuario.getNombre());
            System.out.println("Username: " + usuario.getUsuario());

        } else {

            System.out.println("Usuario o contraseña incorrectos");

        }

    }

}
