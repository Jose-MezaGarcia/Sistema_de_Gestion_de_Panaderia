package com.example.gestion_panaderia.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venta {
    private String id;
    private LocalDateTime fecha;
    private Empleado vendedor;
    private Cliente cliente;
    private List<DetalleVenta> detalles;
    private double subtotal;
    private double descuento;
    private double total;

    public Venta() {
        this.detalles = new ArrayList<>();
    }

    public Venta(String id, LocalDateTime fecha, Empleado vendedor, Cliente cliente, List<DetalleVenta> detalles) {
        this.id = id;
        this.fecha = fecha;
        this.vendedor = vendedor;
        this.cliente = cliente;
        this.detalles = detalles != null ? detalles : new ArrayList<>();
        calcularTotales();
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Empleado getVendedor() { return vendedor; }
    public void setVendedor(Empleado vendedor) { this.vendedor = vendedor; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        calcularTotales();
    }

    public List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
        calcularTotales();
    }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getDescuento() { return descuento; }
    public void setDescuento(double descuento) { this.descuento = descuento; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    /**
     * Calcula el subtotal de todos los detalles
     */
    public double calcularSubtotal() {
        if (detalles == null || detalles.isEmpty()) return 0;
        return detalles.stream()
                .mapToDouble(DetalleVenta::getSubtotal)
                .sum();
    }

    /**
     * Calcula el monto del descuento según la calificación del cliente
     */
    public double calcularDescuento() {
        if (cliente == null || cliente.getCalificacion() == null) {
            return 0;
        }

        double porcentajeDescuento = cliente.getCalificacion().getDescuento();
        return subtotal * porcentajeDescuento;
    }

    /**
     * Calcula todos los totales: subtotal, descuento y total
     */
    public void calcularTotales() {
        this.subtotal = calcularSubtotal();
        this.descuento = calcularDescuento();
        this.total = subtotal - descuento;
    }

    /**
     * Obtiene el porcentaje de descuento aplicado
     */
    public double getPorcentajeDescuento() {
        if (cliente == null || cliente.getCalificacion() == null) {
            return 0;
        }
        return cliente.getCalificacion().getDescuento() * 100;
    }
}