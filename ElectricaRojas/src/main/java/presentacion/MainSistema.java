package presentacion;

import javax.swing.UIManager;

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
