package com.example.gestion_panaderia.model;

/**
 * Clase que representa el detalle de una venta
 * Aquí se guarda el producto, la cantidad y el subtotal
 */
public class DetalleVenta {
    private Producto producto;
    private int cantidad;
    private double subtotal;

    /**
     * Se crea un detalle vacío
     */
    public DetalleVenta() {}

    /**
     * Se crea un detalle con producto y cantidad
     * Se calcula el subtotal automáticamente
     */
    public DetalleVenta(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = calcularSubtotal();
    }

    /**
     * Se obtiene el producto del detalle
     */
    public Producto getProducto() { return producto; }

    /**
     * Se cambia el producto del detalle
     * Se recalcula el subtotal
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
        this.subtotal = calcularSubtotal();
    }

    /**
     * Se obtiene la cantidad del producto
     */
    public int getCantidad() { return cantidad; }

    /**
     * Se cambia la cantidad del producto
     * Se recalcula el subtotal
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = calcularSubtotal();
    }

    /**
     * Se obtiene el subtotal del detalle
     */
    public double getSubtotal() { return subtotal; }

    /**
     * Se cambia el subtotal manualmente
     */
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    /**
     * Se calcula el subtotal multiplicando precio por cantidad
     * Si no hay producto se devuelve 0
     */
    public double calcularSubtotal() {
        if (producto != null) {
            return producto.getPrecio() * cantidad;
        }
        return 0;
    }
}
