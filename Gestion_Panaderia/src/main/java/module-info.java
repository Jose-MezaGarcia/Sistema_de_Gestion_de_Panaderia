module com.example.gestion_panaderia {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;



    opens com.example.gestion_panaderia to javafx.fxml;
    opens com.example.gestion_panaderia.controller to javafx.fxml;
    opens com.example.gestion_panaderia.model to com.google.gson;
    
    exports com.example.gestion_panaderia;
    exports com.example.gestion_panaderia.controller;
    exports com.example.gestion_panaderia.model;
}
