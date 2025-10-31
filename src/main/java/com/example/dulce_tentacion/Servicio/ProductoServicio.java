package com.example.gestion_panaderia.Servicio;

import com.example.gestion_panaderia.Repositorio.ILeerRepo;
import com.example.gestion_panaderia.Repositorio.IEscribirRepo;
import com.example.gestion_panaderia.modelo.Producto;

import java.util.List;

/**
 * Implementa las operaciones del servicio de productos.
 */

public class ProductoServicio implements IProductoServicio {

    private final ILeerRepo<Producto> lector;
    private final IEscribirRepo<Producto> escritor;

    public ProductoServicio(ILeerRepo<Producto> lector, IEscribirRepo<Producto> escritor) {
        this.lector = lector;
        this.escritor = escritor;
    }

    @Override
    public void agregarProducto(Producto p) {
        List<Producto> productos = lector.leer();
        productos.add(p);
        escritor.escribir(productos);
        System.out.println("Producto agregado: " + p.getNombre());
    }

    @Override
    public Producto buscarPorCodigo(String codigo) {
        List<Producto> productos = lector.leer();

        for (Producto p : productos) {
            if (p.getCodigo().equals(codigo)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public List<Producto> listarProductos() {
        return lector.leer();
    }
}
