package presentacion;

import entidad.Categoria;
import entidad.Cliente;
import entidad.DetalleVenta;
import entidad.Producto;
import entidad.Usuario;
import entidad.Venta;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;
import negocio.CategoriaBO;
import negocio.ClienteBO;
import negocio.ProductoBO;
import negocio.VentaBO;

public class VentasForm extends JFrame {

    private JTextField txtBuscarProducto;
    private JComboBox<Object> cboCategoria;
    private JTable tablaProductos;
    private DefaultTableModel modeloProductos;

    private JComboBox<Object> cboCliente;
    private JTextField txtCantidad;
    private JLabel lblProductoSeleccionado;

    private JTable tablaCarrito;
    private DefaultTableModel modeloCarrito;
    private JLabel lblTotal;
    private JButton btnQuitarCarrito;

    private Usuario usuario;
    private ClienteBO clienteBO;
    private ProductoBO productoBO;
    private CategoriaBO categoriaBO;
    private VentaBO ventaBO;
    private List<Producto> productosActuales;
    private List<Producto> productosFiltrados;
    private List<DetalleVenta> detalles;
    private List<Producto> productosCarrito;
    private Producto productoSeleccionado;
    private BigDecimal total;

    public VentasForm() {
        this(null);
    }

    public VentasForm(Usuario usuario) {
        this.usuario = usuario;
        clienteBO = new ClienteBO();
        productoBO = new ProductoBO();
        categoriaBO = new CategoriaBO();
        ventaBO = new VentaBO();
        productosActuales = new ArrayList<>();
        productosFiltrados = new ArrayList<>();
        detalles = new ArrayList<>();
        productosCarrito = new ArrayList<>();
        total = BigDecimal.ZERO;
        configurarVentana();
        construirFormulario();
        cargarClientes();
        cargarCategorias();
        cargarProductos();
        actualizarInfoProducto(null);
        actualizarTotal();
    }

    private void configurarVentana() {
        setTitle("ElectricaRojas - Ventas");
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

        JLabel lblTitulo = new JLabel("Ventas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        marca.add(lblTitulo, BorderLayout.CENTER);
        encabezado.add(marca, BorderLayout.WEST);

        JLabel lblModulo = new JLabel("Punto de venta");
        lblModulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblModulo.setForeground(new Color(224, 240, 255));
        encabezado.add(lblModulo, BorderLayout.EAST);

        principal.add(encabezado, BorderLayout.NORTH);

        JPanel contenido = new JPanel(new BorderLayout(0, 18));
        contenido.setOpaque(false);
        contenido.setBorder(BorderFactory.createEmptyBorder(24, 34, 26, 34));
        principal.add(contenido, BorderLayout.CENTER);

        JPanel superior = new JPanel(new BorderLayout(24, 0));
        superior.setOpaque(false);
        superior.add(crearPanelProductos(), BorderLayout.CENTER);
        superior.add(crearPanelVenta(), BorderLayout.EAST);

        contenido.add(superior, BorderLayout.CENTER);
        contenido.add(crearPanelCarrito(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelProductos() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setOpaque(false);

        JLabel lblTitulo = new JLabel("Productos disponibles");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(37, 44, 76));
        panel.add(lblTitulo, BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(0, 14));
        cuerpo.setOpaque(false);

        JPanel filtros = new JPanel(new GridBagLayout());
        filtros.setOpaque(false);
        filtros.setPreferredSize(new Dimension(0, 44));

        txtBuscarProducto = crearCampo();
        txtBuscarProducto.setPreferredSize(new Dimension(360, 38));

        cboCategoria = new JComboBox<>();
        cboCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboCategoria.setPreferredSize(new Dimension(230, 38));

        JPanel botonesBusqueda = new JPanel(new GridLayout(1, 2, 10, 0));
        botonesBusqueda.setOpaque(false);
        botonesBusqueda.setPreferredSize(new Dimension(250, 38));
        JButton btnBuscar = crearBoton("Buscar", new Color(18, 139, 226));
        JButton btnTodos = crearBoton("Ver todos", new Color(96, 125, 139));
        btnBuscar.addActionListener(e -> filtrarProductos());
        btnTodos.addActionListener(e -> limpiarFiltrosProductos());
        botonesBusqueda.add(btnBuscar);
        botonesBusqueda.add(btnTodos);

        GridBagConstraints gbcFiltro = new GridBagConstraints();
        gbcFiltro.gridy = 0;
        gbcFiltro.insets = new Insets(0, 0, 0, 12);
        gbcFiltro.fill = GridBagConstraints.HORIZONTAL;
        gbcFiltro.weightx = 1.0;
        filtros.add(txtBuscarProducto, gbcFiltro);

        gbcFiltro.gridx = 1;
        gbcFiltro.weightx = 0;
        filtros.add(cboCategoria, gbcFiltro);

        gbcFiltro.gridx = 2;
        gbcFiltro.insets = new Insets(0, 0, 0, 0);
        filtros.add(botonesBusqueda, gbcFiltro);

        cuerpo.add(filtros, BorderLayout.NORTH);

        modeloProductos = new DefaultTableModel(
                new Object[]{"Nombre", "Categoria", "Descripcion", "Precio", "Stock"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaProductos = crearTabla(modeloProductos);
        tablaProductos.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarProductoTabla();
            }
        });

        JScrollPane scroll = new JScrollPane(tablaProductos);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(224, 229, 238)));
        cuerpo.add(scroll, BorderLayout.CENTER);
        panel.add(cuerpo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelVenta() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(430, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 229, 238)),
                BorderFactory.createEmptyBorder(22, 22, 22, 22)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblDatos = new JLabel("Datos de venta");
        lblDatos.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblDatos.setForeground(new Color(37, 44, 76));
        gbc.gridy = 0;
        gbc.insets = new Insets(18, 0, 28, 0);
        panel.add(lblDatos, gbc);

        cboCliente = new JComboBox<>();
        cboCliente.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboCliente.setPreferredSize(new Dimension(320, 34));

        lblProductoSeleccionado = crearEtiquetaInfo("Selecciona un producto de la lista");
        txtCantidad = crearCampo();
        txtCantidad.setPreferredSize(new Dimension(320, 34));

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 12, 0);
        panel.add(crearGrupoCampoVenta("Cliente", cboCliente), gbc);

        gbc.gridy = 2;
        panel.add(crearGrupoCampoVenta("Producto", lblProductoSeleccionado), gbc);

        gbc.gridy = 3;
        panel.add(crearGrupoCampoVenta("Cantidad a comprar", txtCantidad), gbc);

        JPanel espacio = new JPanel();
        espacio.setOpaque(false);
        gbc.gridy = 4;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(espacio, gbc);

        JPanel acciones = new JPanel(new GridLayout(2, 1, 0, 10));
        acciones.setOpaque(false);
        acciones.setPreferredSize(new Dimension(320, 100));

        JButton btnAgregar = crearBoton("Agregar al carrito", new Color(18, 139, 226));
        JButton btnConfirmar = crearBoton("Confirmar", new Color(29, 172, 125));

        btnAgregar.addActionListener(e -> agregarAlCarrito());
        btnConfirmar.addActionListener(e -> confirmarVenta());

        acciones.add(btnAgregar);
        acciones.add(btnConfirmar);

        gbc.gridy = 5;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 4, 0);
        panel.add(acciones, gbc);

        return panel;
    }

    private JPanel crearPanelCarrito() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(0, 265));

        JPanel superior = new JPanel(new BorderLayout());
        superior.setOpaque(false);

        JLabel lblCarrito = new JLabel("Carrito");
        lblCarrito.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblCarrito.setForeground(new Color(37, 44, 76));
        superior.add(lblCarrito, BorderLayout.WEST);

        lblTotal = new JLabel();
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTotal.setForeground(new Color(18, 139, 226));

        panel.add(superior, BorderLayout.NORTH);

        modeloCarrito = new DefaultTableModel(
                new Object[]{"Producto", "Categoria", "Cantidad", "Precio", "Subtotal"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCarrito = crearTabla(modeloCarrito);
        JScrollPane scroll = new JScrollPane(tablaCarrito);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(224, 229, 238)));
        panel.add(scroll, BorderLayout.CENTER);

        JPanel accionesCarrito = new JPanel(new BorderLayout());
        accionesCarrito.setOpaque(false);

        btnQuitarCarrito = crearBoton("Quitar del carrito", new Color(239, 83, 80));
        btnQuitarCarrito.setPreferredSize(new Dimension(190, 38));
        btnQuitarCarrito.addActionListener(e -> quitarDelCarrito());
        accionesCarrito.add(btnQuitarCarrito, BorderLayout.WEST);
        accionesCarrito.add(lblTotal, BorderLayout.EAST);

        panel.add(accionesCarrito, BorderLayout.SOUTH);

        return panel;
    }

    private JTable crearTabla(DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setRowHeight(32);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(new Color(235, 241, 250));
        tabla.getTableHeader().setForeground(new Color(37, 44, 76));
        tabla.setGridColor(new Color(230, 235, 244));
        return tabla;
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

    private JLabel crearEtiquetaInfo(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        etiqueta.setForeground(new Color(37, 44, 76));
        etiqueta.setPreferredSize(new Dimension(320, 34));
        etiqueta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 229, 238)),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        etiqueta.setOpaque(true);
        etiqueta.setBackground(new Color(248, 250, 253));
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
        boton.setPreferredSize(new Dimension(140, 38));
        if ("Volver".equals(texto)) {
            boton.setIcon(Iconos.flechaIzquierda(Color.WHITE, 15));
            boton.setIconTextGap(8);
        }
        return boton;
    }

    private JPanel crearGrupoCampoVenta(String etiqueta, java.awt.Component componente) {
        JPanel grupo = new JPanel(new BorderLayout(0, 6));
        grupo.setOpaque(false);
        grupo.setPreferredSize(new Dimension(320, 60));

        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(70, 76, 105));
        grupo.add(lbl, BorderLayout.NORTH);
        grupo.add(componente, BorderLayout.CENTER);

        return grupo;
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

    private void cargarClientes() {
        DefaultComboBoxModel<Object> modelo = new DefaultComboBoxModel<>();
        modelo.addElement("Publico general");

        for (Cliente cliente : clienteBO.listarClientes()) {
            if (!"Publico general".equalsIgnoreCase(cliente.getNombre())) {
                modelo.addElement(cliente);
            }
        }

        cboCliente.setModel(modelo);
        cboCliente.setRenderer((lista, valor, indice, seleccionado, enfocado) -> crearEtiquetaCombo(valor, seleccionado));
    }

    private void cargarCategorias() {
        DefaultComboBoxModel<Object> modelo = new DefaultComboBoxModel<>();
        modelo.addElement("Todas las categorias");

        for (Categoria categoria : categoriaBO.listarCategorias()) {
            modelo.addElement(categoria);
        }

        cboCategoria.setModel(modelo);
        cboCategoria.setRenderer((lista, valor, indice, seleccionado, enfocado) -> crearEtiquetaCombo(valor, seleccionado));
    }

    private JLabel crearEtiquetaCombo(Object valor, boolean seleccionado) {
        JLabel etiqueta = new JLabel();
        etiqueta.setOpaque(true);
        etiqueta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        etiqueta.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        if (valor instanceof Cliente) {
            etiqueta.setText(((Cliente) valor).getNombre());
        } else if (valor instanceof Categoria) {
            etiqueta.setText(((Categoria) valor).getNombre());
        } else if (valor != null) {
            etiqueta.setText(valor.toString());
        }

        etiqueta.setBackground(seleccionado ? new Color(18, 139, 226) : Color.WHITE);
        etiqueta.setForeground(seleccionado ? Color.WHITE : new Color(37, 44, 76));
        return etiqueta;
    }

    private void cargarProductos() {
        productosActuales = productoBO.listarProductos();
        filtrarProductos();
    }

    private void filtrarProductos() {
        String nombre = txtBuscarProducto.getText().trim().toLowerCase();
        Object categoriaSeleccionada = cboCategoria.getSelectedItem();
        Integer idCategoria = null;

        if (categoriaSeleccionada instanceof Categoria) {
            idCategoria = ((Categoria) categoriaSeleccionada).getIdCategoria();
        }

        productosFiltrados = new ArrayList<>();
        modeloProductos.setRowCount(0);

        for (Producto producto : productosActuales) {
            boolean coincideNombre = nombre.isEmpty() || producto.getNombre().toLowerCase().contains(nombre);
            boolean coincideCategoria = idCategoria == null || producto.getIdCategoria() == idCategoria;

            if (coincideNombre && coincideCategoria) {
                productosFiltrados.add(producto);
                modeloProductos.addRow(new Object[]{
                    producto.getNombre(),
                    producto.getNombreCategoria(),
                    producto.getDescripcion(),
                    producto.getPrecio(),
                    producto.getStock()
                });
            }
        }

        productoSeleccionado = null;
        tablaProductos.clearSelection();
        actualizarInfoProducto(null);
    }

    private void limpiarFiltrosProductos() {
        txtBuscarProducto.setText("");
        cboCategoria.setSelectedIndex(0);
        filtrarProductos();
    }

    private void seleccionarProductoTabla() {
        int fila = tablaProductos.getSelectedRow();

        if (fila < 0 || fila >= productosFiltrados.size()) {
            productoSeleccionado = null;
            actualizarInfoProducto(null);
            return;
        }

        productoSeleccionado = productosFiltrados.get(fila);
        actualizarInfoProducto(productoSeleccionado);
    }

    private void actualizarInfoProducto(Producto producto) {
        if (producto == null) {
            lblProductoSeleccionado.setText("Selecciona un producto de la lista");
            return;
        }

        lblProductoSeleccionado.setText(producto.getNombre());
    }

    private void agregarAlCarrito() {
        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto de la lista.");
            return;
        }

        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.");
                return;
            }

            int cantidadActual = cantidadEnCarrito(productoSeleccionado.getIdProducto());

            if (cantidadActual + cantidad > productoSeleccionado.getStock()) {
                JOptionPane.showMessageDialog(this, "Stock insuficiente para este producto.");
                return;
            }

            int indice = indiceDetalle(productoSeleccionado.getIdProducto());

            if (indice >= 0) {
                DetalleVenta detalle = detalles.get(indice);
                detalle.setCantidad(detalle.getCantidad() + cantidad);
            } else {
                DetalleVenta detalle = new DetalleVenta(productoSeleccionado.getIdProducto(), cantidad);
                detalle.setPrecio(productoSeleccionado.getPrecio());
                detalles.add(detalle);
                productosCarrito.add(productoSeleccionado);
            }

            recalcularCarrito();
            txtCantidad.setText("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ingresa una cantidad valida.");
        }
    }

    private int indiceDetalle(int idProducto) {
        for (int i = 0; i < detalles.size(); i++) {
            if (detalles.get(i).getIdProducto() == idProducto) {
                return i;
            }
        }

        return -1;
    }

    private int cantidadEnCarrito(int idProducto) {
        int cantidad = 0;

        for (DetalleVenta detalle : detalles) {
            if (detalle.getIdProducto() == idProducto) {
                cantidad += detalle.getCantidad();
            }
        }

        return cantidad;
    }

    private void quitarDelCarrito() {
        int fila = tablaCarrito.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto del carrito.");
            return;
        }

        detalles.remove(fila);
        productosCarrito.remove(fila);
        recalcularCarrito();
    }

    private void recalcularCarrito() {
        modeloCarrito.setRowCount(0);
        total = BigDecimal.ZERO;

        for (int i = 0; i < detalles.size(); i++) {
            DetalleVenta detalle = detalles.get(i);
            Producto producto = productosCarrito.get(i);
            BigDecimal subtotal = detalle.getPrecio().multiply(new BigDecimal(detalle.getCantidad()));
            total = total.add(subtotal);

            modeloCarrito.addRow(new Object[]{
                producto.getNombre(),
                producto.getNombreCategoria(),
                detalle.getCantidad(),
                detalle.getPrecio(),
                subtotal
            });
        }

        actualizarTotal();
    }

    private void confirmarVenta() {
        if (detalles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Agrega al menos un producto al carrito.");
            return;
        }

        int respuesta = JOptionPane.showOptionDialog(
                this,
                "Se realizara la venta y se descontara el stock. Deseas continuar?",
                "Confirmar venta",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Si", "Cancelar"},
                "Si"
        );

        if (respuesta != 0) {
            return;
        }

        Integer idCliente = null;
        Object clienteSeleccionado = cboCliente.getSelectedItem();

        if (clienteSeleccionado instanceof Cliente) {
            idCliente = ((Cliente) clienteSeleccionado).getIdCliente();
        }

        int idUsuario = usuario != null ? usuario.getIdUsuario() : 1;
        Venta venta = new Venta(idUsuario, idCliente);
        venta.setDetalles(new ArrayList<>(detalles));

        if (ventaBO.registrarVenta(venta)) {
            JOptionPane.showMessageDialog(this, "Venta realizada correctamente. Total: $" + venta.getTotal());
            limpiarVenta();
            cargarProductos();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo realizar la venta.");
        }
    }

    private void limpiarVenta() {
        detalles.clear();
        productosCarrito.clear();
        modeloCarrito.setRowCount(0);
        total = BigDecimal.ZERO;
        txtCantidad.setText("");
        cboCliente.setSelectedIndex(0);
        actualizarTotal();
    }

    private void actualizarTotal() {
        lblTotal.setText("Total: $" + total);
    }

    private void volverAlMenu() {
        if (usuario != null) {
            new MenuPrincipal(usuario).setVisible(true);
        }
        dispose();
    }

}
