package com.example.gestion_panaderia.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Pedido {
    private String id;
    private String titulo;
    private String cliente;
    private String fechaEntrega; // Guardar como String para JSON
    private String horaEntrega;  // Guardar como String para JSON
    private String descripcion;
    private String estado;
    private String telefono;
    private String fechaCreacion; // Guardar como String para JSON

    public Pedido() {
        this.fechaCreacion = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

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

    // Getters y Setters básicos
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    // ==================== MÉTODOS PARA FECHAS/HORAS ====================

    // Fecha entrega como String (para JSON)
    public String getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(String fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    // Fecha entrega como LocalDate (para UI)
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

    public void setFechaEntregaFromLocalDate(LocalDate fecha) {
        this.fechaEntrega = fecha != null ? fecha.toString() : null;
    }

    // Hora entrega como String (para JSON)
    public String getHoraEntrega() { return horaEntrega; }
    public void setHoraEntrega(String horaEntrega) { this.horaEntrega = horaEntrega; }

    // Hora entrega como LocalTime (para UI)
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

    public void setHoraEntregaFromLocalTime(LocalTime hora) {
        this.horaEntrega = hora != null ? hora.toString() : null;
    }

    // Fecha creación como LocalDateTime (para UI)
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