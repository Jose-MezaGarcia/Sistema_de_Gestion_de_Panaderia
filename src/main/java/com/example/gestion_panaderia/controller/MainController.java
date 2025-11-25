package com.example.gestion_panaderia.controller;

import com.example.gestion_panaderia.Main;
import javafx.fxml.FXML;

public class MainController implements IController {
    
    @FXML
    public void initialize() {
        inicializar();
    }
    
    @Override
    public void inicializar() {
    }
    
    public void cambiarVista(String fxml) {
        try {
            String rutaCompleta = "/fxml/" + fxml;
            Main.cambiarVista(rutaCompleta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
