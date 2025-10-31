package com.example.gestion_panaderia.modelo;

public class DetalleVenta{
    private Producto producto;
    private int cantidad;
    private double precio;
    private double subtotal;

    public DetalleVenta(Producto producto, int cantidad){
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.subtotal = calcularSubtotal();
    }

    private double calcularSubtotal(){
        this.subtotal = this.producto.get.precio() * this.cantidad
                return this.subtotal;
    }

    public double getPrecio() {
        return precio;
    }
    public Producto getProducto() {
        return producto;
    }
    public int getCantidad() {
        return cantidad;
    }
    public double getSubtotal() {
        return subtotal;
    }
}
