package com.example.gestion_panaderia.service;

import com.example.gestion_panaderia.model.Producto;
import com.example.gestion_panaderia.repository.IRepository;

import java.util.List;

public class ProductoServiceImpl implements IProductoService {
    
    private IRepository<Producto> productoRepo;
    
    public ProductoServiceImpl(IRepository<Producto> productoRepo) {
        this.productoRepo = productoRepo;
    }
    
    @Override
    public void agregarProducto(Producto p) {
        List<Producto> productos = productoRepo.cargar();
        productos.add(p);
        productoRepo.guardar(productos);
    }
    
    @Override
    public void actualizarProducto(Producto p) {
        List<Producto> productos = productoRepo.cargar();
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId().equals(p.getId())) {
                productos.set(i, p);
                break;
            }
        }
        productoRepo.guardar(productos);
    }
    
    @Override
    public Producto buscarPorId(String id) {
        return productoRepo.findById(id);
    }
    
    @Override
    public List<Producto> listarProductos() {
        return productoRepo.cargar();
    }
}
