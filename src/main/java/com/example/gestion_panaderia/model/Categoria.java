package com.example.gestion_panaderia.model;

public class Categoria {
    private String nombre;
    
    public Categoria() {}
    public Categoria(String nombre) { this.nombre = nombre; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    @Override
    public String toString() { return nombre; }
}
