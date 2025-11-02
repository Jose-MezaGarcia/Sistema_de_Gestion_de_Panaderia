package com.example.gestion_panaderia.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonRepository<T> implements IRepository<T> {
    
    private String rutaArchivo;
    private Class<T> tipo;
    private Gson gson;
    
    public JsonRepository(String rutaArchivo, Class<T> tipo) {
        this.rutaArchivo = rutaArchivo;
        this.tipo = tipo;
        this.gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .setPrettyPrinting()
            .create();
        
        File file = new File(rutaArchivo);
        if (!file.exists()) {
            try {
                file.createNewFile();
                guardar(new ArrayList<>());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public List<T> cargar() {
        try (Reader reader = new FileReader(rutaArchivo)) {
            Type listType = TypeToken.getParameterized(List.class, tipo).getType();
            List<T> lista = gson.fromJson(reader, listType);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    @Override
    public T findById(String id) {
        List<T> lista = cargar();
        for (T item : lista) {
            try {
                java.lang.reflect.Method metodoGetId = item.getClass().getMethod("getId");
                String itemId = (String) metodoGetId.invoke(item);
                if (itemId.equals(id)) {
                    return item;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    @Override
    public void guardar(List<T> lista) {
        try (Writer writer = new FileWriter(rutaArchivo)) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void eliminar(String id) {
        List<T> lista = cargar();
        lista.removeIf(item -> {
            try {
                java.lang.reflect.Method metodoGetId = item.getClass().getMethod("getId");
                String itemId = (String) metodoGetId.invoke(item);
                return itemId.equals(id);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
        guardar(lista);
    }
}
