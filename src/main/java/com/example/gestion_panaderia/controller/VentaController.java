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

/**
 * Se controla la pantalla de ventas de la aplicación.
 * Se registran nuevas ventas, se agregan productos y se procesan los pagos.
 * También se maneja el stock de productos automáticamente.
 */
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

    // ==================== SERVICIOS ====================
    /**
     * Se usa para buscar y actualizar productos.
     * Se conecta al archivo JSON de productos.
     */
    private IProductoService productoService;

    /**
     * Se usa para registrar nuevas ventas.
     * Se guarda en el archivo JSON de ventas.
     */
    private IVentaService ventaService;

    /**
     * Se guardan los detalles de la venta actual.
     * Se actualiza automáticamente la tabla y el total.
     */
    private ObservableList<DetalleVenta> detallesActuales;

    @FXML
    public void initialize() {
        inicializar();
    }

    /**
     * Se prepara el controlador para usarse.
     * Se inicializan servicios, se configuran componentes y se preparan datos.
     */
    @Override
    public void inicializar() {
        // Se conecta con los archivos JSON de productos y ventas
        JsonRepository<Producto> productoRepo = new JsonRepository<>("productos.json", Producto.class);
        JsonRepository<Venta> ventaRepo = new JsonRepository<>("ventas.json", Venta.class);

        productoService = new ProductoServiceImpl(productoRepo);
        ventaService = new VentaServiceImpl(ventaRepo);

        // Se prepara la lista para los detalles de venta
        detallesActuales = FXCollections.observableArrayList();

        // Se configuran todos los componentes visuales
        configurarTabla();
        configurarEventos();
        usuarioActual();

        // Se configura el campo total como solo lectura
        txtTotal.setEditable(false);
        txtTotal.setText("$0.00");
    }

    // ==================== MÉTODOS DE NAVEGACIÓN ====================

    /**
     * Se abre la pantalla de ventas (esta misma).
     * Se cambia a la interfaz de gestión de ventas.
     */
    @FXML
    private void abrirVentas(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/ventas/ventas.fxml", "Dulce Tentación - Ventas");
    }

    /**
     * Se abre la pantalla de pedidos.
     * Se navega a la gestión de pedidos.
     */
    @FXML
    private void abrirPedidos(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/pedidos/pedidos.fxml", "Dulce Tentación - Pedidos");
    }

    /**
     * Se abre la pantalla de productos.
     * Se navega a la gestión de productos.
     */
    @FXML
    private void abrirProductos(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/productos/productos.fxml", "Dulce Tentación - Productos");
    }

    /**
     * Se abre la pantalla de inventario.
     * Se navega al control de stock.
     */
    @FXML
    private void abrirInventario(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/inventario/inventario.fxml", "Dulce Tentación - Inventario");
    }

    /**
     * Se abre la pantalla de clientes.
     * Se navega a la gestión de clientes.
     */
    @FXML
    private void abrirClientes(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/clientes/clientes.fxml", "Dulce Tentación - Clientes");
    }

    /**
     * Se abre la pantalla de recibos.
     * Se navega a la visualización de recibos.
     */
    @FXML
    private void abrirRecibos(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/recibos/recibos.fxml", "Dulce Tentación - Recibos");
    }

    /**
     * Se abre la pantalla de reportes.
     * Se navega a los reportes y estadísticas.
     */
    @FXML
    private void abrirReportes(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/reportes/reportes.fxml", "Dulce Tentación - Reportes");
    }

    /**
     * Se cambia entre ventanas de la aplicación.
     * Se carga el archivo FXML y se muestra la nueva pantalla.
     *
     * @param event Se usa para obtener la ventana actual
     * @param rutaFXML Se carga esta interfaz gráfica
     * @param titulo Se muestra este título en la ventana
     */
    private void cambiarVentana(javafx.event.ActionEvent event, String rutaFXML, String titulo) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource(rutaFXML));
            javafx.scene.Parent root = loader.load();

            // Se obtiene la ventana actual desde el botón que disparó el evento
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

    /**
     * Se muestra el usuario actual en la interfaz.
     * CORRECCIÓN: por ahora se muestra un usuario de prueba.
     */
    public void usuarioActual(){
        usuarioActual.setText("aedadooad");
    }

    /**
     * Se configura la tabla de detalles de venta.
     * Se define qué datos van en cada columna.
     */
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

    /**
     * Se configuran los eventos de botones y campos.
     * Se define qué pasa cuando se hacen clics o se presiona Enter.
     */
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

        // Se busca producto automáticamente al presionar Enter en código
        if (txtCodigo != null) {
            txtCodigo.setOnAction(event -> buscarProductoPorCodigo());
        }
    }

    /**
     * Se busca un producto por su código.
     * Se llenan automáticamente los campos de producto y precio.
     */
    private void buscarProductoPorCodigo() {
        String codigo = txtCodigo.getText().trim();
        if (!codigo.isEmpty()) {
            Producto producto = productoService.buscarPorId(codigo);
            if (producto != null) {
                txtProducto.setText(producto.getNombre());
                txtPrecio.setText(String.valueOf(producto.getPrecio()));
                txtCantidad.requestFocus(); // Se mueve el foco a cantidad
            } else {
                mostrarAlerta("Producto no encontrado",
                        "No existe un producto con el código: " + codigo,
                        Alert.AlertType.WARNING);
                limpiarCampos();
            }
        }
    }

    /**
     * Se agrega un producto a la venta actual.
     * Se valida stock y se calcula el subtotal.
     */
    private void handleAgregarDetalle() {
        try {
            String codigo = txtCodigo.getText().trim();
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            // Se validan datos básicos
            if (codigo.isEmpty()) {
                mostrarAlerta("Error", "Ingrese un código de producto", Alert.AlertType.ERROR);
                return;
            }

            if (cantidad <= 0) {
                mostrarAlerta("Error", "La cantidad debe ser mayor a 0", Alert.AlertType.ERROR);
                return;
            }

            // Se busca el producto
            Producto producto = productoService.buscarPorId(codigo);

            if (producto == null) {
                mostrarAlerta("Error", "Producto no encontrado", Alert.AlertType.ERROR);
                return;
            }

            // Se valida que haya suficiente stock
            if (cantidad > producto.getStock()) {
                mostrarAlerta("Error",
                        "Stock insuficiente. Disponible: " + producto.getStock(),
                        Alert.AlertType.ERROR);
                return;
            }

            // Se crea y agrega el detalle de venta
            DetalleVenta detalle = new DetalleVenta(producto, cantidad);
            detallesActuales.add(detalle);

            // Se actualiza el total y se limpian campos
            actualizarTotal();
            limpiarCampos();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Cantidad inválida", Alert.AlertType.ERROR);
        }
    }

    /**
     * Se guarda la venta completa en el sistema.
     * Se registra la venta y se actualiza el stock de productos.
     */
    private void handleGuardarVenta() {
        if (detallesActuales.isEmpty()) {
            mostrarAlerta("Error",
                    "Agregue al menos un producto a la venta",
                    Alert.AlertType.ERROR);
            return;
        }

        try {
            // Se crea un vendedor temporal (en sistema real se usaría el usuario logueado)
            Empleado vendedor = new Empleado("1", "Vendedor", "vendedor", "123", "Vendedor");

            // Se registra la venta
            Venta venta = ventaService.registrarVenta(
                    new ArrayList<>(detallesActuales), vendedor);

            // Se actualiza el stock de cada producto vendido
            for (DetalleVenta detalle : detallesActuales) {
                Producto p = detalle.getProducto();
                p.actualizarStock(-detalle.getCantidad());
                productoService.actualizarProducto(p);
            }

            // Se muestra confirmación
            mostrarAlerta("Éxito",
                    "Venta registrada correctamente.\nID: " + venta.getId() +
                            "\nTotal: $" + String.format("%.2f", venta.getTotal()),
                    Alert.AlertType.INFORMATION);

            // Se limpia la venta actual
            detallesActuales.clear();
            actualizarTotal();

        } catch (Exception e) {
            mostrarAlerta("Error",
                    "No se pudo registrar la venta: " + e.getMessage(),
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Se cancela la venta actual.
     * Se pide confirmación si hay productos agregados.
     */
    private void handleCancelar() {
        if (!detallesActuales.isEmpty()) {
            // Se pide confirmación antes de cancelar
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

    /**
     * Se actualiza el total de la venta.
     * Se suman todos los subtotales de los detalles.
     */
    private void actualizarTotal() {
        double total = detallesActuales.stream()
                .mapToDouble(DetalleVenta::getSubtotal)
                .sum();
        txtTotal.setText(String.format("$%.2f", total));
    }

    /**
     * Se limpian los campos de entrada de productos.
     * Se deja listo para agregar un nuevo producto.
     */
    private void limpiarCampos() {
        txtCodigo.clear();
        txtProducto.clear();
        txtPrecio.clear();
        txtCantidad.clear();
        txtCodigo.requestFocus(); // Se vuelve al campo de código
    }

    /**
     * Se muestra una alerta al usuario con título y mensaje.
     *
     * @param titulo Se muestra como título de la ventana
     * @param mensaje Se muestra como contenido principal
     * @param tipo Se define el tipo de alerta (error, info, etc)
     */
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}