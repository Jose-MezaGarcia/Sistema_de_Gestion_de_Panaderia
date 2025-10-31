package com.example.gestion_panaderia.modelo;

public class Producto {
    private String id;
    private String nombre;
    private String precio;
    private int stock;
    private Categoria categoria;

    public Producto(String id, String nombre, String precio, int stock, Categoria categoria ) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }
    public void actualizarStock(int cantidad){
        this.stock +=  cantidad;
    }

    @Override
    public String toString() {
        return id + "|" + nombre + "| $" + String.format("%.2f", precio);
    }
    public String getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public String getPrecio() {
        return precio;
    }
    public int getStock() {
        return stock;
    }
    public Categoria getCategoria() {
        return categoria;
    }
}
