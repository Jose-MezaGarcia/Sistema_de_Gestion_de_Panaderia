package com.example.gestion_panaderia.controller;

import com.example.gestion_panaderia.Main;
import javafx.fxml.FXML;

/**
 * Controlador principal de la aplicación Dulce Tentación.
 * Se encarga de manejar la inicialización básica y el cambio de vistas
 * dentro de la aplicación, centralizando la navegación entre pantallas.
 *
 * Implementa {@link IController} para estandarizar el ciclo de vida
 * de los controladores.
 *
 * @author ¿?
 * @version ¿?
 */
public class MainController implements IController {

    /**
     * Método de inicialización automática de JavaFX.
     * Se ejecuta al cargar la vista FXML.
     */
    @FXML
    public void initialize() {
        inicializar();
    }

    /**
     * Inicializa el controlador.
     * Actualmente no contiene lógica, pero puede ampliarse en el futuro.
     */
    @Override
    public void inicializar() {
    }

    /**
     * Cambia la vista actual a otra definida por un archivo FXML.
     * @param fxml Nombre del archivo FXML dentro de la carpeta /fxml/
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
