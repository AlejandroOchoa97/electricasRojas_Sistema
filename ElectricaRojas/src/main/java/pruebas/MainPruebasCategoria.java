package pruebas;

import entidad.Categoria;
import java.time.LocalDateTime;
import java.util.List;
import negocio.CategoriaBO;

public class MainPruebasCategoria {

    public static void main(String[] args) {

        CategoriaBO categoriaBO = new CategoriaBO();

        System.out.println("=== PRUEBA INSERTAR CATEGORIA ===");

        Categoria categoriaNueva = new Categoria("Categoria prueba " + LocalDateTime.now());

        boolean insertada = categoriaBO.insertarCategoria(categoriaNueva);

        if (insertada) {
            System.out.println("Categoria insertada correctamente.");
        } else {
            System.out.println("No se pudo insertar la categoria.");
        }

        System.out.println("\n=== PRUEBA LISTAR CATEGORIAS ===");

        List<Categoria> categorias = categoriaBO.listarCategorias();

        for (Categoria categoria : categorias) {
            System.out.println(categoria);
        }

        System.out.println("\n=== PRUEBA FINALIZADA ===");
    }
}
