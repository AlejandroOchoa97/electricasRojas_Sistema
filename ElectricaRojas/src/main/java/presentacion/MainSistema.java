/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import javax.swing.UIManager;

/**
 * Punto de entrada del sistema.
 * Arranca la aplicacion mostrando primero la pantalla de login.
 */
public class MainSistema {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo aplicar look and feel: " + e.getMessage());
        }

        java.awt.EventQueue.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
