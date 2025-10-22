module com.example.gestion_panaderia {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gestion_panaderia to javafx.fxml;
    exports com.example.gestion_panaderia;
}