package com.example.gestion_panaderia.repository;

/**
 * Interfaz de repositorio
 * Se combina la lectura y escritura de datos en una sola interfaz
 * @param <T> tipo de objeto que se maneja en el repositorio
 */
public interface IRepository<T> extends ReadRepository<T>, WriteRepository<T> {
}