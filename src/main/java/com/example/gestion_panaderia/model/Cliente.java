package com.example.gestion_panaderia.model;

/**
 * Clase que representa a un cliente
 * Aquí se guardan sus datos básicos, preferencias y calificación
 */
public class Cliente extends Usuario {
    private String telefono;
    private String direccion;
    private String preferencias;
    private CalificacionCliente calificacion;

    /**
     * Enum de calificación del cliente
     * Se usa para medir satisfacción y aplicar descuentos
     */
    public enum CalificacionCliente {
        TRISTE("😠", 0.0),
        NEUTRAL("😐", 0.05),
        FELIZ("😊", 0.10);

        private final String emoji;
        private final double descuento;

        /**
         * Se crea una calificación con emoji y descuento
         */
        CalificacionCliente(String emoji, double descuento) {
            this.emoji = emoji;
            this.descuento = descuento;
        }

        /**
         * Se obtiene el emoji de la calificación
         */
        public String getEmoji() { return emoji; }

        /**
         * Se obtiene el descuento asociado a la calificación
         */
        public double getDescuento() { return descuento; }
    }

    // ==================== CONSTRUCTORES ====================

    /**
     * Se crea un cliente vacío con calificación NEUTRAL por defecto
     */
    public Cliente() {
        super();
        this.calificacion = CalificacionCliente.NEUTRAL;
    }

    /**
     * Se crea un cliente con datos básicos
     * Se asigna calificación NEUTRAL por defecto
     */
    public Cliente(String id, String nombre, String usuario, String password,
                   String telefono, String direccion) {
        super(id, nombre, usuario, password);
        this.telefono = telefono;
        this.direccion = direccion;
        this.calificacion = CalificacionCliente.NEUTRAL;
    }

    // ==================== GETTERS Y SETTERS ====================

    /**
     * Se obtiene el teléfono del cliente
     */
    public String getTelefono() { return telefono; }

    /**
     * Se cambia el teléfono del cliente
     */
    public void setTelefono(String telefono) { this.telefono = telefono; }

    /**
     * Se obtiene la dirección del cliente
     */
    public String getDireccion() { return direccion; }

    /**
     * Se cambia la dirección del cliente
     */
    public void setDireccion(String direccion) { this.direccion = direccion; }

    /**
     * Se obtienen las preferencias del cliente
     */
    public String getPreferencias() { return preferencias; }

    /**
     * Se cambian las preferencias del cliente
     */
    public void setPreferencias(String preferencias) { this.preferencias = preferencias; }

    /**
     * Se obtiene la calificación del cliente
     */
    public CalificacionCliente getCalificacion() { return calificacion; }

    /**
     * Se cambia la calificación del cliente
     */
    public void setCalificacion(CalificacionCliente calificacion) { this.calificacion = calificacion; }

    /**
     * Se obtiene el descuento según la calificación
     */
    public double getDescuento() {
        return calificacion != null ? calificacion.getDescuento() : 0.0;
    }
}
