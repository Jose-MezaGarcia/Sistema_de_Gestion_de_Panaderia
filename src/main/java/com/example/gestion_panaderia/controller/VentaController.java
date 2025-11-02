package com.example.gestion_panaderia.controller;

import com.example.gestion_panaderia.model.*;
import com.example.gestion_panaderia.service.*;
import com.example.gestion_panaderia.repository.JsonRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class VentaController implements IController {
    @FXML
    public Label usuarioActual;

    @FXML
    private TableView<DetalleVenta> tableView;
    
    @FXML
    private TableColumn<DetalleVenta, String> colCodigo;
    
    @FXML
    private TableColumn<DetalleVenta, String> colProducto;
    
    @FXML
    private TableColumn<DetalleVenta, Double> colPrecio;
    
    @FXML
    private TableColumn<DetalleVenta, Integer> colCantidad;
    
    @FXML
    private TableColumn<DetalleVenta, Double> colSubtotal;
    
    @FXML
    private TextField txtCodigo;
    
    @FXML
    private TextField txtProducto;
    
    @FXML
    private TextField txtPrecio;
    
    @FXML
    private TextField txtCantidad;
    
    @FXML
    private TextField txtTotal;
    
    @FXML
    private Button btnAgregar;
    
    @FXML
    private Button btnGuardar;
    
    @FXML
    private Button btnCancelar;

    //logicas para intercambios entre ventanas


    @FXML
    private void abrirVentas(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/ventas/ventas.fxml", "Dulce Tentación - Ventas");
    }

    @FXML
    private void abrirPedidos(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/pedidos/pedidos.fxml", "Dulce Tentación - Pedidos");
    }

    @FXML
    private void abrirProductos(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/productos/productos.fxml", "Dulce Tentación - Productos");
    }

    @FXML
    private void abrirInventario(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/inventario/inventario.fxml", "Dulce Tentación - Inventario");
    }

    @FXML
    private void abrirClientes(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/clientes/clientes.fxml", "Dulce Tentación - Clientes");
    }

    @FXML
    private void abrirRecibos(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/recibos/recibos.fxml", "Dulce Tentación - Recibos");
    }

    @FXML
    private void abrirReportes(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/reportes/reportes.fxml", "Dulce Tentación - Reportes");
    }


    private IProductoService productoService;
    private IVentaService ventaService;
    private ObservableList<DetalleVenta> detallesActuales;
    
    @FXML
    public void initialize() {
        inicializar();
    }
    
    @Override
    public void inicializar() {
        JsonRepository<Producto> productoRepo = new JsonRepository<>("productos.json", Producto.class);
        JsonRepository<Venta> ventaRepo = new JsonRepository<>("ventas.json", Venta.class);

        productoService = new ProductoServiceImpl(productoRepo);
        ventaService = new VentaServiceImpl(ventaRepo);

        detallesActuales = FXCollections.observableArrayList();

        configurarTabla();
        configurarEventos();
        usuarioActual();

        txtTotal.setEditable(false);
        txtTotal.setText("$0.00");

    }
    //logica para el intercambio entre ventanas
    private void cambiarVentana(javafx.event.ActionEvent event, String rutaFXML, String titulo) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource(rutaFXML));
            javafx.scene.Parent root = loader.load();

            // Obtener la ventana actual desde el botón que disparó el evento
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

    // mostrar usuario actual en la ficha de la izquierda
    public void usuarioActual(){
        usuarioActual.setText("aedadooad");
    }
    
    private void configurarTabla() {
        if (colCodigo != null) {
            colCodigo.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getProducto().getId()));
        }
        
        if (colProducto != null) {
            colProducto.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getProducto().getNombre()));
        }
        
        if (colPrecio != null) {
            colPrecio.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleDoubleProperty(
                    cellData.getValue().getProducto().getPrecio()).asObject());
        }
        
        if (colCantidad != null) {
            colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        }
        
        if (colSubtotal != null) {
            colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        }
        
        if (tableView != null) {
            tableView.setItems(detallesActuales);
        }
    }
    
    private void configurarEventos() {
        if (btnAgregar != null) {
            btnAgregar.setOnAction(event -> handleAgregarDetalle());
        }
        
        if (btnGuardar != null) {
            btnGuardar.setOnAction(event -> handleGuardarVenta());
        }
        
        if (btnCancelar != null) {
            btnCancelar.setOnAction(event -> handleCancelar());
        }
        
        // Buscar producto por código
        if (txtCodigo != null) {
            txtCodigo.setOnAction(event -> buscarProductoPorCodigo());
        }
    }
    
    private void buscarProductoPorCodigo() {
        String codigo = txtCodigo.getText().trim();
        if (!codigo.isEmpty()) {
            Producto producto = productoService.buscarPorId(codigo);
            if (producto != null) {
                txtProducto.setText(producto.getNombre());
                txtPrecio.setText(String.valueOf(producto.getPrecio()));
                txtCantidad.requestFocus();
            } else {
                mostrarAlerta("Producto no encontrado", 
                    "No existe un producto con el código: " + codigo, 
                    Alert.AlertType.WARNING);
                limpiarCampos();
            }
        }
    }
    
    private void handleAgregarDetalle() {
        try {
            String codigo = txtCodigo.getText().trim();
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            
            if (codigo.isEmpty()) {
                mostrarAlerta("Error", "Ingrese un código de producto", Alert.AlertType.ERROR);
                return;
            }
            
            if (cantidad <= 0) {
                mostrarAlerta("Error", "La cantidad debe ser mayor a 0", Alert.AlertType.ERROR);
                return;
            }
            
            Producto producto = productoService.buscarPorId(codigo);
            
            if (producto == null) {
                mostrarAlerta("Error", "Producto no encontrado", Alert.AlertType.ERROR);
                return;
            }
            
            if (cantidad > producto.getStock()) {
                mostrarAlerta("Error", 
                    "Stock insuficiente. Disponible: " + producto.getStock(), 
                    Alert.AlertType.ERROR);
                return;
            }
            
            DetalleVenta detalle = new DetalleVenta(producto, cantidad);
            detallesActuales.add(detalle);
            
            actualizarTotal();
            limpiarCampos();
            
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Cantidad inválida", Alert.AlertType.ERROR);
        }
    }
    
    private void handleGuardarVenta() {
        if (detallesActuales.isEmpty()) {
            mostrarAlerta("Error", 
                "Agregue al menos un producto a la venta", 
                Alert.AlertType.ERROR);
            return;
        }
        
        try {
            Empleado vendedor = new Empleado("1", "Vendedor", "vendedor", "123", "Vendedor");
            
            Venta venta = ventaService.registrarVenta(
                new ArrayList<>(detallesActuales), vendedor);
            
            for (DetalleVenta detalle : detallesActuales) {
                Producto p = detalle.getProducto();
                p.actualizarStock(-detalle.getCantidad());
                productoService.actualizarProducto(p);
            }
            
            mostrarAlerta("Éxito", 
                "Venta registrada correctamente.\nID: " + venta.getId() + 
                "\nTotal: $" + String.format("%.2f", venta.getTotal()), 
                Alert.AlertType.INFORMATION);
            
            detallesActuales.clear();
            actualizarTotal();
            
        } catch (Exception e) {
            mostrarAlerta("Error", 
                "No se pudo registrar la venta: " + e.getMessage(), 
                Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    private void handleCancelar() {
        if (!detallesActuales.isEmpty()) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar cancelación");
            confirmacion.setHeaderText("¿Desea cancelar la venta?");
            confirmacion.setContentText("Se perderán todos los productos agregados.");
            
            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                detallesActuales.clear();
                actualizarTotal();
                limpiarCampos();
            }
        } else {
            limpiarCampos();
        }
    }
    
    private void actualizarTotal() {
        double total = detallesActuales.stream()
            .mapToDouble(DetalleVenta::getSubtotal)
            .sum();
        txtTotal.setText(String.format("$%.2f", total));
    }
    
    private void limpiarCampos() {
        txtCodigo.clear();
        txtProducto.clear();
        txtPrecio.clear();
        txtCantidad.clear();
        txtCodigo.requestFocus();
    }
    
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
