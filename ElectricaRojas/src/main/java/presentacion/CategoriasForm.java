package presentacion;

import entidad.Categoria;
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
import negocio.CategoriaBO;

public class CategoriasForm extends JFrame {

    private JTextField txtBuscar;
    private JTable tablaCategorias;
    private DefaultTableModel modeloTabla;
    private List<Categoria> categoriasActuales;
    private CategoriaBO categoriaBO;
    private Usuario usuario;

    public CategoriasForm() {
        this(null);
    }

    public CategoriasForm(Usuario usuario) {
        this.usuario = usuario;
        categoriaBO = new CategoriaBO();
        categoriasActuales = new ArrayList<>();
        configurarVentana();
        construirFormulario();
        cargarCategorias();
    }

    private void configurarVentana() {
        setTitle("ElectricaRojas - Categorias");
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

        JLabel lblTitulo = new JLabel("Categorias");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        marca.add(lblTitulo, BorderLayout.CENTER);
        encabezado.add(marca, BorderLayout.WEST);

        JLabel lblModulo = new JLabel("Tipos de producto");
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
        btnBuscar.addActionListener(e -> buscarCategorias());
        btnTodos.addActionListener(e -> cargarCategorias());

        botonesBusqueda.add(btnBuscar);
        botonesBusqueda.add(btnTodos);
        barraSuperior.add(botonesBusqueda, BorderLayout.EAST);

        contenido.add(barraSuperior, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new Object[]{"Nombre"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCategorias = new JTable(modeloTabla);
        tablaCategorias.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaCategorias.setRowHeight(32);
        tablaCategorias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCategorias.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaCategorias.getTableHeader().setBackground(new Color(235, 241, 250));
        tablaCategorias.getTableHeader().setForeground(new Color(37, 44, 76));
        tablaCategorias.setGridColor(new Color(230, 235, 244));

        JScrollPane scroll = new JScrollPane(tablaCategorias);
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
        btnEditar.addActionListener(e -> editarCategoria());
        btnEliminar.addActionListener(e -> eliminarCategoria());

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

    private void cargarCategorias() {
        txtBuscar.setText("");
        llenarTabla(categoriaBO.listarCategorias());
    }

    private void buscarCategorias() {
        llenarTabla(categoriaBO.buscarCategoriaPorNombre(txtBuscar.getText().trim()));
    }

    private void llenarTabla(List<Categoria> categorias) {
        categoriasActuales = categorias;
        modeloTabla.setRowCount(0);

        for (Categoria categoria : categoriasActuales) {
            modeloTabla.addRow(new Object[]{
                categoria.getNombre()
            });
        }
    }

    private Categoria obtenerCategoriaSeleccionada() {
        int fila = tablaCategorias.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una categoria de la lista.");
            return null;
        }

        return categoriasActuales.get(fila);
    }

    private void abrirAgregar() {
        CategoriaDialog dialogo = new CategoriaDialog(this, null);
        dialogo.setVisible(true);

        if (dialogo.isGuardado()) {
            cargarCategorias();
        }
    }

    private void editarCategoria() {
        Categoria categoria = obtenerCategoriaSeleccionada();

        if (categoria == null) {
            return;
        }

        int respuesta = JOptionPane.showOptionDialog(
                this,
                "Se editara la categoria seleccionada. Deseas continuar?",
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

        CategoriaDialog dialogo = new CategoriaDialog(this, categoria);
        dialogo.setVisible(true);

        if (dialogo.isGuardado()) {
            cargarCategorias();
        }
    }

    private void eliminarCategoria() {
        Categoria categoria = obtenerCategoriaSeleccionada();

        if (categoria == null) {
            return;
        }

        int respuesta = JOptionPane.showOptionDialog(
                this,
                "Se eliminara la categoria seleccionada. Deseas continuar?",
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

        if (categoriaBO.eliminarCategoria(categoria.getIdCategoria())) {
            JOptionPane.showMessageDialog(this, "Categoria eliminada correctamente.");
            cargarCategorias();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar la categoria. Verifica que no tenga productos registrados.");
        }
    }

    private void volverAlMenu() {
        if (usuario != null) {
            new MenuPrincipal(usuario).setVisible(true);
        }
        dispose();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new CategoriasForm().setVisible(true));
    }

    private class CategoriaDialog extends JDialog {

        private JTextField txtNombre;
        private Categoria categoriaEditar;
        private boolean guardado;

        public CategoriaDialog(JFrame padre, Categoria categoriaEditar) {
            super(padre, true);
            this.categoriaEditar = categoriaEditar;
            configurarDialogo();
            construirDialogo();
            cargarDatos();
        }

        private void configurarDialogo() {
            setTitle(categoriaEditar == null ? "Agregar categoria" : "Editar categoria");
            setSize(470, 310);
            setLocationRelativeTo(CategoriasForm.this);
            setResizable(false);
        }

        private void construirDialogo() {
            JPanel principal = new JPanel(new BorderLayout());
            principal.setBackground(new Color(244, 247, 252));
            setContentPane(principal);

            JPanel encabezado = new JPanel(new BorderLayout());
            encabezado.setBackground(new Color(18, 139, 226));
            encabezado.setBorder(BorderFactory.createEmptyBorder(20, 28, 20, 28));

            JLabel titulo = new JLabel(categoriaEditar == null ? "Agregar categoria" : "Editar categoria");
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
            titulo.setForeground(Color.WHITE);
            encabezado.add(titulo, BorderLayout.WEST);

            JLabel subtitulo = new JLabel("Categorias");
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
            agregarCampoDialogo(campos, "Nombre", txtNombre, 0);

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
            if (categoriaEditar == null) {
                return;
            }

            txtNombre.setText(categoriaEditar.getNombre());
        }

        private void guardar() {
            Categoria categoria = new Categoria();

            if (categoriaEditar != null) {
                categoria.setIdCategoria(categoriaEditar.getIdCategoria());
            }

            categoria.setNombre(txtNombre.getText().trim());

            boolean resultado;

            if (categoriaEditar == null) {
                resultado = categoriaBO.insertarCategoria(categoria);
            } else {
                resultado = categoriaBO.actualizarCategoria(categoria);
            }

            if (resultado) {
                JOptionPane.showMessageDialog(this, "Categoria guardada correctamente.");
                guardado = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar la categoria. Verifica los datos.");
            }
        }

        public boolean isGuardado() {
            return guardado;
        }
    }
}
