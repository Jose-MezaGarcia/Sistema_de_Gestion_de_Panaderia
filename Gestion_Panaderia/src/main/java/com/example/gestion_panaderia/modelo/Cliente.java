package com.example.gestion_panaderia.modelo;

public class Cliente {
    private Strind id;
    private String nombre;
    private String telefono;
    private String correo;

    public Cliente(String id, String nombre, String telefono, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
    }
    public String getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public String getTelefono() {
        return telefono;
    }
    public String getCorreo() {
        this.correo = correo;
    }
}
