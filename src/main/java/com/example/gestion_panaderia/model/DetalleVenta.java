package com.example.gestion_panaderia.model;

public class DetalleVenta {
    private Producto producto;
    private int cantidad;
    private double subtotal;
    
    public DetalleVenta() {}
    
    public DetalleVenta(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = calcularSubtotal();
    }
    
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) {
        this.producto = producto;
        this.subtotal = calcularSubtotal();
    }
    
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = calcularSubtotal();
    }
    
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    
    public double calcularSubtotal() {
        if (producto != null) {
            return producto.getPrecio() * cantidad;
        }
        return 0;
    }
}
