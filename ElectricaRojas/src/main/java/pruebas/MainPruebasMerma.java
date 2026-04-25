/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pruebas;

import entidad.Merma;
import java.util.List;
import negocio.MermaBO;
import persistencia.ConexionBD;

/**
 * Prueba rapida del modulo mermas desde consola.
 * Registra una merma y muestra el historial guardado.
 */
public class MainPruebasMerma {

    public static void main(String[] args) {

        MermaBO mermaBO = new MermaBO();

        System.out.println("=== PRUEBA REGISTRAR MERMA ===");

        Merma merma = new Merma(1, 1, "Producto danado");

        boolean registrada = mermaBO.registrarMerma(merma);

        if (registrada) {
            System.out.println("Merma registrada correctamente.");
            System.out.println(merma);
        } else {
            System.out.println("No se pudo registrar la merma.");
        }

        System.out.println("\n=== PRUEBA LISTAR MERMAS ===");

        List<Merma> mermas = mermaBO.listarMermas();

        for (Merma mermaLista : mermas) {
            System.out.println(mermaLista);
        }

        System.out.println("\n=== PRUEBA FINALIZADA ===");
        ConexionBD.cerrarDriverMysql();
    }
}
