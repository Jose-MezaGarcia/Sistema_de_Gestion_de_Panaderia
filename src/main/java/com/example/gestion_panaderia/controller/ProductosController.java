package com.example.gestion_panaderia.controller;
import com.example.gestion_panaderia.model.*;
import com.example.gestion_panaderia.model.DetalleVenta;
import com.example.gestion_panaderia.service.*;
import com.example.gestion_panaderia.repository.JsonRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador de productos
 * Aquí se maneja la vista de productos y la navegación entre ventanas
 */
public class ProductosController implements IController {

    @FXML public Label usuarioActual;

    @FXML private TableView<DetalleVenta> tableView;
    @FXML private TableColumn<DetalleVenta, String> colCodigo;
    @FXML private TableColumn<DetalleVenta, String> colProducto;
    @FXML private TableColumn<DetalleVenta, Double> colPrecio;
    @FXML private TableColumn<DetalleVenta, Integer> colCantidad;
    @FXML private TableColumn<DetalleVenta, Double> colSubtotal;

    @FXML private TextField txtCodigo;
    @FXML private TextField txtProducto;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtTotal;

    @FXML private Button btnAgregar;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    // ==================== NAVEGACIÓN ENTRE VENTANAS ====================

    /**
     * Se abre la ventana de ventas
     */
    @FXML
    private void abrirVentas(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/ventas/ventas.fxml", "Dulce Tentación - Ventas");
    }

    /**
     * Se abre la ventana de pedidos
     */
    @FXML
    private void abrirPedidos(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/pedidos/pedidos.fxml", "Dulce Tentación - Pedidos");
    }

    /**
     * Se abre la ventana de productos
     */
    @FXML
    private void abrirProductos(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/productos/productos.fxml", "Dulce Tentación - Productos");
    }

    /**
     * Se abre la ventana de inventario
     */
    @FXML
    private void abrirInventario(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/inventario/inventario.fxml", "Dulce Tentación - Inventario");
    }

    /**
     * Se abre la ventana de clientes
     */
    @FXML
    private void abrirClientes(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/clientes/clientes.fxml", "Dulce Tentación - Clientes");
    }

    /**
     * Se abre la ventana de recibos
     */
    @FXML
    private void abrirRecibos(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/recibos/recibos.fxml", "Dulce Tentación - Recibos");
    }

    /**
     * Se abre la ventana de reportes
     */
    @FXML
    private void abrirReportes(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/reportes/reportes.fxml", "Dulce Tentación - Reportes");
    }

    // ==================== INICIALIZACIÓN ====================

    /**
     * Se inicializa el controlador al cargar la vista
     */
    @FXML
    public void initialize() {
        inicializar();
    }

    /**
     * Se inicializan los eventos y se muestra el usuario actual
     */
    @Override
    public void inicializar() {
        configurarEventos();
        usuarioActual();
    }

    /**
     * Se configuran los eventos de la vista
     * (por ahora vacío)
     */
    private void configurarEventos() {
    }

    /**
     * Se muestra el usuario actual en la vista
     */
    private void usuarioActual() {
        if (usuarioActual != null) {
            usuarioActual.setText("aedadooad"); // Cambia por el usuario real
        }
    }

    // ==================== CAMBIO DE VENTANAS ====================

    /**
     * Se cambia la ventana actual a otra definida por ruta y título
     * Se carga el FXML y se actualiza la escena
     */
    private void cambiarVentana(javafx.event.ActionEvent event, String rutaFXML, String titulo) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource(rutaFXML));
            javafx.scene.Parent root = loader.load();

            javafx.stage.Stage stage = (javafx.stage.Stage)
                    ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            stage.setScene(new javafx.scene.Scene(root));
            stage.setTitle(titulo);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cambiar de ventana: " + rutaFXML);
        }
    }
}
