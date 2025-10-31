package com.example.gestion_panaderia.modelo;

public class Usuario {
    private String id;
    private String nombre;
    private String usuario ;
    private String contraseña;

    public Usuario(String id, String nombre, String usuario, String Contraseña) {
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
        this.contraseña = contraseña;
    }
    public String getId() {
        return id;
    }
    public void getNombre() {
        return nombre;
    }
    public void getUsuario(){
        return usuario;
    }
    public void setContraseña() {
        return contraseña;
    }
}
