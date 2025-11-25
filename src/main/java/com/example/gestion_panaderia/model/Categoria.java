package com.example.gestion_panaderia.model;

/**
 * Clase que representa una categoría
 * Aquí se guarda el nombre de la categoría de un producto o materia prima
 */
public class Categoria {
    private String nombre;

    /**
     * Se crea una categoría vacía
     */
    public Categoria() {}

    /**
     * Se crea una categoría con nombre
     */
    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Se obtiene el nombre de la categoría
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Se cambia el nombre de la categoría
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Se devuelve el nombre como texto
     */
    @Override
    public String toString() {
        return nombre;
    }
}
