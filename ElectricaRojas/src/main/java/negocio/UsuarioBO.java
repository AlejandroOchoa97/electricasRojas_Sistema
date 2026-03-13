/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import entidad.Usuario;
import persisntencia.UsuarioDAO;

/**
 *
 * @author aleja
 */
public class UsuarioBO {
    
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario iniciarSesion(String usuario, String password) {

        return usuarioDAO.login(usuario, password);

    }
    
}
