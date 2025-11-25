// IProductoService.java
package com.example.gestion_panaderia.service;

import com.example.gestion_panaderia.model.Producto;
import java.util.List;

public interface IProductoService {
    void agregarProducto(Producto producto);
    void actualizarProducto(Producto producto);
    void eliminarProducto(String id);  // <- AGREGAR ESTE MÉTODO
    Producto buscarPorId(String id);
    List<Producto> listarProductos();
}