package com.example.dulce_tentacion.Servicio;

import com.example.dulce_tentacion.Modelo.Producto;
import com.example.dulce_tentacion.Repositorio.IRepositorio;

import java.util.List;

/**
 * Implementación de la lógica de negocio para productos.
 * Depende del repositorio de productos (IRepositorio<Producto>).
 */
public class ProductoServicio implements IProductoServicio {

    private final IRepositorio<Producto> productoRepo; // Depende de la capa Repositorio

    public ProductoServicio(IRepositorio<Producto> productoRepo) {
        this.productoRepo = productoRepo;
    }

    @Override
    public void agregarProducto(Producto p) {
        // Implementar lógica para agregar un producto (leer, agregar, guardar)
    }

    @Override
    public void actualizarProducto(Producto p) {
        // Implementar actualización de producto existente
    }

    @Override
    public Producto buscarPorId(String id) {
        // Implementar búsqueda de producto por ID
        return null;
    }

    @Override
    public List<Producto> listarProductos() {
        // Implementar lectura de lista de productos desde el repositorio
        return null;
    }
}
