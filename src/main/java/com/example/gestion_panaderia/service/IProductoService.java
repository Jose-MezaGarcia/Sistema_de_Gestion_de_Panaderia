package com.example.gestion_panaderia.service;

import com.example.gestion_panaderia.model.Producto;
import java.util.List;

public interface IProductoService {
    void agregarProducto(Producto p);
    void actualizarProducto(Producto p);
    Producto buscarPorId(String id);
    List<Producto> listarProductos();
}
