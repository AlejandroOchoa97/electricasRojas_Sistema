/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import entidad.Usuario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import negocio.UsuarioBO;

/**
 * Pantalla de inicio de sesion.
 * Valida usuario y contrasena para entrar al menu principal con el rol correspondiente.
 */
public class LoginForm extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private UsuarioBO usuarioBO;

    public LoginForm() {
        usuarioBO = new UsuarioBO();
        configurarVentana();
        construirFormulario();
    }

    private void configurarVentana() {
        setTitle("ElectricaRojas - Login");
        setSize(860, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void construirFormulario() {
        FondoPanel fondo = new FondoPanel();
        fondo.setLayout(new GridBagLayout());
        setContentPane(fondo);

        TarjetaPanel tarjeta = new TarjetaPanel();
        tarjeta.setPreferredSize(new Dimension(680, 380));
        tarjeta.setLayout(new BorderLayout());

        PanelMarca panelMarca = new PanelMarca();
        panelMarca.setPreferredSize(new Dimension(295, 380));
        tarjeta.add(panelMarca, BorderLayout.WEST);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setOpaque(false);
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(54, 60, 54, 60));
        tarjeta.add(panelFormulario, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 14, 0);

        JLabel lblTitulo = new JLabel("Iniciar sesion");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 27));
        lblTitulo.setForeground(new Color(43, 50, 82));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 32, 0);
        panelFormulario.add(lblTitulo, gbc);

        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUsuario.setForeground(new Color(70, 76, 105));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 6, 0);
        panelFormulario.add(lblUsuario, gbc);

        txtUsuario = new CampoRedondeado("");
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 16, 0);
        panelFormulario.add(txtUsuario, gbc);

        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPassword.setForeground(new Color(70, 76, 105));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 6, 0);
        panelFormulario.add(lblPassword, gbc);

        txtPassword = new PasswordRedondeado("");
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 0, 0);
        panelFormulario.add(txtPassword, gbc);

        JButton btnLogin = new BotonGradiente("INICIAR SESION");
        btnLogin.addActionListener((ActionEvent e) -> iniciarSesion());
        gbc.gridy = 5;
        gbc.insets = new Insets(28, 0, 0, 0);
        panelFormulario.add(btnLogin, gbc);

        txtPassword.addActionListener((ActionEvent e) -> iniciarSesion());

        fondo.add(tarjeta);
    }

    private void iniciarSesion() {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa usuario y password.");
            return;
        }

        Usuario usuarioLogin = usuarioBO.iniciarSesion(usuario, password);

        if (usuarioLogin == null) {
            JOptionPane.showMessageDialog(this, "Usuario o password incorrectos.");
            txtPassword.setText("");
            txtPassword.requestFocus();
            return;
        }

        MenuPrincipal menu = new MenuPrincipal(usuarioLogin);
        menu.setVisible(true);
        dispose();
    }

    private static class FondoPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, new Color(55, 202, 235), getWidth(), getHeight(), new Color(38, 79, 211));
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(new Color(255, 255, 255, 35));
            for (int i = 0; i < getWidth(); i += 34) {
                g2.fillOval(i, getHeight() - 90 + (i % 68), 5, 5);
            }
            g2.dispose();
        }
    }

    private static class TarjetaPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 28));
            g2.fillRoundRect(8, 12, getWidth() - 16, getHeight() - 10, 18, 18);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        public boolean isOpaque() {
            return false;
        }
    }

    private static class PanelMarca extends JPanel {

        public PanelMarca() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, new Color(22, 136, 226), 0, getHeight(), new Color(12, 35, 84));
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            g2.setColor(new Color(255, 255, 255, 16));
            g2.drawArc(36, 70, 250, 170, 190, 110);
            g2.drawArc(12, 118, 280, 190, 195, 110);

            dibujarLogo(g2, 58, 70);

            g2.setFont(new Font("Segoe UI", Font.BOLD, 38));
            g2.setColor(Color.WHITE);
            g2.drawString("Hola,", 58, 230);
            g2.drawString("bienvenido!", 58, 286);

            g2.dispose();
            super.paintComponent(g);
        }

        private void dibujarLogo(Graphics2D g2, int x, int y) {
            g2.setColor(new Color(255, 255, 255, 238));
            g2.fillOval(x, y + 5, 50, 50);
            g2.setColor(new Color(255, 198, 38));
            Polygon rayo = new Polygon();
            rayo.addPoint(x + 29, y + 12);
            rayo.addPoint(x + 13, y + 37);
            rayo.addPoint(x + 28, y + 31);
            rayo.addPoint(x + 22, y + 50);
            rayo.addPoint(x + 44, y + 22);
            rayo.addPoint(x + 29, y + 27);
            g2.fillPolygon(rayo);

            g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
            g2.setColor(Color.WHITE);
            g2.drawString("ELECTRICA", x + 64, y + 25);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 17));
            g2.drawString("ROJAS", x + 64, y + 49);
        }
    }

    private static class CampoRedondeado extends JTextField {

        private String placeholder;

        public CampoRedondeado(String placeholder) {
            this.placeholder = placeholder;
            setPreferredSize(new Dimension(285, 44));
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setForeground(new Color(65, 68, 92));
            setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 18));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2.setColor(new Color(205, 214, 230));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
            g2.dispose();
            super.paintComponent(g);

            if (getText().isEmpty()) {
                g.setColor(new Color(150, 157, 177));
                g.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                g.drawString(placeholder, 18, 26);
            }
        }
    }

    private static class PasswordRedondeado extends JPasswordField {

        private String placeholder;

        public PasswordRedondeado(String placeholder) {
            this.placeholder = placeholder;
            setPreferredSize(new Dimension(285, 44));
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setForeground(new Color(65, 68, 92));
            setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 18));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2.setColor(new Color(205, 214, 230));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
            g2.dispose();
            super.paintComponent(g);

            if (getPassword().length == 0) {
                g.setColor(new Color(150, 157, 177));
                g.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                g.drawString(placeholder, 18, 26);
            }
        }
    }

    private static class BotonGradiente extends JButton {

        public BotonGradiente(String texto) {
            super(texto);
            setPreferredSize(new Dimension(285, 44));
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, new Color(20, 177, 221), getWidth(), 0, new Color(36, 111, 229));
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
