/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import entidad.Producto;
import java.math.BigDecimal;
import java.util.List;
import persistencia.ProductoDAO;

/**
 * Capa de negocio para productos.
 * Centraliza validaciones como precio, stock y categoria antes de usar el DAO.
 */
public class ProductoBO {

    private ProductoDAO productoDAO;

    public ProductoBO() {
        productoDAO = new ProductoDAO();
    }

    public boolean insertarProducto(Producto producto) {
        if (!validarProducto(producto)) {
            return false;
        }

        return productoDAO.insertarProducto(producto);
    }

    public List<Producto> listarProductos() {
        return productoDAO.listarProductos();
    }

    public List<Producto> buscarProductoPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return productoDAO.listarProductos();
        }

        return productoDAO.buscarProductoPorNombre(nombre);
    }

    public Producto buscarProductoPorId(int idProducto) {
        if (idProducto <= 0) {
            System.out.println("El id del producto no es valido.");
            return null;
        }

        return productoDAO.buscarProductoPorId(idProducto);
    }

    public List<Producto> listarProductosPorCategoria(int idCategoria) {
        if (idCategoria <= 0) {
            System.out.println("El id de la categoria no es valido.");
            return productoDAO.listarProductos();
        }

        return productoDAO.listarProductosPorCategoria(idCategoria);
    }

    public boolean actualizarProducto(Producto producto) {
        if (producto == null || producto.getIdProducto() <= 0) {
            System.out.println("El id del producto no es valido.");
            return false;
        }

        if (!validarProducto(producto)) {
            return false;
        }

        return productoDAO.actualizarProducto(producto);
    }

    public boolean actualizarStock(int idProducto, int nuevoStock) {
        if (idProducto <= 0) {
            System.out.println("El id del producto no es valido.");
            return false;
        }

        if (nuevoStock < 0) {
            System.out.println("El stock no puede ser negativo.");
            return false;
        }

        return productoDAO.actualizarStock(idProducto, nuevoStock);
    }

    public boolean descontarStock(int idProducto, int cantidad) {
        if (cantidad <= 0) {
            System.out.println("La cantidad debe ser mayor a cero.");
            return false;
        }

        Producto producto = buscarProductoPorId(idProducto);

        if (producto == null) {
            System.out.println("Producto no encontrado.");
            return false;
        }

        if (producto.getStock() < cantidad) {
            System.out.println("Stock insuficiente.");
            return false;
        }

        int nuevoStock = producto.getStock() - cantidad;
        return productoDAO.actualizarStock(idProducto, nuevoStock);
    }

    public boolean aumentarStock(int idProducto, int cantidad) {
        if (cantidad <= 0) {
            System.out.println("La cantidad debe ser mayor a cero.");
            return false;
        }

        Producto producto = buscarProductoPorId(idProducto);

        if (producto == null) {
            System.out.println("Producto no encontrado.");
            return false;
        }

        int nuevoStock = producto.getStock() + cantidad;
        return productoDAO.actualizarStock(idProducto, nuevoStock);
    }

    public boolean eliminarProducto(int idProducto) {
        if (idProducto <= 0) {
            System.out.println("El id del producto no es valido.");
            return false;
        }

        return productoDAO.eliminarProducto(idProducto);
    }

    private boolean validarProducto(Producto producto) {
        if (producto == null) {
            System.out.println("El producto no puede ser nulo.");
            return false;
        }

        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            System.out.println("El nombre del producto es obligatorio.");
            return false;
        }

        if (producto.getDescripcion() == null || producto.getDescripcion().trim().isEmpty()) {
            System.out.println("La descripcion del producto es obligatoria.");
            return false;
        }

        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("El precio debe ser mayor a cero.");
            return false;
        }

        if (producto.getStock() < 0) {
            System.out.println("El stock no puede ser negativo.");
            return false;
        }

        if (producto.getIdCategoria() <= 0) {
            System.out.println("La categoria del producto es obligatoria.");
            return false;
        }

        return true;
    }
}
