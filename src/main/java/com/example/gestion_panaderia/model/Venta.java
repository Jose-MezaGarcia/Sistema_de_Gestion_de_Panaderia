package com.example.gestion_panaderia.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Clase que representa una venta
 * Aquí se guardan los datos básicos: id, fecha, vendedor, detalles y total
 */
public class Venta {
    private String id;
    private LocalDateTime fecha;
    private Empleado vendedor;
    private List<DetalleVenta> detalles;
    private double total;

    /**
     * Se crea una venta vacía
     * Se inicializa la lista de detalles
     */
    public Venta() {
        this.detalles = new ArrayList<>();
    }

    /**
     * Se crea una venta con datos básicos
     * Se calcula el total automáticamente
     */
    public Venta(String id, LocalDateTime fecha, Empleado vendedor, List<DetalleVenta> detalles) {
        this.id = id;
        this.fecha = fecha;
        this.vendedor = vendedor;
        this.detalles = detalles != null ? detalles : new ArrayList<>();
        this.total = calcularTotal();
    }

    /** Se obtiene el id de la venta */
    public String getId() { return id; }
    /** Se cambia el id de la venta */
    public void setId(String id) { this.id = id; }

    /** Se obtiene la fecha de la venta */
    public LocalDateTime getFecha() { return fecha; }
    /** Se cambia la fecha de la venta */
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    /** Se obtiene el vendedor de la venta */
    public Empleado getVendedor() { return vendedor; }
    /** Se cambia el vendedor de la venta */
    public void setVendedor(Empleado vendedor) { this.vendedor = vendedor; }

    /** Se obtienen los detalles de la venta */
    public List<DetalleVenta> getDetalles() { return detalles; }
    /** Se cambian los detalles de la venta y se recalcula el total */
    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
        this.total = calcularTotal();
    }

    /** Se obtiene el total de la venta */
    public double getTotal() { return total; }
    /** Se cambia el total de la venta manualmente */
    public void setTotal(double total) { this.total = total; }

    /**
     * Se calcula el total sumando los subtotales de cada detalle
     */
    public double calcularTotal() {
        if (detalles == null) return 0;
        return detalles.stream()
                .mapToDouble(DetalleVenta::getSubtotal)
                .sum();
    }

    /**
     * Se aplica el descuento del cliente al subtotal
     * Si el cliente tiene calificación se usa su porcentaje de descuento
     */
    private double aplicarDescuentoCliente(double subtotal, Cliente cliente) {
        if (cliente != null && cliente.getCalificacion() != null) {
            double descuento = cliente.getDescuento();
            return subtotal * (1 - descuento);
        }
        return subtotal;
    }
}
