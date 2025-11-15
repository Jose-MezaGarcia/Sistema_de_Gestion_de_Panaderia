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
 * Controlador de la vista de Inventario en la aplicación Dulce Tentación.
 * Se encarga de manejar la lógica de la interfaz gráfica relacionada con
 * el inventario de productos, incluyendo navegación entre ventanas y
 * configuración inicial de la vista.
 *
 * @author ¿nose que ponerle aqui?
 * @version ¿nose que ponerle aqui?
 */
public class InventarioController implements IController{

    /** Etiqueta que muestra el usuario actual en la interfaz */
    @FXML
    public Label usuarioActual;

    /** Tabla que muestra los detalles de venta en el inventario */
    @FXML
    private TableView<DetalleVenta> tableView;

    /** Columna para el código del producto */
    @FXML
    private TableColumn<DetalleVenta, String> colCodigo;

    /** Columna para el nombre del producto */
    @FXML
    private TableColumn<DetalleVenta, String> colProducto;

    /** Columna para el precio del producto */
    @FXML
    private TableColumn<DetalleVenta, Double> colPrecio;

    /** Columna para la cantidad disponible */
    @FXML
    private TableColumn<DetalleVenta, Integer> colCantidad;

    /** Columna para el subtotal */
    @FXML
    private TableColumn<DetalleVenta, Double> colSubtotal;

    /** Campo de texto para ingresar el código del producto */
    @FXML
    private TextField txtCodigo;

    /** Campo de texto para ingresar el nombre del producto */
    @FXML
    private TextField txtProducto;

    /** Campo de texto para ingresar el precio del producto */
    @FXML
    private TextField txtPrecio;

    /** Campo de texto para ingresar la cantidad */
    @FXML
    private TextField txtCantidad;

    /** Campo de texto para mostrar el total */
    @FXML
    private TextField txtTotal;

    /** Botón para agregar productos al inventario */
    @FXML
    private Button btnAgregar;

    /** Botón para guardar cambios en el inventario */
    @FXML
    private Button btnGuardar;

    /** Botón para cancelar la operación */
    @FXML
    private Button btnCancelar;

    // ---------------- Métodos de navegación ----------------

    /**
     * Abre la ventana de ventas.
     * @param event Evento de acción al presionar el botón
     */
    @FXML
    private void abrirVentas(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/ventas/ventas.fxml", "Dulce Tentación - Ventas");
    }

    /** Abre la ventana de pedidos */
    @FXML
    private void abrirPedidos(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/pedidos/pedidos.fxml", "Dulce Tentación - Pedidos");
    }

    /** Abre la ventana de productos */
    @FXML
    private void abrirProductos(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/productos/productos.fxml", "Dulce Tentación - Productos");
    }

    /** Abre la ventana de inventario */
    @FXML
    private void abrirInventario(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/inventario/inventario.fxml", "Dulce Tentación - Inventario");
    }

    /** Abre la ventana de clientes */
    @FXML
    private void abrirClientes(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/clientes/clientes.fxml", "Dulce Tentación - Clientes");
    }

    /** Abre la ventana de recibos */
    @FXML
    private void abrirRecibos(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/recibos/recibos.fxml", "Dulce Tentación - Recibos");
    }

    /** Abre la ventana de reportes */
    @FXML
    private void abrirReportes(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/reportes/reportes.fxml", "Dulce Tentación - Reportes");
    }

    // ---------------- Inicialización ----------------

    /**
     * Método de inicialización automática de JavaFX.
     * Se ejecuta al cargar la vista FXML.
     */
    @FXML
    public void initialize() {
        inicializar();
    }

    /**
     * Inicializa los componentes del controlador.
     * Configura eventos y muestra el usuario actual.
     */
    @Override
    public void inicializar() {
        configurarEventos();
        usuarioActual();
    }

    /** Configura los eventos de la interfaz */
    private void configurarEventos() {
        // Aquí puedes añadir  validaciones
    }

    /** Muestra el usuario actual en la etiqueta */
    private void usuarioActual() {
        if (usuarioActual != null) {
            usuarioActual.setText("aedadooad"); // Cambia por el usuario real
        }
    }
    
    /**
     * Cambia la ventana actual a otra vista FXML.
     * @param event Evento de acción al presionar el botón
     * @param rutaFXML Ruta del archivo FXML
     * @param titulo Título de la nueva ventana
     */
    private void cambiarVentana(javafx.event.ActionEvent event, String rutaFXML, String titulo) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource(rutaFXML));
            javafx.scene.Parent root = loader.load();

            // Obtener la ventana actual cuando se preciona el botoncito
            javafx.stage.Stage stage = (javafx.stage.Stage)
                    ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            stage.setScene(new javafx.scene.Scene(root));
            stage.setTitle(titulo);
            stage.show();
            //en caso de fallar se informa en la consola del intellidea
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cambiar de ventana: " + rutaFXML);
        }
    }











}
