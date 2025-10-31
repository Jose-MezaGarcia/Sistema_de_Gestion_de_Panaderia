package com.example.dulce_tentacion.Repositorio;

import java.util.List;

public interface ILeerRepo<T> {
    List<T> leer(); // Método leer datos desde archivo o fuente
}
