package presentacion;

import entidad.Merma;
import entidad.Producto;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;
import negocio.MermaBO;
import negocio.ProductoBO;

public class MermasForm extends JFrame {

    private JComboBox<Object> cboProducto;
    private JTextField txtCantidad;
    private JTextArea txtMotivo;
    private JLabel lblStock;
    private JTable tablaMermas;
    private DefaultTableModel modeloTabla;
    private Usuario usuario;
    private ProductoBO productoBO;
    private MermaBO mermaBO;
    private List<Producto> productosActuales;

    public MermasForm() {
        this(null);
    }

    public MermasForm(Usuario usuario) {
        this.usuario = usuario;
        productoBO = new ProductoBO();
        mermaBO = new MermaBO();
        productosActuales = new ArrayList<>();
        configurarVentana();
        construirFormulario();
        cargarProductos();
        cargarMermas();
        actualizarStock();
    }

    private void configurarVentana() {
        setTitle("ElectricaRojas - Mermas");
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

        JLabel lblTitulo = new JLabel("Mermas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        marca.add(lblTitulo, BorderLayout.CENTER);
        encabezado.add(marca, BorderLayout.WEST);

        JLabel lblModulo = new JLabel("Perdidas y danos");
        lblModulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblModulo.setForeground(new Color(224, 240, 255));
        encabezado.add(lblModulo, BorderLayout.EAST);

        principal.add(encabezado, BorderLayout.NORTH);

        JPanel contenido = new JPanel(new BorderLayout(24, 0));
        contenido.setOpaque(false);
        contenido.setBorder(BorderFactory.createEmptyBorder(28, 34, 30, 34));
        principal.add(contenido, BorderLayout.CENTER);

        contenido.add(crearPanelRegistro(), BorderLayout.WEST);
        contenido.add(crearPanelHistorial(), BorderLayout.CENTER);
    }

    private JPanel crearPanelRegistro() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(390, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 229, 238)),
                BorderFactory.createEmptyBorder(22, 22, 22, 22)
        ));

        JLabel lblDatos = new JLabel("Registrar merma");
        lblDatos.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblDatos.setForeground(new Color(37, 44, 76));
        panel.add(lblDatos, BorderLayout.NORTH);

        JPanel campos = new JPanel(new GridBagLayout());
        campos.setOpaque(false);
        campos.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        cboProducto = new JComboBox<>();
        cboProducto.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboProducto.setPreferredSize(new Dimension(320, 34));
        cboProducto.addActionListener(e -> actualizarStock());

        lblStock = crearEtiquetaInfo("0");
        txtCantidad = crearCampo();
        txtMotivo = new JTextArea(4, 20);
        txtMotivo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtMotivo.setLineWrap(true);
        txtMotivo.setWrapStyleWord(true);
        txtMotivo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(205, 214, 230)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        int fila = 0;
        agregarCampo(campos, "Producto", cboProducto, fila++);
        agregarCampo(campos, "Stock disponible", lblStock, fila++);
        agregarCampo(campos, "Cantidad", txtCantidad, fila++);
        agregarCampo(campos, "Motivo", new JScrollPane(txtMotivo), fila++);

        panel.add(campos, BorderLayout.CENTER);

        JPanel botones = new JPanel(new GridLayout(2, 1, 0, 10));
        botones.setOpaque(false);

        JButton btnRegistrar = crearBoton("Registrar merma", new Color(18, 139, 226));
        JButton btnLimpiar = crearBoton("Limpiar", new Color(96, 125, 139));

        btnRegistrar.addActionListener(e -> registrarMerma());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        botones.add(btnRegistrar);
        botones.add(btnLimpiar);

        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelHistorial() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);

        JLabel lblHistorial = new JLabel("Historial de mermas");
        lblHistorial.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHistorial.setForeground(new Color(37, 44, 76));
        panel.add(lblHistorial, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new Object[]{"Fecha", "Producto", "Cantidad", "Motivo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaMermas = new JTable(modeloTabla);
        tablaMermas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaMermas.setRowHeight(32);
        tablaMermas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaMermas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaMermas.getTableHeader().setBackground(new Color(235, 241, 250));
        tablaMermas.getTableHeader().setForeground(new Color(37, 44, 76));
        tablaMermas.setGridColor(new Color(230, 235, 244));

        JScrollPane scroll = new JScrollPane(tablaMermas);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(224, 229, 238)));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JTextField crearCampo() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setPreferredSize(new Dimension(320, 34));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(205, 214, 230)),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        return campo;
    }

    private JLabel crearEtiquetaInfo(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        etiqueta.setForeground(new Color(37, 44, 76));
        etiqueta.setPreferredSize(new Dimension(320, 34));
        etiqueta.setOpaque(true);
        etiqueta.setBackground(new Color(248, 250, 253));
        etiqueta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 229, 238)),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        return etiqueta;
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
        boton.setPreferredSize(new Dimension(150, 38));
        if ("Volver".equals(texto)) {
            boton.setIcon(Iconos.flechaIzquierda(Color.WHITE, 15));
            boton.setIconTextGap(8);
        }
        return boton;
    }

    private void agregarCampo(JPanel panel, String etiqueta, java.awt.Component componente, int fila) {
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
        gbc.insets = new Insets(0, 0, 12, 0);
        panel.add(componente, gbc);
    }

    private void cargarProductos() {
        productosActuales = productoBO.listarProductos();
        DefaultComboBoxModel<Object> modelo = new DefaultComboBoxModel<>();
        modelo.addElement("Seleccionar");

        for (Producto producto : productosActuales) {
            modelo.addElement(producto);
        }

        cboProducto.setModel(modelo);
        cboProducto.setRenderer((lista, valor, indice, seleccionado, enfocado) -> {
            JLabel etiqueta = new JLabel();
            etiqueta.setOpaque(true);
            etiqueta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            etiqueta.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

            if (valor instanceof Producto) {
                Producto producto = (Producto) valor;
                etiqueta.setText(producto.getNombre() + " | Stock: " + producto.getStock());
            } else if (valor != null) {
                etiqueta.setText(valor.toString());
            }

            etiqueta.setBackground(seleccionado ? new Color(18, 139, 226) : Color.WHITE);
            etiqueta.setForeground(seleccionado ? Color.WHITE : new Color(37, 44, 76));
            return etiqueta;
        });
    }

    private void cargarMermas() {
        List<Merma> mermas = mermaBO.listarMermas();
        Map<Integer, String> nombresProductos = crearMapaProductos();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        modeloTabla.setRowCount(0);

        for (Merma merma : mermas) {
            String fecha = merma.getFecha() != null ? merma.getFecha().format(formatoFecha) : "";
            String producto = nombresProductos.getOrDefault(merma.getIdProducto(), "Producto eliminado");

            modeloTabla.addRow(new Object[]{
                fecha,
                producto,
                merma.getCantidad(),
                merma.getMotivo()
            });
        }
    }

    private Map<Integer, String> crearMapaProductos() {
        Map<Integer, String> mapa = new HashMap<>();

        for (Producto producto : productoBO.listarProductos()) {
            mapa.put(producto.getIdProducto(), producto.getNombre());
        }

        return mapa;
    }

    private void actualizarStock() {
        Object item = cboProducto.getSelectedItem();

        if (!(item instanceof Producto)) {
            lblStock.setText("0");
            return;
        }

        Producto producto = (Producto) item;
        lblStock.setText(String.valueOf(producto.getStock()));
    }

    private void registrarMerma() {
        Object item = cboProducto.getSelectedItem();

        if (!(item instanceof Producto)) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto.");
            return;
        }

        try {
            Producto producto = (Producto) item;
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            String motivo = txtMotivo.getText().trim();

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.");
                return;
            }

            if (cantidad > producto.getStock()) {
                JOptionPane.showMessageDialog(this, "Stock insuficiente para registrar la merma.");
                return;
            }

            if (motivo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El motivo es obligatorio.");
                return;
            }

            int respuesta = JOptionPane.showOptionDialog(
                    this,
                    "Se registrara la merma y se descontara el stock. Deseas continuar?",
                    "Confirmar merma",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    new Object[]{"Si", "Cancelar"},
                    "Cancelar"
            );

            if (respuesta != 0) {
                return;
            }

            Merma merma = new Merma(producto.getIdProducto(), cantidad, motivo);

            if (mermaBO.registrarMerma(merma)) {
                JOptionPane.showMessageDialog(this, "Merma registrada correctamente.");
                limpiarFormulario();
                cargarProductos();
                cargarMermas();
                actualizarStock();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar la merma. Verifica los datos.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ingresa una cantidad valida.");
        }
    }

    private void limpiarFormulario() {
        cboProducto.setSelectedIndex(0);
        txtCantidad.setText("");
        txtMotivo.setText("");
        actualizarStock();
    }

    private void volverAlMenu() {
        if (usuario != null) {
            new MenuPrincipal(usuario).setVisible(true);
        }
        dispose();
    }

}
