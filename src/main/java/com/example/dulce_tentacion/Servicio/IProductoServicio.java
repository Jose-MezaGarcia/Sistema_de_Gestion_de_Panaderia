package com.example.dulce_tentacion.Servicio;

import com.example.dulce_tentacion.Modelo.Producto;
import java.util.List;

/**
 * Interfaz que define las operaciones relacionadas con los productos.
 */
public interface IProductoServicio {

    void agregarProducto(Producto p);

    void actualizarProducto(Producto p);

    Producto buscarPorId(String id);

    List<Producto> listarProductos();
}
