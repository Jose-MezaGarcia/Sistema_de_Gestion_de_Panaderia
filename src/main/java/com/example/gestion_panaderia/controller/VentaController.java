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
    @FXML public Label usuarioActual;

    // Componentes de Cliente
    @FXML private ComboBox<Cliente> cmbCliente;
    @FXML private Label lblCalificacionCliente;
    @FXML private Label lblDescuentoCliente;

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
    @FXML private TextField txtSubtotal;
    @FXML private TextField txtDescuento;
    @FXML private TextField txtTotal;

    @FXML private Button btnAgregar;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    // Servicios y repositorios
    private IProductoService productoService;
    private IVentaService ventaService;
    private JsonRepository<Cliente> clienteRepository;
    private ObservableList<DetalleVenta> detallesActuales;
    private Cliente clienteSeleccionado;

    @FXML
    public void initialize() {
        inicializar();
    }

    @Override
    public void inicializar() {
        // Inicializar repositorios y servicios
        JsonRepository<Producto> productoRepo = new JsonRepository<>("productos.json", Producto.class);
        JsonRepository<Venta> ventaRepo = new JsonRepository<>("ventas.json", Venta.class);
        clienteRepository = new JsonRepository<>("clientes.json", Cliente.class);

        productoService = new ProductoServiceImpl(productoRepo);
        ventaService = new VentaServiceImpl(ventaRepo);

        detallesActuales = FXCollections.observableArrayList();

        configurarTabla();
        configurarEventos();
        cargarClientes();
        usuarioActual();

        // Campos no editables
        txtSubtotal.setEditable(false);
        txtDescuento.setEditable(false);
        txtTotal.setEditable(false);

        // Valores iniciales
        actualizarTotales();
    }

    /**
     * Carga los clientes en el ComboBox
     */
    private void cargarClientes() {
        try {
            List<Cliente> clientes = clienteRepository.cargar();
            ObservableList<Cliente> clientesObservable = FXCollections.observableArrayList(clientes);
            cmbCliente.setItems(clientesObservable);

            // Configurar cómo se muestra el cliente en el ComboBox
            cmbCliente.setCellFactory(param -> new ListCell<Cliente>() {
                @Override
                protected void updateItem(Cliente cliente, boolean empty) {
                    super.updateItem(cliente, empty);
                    if (empty || cliente == null) {
                        setText(null);
                    } else {
                        String emoji = cliente.getCalificacion() != null ?
                                cliente.getCalificacion().getEmoji() : "😐";
                        setText(emoji + " " + cliente.getNombre() + " - " + cliente.getId());
                    }
                }
            });

            cmbCliente.setButtonCell(new ListCell<Cliente>() {
                @Override
                protected void updateItem(Cliente cliente, boolean empty) {
                    super.updateItem(cliente, empty);
                    if (empty || cliente == null) {
                        setText("Seleccionar cliente...");
                    } else {
                        String emoji = cliente.getCalificacion() != null ?
                                cliente.getCalificacion().getEmoji() : "😐";
                        setText(emoji + " " + cliente.getNombre());
                    }
                }
            });

        } catch (Exception e) {
            System.err.println("Error al cargar clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Maneja la selección de un cliente
     */
    private void handleSeleccionCliente() {
        clienteSeleccionado = cmbCliente.getValue();

        if (clienteSeleccionado != null) {
            // Mostrar información del cliente
            Cliente.CalificacionCliente calificacion = clienteSeleccionado.getCalificacion();

            if (calificacion != null) {
                lblCalificacionCliente.setText(calificacion.getEmoji() + " " + calificacion.name());

                int porcentaje = (int)(calificacion.getDescuento() * 100);
                lblDescuentoCliente.setText(porcentaje + "% de descuento");

                // Aplicar estilo según calificación
                String estilo = "";
                switch (calificacion) {
                    case FELIZ:
                        estilo = "-fx-text-fill: #2E8B57; -fx-font-weight: bold;";
                        break;
                    case NEUTRAL:
                        estilo = "-fx-text-fill: #FF8C00; -fx-font-weight: bold;";
                        break;
                    case TRISTE:
                        estilo = "-fx-text-fill: #DC143C; -fx-font-weight: bold;";
                        break;
                }
                lblCalificacionCliente.setStyle(estilo);
                lblDescuentoCliente.setStyle(estilo);
            } else {
                lblCalificacionCliente.setText("Sin calificación");
                lblDescuentoCliente.setText("0% de descuento");
                lblCalificacionCliente.setStyle("-fx-text-fill: #5c4033;");
                lblDescuentoCliente.setStyle("-fx-text-fill: #5c4033;");
            }

            // Recalcular totales con el descuento del cliente
            actualizarTotales();
        } else {
            lblCalificacionCliente.setText("No seleccionado");
            lblDescuentoCliente.setText("0% de descuento");
            lblCalificacionCliente.setStyle("-fx-text-fill: #5c4033;");
            lblDescuentoCliente.setStyle("-fx-text-fill: #5c4033;");
            actualizarTotales();
        }
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

        // Evento de selección de cliente
        if (cmbCliente != null) {
            cmbCliente.setOnAction(event -> handleSeleccionCliente());
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

            actualizarTotales();
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

            // Registrar la venta usando el servicio con cliente
            Venta venta = ventaService.registrarVenta(
                    new ArrayList<>(detallesActuales),
                    vendedor,
                    clienteSeleccionado
            );

            // Actualizar stock de productos
            for (DetalleVenta detalle : detallesActuales) {
                Producto p = detalle.getProducto();
                p.actualizarStock(-detalle.getCantidad());
                productoService.actualizarProducto(p);
            }

            // Mostrar resumen
            String resumenCliente = clienteSeleccionado != null ?
                    "\n👤 Cliente: " + clienteSeleccionado.getNombre() +
                            "\n🎯 Calificación: " + clienteSeleccionado.getCalificacion().getEmoji() +
                            "\n💰 Descuento aplicado: " + String.format("%.0f%%", venta.getPorcentajeDescuento()) +
                            "\n💵 Monto descontado: $" + String.format("%.2f", venta.getDescuento()) :
                    "\n⚠️ Sin cliente registrado (sin descuento)";

            mostrarAlerta("✅ Venta Registrada",
                    "Venta guardada exitosamente." +
                            "\n━━━━━━━━━━━━━━━━━━━━" +
                            "\n🆔 ID de Venta: " + venta.getId() +
                            resumenCliente +
                            "\n━━━━━━━━━━━━━━━━━━━━" +
                            "\n📊 Subtotal: $" + String.format("%.2f", venta.getSubtotal()) +
                            "\n✅ TOTAL PAGADO: $" + String.format("%.2f", venta.getTotal()),
                    Alert.AlertType.INFORMATION);

            limpiarVenta();

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
                limpiarVenta();
            }
        } else {
            limpiarVenta();
        }
    }

    /**
     * Actualiza los totales según el cliente seleccionado
     */
    private void actualizarTotales() {
        // Calcular subtotal
        double subtotal = detallesActuales.stream()
                .mapToDouble(DetalleVenta::getSubtotal)
                .sum();

        // Calcular descuento
        double descuento = 0;
        if (clienteSeleccionado != null && clienteSeleccionado.getCalificacion() != null) {
            descuento = subtotal * clienteSeleccionado.getCalificacion().getDescuento();
        }

        // Calcular total
        double total = subtotal - descuento;

        // Actualizar campos
        txtSubtotal.setText(String.format("$%.2f", subtotal));
        txtDescuento.setText(String.format("$%.2f", descuento));
        txtTotal.setText(String.format("$%.2f", total));
    }

    private void limpiarCampos() {
        txtCodigo.clear();
        txtProducto.clear();
        txtPrecio.clear();
        txtCantidad.clear();
        txtCodigo.requestFocus();
    }

    private void limpiarVenta() {
        detallesActuales.clear();
        cmbCliente.setValue(null);
        clienteSeleccionado = null;
        lblCalificacionCliente.setText("No seleccionado");
        lblDescuentoCliente.setText("0% de descuento");
        actualizarTotales();
        limpiarCampos();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void usuarioActual() {
        if (usuarioActual != null) {
            usuarioActual.setText("Administrador");
        }
    }

    // Métodos de navegación
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