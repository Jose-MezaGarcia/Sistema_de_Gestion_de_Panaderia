package com.example.gestion_panaderia.modelo;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

public class Venta {
    private String id;
    private LocalDateTime fecha;
    private Empleado vendedor;
    private List<DetalleVenta> detalles;
    private double total;

    public Venta(String id, Empleado vendedor){
        this.id = id;
        this.fecha = LocalDateTime.now();
        this.vendedor = vendedor;
        this.detalles = new ArrayList<>();
        this.total = 0;
    }

    public double calcularTotal(){
        this.total= 0.0;
        for(DetalleVenta detalle : detalles){
            this.total += detalle.calcularSubtotal();
        }
        return this.total;
    }

    public String agregarDetalle(DetalleVenta detalle){
        this.detalles.add(detalle);
        calcularTotal();
    }

    public String getId(){
        return this.id;
    }
    public LocalDateTime getFecha(){
        return this.fecha;
    }

    public Empleado getVendedor() {
        return vendedor;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }
    public double getTotal() {
        return total;
    }
}
