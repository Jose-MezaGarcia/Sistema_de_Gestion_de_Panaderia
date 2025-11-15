package com.example.gestion_panaderia.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una venta dentro del sistema de gestión de panadería.
 * Cada venta tiene un identificador, fecha, vendedor responsable,
 * una lista de detalles de venta y un total calculado.
 *
 * La clase se encarga de agrupar los productos vendidos
 * y calcular el monto total de la transacción.
 *
 * Relaciona las entidades {@link Empleado} y {@link DetalleVenta}.
 *
 * @author ¿?
 * @version ¿?
 */

public class Venta {

    /** Identificador único de la venta */
    private String id;

    /** Fecha y hora en que se realizó la venta */
    private LocalDateTime fecha;

    /** Empleado responsable de la venta */
    private Empleado vendedor;

    /** Lista de detalles de la venta (productos y cantidades) */
    private List<DetalleVenta> detalles;

    /** Total de la venta calculado a partir de los detalles */
    private double total;

    /** Constructor vacío requerido por frameworks y librerías de persistencia */
    public Venta() {
        this.detalles = new ArrayList<>();
    }

    /**
     * Constructor que inicializa una venta con todos sus datos.
     * El total se calcula automáticamente en base a los detalles.
     *
     * @param id Identificador único de la venta
     * @param fecha Fecha y hora de la venta
     * @param vendedor Empleado responsable de la venta
     * @param detalles Lista de detalles de la venta
     */
    public Venta(String id, LocalDateTime fecha, Empleado vendedor, List<DetalleVenta> detalles) {
        this.id = id;
        this.fecha = fecha;
        this.vendedor = vendedor;
        this.detalles = detalles != null ? detalles : new ArrayList<>();
        this.total = calcularTotal();
    }

    /** @return identificador de la venta */
    public String getId() { return id; }

    /** @param id nuevo identificador de la venta */
    public void setId(String id) { this.id = id; }

    /** @return fecha y hora de la venta */
    public LocalDateTime getFecha() { return fecha; }

    /** @param fecha nueva fecha de la venta */
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    /** @return empleado responsable de la venta */
    public Empleado getVendedor() { return vendedor; }

    /** @param vendedor nuevo empleado responsable */
    public void setVendedor(Empleado vendedor) { this.vendedor = vendedor; }

    /** @return lista de detalles de la venta */
    public List<DetalleVenta> getDetalles() { return detalles; }

    /**
     * Establece los detalles de la venta y recalcula el total.
     * @param detalles nueva lista de detalles
     */
    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
        this.total = calcularTotal();
    }

    /** @return total de la venta */
    public double getTotal() { return total; }

    /** @param total nuevo total de la venta */
    public void setTotal(double total) { this.total = total; }

    /**
     * Calcula el total de la venta sumando los subtotales de cada detalle.
     * @return total calculado
     */
    public double calcularTotal() {
        if (detalles == null) return 0;
        return detalles.stream()
                .mapToDouble(DetalleVenta::getSubtotal)
                .sum();
    }
}