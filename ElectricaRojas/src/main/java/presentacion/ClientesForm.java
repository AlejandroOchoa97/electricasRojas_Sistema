package presentacion;

import entidad.Cliente;
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
import javax.swing.JButton;
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
import negocio.ClienteBO;

public class ClientesForm extends JFrame {

    private JTextField txtBuscar;
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    private List<Cliente> clientesActuales;
    private ClienteBO clienteBO;
    private Usuario usuario;

    public ClientesForm() {
        this(null);
    }

    public ClientesForm(Usuario usuario) {
        this.usuario = usuario;
        clienteBO = new ClienteBO();
        clientesActuales = new ArrayList<>();
        configurarVentana();
        construirFormulario();
        cargarClientes();
    }

    private void configurarVentana() {
        setTitle("ElectricaRojas - Clientes");
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

        JLabel lblTitulo = new JLabel("Clientes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        marca.add(lblTitulo, BorderLayout.CENTER);
        encabezado.add(marca, BorderLayout.WEST);

        JLabel lblModulo = new JLabel("Registro y busqueda");
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
        btnBuscar.addActionListener(e -> buscarClientes());
        btnTodos.addActionListener(e -> cargarClientes());

        botonesBusqueda.add(btnBuscar);
        botonesBusqueda.add(btnTodos);
        barraSuperior.add(botonesBusqueda, BorderLayout.EAST);

        contenido.add(barraSuperior, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                new Object[]{"Nombre", "Telefono", "Correo", "Direccion"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaClientes.setRowHeight(32);
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaClientes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaClientes.getTableHeader().setBackground(new Color(235, 241, 250));
        tablaClientes.getTableHeader().setForeground(new Color(37, 44, 76));
        tablaClientes.setGridColor(new Color(230, 235, 244));

        JScrollPane scroll = new JScrollPane(tablaClientes);
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
        btnEditar.addActionListener(e -> editarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());

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

    private void cargarClientes() {
        txtBuscar.setText("");
        llenarTabla(clienteBO.listarClientes());
    }

    private void buscarClientes() {
        llenarTabla(clienteBO.buscarClientePorNombre(txtBuscar.getText().trim()));
    }

    private void llenarTabla(List<Cliente> clientes) {
        clientesActuales = clientes;
        modeloTabla.setRowCount(0);

        for (Cliente cliente : clientesActuales) {
            modeloTabla.addRow(new Object[]{
                cliente.getNombre(),
                cliente.getTelefono(),
                cliente.getCorreo(),
                cliente.getDireccion()
            });
        }
    }

    private Cliente obtenerClienteSeleccionado() {
        int fila = tablaClientes.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente de la lista.");
            return null;
        }

        return clientesActuales.get(fila);
    }

    private void abrirAgregar() {
        ClienteDialog dialogo = new ClienteDialog(this, null);
        dialogo.setVisible(true);

        if (dialogo.isGuardado()) {
            cargarClientes();
        }
    }

    private void editarCliente() {
        Cliente cliente = obtenerClienteSeleccionado();

        if (cliente == null) {
            return;
        }

        int respuesta = JOptionPane.showOptionDialog(
                this,
                "Se editara el cliente seleccionado. Deseas continuar?",
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

        ClienteDialog dialogo = new ClienteDialog(this, cliente);
        dialogo.setVisible(true);

        if (dialogo.isGuardado()) {
            cargarClientes();
        }
    }

    private void eliminarCliente() {
        Cliente cliente = obtenerClienteSeleccionado();

        if (cliente == null) {
            return;
        }

        int respuesta = JOptionPane.showOptionDialog(
                this,
                "Se eliminara el cliente seleccionado. Deseas continuar?",
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

        if (clienteBO.eliminarCliente(cliente.getIdCliente())) {
            JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente.");
            cargarClientes();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar el cliente. Verifica que no tenga ventas registradas.");
        }
    }

    private void volverAlMenu() {
        if (usuario != null) {
            new MenuPrincipal(usuario).setVisible(true);
        }
        dispose();
    }

    private class ClienteDialog extends JDialog {

        private JTextField txtNombre;
        private JTextField txtTelefono;
        private JTextField txtCorreo;
        private JTextField txtDireccion;
        private Cliente clienteEditar;
        private boolean guardado;

        public ClienteDialog(JFrame padre, Cliente clienteEditar) {
            super(padre, true);
            this.clienteEditar = clienteEditar;
            configurarDialogo();
            construirDialogo();
            cargarDatos();
        }

        private void configurarDialogo() {
            setTitle(clienteEditar == null ? "Agregar cliente" : "Editar cliente");
            setSize(470, 470);
            setLocationRelativeTo(ClientesForm.this);
            setResizable(false);
        }

        private void construirDialogo() {
            JPanel principal = new JPanel(new BorderLayout());
            principal.setBackground(new Color(244, 247, 252));
            setContentPane(principal);

            JPanel encabezado = new JPanel(new BorderLayout());
            encabezado.setBackground(new Color(18, 139, 226));
            encabezado.setBorder(BorderFactory.createEmptyBorder(20, 28, 20, 28));

            JLabel titulo = new JLabel(clienteEditar == null ? "Agregar cliente" : "Editar cliente");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
            titulo.setForeground(Color.WHITE);
            encabezado.add(titulo, BorderLayout.WEST);

            JLabel subtitulo = new JLabel("Clientes");
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
            txtTelefono = crearCampoDialogo();
            txtCorreo = crearCampoDialogo();
            txtDireccion = crearCampoDialogo();

            int fila = 0;
            agregarCampoDialogo(campos, "Nombre", txtNombre, fila++);
            agregarCampoDialogo(campos, "Telefono", txtTelefono, fila++);
            agregarCampoDialogo(campos, "Correo", txtCorreo, fila++);
            agregarCampoDialogo(campos, "Direccion", txtDireccion, fila++);

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
            if (clienteEditar == null) {
                return;
            }

            txtNombre.setText(clienteEditar.getNombre());
            txtTelefono.setText(clienteEditar.getTelefono());
            txtCorreo.setText(clienteEditar.getCorreo());
            txtDireccion.setText(clienteEditar.getDireccion());
        }

        private void guardar() {
            Cliente cliente = new Cliente();

            if (clienteEditar != null) {
                cliente.setIdCliente(clienteEditar.getIdCliente());
            }

            cliente.setNombre(txtNombre.getText().trim());
            cliente.setTelefono(txtTelefono.getText().trim());
            cliente.setCorreo(txtCorreo.getText().trim());
            cliente.setDireccion(txtDireccion.getText().trim());

            boolean resultado;

            if (clienteEditar == null) {
                resultado = clienteBO.insertarCliente(cliente);
            } else {
                resultado = clienteBO.actualizarCliente(cliente);
            }

            if (resultado) {
                JOptionPane.showMessageDialog(this, "Cliente guardado correctamente.");
                guardado = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar el cliente. Verifica los datos.");
            }
        }

        public boolean isGuardado() {
            return guardado;
        }
    }
}
