package com.example.gestion_panaderia.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venta {
    private String id;
    private LocalDateTime fecha;
    private Empleado vendedor;
    private List<DetalleVenta> detalles;
    private double total;
    
    public Venta() {
        this.detalles = new ArrayList<>();
    }
    
    public Venta(String id, LocalDateTime fecha, Empleado vendedor, List<DetalleVenta> detalles) {
        this.id = id;
        this.fecha = fecha;
        this.vendedor = vendedor;
        this.detalles = detalles != null ? detalles : new ArrayList<>();
        this.total = calcularTotal();
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public Empleado getVendedor() { return vendedor; }
    public void setVendedor(Empleado vendedor) { this.vendedor = vendedor; }
    public List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
        this.total = calcularTotal();
    }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    
    public double calcularTotal() {
        if (detalles == null) return 0;
        return detalles.stream()
            .mapToDouble(DetalleVenta::getSubtotal)
            .sum();
    }
}
