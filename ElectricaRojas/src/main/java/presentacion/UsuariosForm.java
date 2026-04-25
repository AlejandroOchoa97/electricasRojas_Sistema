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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;
import negocio.UsuarioBO;

/**
 * Pantalla para administrar usuarios del sistema.
 * Se usa para crear accesos y asignar roles como ADMIN o VENDEDOR.
 */
public class UsuariosForm extends JFrame {

    private JTextField txtBuscar;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private List<Usuario> usuariosActuales;
    private UsuarioBO usuarioBO;
    private Usuario usuarioSesion;

    public UsuariosForm() {
        this(null);
    }

    public UsuariosForm(Usuario usuarioSesion) {
        this.usuarioSesion = usuarioSesion;
        usuarioBO = new UsuarioBO();
        usuariosActuales = new ArrayList<>();
        configurarVentana();
        construirFormulario();
        cargarUsuarios();
    }

    private void configurarVentana() {
        setTitle("ElectricaRojas - Usuarios");
        setSize(1200, 760);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void construirFormulario() {
        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(new Color(244, 247, 252));
        setContentPane(principal);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(new Color(18, 139, 226));
        encabezado.setBorder(BorderFactory.createEmptyBorder(22, 34, 22, 34));

        JPanel marca = new JPanel(new BorderLayout(14, 0));
        marca.setOpaque(false);

        JButton btnRegresar = Iconos.crearBotonRegresar();
        btnRegresar.addActionListener(e -> volverAlMenu());
        marca.add(btnRegresar, BorderLayout.WEST);

        JLabel lblTitulo = new JLabel("Usuarios");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        marca.add(lblTitulo, BorderLayout.CENTER);
        encabezado.add(marca, BorderLayout.WEST);

        JLabel lblModulo = new JLabel("Administracion de accesos");
        lblModulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblModulo.setForeground(new Color(224, 240, 255));
        encabezado.add(lblModulo, BorderLayout.EAST);

        principal.add(encabezado, BorderLayout.NORTH);

        JPanel contenido = new JPanel(new BorderLayout(0, 18));
        contenido.setOpaque(false);
        contenido.setBorder(BorderFactory.createEmptyBorder(28, 34, 30, 34));
        principal.add(contenido, BorderLayout.CENTER);

        JPanel barraSuperior = new JPanel(new BorderLayout(14, 0));
        barraSuperior.setOpaque(false);

        txtBuscar = crearCampo();
        txtBuscar.setPreferredSize(new Dimension(520, 38));
        barraSuperior.add(txtBuscar, BorderLayout.CENTER);

        JPanel botonesBusqueda = new JPanel(new GridLayout(1, 2, 10, 0));
        botonesBusqueda.setOpaque(false);

        JButton btnBuscar = crearBoton("Buscar", new Color(18, 139, 226));
        JButton btnTodos = crearBoton("Ver todos", new Color(96, 125, 139));
        btnBuscar.addActionListener(e -> buscarUsuarios());
        btnTodos.addActionListener(e -> cargarUsuarios());

        botonesBusqueda.add(btnBuscar);
        botonesBusqueda.add(btnTodos);
        barraSuperior.add(botonesBusqueda, BorderLayout.EAST);

        contenido.add(barraSuperior, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                new Object[]{"Nombre", "Usuario", "Password", "Rol"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaUsuarios.setRowHeight(32);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaUsuarios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaUsuarios.getTableHeader().setBackground(new Color(235, 241, 250));
        tablaUsuarios.getTableHeader().setForeground(new Color(37, 44, 76));
        tablaUsuarios.setGridColor(new Color(230, 235, 244));

        JScrollPane scroll = new JScrollPane(tablaUsuarios);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(224, 229, 238)));
        contenido.add(scroll, BorderLayout.CENTER);

        JPanel barraAcciones = new JPanel(new BorderLayout());
        barraAcciones.setOpaque(false);

        JPanel accionesIzquierda = new JPanel(new GridLayout(1, 3, 12, 0));
        accionesIzquierda.setOpaque(false);

        JButton btnAgregar = crearBoton("Agregar", new Color(18, 139, 226));
        JButton btnEditar = crearBoton("Editar", new Color(18, 139, 226));
        JButton btnEliminar = crearBoton("Eliminar", new Color(239, 83, 80));

        btnAgregar.addActionListener(e -> abrirAgregar());
        btnEditar.addActionListener(e -> editarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());

        accionesIzquierda.add(btnAgregar);
        accionesIzquierda.add(btnEditar);
        accionesIzquierda.add(btnEliminar);

        barraAcciones.add(accionesIzquierda, BorderLayout.WEST);

        contenido.add(barraAcciones, BorderLayout.SOUTH);
    }

    private JTextField crearCampo() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(205, 214, 230)),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        return campo;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setUI(new BasicButtonUI());
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setOpaque(true);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(118, 38));
        if ("Volver".equals(texto)) {
            boton.setIcon(Iconos.flechaIzquierda(Color.WHITE, 15));
            boton.setIconTextGap(8);
        }
        return boton;
    }

    private void cargarUsuarios() {
        txtBuscar.setText("");
        llenarTabla(usuarioBO.listarUsuarios());
    }

    private void buscarUsuarios() {
        llenarTabla(usuarioBO.buscarUsuarioPorNombre(txtBuscar.getText().trim()));
    }

    private void llenarTabla(List<Usuario> usuarios) {
        usuariosActuales = usuarios;
        modeloTabla.setRowCount(0);

        for (Usuario usuario : usuariosActuales) {
            modeloTabla.addRow(new Object[]{
                usuario.getNombre(),
                usuario.getUsuario(),
                "********",
                usuario.getRol()
            });
        }
    }

    private Usuario obtenerUsuarioSeleccionado() {
        int fila = tablaUsuarios.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario de la lista.");
            return null;
        }

        return usuariosActuales.get(fila);
    }

    private void abrirAgregar() {
        UsuarioDialog dialogo = new UsuarioDialog(this, null);
        dialogo.setVisible(true);

        if (dialogo.isGuardado()) {
            cargarUsuarios();
        }
    }

    private void editarUsuario() {
        Usuario usuario = obtenerUsuarioSeleccionado();

        if (usuario == null) {
            return;
        }

        int respuesta = JOptionPane.showOptionDialog(
                this,
                "Se editara el usuario seleccionado. Deseas continuar?",
                "Confirmar edicion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Si", "Cancelar"},
                "Si"
        );

        if (respuesta != 0) {
            return;
        }

        UsuarioDialog dialogo = new UsuarioDialog(this, usuario);
        dialogo.setVisible(true);

        if (dialogo.isGuardado()) {
            cargarUsuarios();
        }
    }

    private void eliminarUsuario() {
        Usuario usuario = obtenerUsuarioSeleccionado();

        if (usuario == null) {
            return;
        }

        if (usuarioSesion != null && usuario.getIdUsuario() == usuarioSesion.getIdUsuario()) {
            JOptionPane.showMessageDialog(this, "No puedes eliminar el usuario con sesion activa.");
            return;
        }

        int respuesta = JOptionPane.showOptionDialog(
                this,
                "Se eliminara el usuario seleccionado. Deseas continuar?",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                new Object[]{"Si", "Cancelar"},
                "Cancelar"
        );

        if (respuesta != 0) {
            return;
        }

        if (usuarioBO.eliminarUsuario(usuario.getIdUsuario())) {
            JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.");
            cargarUsuarios();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar el usuario. Verifica que no tenga ventas registradas.");
        }
    }

    private void volverAlMenu() {
        if (usuarioSesion != null) {
            new MenuPrincipal(usuarioSesion).setVisible(true);
        }
        dispose();
    }

    private class UsuarioDialog extends JDialog {

        private JTextField txtNombre;
        private JTextField txtUsuario;
        private JTextField txtPassword;
        private JComboBox<String> cboRol;
        private Usuario usuarioEditar;
        private boolean guardado;

        public UsuarioDialog(JFrame padre, Usuario usuarioEditar) {
            super(padre, true);
            this.usuarioEditar = usuarioEditar;
            configurarDialogo();
            construirDialogo();
            cargarDatos();
        }

        private void configurarDialogo() {
            setTitle(usuarioEditar == null ? "Agregar usuario" : "Editar usuario");
            setSize(470, 450);
            setLocationRelativeTo(UsuariosForm.this);
            setResizable(false);
        }

        private void construirDialogo() {
            JPanel principal = new JPanel(new BorderLayout());
            principal.setBackground(new Color(244, 247, 252));
            setContentPane(principal);

            JPanel encabezado = new JPanel(new BorderLayout());
            encabezado.setBackground(new Color(18, 139, 226));
            encabezado.setBorder(BorderFactory.createEmptyBorder(20, 28, 20, 28));

            JLabel titulo = new JLabel(usuarioEditar == null ? "Agregar usuario" : "Editar usuario");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
            titulo.setForeground(Color.WHITE);
            encabezado.add(titulo, BorderLayout.WEST);

            JLabel subtitulo = new JLabel("Accesos");
            subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            subtitulo.setForeground(new Color(224, 240, 255));
            encabezado.add(subtitulo, BorderLayout.EAST);

            principal.add(encabezado, BorderLayout.NORTH);

            JPanel campos = new JPanel(new GridBagLayout());
            campos.setBackground(Color.WHITE);
            campos.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(224, 229, 238)),
                    BorderFactory.createEmptyBorder(22, 28, 20, 28)
            ));

            txtNombre = crearCampoDialogo();
            txtUsuario = crearCampoDialogo();
            txtPassword = crearCampoDialogo();
            cboRol = new JComboBox<>(new String[]{"Seleccionar", "ADMIN", "VENDEDOR"});
            cboRol.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            cboRol.setPreferredSize(new Dimension(330, 34));

            int fila = 0;
            agregarCampoDialogo(campos, "Nombre", txtNombre, fila++);
            agregarCampoDialogo(campos, "Usuario", txtUsuario, fila++);
            agregarCampoDialogo(campos, "Password", txtPassword, fila++);
            agregarCampoDialogo(campos, "Rol", cboRol, fila++);

            JPanel cuerpo = new JPanel(new BorderLayout());
            cuerpo.setOpaque(false);
            cuerpo.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 28));
            cuerpo.add(campos, BorderLayout.CENTER);
            principal.add(cuerpo, BorderLayout.CENTER);

            JPanel botones = new JPanel(new GridLayout(1, 2, 12, 0));
            botones.setOpaque(false);
            botones.setBorder(BorderFactory.createEmptyBorder(0, 28, 24, 28));

            JButton btnGuardar = crearBoton("Guardar", new Color(18, 139, 226));
            JButton btnCancelar = crearBoton("Cancelar", new Color(96, 125, 139));

            btnGuardar.addActionListener(e -> guardar());
            btnCancelar.addActionListener(e -> dispose());

            botones.add(btnGuardar);
            botones.add(btnCancelar);
            principal.add(botones, BorderLayout.SOUTH);
        }

        private JTextField crearCampoDialogo() {
            JTextField campo = new JTextField();
            campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            campo.setPreferredSize(new Dimension(330, 34));
            campo.setBackground(new Color(248, 250, 253));
            campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(205, 214, 230)),
                    BorderFactory.createEmptyBorder(4, 10, 4, 10)
            ));
            return campo;
        }

        private void agregarCampoDialogo(JPanel panel, String etiqueta, java.awt.Component componente, int fila) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = fila * 2;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(0, 0, 5, 0);

            JLabel lbl = new JLabel(etiqueta);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lbl.setForeground(new Color(70, 76, 105));
            panel.add(lbl, gbc);

            gbc.gridy = fila * 2 + 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            gbc.insets = new Insets(0, 0, 10, 0);
            panel.add(componente, gbc);
        }

        private void cargarDatos() {
            if (usuarioEditar == null) {
                return;
            }

            txtNombre.setText(usuarioEditar.getNombre());
            txtUsuario.setText(usuarioEditar.getUsuario());
            txtPassword.setText(usuarioEditar.getPassword());
            cboRol.setSelectedItem(usuarioEditar.getRol());
        }

        private void guardar() {
            Usuario usuario = new Usuario();

            if (usuarioEditar != null) {
                usuario.setIdUsuario(usuarioEditar.getIdUsuario());
            }

            usuario.setNombre(txtNombre.getText().trim());
            usuario.setUsuario(txtUsuario.getText().trim());
            usuario.setPassword(txtPassword.getText().trim());
            usuario.setRol(cboRol.getSelectedItem().toString());

            boolean resultado;

            if (usuarioEditar == null) {
                resultado = usuarioBO.insertarUsuario(usuario);
            } else {
                resultado = usuarioBO.actualizarUsuario(usuario);
            }

            if (resultado) {
                JOptionPane.showMessageDialog(this, "Usuario guardado correctamente.");
                guardado = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar el usuario. Verifica los datos o que el usuario no este duplicado.");
            }
        }

        public boolean isGuardado() {
            return guardado;
        }
    }
}
