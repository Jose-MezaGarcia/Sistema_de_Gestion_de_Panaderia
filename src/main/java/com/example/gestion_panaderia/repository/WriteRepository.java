package com.example.gestion_panaderia.repository;

import java.util.List;

public interface WriteRepository<T> {
    void guardar(List<T> lista);
    void eliminar(String id);
}
