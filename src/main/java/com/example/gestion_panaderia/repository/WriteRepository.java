package com.example.gestion_panaderia.repository;

import java.util.List;

/**
 * Interfaz de repositorio de escritura
 * Se definen las operaciones básicas para guardar y eliminar datos
 * @param <T> tipo de objeto que se maneja en el repositorio
 */
public interface WriteRepository<T> {

    /**
     * Se guardan los objetos en la fuente de datos
     */
    void guardar(List<T> lista);

    /**
     * Se elimina un objeto según su id
     */
    void eliminar(String id);
}
