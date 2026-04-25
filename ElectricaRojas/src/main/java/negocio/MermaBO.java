/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import entidad.Merma;
import java.util.List;
import persistencia.MermaDAO;

/**
 * Capa de negocio para mermas.
 * Revisa que la cantidad y el motivo sean validos antes de tocar el inventario.
 */
public class MermaBO {

    private MermaDAO mermaDAO;

    public MermaBO() {
        mermaDAO = new MermaDAO();
    }

    public boolean registrarMerma(Merma merma) {
        if (!validarMerma(merma)) {
            return false;
        }

        return mermaDAO.registrarMerma(merma);
    }

    public List<Merma> listarMermas() {
        return mermaDAO.listarMermas();
    }

    private boolean validarMerma(Merma merma) {
        if (merma == null) {
            System.out.println("La merma no puede ser nula.");
            return false;
        }

        if (merma.getIdProducto() <= 0) {
            System.out.println("El producto de la merma no es valido.");
            return false;
        }

        if (merma.getCantidad() <= 0) {
            System.out.println("La cantidad debe ser mayor a cero.");
            return false;
        }

        if (merma.getMotivo() == null || merma.getMotivo().trim().isEmpty()) {
            System.out.println("El motivo de la merma es obligatorio.");
            return false;
        }

        return true;
    }
}
