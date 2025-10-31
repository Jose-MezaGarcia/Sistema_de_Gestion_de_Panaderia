package com.example.gestion_panaderia.modelo;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

public class Inventario {
    private List<Producto> productos;

    public Inventario() {
        this.productos = new ArrayList<>();
    }

    public Producto buscarPorNombre(String nombre) {
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }

    public void agregarProducto(Producto p) {
        this.productos.add(p);
    }

    public boolean eliminarProducto(String codigo) {
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            if (p.getCodigo().equals(codigo)) {
                productos.remove(i);
                return true;
            }
        }
        return false;
    }

    _
    public boolean actualizarStock(String codigo, int cantidadAAgregar) {
        Producto productoEncontrado = null;

        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            if (p.getCodigo().equals(codigo)) {
                productoEncontrado = p;
                break;
            }
        }

        if (productoEncontrado != null) {
            productoEncontrado.actualizarStock(cantidadAAgregar);
            return true;
        }
        return false;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this.productos);
    }
}