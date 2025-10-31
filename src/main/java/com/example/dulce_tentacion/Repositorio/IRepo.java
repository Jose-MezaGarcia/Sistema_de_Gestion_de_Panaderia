package com.example.dulce_tentacion.Repositorio;

import java.util.List;

public interface IRepo<T> {
    void guardar(List<T> datos);
    List<T> leer();
}
