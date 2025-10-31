package com.example.gestion_panaderia.Repositorio;

import java.util.List;

/**
 * Interfaz para la lectura de datos desde un origen.
 */
public interface ILeerRepo<T> {
    List<T> leer();
}
