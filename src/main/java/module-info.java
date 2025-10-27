module com.example.dulce_tentacion {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.dulce_tentacion to javafx.fxml;
    exports com.example.dulce_tentacion;
}