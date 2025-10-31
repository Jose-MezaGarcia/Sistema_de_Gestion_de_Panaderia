package com.example.dulce_tentacion.Repositorio;

import java.util.List;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

// Esta clase depende de las entidades del Modelo (Producto, Venta, etc.)
public class JsonRepo<T> implements IRepo<T>, ILeerRepo<T>, IEscribirRepo<T> {
    private final String filePath;
    private final Gson gson = new Gson();

    public JsonRepo(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<T> leer() {
        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, new TypeToken<List<T>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void escribir(List<T> datos) {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(datos, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}