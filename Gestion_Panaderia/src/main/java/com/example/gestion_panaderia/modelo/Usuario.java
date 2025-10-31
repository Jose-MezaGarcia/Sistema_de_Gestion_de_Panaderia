package com.example.gestion_panaderia.modelo;

public class Usuario {
    private String id;
    private String nombre;
    private String apellido;
    private String contraseña;

    public Usuario(String id, String nombre, String apellido, String Contraseña) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.contraseña = contraseña;
    }

    public String getId() {
        return id;
    }

    public void getNombre() {
        return nombre;
    }

    public void getApellido() {
        return apellido;
    }

    public void setContraseña() {
        return contraseña;
    }
}
