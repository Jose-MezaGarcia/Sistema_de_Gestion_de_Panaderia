package com.example.gestion_panaderia.repository;

import java.util.List;

public interface ReadRepository<T> {
    List<T> cargar();
    T findById(String id);
}
