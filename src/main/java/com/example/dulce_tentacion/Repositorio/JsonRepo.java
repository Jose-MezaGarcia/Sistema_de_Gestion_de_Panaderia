package com.example.gestion_panaderia.Repositorio;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación genérica para leer y escribir datos en formato JSON.
 */
public class JsonRepo<T> implements ILeerRepo<T>, IEscribirRepo<T> {

    private final String rutaArchivo;
    private final Type tipoLista;
    private final Gson gson;

    public JsonRepo(String rutaArchivo, Type tipoLista) {
        this.rutaArchivo = rutaArchivo;
        this.tipoLista = tipoLista;
        this.gson = new Gson();
    }

    @Override
    public List<T> leer() {
        try (FileReader lector = new FileReader(rutaArchivo)) {
            List<T> datos = gson.fromJson(lector, tipoLista);
            if (datos == null) {
                return new ArrayList<>();
            }
            return datos;
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void escribir(List<T> datos) {
        try (FileWriter escritor = new FileWriter(rutaArchivo)) {
            gson.toJson(datos, escritor);
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo: " + e.getMessage());
        }
    }
}
