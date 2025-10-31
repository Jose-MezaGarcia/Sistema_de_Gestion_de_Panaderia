package com.example.dulce_tentacion.Repositorio;

import java.util.List;

public interface IEscribirRepo<T> {
    void escribir(List<T> datos); // Escribe datos en archivo o fuente
}