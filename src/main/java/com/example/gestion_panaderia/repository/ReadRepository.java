package com.example.gestion_panaderia.repository;

import java.util.List;

/**
 * Interfaz de repositorio de lectura
 * Se definen las operaciones básicas para obtener datos
 * @param <T> tipo de objeto que se maneja en el repositorio
 */
public interface ReadRepository<T> {

    /**
     * Se cargan todos los objetos
     * Puede venir de un archivo, base de datos, etc
     */
    List<T> cargar();

    /**
     * Se busca un objeto por su id
     * Si no existe se devuelve null
     */
    T findById(String id);
}
