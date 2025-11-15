package com.example.gestion_panaderia.model;

/**
 * Representa una categoría de productos dentro del sistema de gestión de panadería.
 * Cada categoría agrupa productos bajo un mismo nombre (ejemplo: "Pan dulce", "Bebidas").
 *
 * Esta clase forma parte del modelo de datos y se utiliza para organizar
 * y clasificar los productos en el inventario.
 *
 * @author ¿?
 * @version ¿?
 */
public class Categoria {

    /** Nombre de la categoría (ejemplo: "Pan", "Pasteles") */
    private String nombre;

    /** Constructor vacío requerido por frameworks y librerías de persistencia */
    public Categoria() {}

    /**
     * Constructor que inicializa la categoría con un nombre.
     * @param nombre Nombre de la categoría
     */
    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el nombre de la categoría.
     * @return nombre de la categoría
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la categoría.
     * @param nombre nuevo nombre de la categoría
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve el nombre de la categoría como representación en texto.
     * @return nombre de la categoría
     */
    @Override
    public String toString() {
        return nombre;
    }
}
