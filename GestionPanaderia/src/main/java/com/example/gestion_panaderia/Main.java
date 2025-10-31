package com.example.gestion_panaderia;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gestion_panaderia/fxml/login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Sistema de Gestión de Panadería");
        stage.setScene(scene);
        stage.show();
    }
}
