package com.example.gestion_panaderia.model;

/**
 * Clase que representa a un usuario
 * Aquí se guardan sus datos básicos: id, nombre, usuario y contraseña
 */
public class Usuario {
    private String id;
    private String nombre;
    private String usuario;
    private String password;

    /**
     * Se crea un usuario vacío
     */
    public Usuario() {}

    /**
     * Se crea un usuario con datos básicos
     */
    public Usuario(String id, String nombre, String usuario, String password) {
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
        this.password = password;
    }

    /**
     * Se obtiene el id del usuario
     */
    public String getId() { return id; }

    /**
     * Se cambia el id del usuario
     */
    public void setId(String id) { this.id = id; }

    /**
     * Se obtiene el nombre del usuario
     */
    public String getNombre() { return nombre; }

    /**
     * Se cambia el nombre del usuario
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * Se obtiene el nombre de usuario (login)
     */
    public String getUsuario() { return usuario; }

    /**
     * Se cambia el nombre de usuario (login)
     */
    public void setUsuario(String usuario) { this.usuario = usuario; }

    /**
     * Se obtiene la contraseña del usuario
     */
    public String getPassword() { return password; }

    /**
     * Se cambia la contraseña del usuario
     */
    public void setPassword(String password) { this.password = password; }
}
