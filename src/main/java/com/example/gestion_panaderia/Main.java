package com.example.gestion_panaderia;

import com.example.gestion_panaderia.controller.ProductosController;
import com.example.gestion_panaderia.model.Producto;
import com.example.gestion_panaderia.repository.IRepository;
import com.example.gestion_panaderia.repository.JsonRepository;
import com.example.gestion_panaderia.service.IProductoService;
import com.example.gestion_panaderia.service.ProductoServiceImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("Dulce Tentación - Sistema de Gestión");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login/login.fxml"));
        Scene scene = new Scene(loader.load());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    // Método SIMPLE para cambiar vistas - crea el servicio en el momento
    public static void cambiarVista(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath));
            Scene scene = new Scene(loader.load());

            // Inyectar servicios directamente
            Object controller = loader.getController();
            if (controller instanceof ProductosController) {
                inyectarServicioProductos((ProductosController) controller);
            }

            primaryStage.setScene(scene);

        } catch (Exception e) {
            System.err.println("❌ Error cambiando vista: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método específico para productos
    private static void inyectarServicioProductos(ProductosController controller) {
        try {
            System.out.println("⚙️ Configurando servicio de productos...");

            IRepository<Producto> productoRepository = new JsonRepository<>(
                    "productos.json",
                    Producto.class
            );
            IProductoService productoService = new ProductoServiceImpl(productoRepository);

            // Verificar carga de datos
            int cantidadProductos = productoService.listarProductos().size();
            System.out.println("✅ Productos cargados desde JSON: " + cantidadProductos);

            // Inyectar servicio
            controller.setProductoService(productoService);
            System.out.println("✅ Servicio inyectado en ProductosController");

        } catch (Exception e) {
            System.err.println("❌ Error configurando servicio: " + e.getMessage());
            e.printStackTrace();
        }
    }
}