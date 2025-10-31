package com.example.gestion_panaderia.Repositorio;

import java.util.List;

/**
 * Interfaz para la escritura de datos hacia un origen.
 */
public interface IEscribirRepo<T> {
    void escribir(List<T> datos);
}
