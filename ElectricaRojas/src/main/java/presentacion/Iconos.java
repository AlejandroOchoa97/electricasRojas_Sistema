/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Utilidad para cargar iconos usados en las pantallas.
 * Tambien crea el boton de regreso para mantener el mismo estilo en todo el sistema.
 */
public class Iconos {

    public static Icon flechaIzquierda(Color color, int tamano) {
        return new IconoFlechaIzquierda(color, tamano);
    }

    public static Icon regresar(Color color, int tamano) {
        return new IconoRegresar(color, tamano);
    }

    public static JButton crearBotonRegresar() {
        JButton boton = new JButton();
        boton.setUI(new BasicButtonUI());
        boton.setIcon(cargarIconoVolver());
        boton.setToolTipText("Volver");
        boton.setPreferredSize(new Dimension(38, 38));
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setRolloverEnabled(false);
        boton.setOpaque(false);
        boton.setBorder(BorderFactory.createEmptyBorder());
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    public static Icon cargarIconoVolver() {
        java.net.URL recurso = Iconos.class.getResource("/iconos/icons8-volver-30.png");

        if (recurso == null) {
            return regresar(Color.WHITE, 24);
        }

        return new ImageIcon(recurso);
    }

    private static class IconoFlechaIzquierda implements Icon {

        private final Color color;
        private final int tamano;

        public IconoFlechaIzquierda(Color color, int tamano) {
            this.color = color;
            this.tamano = tamano;
        }

        @Override
        public int getIconWidth() {
            return tamano;
        }

        @Override
        public int getIconHeight() {
            return tamano;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            int medio = y + tamano / 2;
            int izquierda = x + 3;
            int derecha = x + tamano - 3;

            g2.drawLine(derecha, medio, izquierda, medio);
            g2.drawLine(izquierda, medio, x + tamano / 2, y + 3);
            g2.drawLine(izquierda, medio, x + tamano / 2, y + tamano - 3);

            g2.dispose();
        }
    }

    private static class IconoRegresar implements Icon {

        private final Color color;
        private final int tamano;

        public IconoRegresar(Color color, int tamano) {
            this.color = color;
            this.tamano = tamano;
        }

        @Override
        public int getIconWidth() {
            return tamano;
        }

        @Override
        public int getIconHeight() {
            return tamano;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            int izquierda = x + 4;
            int derecha = x + tamano - 5;
            int arriba = y + 5;
            int medio = y + tamano / 2;
            int abajo = y + tamano - 5;

            g2.drawLine(izquierda + 8, arriba, izquierda, medio);
            g2.drawLine(izquierda, medio, izquierda + 8, abajo);
            g2.drawLine(izquierda + 2, medio, derecha, medio);
            g2.drawLine(derecha, medio, derecha, arriba + 4);

            g2.dispose();
        }
    }
}
