/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import com.toedter.calendar.JDateChooser;
import entidad.Usuario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;
import negocio.ReporteBO;

/**
 * Pantalla de reportes.
 * Permite consultar inventario, ventas, productos mas vendidos y mermas.
 */
public class ReportesForm extends JFrame {

    private JComboBox<String> cboReporte;
    private JDateChooser txtFechaInicio;
    private JDateChooser txtFechaFin;
    private JTable tablaReportes;
    private DefaultTableModel modeloTabla;
    private JLabel lblTituloReporte;
    private JLabel lblRegistros;
    private JLabel lblResumenUno;
    private JLabel lblResumenDos;
    private Usuario usuario;
    private ReporteBO reporteBO;

    public ReportesForm() {
        this(null);
    }

    public ReportesForm(Usuario usuario) {
        this.usuario = usuario;
        reporteBO = new ReporteBO();
        configurarVentana();
        construirFormulario();
        prepararReporte();
    }

    private void configurarVentana() {
        setTitle("ElectricaRojas - Reportes");
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

        JButton btnRegresar = crearBotonRegresar();
        btnRegresar.addActionListener(e -> volverAlMenu());
        marca.add(btnRegresar, BorderLayout.WEST);

        JLabel lblTitulo = new JLabel("Reportes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        marca.add(lblTitulo, BorderLayout.CENTER);

        encabezado.add(marca, BorderLayout.WEST);

        JLabel lblModulo = new JLabel("Analisis del negocio");
        lblModulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblModulo.setForeground(new Color(224, 240, 255));
        encabezado.add(lblModulo, BorderLayout.EAST);

        principal.add(encabezado, BorderLayout.NORTH);

        JPanel contenido = new JPanel(new BorderLayout(0, 18));
        contenido.setOpaque(false);
        contenido.setBorder(BorderFactory.createEmptyBorder(28, 34, 30, 34));
        principal.add(contenido, BorderLayout.CENTER);

        contenido.add(crearBarraSuperior(), BorderLayout.NORTH);
        contenido.add(crearPanelCentral(), BorderLayout.CENTER);
    }

    private JPanel crearBarraSuperior() {
        JPanel panel = new JPanel(new BorderLayout(14, 0));
        panel.setOpaque(false);

        JPanel filtros = new JPanel(new GridLayout(1, 3, 12, 0));
        filtros.setOpaque(false);

        cboReporte = new JComboBox<>(new String[]{
            "Inventario general",
            "Productos con bajo stock",
            "Ventas",
            "Productos mas vendidos",
            "Mermas"
        });
        cboReporte.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboReporte.addActionListener(e -> prepararReporte());

        txtFechaInicio = crearCampoFecha("Fecha inicio");
        txtFechaFin = crearCampoFecha("Fecha fin");

        filtros.add(cboReporte);
        filtros.add(txtFechaInicio);
        filtros.add(txtFechaFin);
        panel.add(filtros, BorderLayout.CENTER);

        JPanel botones = new JPanel(new GridLayout(1, 1, 10, 0));
        botones.setOpaque(false);
        botones.setPreferredSize(new Dimension(170, 44));

        JButton btnGenerar = crearBoton("Generar", new Color(18, 139, 226));
        btnGenerar.addActionListener(e -> generarReporte());

        botones.add(btnGenerar);
        panel.add(botones, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout(0, 18));
        panel.setOpaque(false);

        lblTituloReporte = new JLabel("Reporte");
        lblTituloReporte.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTituloReporte.setForeground(new Color(37, 44, 76));
        panel.add(lblTituloReporte, BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(0, 18));
        cuerpo.setOpaque(false);
        cuerpo.add(crearPanelResumen(), BorderLayout.NORTH);
        cuerpo.add(crearTablaReportes(), BorderLayout.CENTER);

        panel.add(cuerpo, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelResumen() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 14, 0));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(0, 92));

        lblRegistros = new JLabel("0");
        lblResumenUno = new JLabel("-");
        lblResumenDos = new JLabel("-");

        panel.add(crearTarjetaResumen("Registros", lblRegistros, new Color(18, 139, 226)));
        panel.add(crearTarjetaResumen("Resumen", lblResumenUno, new Color(29, 172, 125)));
        panel.add(crearTarjetaResumen("Detalle", lblResumenDos, new Color(96, 125, 139)));

        return panel;
    }

    private JPanel crearTarjetaResumen(String titulo, JLabel valor, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 229, 238)),
                BorderFactory.createEmptyBorder(14, 18, 14, 18)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitulo.setForeground(new Color(112, 121, 145));
        panel.add(lblTitulo, BorderLayout.NORTH);

        valor.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valor.setForeground(color);
        panel.add(valor, BorderLayout.CENTER);

        return panel;
    }

    private JScrollPane crearTablaReportes() {
        modeloTabla = new DefaultTableModel(new Object[]{"Informacion"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaReportes = new JTable(modeloTabla);
        tablaReportes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaReportes.setRowHeight(36);
        tablaReportes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaReportes.setShowVerticalLines(false);
        tablaReportes.setGridColor(new Color(235, 239, 247));
        tablaReportes.setSelectionBackground(new Color(218, 238, 255));
        tablaReportes.setSelectionForeground(new Color(37, 44, 76));
        tablaReportes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaReportes.getTableHeader().setBackground(new Color(248, 250, 253));
        tablaReportes.getTableHeader().setForeground(new Color(37, 44, 76));
        tablaReportes.getTableHeader().setPreferredSize(new Dimension(0, 38));

        JScrollPane scroll = new JScrollPane(tablaReportes);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(224, 229, 238)));
        return scroll;
    }

    private JDateChooser crearCampoFecha(String titulo) {
        JDateChooser campo = new JDateChooser();
        campo.setDateFormatString("yyyy-MM-dd");
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(205, 214, 230)),
                titulo
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
        boton.setPreferredSize(new Dimension(120, 38));
        if ("Volver".equals(texto)) {
            boton.setIcon(Iconos.flechaIzquierda(Color.WHITE, 15));
            boton.setIconTextGap(8);
        }
        return boton;
    }

    private JButton crearBotonRegresar() {
        return Iconos.crearBotonRegresar();
    }

    private void prepararReporte() {
        int opcion = cboReporte.getSelectedIndex();

        switch (opcion) {
            case 0:
                lblTituloReporte.setText("Inventario general");
                configurarColumnas(new String[]{"Producto", "Categoria", "Descripcion", "Precio", "Stock"});
                break;
            case 1:
                lblTituloReporte.setText("Productos con bajo stock");
                configurarColumnas(new String[]{"Producto", "Categoria", "Stock"});
                break;
            case 2:
                lblTituloReporte.setText("Ventas");
                configurarColumnas(new String[]{"Fecha", "Cliente", "Vendedor", "Total"});
                break;
            case 3:
                lblTituloReporte.setText("Productos mas vendidos");
                configurarColumnas(new String[]{"Producto", "Cantidad vendida", "Total vendido"});
                break;
            default:
                lblTituloReporte.setText("Mermas");
                configurarColumnas(new String[]{"Fecha", "Producto", "Cantidad", "Motivo"});
                break;
        }

        actualizarEstadoFechas();
        limpiarResumen();
        modeloTabla.setRowCount(0);
    }

    private void actualizarEstadoFechas() {
        boolean usaFechas = cboReporte.getSelectedIndex() >= 2;

        txtFechaInicio.setEnabled(usaFechas);
        txtFechaFin.setEnabled(usaFechas);

        txtFechaInicio.getDateEditor().getUiComponent().setBackground(
                usaFechas ? Color.WHITE : new Color(235, 238, 243)
        );
        txtFechaFin.getDateEditor().getUiComponent().setBackground(
                usaFechas ? Color.WHITE : new Color(235, 238, 243)
        );

        if (!usaFechas) {
            txtFechaInicio.setDate(null);
            txtFechaFin.setDate(null);
        }
    }

    private void generarReporte() {
        int opcion = cboReporte.getSelectedIndex();
        List<Object[]> reporte;
        String fechaInicio = obtenerFechaValida(txtFechaInicio);
        String fechaFin = obtenerFechaValida(txtFechaFin);

        if (fechaInicio == null || fechaFin == null) {
            return;
        }

        switch (opcion) {
            case 0:
                lblTituloReporte.setText("Inventario general");
                configurarColumnas(new String[]{"Producto", "Categoria", "Descripcion", "Precio", "Stock"});
                reporte = reporteBO.reporteInventarioGeneralTabla();
                llenarTabla(reporte);
                actualizarResumenInventario(reporte);
                break;
            case 1:
                lblTituloReporte.setText("Productos con bajo stock");
                configurarColumnas(new String[]{"Producto", "Categoria", "Stock"});
                reporte = reporteBO.reporteProductosBajoStockTabla(10);
                llenarTabla(reporte, "Todos los productos estan dentro del rango.");
                actualizarResumenBajoStock(reporte);
                break;
            case 2:
                if (fechaInicio.isEmpty() || fechaFin.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Selecciona la fecha de inicio y la fecha fin.");
                    return;
                }
                lblTituloReporte.setText("Ventas");
                configurarColumnas(new String[]{"Fecha", "Cliente", "Vendedor", "Total"});
                reporte = reporteBO.reporteVentasTabla(fechaInicio, fechaFin);
                llenarTabla(reporte);
                actualizarResumenVentas(reporte);
                break;
            case 3:
                if (fechaInicio.isEmpty() || fechaFin.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Selecciona la fecha de inicio y la fecha fin.");
                    return;
                }
                lblTituloReporte.setText("Productos mas vendidos");
                configurarColumnas(new String[]{"Producto", "Cantidad vendida", "Total vendido"});
                reporte = reporteBO.reporteProductosMasVendidosTabla(fechaInicio, fechaFin);
                llenarTabla(reporte);
                actualizarResumenMasVendidos(reporte);
                break;
            default:
                if (fechaInicio.isEmpty() || fechaFin.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Selecciona la fecha de inicio y la fecha fin.");
                    return;
                }
                lblTituloReporte.setText("Mermas");
                configurarColumnas(new String[]{"Fecha", "Producto", "Cantidad", "Motivo"});
                reporte = reporteBO.reporteMermasTabla(fechaInicio, fechaFin);
                llenarTabla(reporte);
                actualizarResumenMermas(reporte);
                break;
        }
    }

    private void configurarColumnas(String[] columnas) {
        modeloTabla.setColumnIdentifiers(columnas);
        modeloTabla.setRowCount(0);
    }

    private String obtenerFechaValida(JDateChooser campo) {
        if (!campo.isEnabled() || campo.getDate() == null) {
            return "";
        }

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        return formato.format(campo.getDate());
    }

    private void llenarTabla(List<Object[]> reporte) {
        llenarTabla(reporte, "No hay informacion para mostrar.");
    }

    private void llenarTabla(List<Object[]> reporte, String mensajeVacio) {
        modeloTabla.setRowCount(0);

        if (reporte == null || reporte.isEmpty()) {
            Object[] fila = new Object[modeloTabla.getColumnCount()];
            fila[0] = mensajeVacio;
            modeloTabla.addRow(fila);
            return;
        }

        for (Object[] fila : reporte) {
            modeloTabla.addRow(fila);
        }
    }

    private void actualizarResumenInventario(List<Object[]> reporte) {
        lblRegistros.setText(String.valueOf(reporte.size()));
        lblResumenUno.setText("Stock total: " + sumarEnteros(reporte, 4));
        lblResumenDos.setText("Categorias activas");
    }

    private void actualizarResumenBajoStock(List<Object[]> reporte) {
        lblRegistros.setText(String.valueOf(reporte.size()));
        lblResumenUno.setText("Rango bajo");
        lblResumenDos.setText("Revisar compra");
    }

    private void actualizarResumenVentas(List<Object[]> reporte) {
        lblRegistros.setText(String.valueOf(reporte.size()));
        lblResumenUno.setText("Total: $" + sumarDecimales(reporte, 3));
        lblResumenDos.setText("Ventas registradas");
    }

    private void actualizarResumenMasVendidos(List<Object[]> reporte) {
        lblRegistros.setText(String.valueOf(reporte.size()));
        lblResumenUno.setText("Piezas: " + sumarEnteros(reporte, 1));
        lblResumenDos.setText("Top productos");
    }

    private void actualizarResumenMermas(List<Object[]> reporte) {
        lblRegistros.setText(String.valueOf(reporte.size()));
        lblResumenUno.setText("Piezas: " + sumarEnteros(reporte, 2));
        lblResumenDos.setText("Perdidas registradas");
    }

    private void limpiarResumen() {
        lblRegistros.setText("0");
        lblResumenUno.setText("-");
        lblResumenDos.setText("-");
    }

    private int sumarEnteros(List<Object[]> reporte, int columna) {
        int suma = 0;

        for (Object[] fila : reporte) {
            if (fila[columna] instanceof Number) {
                suma += ((Number) fila[columna]).intValue();
            }
        }

        return suma;
    }

    private BigDecimal sumarDecimales(List<Object[]> reporte, int columna) {
        BigDecimal suma = BigDecimal.ZERO;

        for (Object[] fila : reporte) {
            if (fila[columna] instanceof BigDecimal) {
                suma = suma.add((BigDecimal) fila[columna]);
            }
        }

        return suma;
    }

    private void volverAlMenu() {
        if (usuario != null) {
            new MenuPrincipal(usuario).setVisible(true);
        }
        dispose();
    }

}
