package com.example.dulce_tentacion.Repositorio;

import java.util.List;

public interface IRepo<T> {
    List<T> leer();                // Leer datos (desde JSON)
    void escribir(List<T> datos);  // Guardar datos
}
