package com.example.gestion_panaderia.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa un pedido
 * Aquí se guardan los datos básicos del pedido, cliente, fechas y estado
 */
public class Pedido {
    private String id;
    private String titulo;
    private String cliente;
    private String fechaEntrega; // Se guarda como String para JSON
    private String horaEntrega;  // Se guarda como String para JSON
    private String descripcion;
    private String estado;
    private String telefono;
    private String fechaCreacion; // Se guarda como String para JSON

    /**
     * Se crea un pedido vacío
     * Se asigna la fecha de creación automáticamente
     */
    public Pedido() {
        this.fechaCreacion = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    /**
     * Se crea un pedido con datos básicos
     * La fecha de creación se asigna automáticamente
     */
    public Pedido(String id, String titulo, String cliente, String fechaEntrega,
                  String horaEntrega, String descripcion, String estado, String telefono) {
        this();
        this.id = id;
        this.titulo = titulo;
        this.cliente = cliente;
        this.fechaEntrega = fechaEntrega;
        this.horaEntrega = horaEntrega;
        this.descripcion = descripcion;
        this.estado = estado;
        this.telefono = telefono;
    }

    // ==================== GETTERS Y SETTERS ====================

    /** Se obtiene el id del pedido */
    public String getId() { return id; }
    /** Se cambia el id del pedido */
    public void setId(String id) { this.id = id; }

    /** Se obtiene el título del pedido */
    public String getTitulo() { return titulo; }
    /** Se cambia el título del pedido */
    public void setTitulo(String titulo) { this.titulo = titulo; }

    /** Se obtiene el cliente del pedido */
    public String getCliente() { return cliente; }
    /** Se cambia el cliente del pedido */
    public void setCliente(String cliente) { this.cliente = cliente; }

    /** Se obtiene la descripción del pedido */
    public String getDescripcion() { return descripcion; }
    /** Se cambia la descripción del pedido */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    /** Se obtiene el estado del pedido */
    public String getEstado() { return estado; }
    /** Se cambia el estado del pedido */
    public void setEstado(String estado) { this.estado = estado; }

    /** Se obtiene el teléfono del cliente */
    public String getTelefono() { return telefono; }
    /** Se cambia el teléfono del cliente */
    public void setTelefono(String telefono) { this.telefono = telefono; }

    /** Se obtiene la fecha de creación como String */
    public String getFechaCreacion() { return fechaCreacion; }
    /** Se cambia la fecha de creación como String */
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    // ==================== MÉTODOS PARA FECHAS/HORAS ====================

    /** Se obtiene la fecha de entrega como String (para JSON) */
    public String getFechaEntrega() { return fechaEntrega; }
    /** Se cambia la fecha de entrega como String */
    public void setFechaEntrega(String fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    /** Se obtiene la fecha de entrega como LocalDate (para UI) */
    public LocalDate getFechaEntregaAsLocalDate() {
        if (fechaEntrega != null && !fechaEntrega.isEmpty()) {
            try {
                return LocalDate.parse(fechaEntrega);
            } catch (Exception e) {
                System.err.println("Error parsing fechaEntrega: " + fechaEntrega);
                return null;
            }
        }
        return null;
    }

    /** Se cambia la fecha de entrega desde un LocalDate */
    public void setFechaEntregaFromLocalDate(LocalDate fecha) {
        this.fechaEntrega = fecha != null ? fecha.toString() : null;
    }

    /** Se obtiene la hora de entrega como String (para JSON) */
    public String getHoraEntrega() { return horaEntrega; }
    /** Se cambia la hora de entrega como String */
    public void setHoraEntrega(String horaEntrega) { this.horaEntrega = horaEntrega; }

    /** Se obtiene la hora de entrega como LocalTime (para UI) */
    public LocalTime getHoraEntregaAsLocalTime() {
        if (horaEntrega != null && !horaEntrega.isEmpty()) {
            try {
                return LocalTime.parse(horaEntrega);
            } catch (Exception e) {
                System.err.println("Error parsing horaEntrega: " + horaEntrega);
                return null;
            }
        }
        return null;
    }

    /** Se cambia la hora de entrega desde un LocalTime */
    public void setHoraEntregaFromLocalTime(LocalTime hora) {
        this.horaEntrega = hora != null ? hora.toString() : null;
    }

    /** Se obtiene la fecha de creación como LocalDateTime (para UI) */
    public LocalDateTime getFechaCreacionAsLocalDateTime() {
        if (fechaCreacion != null && !fechaCreacion.isEmpty()) {
            try {
                return LocalDateTime.parse(fechaCreacion);
            } catch (Exception e) {
                System.err.println("Error parsing fechaCreacion: " + fechaCreacion);
                return LocalDateTime.now();
            }
        }
        return LocalDateTime.now();
    }
}
