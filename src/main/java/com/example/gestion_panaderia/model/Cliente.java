package com.example.gestion_panaderia.model;

public class Cliente extends Usuario {
    private String telefono;
    private String direccion;
    private String preferencias;
    private CalificacionCliente calificacion;

    public enum CalificacionCliente {
        TRISTE("😠", 0.0),
        NEUTRAL("😐", 0.05),
        FELIZ("😊", 0.10);

        private final String emoji;
        private final double descuento;

        CalificacionCliente(String emoji, double descuento) {
            this.emoji = emoji;
            this.descuento = descuento;
        }

        public String getEmoji() { return emoji; }
        public double getDescuento() { return descuento; }
    }

    // Constructores
    public Cliente() {
        super();
        this.calificacion = CalificacionCliente.NEUTRAL;
    }

    public Cliente(String id, String nombre, String usuario, String password,
                   String telefono, String direccion) {
        super(id, nombre, usuario, password);
        this.telefono = telefono;
        this.direccion = direccion;
        this.calificacion = CalificacionCliente.NEUTRAL;
    }

    // Getters y Setters
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getPreferencias() { return preferencias; }
    public void setPreferencias(String preferencias) { this.preferencias = preferencias; }

    public CalificacionCliente getCalificacion() { return calificacion; }
    public void setCalificacion(CalificacionCliente calificacion) { this.calificacion = calificacion; }

    public double getDescuento() {
        return calificacion != null ? calificacion.getDescuento() : 0.0;
    }
}
