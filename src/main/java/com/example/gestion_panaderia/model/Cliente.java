package com.example.gestion_panaderia.model;

public class Cliente extends Usuario {
    private String telefono;
    private String direccion;
    
    public Cliente() { super(); }
    
    public Cliente(String id, String nombre, String usuario, String password, String telefono, String direccion) {
        super(id, nombre, usuario, password);
        this.telefono = telefono;
        this.direccion = direccion;
    }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}
