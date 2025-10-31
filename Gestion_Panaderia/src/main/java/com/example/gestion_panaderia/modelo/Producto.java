package com.example.gestion_panaderia.modelo;

public class Producto {
    private String codigo;
    private String nombre;
    private double precio;
    private int stock;
    private Categoria categoria;

    public Producto(String codigo, String nombre, double precio, int stock, Categoria categoria) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void agregarStock(int cantidad) {
        this.stock += cantidad;
    }

    @Override
    public String toString() {
        return codigo + " | " + nombre + " | $" + String.format("%.2f", precio);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    // CORREGIDO: Retorna double
    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public Categoria getCategoria() {
        return categoria;
    }
}