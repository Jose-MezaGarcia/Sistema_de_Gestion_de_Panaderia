// ProductoServiceImpl.java
package com.example.gestion_panaderia.service;

import com.example.gestion_panaderia.model.Producto;
import com.example.gestion_panaderia.repository.IRepository;
import java.util.List;
import java.util.stream.Collectors;

public class ProductoServiceImpl implements IProductoService {

    private IRepository<Producto> productoRepository;

    public ProductoServiceImpl(IRepository<Producto> productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public void agregarProducto(Producto producto) {
        List<Producto> productos = productoRepository.cargar();
        productos.add(producto);
        productoRepository.guardar(productos);
    }

    @Override
    public void actualizarProducto(Producto productoActualizado) {
        List<Producto> productos = productoRepository.cargar();
        productos = productos.stream()
                .map(p -> p.getId().equals(productoActualizado.getId()) ? productoActualizado : p)
                .collect(Collectors.toList());
        productoRepository.guardar(productos);
    }

    @Override
    public void eliminarProducto(String id) {
        List<Producto> productos = productoRepository.cargar();
        productos = productos.stream()
                .filter(p -> !p.getId().equals(id))
                .collect(Collectors.toList());
        productoRepository.guardar(productos);
    }

    @Override
    public Producto buscarPorId(String id) {
        return productoRepository.cargar().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Producto> listarProductos() {
        return productoRepository.cargar();
    }
}