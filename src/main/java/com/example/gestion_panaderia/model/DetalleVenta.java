package com.example.gestion_panaderia.model;

/**
 * Representa un detalle dentro de una venta en la aplicación Dulce Tentación.
 * Cada detalle incluye un producto, la cantidad vendida y el subtotal calculado.
 *
 * Esta clase forma parte del modelo de datos y se utiliza para
 * registrar los productos que forman parte de una venta.
 *
 * El subtotal se calcula automáticamente en base al precio del producto
 * y la cantidad seleccionada.
 *
 * @author ¿?
 * @version ¿?
 */
public class DetalleVenta {

    /** Producto asociado al detalle de la venta */
    private Producto producto;

    /** Cantidad del producto vendida */
    private int cantidad;

    /** Subtotal calculado (precio * cantidad) */
    private double subtotal;

    /** Constructor vacío requerido por frameworks y librerías de persistencia */
    public DetalleVenta() {}

    /**
     * Constructor que inicializa un detalle de venta con producto y cantidad.
     * El subtotal se calcula automáticamente.
     *
     * @param producto Producto vendido
     * @param cantidad Cantidad del producto
     */
    public DetalleVenta(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = calcularSubtotal();
    }

    /**
     * Obtiene el producto del detalle de venta.
     * @return producto asociado
     */
    public Producto getProducto() { return producto; }

    /**
     * Establece el producto del detalle de venta.
     * Recalcula el subtotal automáticamente.
     * @param producto nuevo producto
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
        this.subtotal = calcularSubtotal();
    }

    /**
     * Obtiene la cantidad del producto vendida.
     * @return cantidad del producto
     */
    public int getCantidad() { return cantidad; }

    /**
     * Establece la cantidad del producto vendida.
     * Recalcula el subtotal automáticamente.
     * @param cantidad nueva cantidad
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = calcularSubtotal();
    }

    /**
     * Obtiene el subtotal del detalle de venta.
     * @return subtotal calculado
     */
    public double getSubtotal() { return subtotal; }

    /**
     * Establece manualmente el subtotal del detalle de venta.
     * @param subtotal nuevo subtotal
     */
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    /**
     * Calcula el subtotal en base al precio del producto y la cantidad.
     * @return subtotal calculado, o 0 si el producto es nulo
     */
    public double calcularSubtotal() {
        if (producto != null) {
            return producto.getPrecio() * cantidad;
        }
        return 0;
    }
}
