package negocio;

import entidad.Categoria;
import java.util.List;
import persistencia.CategoriaDAO;

public class CategoriaBO {

    private CategoriaDAO categoriaDAO;

    public CategoriaBO() {
        categoriaDAO = new CategoriaDAO();
    }

    public boolean insertarCategoria(Categoria categoria) {
        if (!validarCategoria(categoria)) {
            return false;
        }

        return categoriaDAO.insertarCategoria(categoria);
    }

    public List<Categoria> listarCategorias() {
        return categoriaDAO.listarCategorias();
    }

    public Categoria buscarCategoriaPorId(int idCategoria) {
        if (idCategoria <= 0) {
            System.out.println("El id de la categoria no es valido.");
            return null;
        }

        return categoriaDAO.buscarCategoriaPorId(idCategoria);
    }

    public List<Categoria> buscarCategoriaPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return categoriaDAO.listarCategorias();
        }

        return categoriaDAO.buscarCategoriaPorNombre(nombre);
    }

    public boolean actualizarCategoria(Categoria categoria) {
        if (categoria == null || categoria.getIdCategoria() <= 0) {
            System.out.println("El id de la categoria no es valido.");
            return false;
        }

        if (!validarCategoria(categoria)) {
            return false;
        }

        return categoriaDAO.actualizarCategoria(categoria);
    }

    public boolean eliminarCategoria(int idCategoria) {
        if (idCategoria <= 0) {
            System.out.println("El id de la categoria no es valido.");
            return false;
        }

        return categoriaDAO.eliminarCategoria(idCategoria);
    }

    private boolean validarCategoria(Categoria categoria) {
        if (categoria == null) {
            System.out.println("La categoria no puede ser nula.");
            return false;
        }

        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            System.out.println("El nombre de la categoria es obligatorio.");
            return false;
        }

        return true;
    }
}
