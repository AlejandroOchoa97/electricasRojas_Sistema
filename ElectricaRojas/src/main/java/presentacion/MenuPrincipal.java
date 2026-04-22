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
import java.awt.GridLayout;
import java.awt.Polygon;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MenuPrincipal extends JFrame {

    private Usuario usuario;
    private BotonModulo btnProductos;
    private BotonModulo btnCategorias;
    private BotonModulo btnClientes;
    private BotonModulo btnVentas;
    private BotonModulo btnMermas;
    private BotonModulo btnReportes;
    private BotonModulo btnUsuarios;

    public MenuPrincipal() {
        this(null);
    }

    public MenuPrincipal(Usuario usuario) {
        this.usuario = usuario;
        configurarVentana();
        construirMenu();
        configurarPermisos();
    }

    private void configurarVentana() {
        setTitle("ElectricaRojas - Menu Principal");
        setSize(1200, 760);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void construirMenu() {
        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(new Color(244, 247, 252));
        setContentPane(principal);

        EncabezadoPanel encabezado = new EncabezadoPanel();
        encabezado.setLayout(new BorderLayout());
        encabezado.setBorder(BorderFactory.createEmptyBorder(26, 42, 28, 42));

        JPanel marca = new JPanel(new BorderLayout(16, 0));
        marca.setOpaque(false);

        LogoMiniPanel logo = new LogoMiniPanel();
        logo.setPreferredSize(new Dimension(48, 48));
        marca.add(logo, BorderLayout.WEST);

        JPanel textosMarca = new JPanel(new BorderLayout());
        textosMarca.setOpaque(false);

        JLabel lblTitulo = new JLabel("ElectricaRojas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 31));
        lblTitulo.setForeground(Color.WHITE);
        textosMarca.add(lblTitulo, BorderLayout.NORTH);

        JLabel lblSubtitulo = new JLabel("Sistema de control de inventario y ventas");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(220, 235, 255));
        textosMarca.add(lblSubtitulo, BorderLayout.SOUTH);

        marca.add(textosMarca, BorderLayout.CENTER);
        encabezado.add(marca, BorderLayout.WEST);

        JPanel panelSesion = new JPanel(new BorderLayout(0, 8));
        panelSesion.setOpaque(false);

        String textoUsuario = "Sesion activa";
        if (usuario != null) {
            textoUsuario = usuario.getNombre() + " | " + usuario.getRol();
        }

        JLabel lblUsuario = new JLabel(textoUsuario);
        lblUsuario.setHorizontalAlignment(SwingConstants.RIGHT);
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsuario.setForeground(Color.WHITE);
        panelSesion.add(lblUsuario, BorderLayout.NORTH);

        JButton btnCerrar = new JButton("Cerrar sesion");
        btnCerrar.setFocusPainted(false);
        btnCerrar.setForeground(new Color(16, 96, 198));
        btnCerrar.setBackground(Color.WHITE);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> cerrarSesion());
        panelSesion.add(btnCerrar, BorderLayout.SOUTH);

        encabezado.add(panelSesion, BorderLayout.EAST);
        principal.add(encabezado, BorderLayout.NORTH);

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setOpaque(false);
        contenido.setBorder(BorderFactory.createEmptyBorder(44, 72, 52, 72));

        JPanel titulos = new JPanel(new BorderLayout());
        titulos.setOpaque(false);

        JLabel lblBienvenida = new JLabel("Panel de control");
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 27));
        lblBienvenida.setForeground(new Color(36, 43, 75));
        titulos.add(lblBienvenida, BorderLayout.NORTH);

        JLabel lblIndicacion = new JLabel("Selecciona un modulo para continuar");
        lblIndicacion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblIndicacion.setForeground(new Color(112, 121, 145));
        titulos.add(lblIndicacion, BorderLayout.SOUTH);

        contenido.add(titulos, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 4, 28, 28));
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(28, 0, 0, 0));

        btnProductos = new BotonModulo("Productos", "Inventario general", "P", new Color(18, 139, 226), new Color(38, 198, 218));
        btnCategorias = new BotonModulo("Categorias", "Clasificacion", "C", new Color(109, 92, 231), new Color(80, 145, 255));
        btnClientes = new BotonModulo("Clientes", "Registro y busqueda", "CL", new Color(29, 172, 125), new Color(76, 205, 146));
        btnVentas = new BotonModulo("Ventas", "Registrar salida", "V", new Color(247, 151, 30), new Color(255, 196, 64));
        btnMermas = new BotonModulo("Mermas", "Perdidas y danos", "M", new Color(239, 83, 80), new Color(255, 138, 101));
        btnReportes = new BotonModulo("Reportes", "Analisis", "R", new Color(55, 71, 79), new Color(96, 125, 139));
        btnUsuarios = new BotonModulo("Usuarios", "Accesos", "U", new Color(18, 139, 226), new Color(109, 92, 231));

        btnProductos.addActionListener(e -> abrirProductos());
        btnCategorias.addActionListener(e -> abrirCategorias());
        btnClientes.addActionListener(e -> abrirClientes());
        btnVentas.addActionListener(e -> abrirVentas());
        btnMermas.addActionListener(e -> abrirMermas());
        btnReportes.addActionListener(e -> abrirReportes());
        btnUsuarios.addActionListener(e -> abrirUsuarios());

        grid.add(btnProductos);
        grid.add(btnCategorias);
        grid.add(btnClientes);
        grid.add(btnVentas);
        grid.add(btnMermas);
        grid.add(btnReportes);
        grid.add(btnUsuarios);

        contenido.add(grid, BorderLayout.CENTER);
        principal.add(contenido, BorderLayout.CENTER);
    }

    private void configurarPermisos() {
        if (usuario == null || usuario.getRol() == null) {
            return;
        }

        if (usuario.getRol().equals("VENDEDOR")) {
            btnUsuarios.setEnabled(false);
        }
    }

    private void abrirProductos() {
        ProductosForm productosForm = new ProductosForm(usuario);
        productosForm.setVisible(true);
        dispose();
    }

    private void abrirClientes() {
        ClientesForm clientesForm = new ClientesForm(usuario);
        clientesForm.setVisible(true);
        dispose();
    }

    private void abrirCategorias() {
        CategoriasForm categoriasForm = new CategoriasForm(usuario);
        categoriasForm.setVisible(true);
        dispose();
    }

    private void abrirUsuarios() {
        UsuariosForm usuariosForm = new UsuariosForm(usuario);
        usuariosForm.setVisible(true);
        dispose();
    }

    private void abrirVentas() {
        VentasForm ventasForm = new VentasForm(usuario);
        ventasForm.setVisible(true);
        dispose();
    }

    private void abrirMermas() {
        MermasForm mermasForm = new MermasForm(usuario);
        mermasForm.setVisible(true);
        dispose();
    }

    private void abrirReportes() {
        ReportesForm reportesForm = new ReportesForm(usuario);
        reportesForm.setVisible(true);
        dispose();
    }

    private void cerrarSesion() {
        LoginForm login = new LoginForm();
        login.setVisible(true);
        dispose();
    }

    private void mostrarPendiente(String formulario) {
        JOptionPane.showMessageDialog(this, formulario + " se desarrollara en el siguiente paso.");
    }

    private static class EncabezadoPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, new Color(18, 139, 226), getWidth(), getHeight(), new Color(27, 78, 220));
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(new Color(255, 255, 255, 28));
            g2.fillOval(getWidth() - 210, -70, 260, 190);
            g2.fillOval(getWidth() - 340, 45, 160, 110);
            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        public boolean isOpaque() {
            return false;
        }
    }

    private static class BotonModulo extends JButton {

        private String titulo;
        private String subtitulo;
        private String icono;
        private Color colorUno;
        private Color colorDos;

        public BotonModulo(String titulo, String subtitulo, String icono, Color colorUno, Color colorDos) {
            this.titulo = titulo;
            this.subtitulo = subtitulo;
            this.icono = icono;
            this.colorUno = colorUno;
            this.colorDos = colorDos;
            setPreferredSize(new Dimension(260, 150));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color fondo = isEnabled() ? Color.WHITE : new Color(235, 238, 243);
            g2.setColor(new Color(0, 0, 0, 18));
            g2.fillRoundRect(6, 8, getWidth() - 12, getHeight() - 10, 22, 22);
            g2.setColor(fondo);
            g2.fillRoundRect(0, 0, getWidth() - 8, getHeight() - 10, 22, 22);

            GradientPaint gp = new GradientPaint(0, 0, colorUno, 88, 88, colorDos);
            g2.setPaint(isEnabled() ? gp : new GradientPaint(0, 0, new Color(170, 177, 190), 88, 88, new Color(190, 196, 205)));
            int iconSize = 64;
            int iconXBox = (getWidth() - 8 - iconSize) / 2;
            int iconYBox = 26;
            g2.fillRoundRect(iconXBox, iconYBox, iconSize, iconSize, 18, 18);

            g2.setFont(new Font("Segoe UI", Font.BOLD, 19));
            g2.setColor(Color.WHITE);
            int iconX = iconXBox + (iconSize - g2.getFontMetrics().stringWidth(icono)) / 2;
            g2.drawString(icono, iconX, iconYBox + 40);

            g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
            g2.setColor(isEnabled() ? new Color(37, 44, 76) : new Color(128, 136, 153));
            int tituloX = (getWidth() - 8 - g2.getFontMetrics().stringWidth(titulo)) / 2;
            g2.drawString(titulo, tituloX, 112);

            g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            g2.setColor(isEnabled() ? new Color(108, 118, 145) : new Color(155, 162, 176));
            int subtituloX = (getWidth() - 8 - g2.getFontMetrics().stringWidth(subtitulo)) / 2;
            g2.drawString(subtitulo, subtituloX, 136);

            if (!isEnabled()) {
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2.setColor(new Color(145, 151, 164));
                String textoBloqueado = "Sin acceso";
                int bloqueadoX = (getWidth() - 8 - g2.getFontMetrics().stringWidth(textoBloqueado)) / 2;
                g2.drawString(textoBloqueado, bloqueadoX, 158);
            }

            g2.dispose();
        }
    }

    private static class LogoMiniPanel extends JPanel {

        public LogoMiniPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.WHITE);
            g2.fillOval(0, 0, 46, 46);

            g2.setColor(new Color(255, 193, 30));
            Polygon rayo = new Polygon();
            rayo.addPoint(26, 7);
            rayo.addPoint(14, 27);
            rayo.addPoint(25, 23);
            rayo.addPoint(20, 39);
            rayo.addPoint(35, 15);
            rayo.addPoint(25, 19);
            g2.fillPolygon(rayo);

            g2.dispose();
        }
    }
}
