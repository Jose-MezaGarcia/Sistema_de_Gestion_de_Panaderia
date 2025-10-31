package com.example.gestion_panaderia.Servicio;

import com.example.gestion_panaderia.modelo.Producto;
import java.util.List;

/**
 * Define las operaciones del servicio de productos.
 */
public interface IProductoServicio {

    void agregarProducto(Producto p);

    Producto buscarPorCodigo(String codigo);

    List<Producto> listarProductos();
}
