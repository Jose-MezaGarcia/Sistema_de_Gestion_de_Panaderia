package com.example.gestion_panaderia.controller;

import com.example.gestion_panaderia.Main;
import javafx.fxml.FXML;

/**
 * Controlador principal
 * Aquí se maneja la inicialización y el cambio de vistas
 */
public class MainController implements IController {

    /**
     * Se inicializa el controlador al cargar la vista
     */
    @FXML
    public void initialize() {
        inicializar();
    }

    /**
     * Se inicializan los componentes principales
     * (por ahora no hace nada)
     */
    @Override
    public void inicializar() {
    }

    /**
     * Se cambia la vista actual a otra definida por el archivo FXML
     * Se construye la ruta completa y se llama al método de Main
     */
    public void cambiarVista(String fxml) {
        try {
            String rutaCompleta = "/fxml/" + fxml;
            Main.cambiarVista(rutaCompleta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
