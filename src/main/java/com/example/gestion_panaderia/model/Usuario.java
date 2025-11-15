package com.example.gestion_panaderia.model;

/**
 * Representa un usuario dentro del sistema de gestión de panadería.
 * Contiene información básica de identificación y autenticación,
 * incluyendo id, nombre, usuario y contraseña.
 *
 * Esta clase sirve como base para otras entidades como {@link Cliente} y {@link Empleado},
 * que extienden sus atributos y funcionalidades.
 *
 * @author ¿?
 * @version ¿?
 */
public class Usuario {
    private String id;
    private String nombre;
    private String usuario;
    private String password;
    
    public Usuario() {}
    
    public Usuario(String id, String nombre, String usuario, String password) {
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
        this.password = password;
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
