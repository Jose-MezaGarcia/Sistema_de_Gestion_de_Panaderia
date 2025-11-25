package com.example.gestion_panaderia.model;

/**
 * Clase que representa un producto
 * Aquí se guardan sus datos básicos: id, nombre, precio, stock y categoría
 */
public class Producto {
    private String id;
    private String nombre;
    private double precio;
    private int stock;
    private Categoria categoria;

    /**
     * Se crea un producto vacío
     */
    public Producto() {}

    /**
     * Se crea un producto con datos básicos
     */
    public Producto(String id, String nombre, double precio, int stock, Categoria categoria) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }

    /**
     * Se obtiene el id del producto
     */
    public String getId() { return id; }

    /**
     * Se cambia el id del producto
     */
    public void setId(String id) { this.id = id; }

    /**
     * Se obtiene el nombre del producto
     */
    public String getNombre() { return nombre; }

    /**
     * Se cambia el nombre del producto
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * Se obtiene el precio del producto
     */
    public double getPrecio() { return precio; }

    /**
     * Se cambia el precio del producto
     */
    public void setPrecio(double precio) { this.precio = precio; }

    /**
     * Se obtiene el stock del producto
     */
    public int getStock() { return stock; }

    /**
     * Se cambia el stock del producto
     */
    public void setStock(int stock) { this.stock = stock; }

    /**
     * Se obtiene la categoría del producto
     */
    public Categoria getCategoria() { return categoria; }

    /**
     * Se cambia la categoría del producto
     */
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    /**
     * Se actualiza el stock sumando la cantidad indicada
     */
    public void actualizarStock(int cantidad) {
        this.stock += cantidad;
    }

    /**
     * Se devuelve el producto como texto con nombre, precio y stock
     */
    @Override
    public String toString() {
        return nombre + " - $" + precio + " (Stock: " + stock + ")";
    }
}
