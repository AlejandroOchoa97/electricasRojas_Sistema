package presentacion;

import entidad.Categoria;
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
import java.math.BigDecimal;
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
import negocio.CategoriaBO;
import negocio.ProductoBO;

public class ProductosForm extends JFrame {

    private JTextField txtBuscar;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;

    private ProductoBO productoBO;
    private CategoriaBO categoriaBO;
    private Usuario usuario;
    private List<Producto> productosActuales;

    public ProductosForm() {
        this(null);
    }

    public ProductosForm(Usuario usuario) {
        this.usuario = usuario;
        productoBO = new ProductoBO();
        categoriaBO = new CategoriaBO();
        productosActuales = new ArrayList<>();
        configurarVentana();
        construirFormulario();
        configurarPermisos();
        cargarProductos();
    }

    private void configurarVentana() {
        setTitle("ElectricaRojas - Productos");
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

        JLabel lblTitulo = new JLabel("Productos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        marca.add(lblTitulo, BorderLayout.CENTER);
        encabezado.add(marca, BorderLayout.WEST);

        JLabel lblModulo = new JLabel("Inventario");
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
        btnBuscar.addActionListener(e -> buscarProductos());
        btnTodos.addActionListener(e -> cargarProductos());

        botonesBusqueda.add(btnBuscar);
        botonesBusqueda.add(btnTodos);
        barraSuperior.add(botonesBusqueda, BorderLayout.EAST);

        contenido.add(barraSuperior, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                new Object[]{"Nombre", "Descripcion", "Precio", "Stock", "Categoria"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaProductos.setRowHeight(32);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProductos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaProductos.getTableHeader().setBackground(new Color(235, 241, 250));
        tablaProductos.getTableHeader().setForeground(new Color(37, 44, 76));
        tablaProductos.setGridColor(new Color(230, 235, 244));

        JScrollPane scroll = new JScrollPane(tablaProductos);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(224, 229, 238)));
        contenido.add(scroll, BorderLayout.CENTER);

        JPanel barraAcciones = new JPanel(new BorderLayout());
        barraAcciones.setOpaque(false);

        JPanel accionesIzquierda = new JPanel(new GridLayout(1, 3, 12, 0));
        accionesIzquierda.setOpaque(false);

        btnAgregar = crearBoton("Agregar", new Color(29, 172, 125));
        btnEditar = crearBoton("Editar", new Color(18, 139, 226));
        btnEliminar = crearBoton("Eliminar", new Color(239, 83, 80));

        btnAgregar.addActionListener(e -> abrirAgregar());
        btnEditar.addActionListener(e -> editarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());

        accionesIzquierda.add(btnAgregar);
        accionesIzquierda.add(btnEditar);
        accionesIzquierda.add(btnEliminar);

        barraAcciones.add(accionesIzquierda, BorderLayout.WEST);

        contenido.add(barraAcciones, BorderLayout.SOUTH);
    }

    private void configurarPermisos() {
        if (usuario != null && "VENDEDOR".equals(usuario.getRol())) {
            btnAgregar.setEnabled(false);
            btnEditar.setEnabled(false);
            btnEliminar.setEnabled(false);
        }
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

    private void cargarProductos() {
        txtBuscar.setText("");
        llenarTabla(productoBO.listarProductos());
    }

    private void buscarProductos() {
        String nombre = txtBuscar.getText().trim();
        llenarTabla(productoBO.buscarProductoPorNombre(nombre));
    }

    private void llenarTabla(List<Producto> productos) {
        productosActuales = productos;
        modeloTabla.setRowCount(0);

        for (Producto producto : productosActuales) {
            modeloTabla.addRow(new Object[]{
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getNombreCategoria()
            });
        }
    }

    private Producto obtenerProductoSeleccionado() {
        int fila = tablaProductos.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto de la lista.");
            return null;
        }

        return productosActuales.get(fila);
    }

    private void abrirAgregar() {
        ProductoDialog dialogo = new ProductoDialog(this, null);
        dialogo.setVisible(true);

        if (dialogo.isGuardado()) {
            cargarProductos();
        }
    }

    private void editarProducto() {
        Producto producto = obtenerProductoSeleccionado();

        if (producto == null) {
            return;
        }

        int respuesta = JOptionPane.showOptionDialog(
                this,
                "Se editara el producto seleccionado. Deseas continuar?",
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

        ProductoDialog dialogo = new ProductoDialog(this, producto);
        dialogo.setVisible(true);

        if (dialogo.isGuardado()) {
            cargarProductos();
        }
    }

    private void eliminarProducto() {
        Producto producto = obtenerProductoSeleccionado();

        if (producto == null) {
            return;
        }

        int respuesta = JOptionPane.showOptionDialog(
                this,
                "Se eliminara el producto seleccionado. Deseas continuar?",
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

        if (productoBO.eliminarProducto(producto.getIdProducto())) {
            JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.");
            cargarProductos();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar el producto. Verifica que no tenga ventas o mermas registradas.");
        }
    }

    private void volverAlMenu() {
        if (usuario != null) {
            new MenuPrincipal(usuario).setVisible(true);
        }
        dispose();
    }

    private class ProductoDialog extends JDialog {

        private JTextField txtNombre;
        private JTextField txtDescripcion;
        private JTextField txtPrecio;
        private JTextField txtStock;
        private JComboBox<Object> cboCategoria;
        private Producto productoEditar;
        private boolean guardado;

        public ProductoDialog(JFrame padre, Producto productoEditar) {
            super(padre, true);
            this.productoEditar = productoEditar;
            configurarDialogo();
            construirDialogo();
            cargarCategorias();
            cargarDatos();
        }

        private void configurarDialogo() {
            setTitle(productoEditar == null ? "Agregar producto" : "Editar producto");
            setSize(470, 500);
            setLocationRelativeTo(ProductosForm.this);
            setResizable(false);
        }

        private void construirDialogo() {
            JPanel principal = new JPanel(new BorderLayout());
            principal.setBackground(new Color(244, 247, 252));
            setContentPane(principal);

            JPanel encabezado = new JPanel(new BorderLayout());
            encabezado.setBackground(new Color(18, 139, 226));
            encabezado.setBorder(BorderFactory.createEmptyBorder(20, 28, 20, 28));

            JLabel titulo = new JLabel(productoEditar == null ? "Agregar producto" : "Editar producto");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
            titulo.setForeground(Color.WHITE);
            encabezado.add(titulo, BorderLayout.WEST);

            JLabel subtitulo = new JLabel("Inventario");
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
            txtDescripcion = crearCampoDialogo();
            txtPrecio = crearCampoDialogo();
            txtStock = crearCampoDialogo();
            cboCategoria = new JComboBox<>();
            cboCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            cboCategoria.setPreferredSize(new Dimension(330, 34));

            int fila = 0;
            agregarCampoDialogo(campos, "Nombre", txtNombre, fila++);
            agregarCampoDialogo(campos, "Descripcion", txtDescripcion, fila++);
            agregarCampoDialogo(campos, "Precio", txtPrecio, fila++);
            agregarCampoDialogo(campos, "Stock", txtStock, fila++);
            agregarCampoDialogo(campos, "Categoria", cboCategoria, fila++);

            JPanel cuerpo = new JPanel(new BorderLayout());
            cuerpo.setOpaque(false);
            cuerpo.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 28));
            cuerpo.add(campos, BorderLayout.CENTER);
            principal.add(cuerpo, BorderLayout.CENTER);

            JPanel botones = new JPanel(new GridLayout(1, 2, 12, 0));
            botones.setOpaque(false);
            botones.setBorder(BorderFactory.createEmptyBorder(0, 28, 24, 28));

            JButton btnGuardar = crearBoton("Guardar", new Color(29, 172, 125));
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
            gbc.insets = new Insets(0, 0, 8, 0);
            panel.add(componente, gbc);
        }

        private void cargarCategorias() {
            DefaultComboBoxModel<Object> modelo = new DefaultComboBoxModel<>();
            modelo.addElement("Seleccionar");

            for (Categoria categoria : categoriaBO.listarCategorias()) {
                modelo.addElement(categoria);
            }

            cboCategoria.setModel(modelo);
            cboCategoria.setRenderer((lista, valor, indice, seleccionado, enfocado) -> {
                JLabel etiqueta = new JLabel();
                etiqueta.setOpaque(true);
                etiqueta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                etiqueta.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

                if (valor instanceof Categoria) {
                    etiqueta.setText(((Categoria) valor).getNombre());
                } else if (valor != null) {
                    etiqueta.setText(valor.toString());
                }

                etiqueta.setBackground(seleccionado ? new Color(18, 139, 226) : Color.WHITE);
                etiqueta.setForeground(seleccionado ? Color.WHITE : new Color(37, 44, 76));
                return etiqueta;
            });
        }

        private void cargarDatos() {
            if (productoEditar == null) {
                return;
            }

            txtNombre.setText(productoEditar.getNombre());
            txtDescripcion.setText(productoEditar.getDescripcion());
            txtPrecio.setText(String.valueOf(productoEditar.getPrecio()));
            txtStock.setText(String.valueOf(productoEditar.getStock()));
            seleccionarCategoria(productoEditar.getIdCategoria());
        }

        private void seleccionarCategoria(int idCategoria) {
            for (int i = 0; i < cboCategoria.getItemCount(); i++) {
                Object item = cboCategoria.getItemAt(i);
                if (item instanceof Categoria && ((Categoria) item).getIdCategoria() == idCategoria) {
                    cboCategoria.setSelectedIndex(i);
                    return;
                }
            }
        }

        private void guardar() {
            try {
                Object itemCategoria = cboCategoria.getSelectedItem();

                if (!(itemCategoria instanceof Categoria)) {
                    JOptionPane.showMessageDialog(this, "Selecciona una categoria.");
                    return;
                }
                
                Categoria categoria = (Categoria) itemCategoria;

                Producto producto = new Producto();

                if (productoEditar != null) {
                    producto.setIdProducto(productoEditar.getIdProducto());
                }

                producto.setNombre(txtNombre.getText().trim());
                producto.setDescripcion(txtDescripcion.getText().trim());
                producto.setPrecio(new BigDecimal(txtPrecio.getText().trim()));
                producto.setStock(Integer.parseInt(txtStock.getText().trim()));
                producto.setIdCategoria(categoria.getIdCategoria());

                boolean resultado;

                if (productoEditar == null) {
                    resultado = productoBO.insertarProducto(producto);
                } else {
                    resultado = productoBO.actualizarProducto(producto);
                }

                if (resultado) {
                    JOptionPane.showMessageDialog(this, "Producto guardado correctamente.");
                    guardado = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo guardar el producto.");
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Verifica precio y stock. Deben ser numeros validos.");
            }
        }

        public boolean isGuardado() {
            return guardado;
        }
    }
}
