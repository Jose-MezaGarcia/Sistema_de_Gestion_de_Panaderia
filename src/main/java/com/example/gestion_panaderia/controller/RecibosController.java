package com.example.gestion_panaderia.controller;

import com.example.gestion_panaderia.Main;
import com.example.gestion_panaderia.model.*;
import com.example.gestion_panaderia.service.*;
import com.example.gestion_panaderia.repository.JsonRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Se controla la pantalla de recibos de la aplicación.
 * Se muestran los recibos guardados, se buscan, se ven detalles y se exportan.
 */
public class RecibosController implements IController {

    // ==================== SERVICIOS ====================
    /**
     * Se usa para obtener los datos de las ventas/recibos.
     * Se conecta al repositorio JSON donde se guarda la información.
     */
    private IVentaService ventaService;

    // ==================== COMPONENTES FXML ====================

    // Búsqueda
    @FXML private TextField txtBuscarRecibo;
    @FXML private Button btnBuscarRecibo;

    // Tabla de recibos
    @FXML private TableView<Venta> tablaRecibos;
    @FXML private TableColumn<Venta, String> colNumero;
    @FXML private TableColumn<Venta, String> colFecha;
    @FXML private TableColumn<Venta, String> colCliente;
    @FXML private TableColumn<Venta, String> colProductos;
    @FXML private TableColumn<Venta, Double> colPrecioUnit;
    @FXML private TableColumn<Venta, Integer> colCantidad;
    @FXML private TableColumn<Venta, Double> colSubtotal;
    @FXML private TableColumn<Venta, Double> colTotal;

    // Detalles del recibo
    @FXML private Label lblReciboNumero;
    @FXML private Label lblReciboFecha;
    @FXML private Label lblReciboCliente;
    @FXML private Label lblReciboProductos;
    @FXML private Label lblReciboSubtotal;
    @FXML private Label lblReciboIVA;
    @FXML private Label lblReciboTotal;

    // Botones de acción
    @FXML private Button btnImprimir;
    @FXML private Button btnGuardarPDF;
    @FXML private Button btnEnviarEmail;
    @FXML private Button btnEliminar;

    // Navegación
    @FXML private Button btnVentas;
    @FXML private Button btnPedidos;
    @FXML private Button btnProductos;
    @FXML private Button btnInventario;
    @FXML private Button btnClientes;
    @FXML private Button btnRecibos;
    @FXML private Button btnReportes;
    @FXML private Label usuarioActual;

    // ==================== DATOS ====================
    /**
     * Se guardan todos los recibos cargados.
     * Se actualiza automáticamente la tabla cuando cambia.
     */
    private ObservableList<Venta> recibosData;

    /**
     * Se filtran los recibos según lo que se busque.
     * Se conecta con recibosData para aplicar filtros.
     */
    private FilteredList<Venta> recibosFiltrados;

    // ==================== CONSTANTES ====================
    /**
     * Se aplica este porcentaje de IVA a los cálculos.
     * CORRECCIÓN: si cambia el IVA, se modifica solo aquí.
     */
    private static final double IVA_PORCENTAJE = 0.10; // 10% IVA

    // ==================== MÉTODOS DE INICIALIZACIÓN ====================

    @FXML
    public void initialize() {
        inicializar();
    }

    /**
     * Se prepara el controlador para usarse.
     * Se inicializan servicios, se configuran componentes y se cargan datos.
     */
    @Override
    public void inicializar() {
        // Se conecta con el archivo JSON donde se guardan las ventas
        JsonRepository<Venta> ventaRepo = new JsonRepository<>("ventas.json", Venta.class);
        ventaService = new VentaServiceImpl(ventaRepo);

        // Se preparan las listas para mostrar datos
        recibosData = FXCollections.observableArrayList();
        recibosFiltrados = new FilteredList<>(recibosData);

        // Se configuran todos los componentes visuales
        configurarTabla();
        configurarEventos();
        configurarNavegacion();

        // Se cargan los recibos desde el servicio
        cargarRecibos();

        // Se muestra el usuario actual en pantalla
        usuarioActual.setText("Administrador");
    }

    /**
     * Se configura cómo se muestra la tabla de recibos.
     * Se definen qué datos van en cada columna y cómo formatearlos.
     */
    private void configurarTabla() {
        // Se muestra el número de recibo
        colNumero.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Se formatea la fecha para que se vea mejor
        colFecha.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getFecha().format(formatter));
        });

        // Por ahora se usa un cliente general
        colCliente.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty("Cliente General"));

        // Se muestran los productos, limitando a 2 para no hacer muy larga la celda
        colProductos.setCellValueFactory(cellData -> {
            String productos = cellData.getValue().getDetalles().stream()
                    .map(d -> d.getProducto().getNombre())
                    .limit(2) // Se muestran solo los primeros 2 productos
                    .collect(Collectors.joining(", "));

            int totalProductos = cellData.getValue().getDetalles().size();
            if (totalProductos > 2) {
                productos += " y " + (totalProductos - 2) + " más...";
            }

            return new javafx.beans.property.SimpleStringProperty(productos);
        });

        // Se toma el primer producto para mostrar precio unitario
        colPrecioUnit.setCellValueFactory(cellData -> {
            if (!cellData.getValue().getDetalles().isEmpty()) {
                return new javafx.beans.property.SimpleDoubleProperty(
                        cellData.getValue().getDetalles().get(0).getProducto().getPrecio()).asObject();
            }
            return new javafx.beans.property.SimpleDoubleProperty(0.0).asObject();
        });

        // Se toma la cantidad del primer producto
        colCantidad.setCellValueFactory(cellData -> {
            if (!cellData.getValue().getDetalles().isEmpty()) {
                return new javafx.beans.property.SimpleIntegerProperty(
                        cellData.getValue().getDetalles().get(0).getCantidad()).asObject();
            }
            return new javafx.beans.property.SimpleIntegerProperty(0).asObject();
        });

        // Se calcula el subtotal del primer producto
        colSubtotal.setCellValueFactory(cellData -> {
            if (!cellData.getValue().getDetalles().isEmpty()) {
                return new javafx.beans.property.SimpleDoubleProperty(
                        cellData.getValue().getDetalles().get(0).getSubtotal()).asObject();
            }
            return new javafx.beans.property.SimpleDoubleProperty(0.0).asObject();
        });

        // Se muestra el total de la venta
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        // Se conecta la tabla con los datos filtrados
        tablaRecibos.setItems(recibosFiltrados);

        // Se detecta cuando se selecciona un recibo para mostrar sus detalles
        tablaRecibos.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> mostrarDetalleRecibo(newSelection));
    }

    /**
     * Se configuran los eventos de botones y campos.
     * Se define qué pasa cuando se hacen clics o se escribe.
     */
    private void configurarEventos() {
        // Búsqueda
        btnBuscarRecibo.setOnAction(event -> buscarRecibos());
        txtBuscarRecibo.setOnAction(event -> buscarRecibos());
        txtBuscarRecibo.textProperty().addListener((obs, oldVal, newVal) -> buscarRecibos());

        // Acciones de recibo
        btnImprimir.setOnAction(event -> imprimirRecibo());
        btnGuardarPDF.setOnAction(event -> guardarPDF());
        btnEnviarEmail.setOnAction(event -> enviarEmail());
        btnEliminar.setOnAction(event -> eliminarRecibo());
    }

    /**
     * Se configuran los botones de navegación entre pantallas.
     * Cada botón lleva a una sección diferente de la aplicación.
     */
    private void configurarNavegacion() {
        btnVentas.setOnAction(event -> abrirVentas());
        btnPedidos.setOnAction(event -> abrirPedidos());
        btnProductos.setOnAction(event -> abrirProductos());
        btnInventario.setOnAction(event -> abrirInventario());
        btnClientes.setOnAction(event -> abrirClientes());
        btnRecibos.setOnAction(event -> abrirRecibos());
        btnReportes.setOnAction(event -> abrirReportes());
    }

    // ==================== MÉTODOS DE NEGOCIO ====================

    /**
     * Se cargan todos los recibos desde el servicio.
     * Si hay error, se muestra alerta al usuario.
     */
    private void cargarRecibos() {
        try {
            List<Venta> ventas = ventaService.listarVentas();
            recibosData.setAll(ventas);

            // Se selecciona automáticamente el primer recibo si existe
            if (!ventas.isEmpty()) {
                tablaRecibos.getSelectionModel().selectFirst();
            }
        } catch (Exception e) {
            System.err.println("Error al cargar recibos: " + e.getMessage());
            mostrarAlerta("Error", "No se pudieron cargar los recibos", Alert.AlertType.ERROR);
        }
    }

    /**
     * Se filtran los recibos según lo que se escriba en la búsqueda.
     * Se busca por número de recibo o nombre de producto.
     */
    @FXML
    private void buscarRecibos() {
        String textoBusqueda = txtBuscarRecibo.getText().toLowerCase();

        recibosFiltrados.setPredicate(recibo -> {
            if (textoBusqueda.isEmpty()) {
                return true;
            }

            // Se busca por número de recibo
            if (recibo.getId().toLowerCase().contains(textoBusqueda)) {
                return true;
            }

            // Se busca por productos en el recibo
            boolean coincideProducto = recibo.getDetalles().stream()
                    .anyMatch(detalle ->
                            detalle.getProducto().getNombre().toLowerCase().contains(textoBusqueda));

            return coincideProducto;
        });
    }

    /**
     * Se muestran los detalles del recibo seleccionado.
     * Se calculan subtotal, IVA y total para mostrarlos.
     *
     * @param recibo Se muestran los datos de este recibo, si es null se limpia la pantalla
     */
    private void mostrarDetalleRecibo(Venta recibo) {
        if (recibo == null) {
            limpiarDetalles();
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            lblReciboNumero.setText("Recibo: " + recibo.getId());
            lblReciboFecha.setText("Fecha: " + recibo.getFecha().format(formatter));
            lblReciboCliente.setText("Cliente: Cliente General");

            // Se construye la lista de productos con sus precios
            StringBuilder productos = new StringBuilder();
            double subtotal = 0;

            for (DetalleVenta detalle : recibo.getDetalles()) {
                productos.append("• ")
                        .append(detalle.getProducto().getNombre())
                        .append(" - $")
                        .append(String.format("%.2f", detalle.getProducto().getPrecio()))
                        .append(" x")
                        .append(detalle.getCantidad())
                        .append("\n");
                subtotal += detalle.getSubtotal();
            }

            lblReciboProductos.setText(productos.toString());
            lblReciboSubtotal.setText(String.format("Subtotal: $%.2f", subtotal));

            // Se calcula IVA y total
            double iva = subtotal * IVA_PORCENTAJE;
            double total = subtotal + iva;

            lblReciboIVA.setText(String.format("IVA (%.0f%%): $%.2f", IVA_PORCENTAJE * 100, iva));
            lblReciboTotal.setText(String.format("TOTAL: $%.2f", total));

        } catch (Exception e) {
            System.err.println("Error al mostrar detalle del recibo: " + e.getMessage());
            mostrarAlerta("Error", "No se pudo cargar el detalle del recibo", Alert.AlertType.ERROR);
        }
    }

    /**
     * Se limpian los detalles del recibo en pantalla.
     * Se usa cuando no hay recibo seleccionado o al eliminar.
     */
    private void limpiarDetalles() {
        lblReciboNumero.setText("Recibo: --");
        lblReciboFecha.setText("Fecha: --");
        lblReciboCliente.setText("Cliente: --");
        lblReciboProductos.setText("--");
        lblReciboSubtotal.setText("Subtotal: $0.00");
        lblReciboIVA.setText(String.format("IVA (%.0f%%): $0.00", IVA_PORCENTAJE * 100));
        lblReciboTotal.setText("TOTAL: $0.00");
    }

    /**
     * Se simula la impresión del recibo seleccionado.
     * Se genera el contenido y se muestra confirmación.
     */
    @FXML
    private void imprimirRecibo() {
        Venta reciboSeleccionado = tablaRecibos.getSelectionModel().getSelectedItem();
        if (reciboSeleccionado == null) {
            mostrarAlerta("Error", "Seleccione un recibo para imprimir", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Se genera el contenido del recibo (en sistema real se enviaría a impresora)
            String contenidoRecibo = generarContenidoRecibo(reciboSeleccionado);
            System.out.println("=== RECIBO IMPRESO ===\n" + contenidoRecibo);

            mostrarAlerta("Éxito", "Recibo " + reciboSeleccionado.getId() + " enviado a impresión",
                    Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo imprimir el recibo: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    /**
     * Se simula guardar el recibo como PDF.
     * En sistema real, se generaría archivo PDF descargable.
     */
    @FXML
    private void guardarPDF() {
        Venta reciboSeleccionado = tablaRecibos.getSelectionModel().getSelectedItem();
        if (reciboSeleccionado == null) {
            mostrarAlerta("Error", "Seleccione un recibo para guardar como PDF", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Se simula generación de PDF (en sistema real se crea archivo)
            String contenidoRecibo = generarContenidoRecibo(reciboSeleccionado);
            System.out.println("=== RECIBO GUARDADO COMO PDF ===\n" + contenidoRecibo);

            mostrarAlerta("Éxito", "Recibo " + reciboSeleccionado.getId() + " guardado como PDF",
                    Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo guardar el PDF: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    /**
     * Se simula enviar el recibo por email.
     * En sistema real, se conectaría con servicio de email.
     */
    @FXML
    private void enviarEmail() {
        Venta reciboSeleccionado = tablaRecibos.getSelectionModel().getSelectedItem();
        if (reciboSeleccionado == null) {
            mostrarAlerta("Error", "Seleccione un recibo para enviar por email", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Se simula envío por email (en sistema real se integra con API de email)
            String contenidoRecibo = generarContenidoRecibo(reciboSeleccionado);
            System.out.println("=== RECIBO ENVIADO POR EMAIL ===\n" + contenidoRecibo);

            mostrarAlerta("Éxito", "Recibo " + reciboSeleccionado.getId() + " enviado por email",
                    Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo enviar el email: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    /**
     * Se elimina el recibo seleccionado después de confirmar.
     * Se pide confirmación porque es una acción irreversible.
     */
    @FXML
    private void eliminarRecibo() {
        Venta reciboSeleccionado = tablaRecibos.getSelectionModel().getSelectedItem();
        if (reciboSeleccionado == null) {
            mostrarAlerta("Error", "Seleccione un recibo para eliminar", Alert.AlertType.ERROR);
            return;
        }

        // Se pide confirmación antes de eliminar
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro de eliminar el recibo " + reciboSeleccionado.getId() + "?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");

        if (confirmacion.showAndWait().get() == ButtonType.OK) {
            try {
                // Se elimina de la lista (en sistema real se eliminaría de base de datos)
                recibosData.remove(reciboSeleccionado);
                limpiarDetalles();
                mostrarAlerta("Éxito", "Recibo eliminado correctamente", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudo eliminar el recibo: " + e.getMessage(),
                        Alert.AlertType.ERROR);
            }
        }
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Se genera el contenido formateado del recibo para imprimir/exportar.
     * Se incluye información completa de productos, precios y totales.
     *
     * @param recibo Se genera el contenido para este recibo
     * @return Se obtiene el texto formateado del recibo
     */
    private String generarContenidoRecibo(Venta recibo) {
        StringBuilder contenido = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        contenido.append("🍞 DULCE TENTACIÓN\n");
        contenido.append("====================\n");
        contenido.append("Recibo: ").append(recibo.getId()).append("\n");
        contenido.append("Fecha: ").append(recibo.getFecha().format(formatter)).append("\n");
        contenido.append("Cliente: Cliente General\n");
        contenido.append("====================\n");
        contenido.append("PRODUCTOS:\n");

        double subtotal = 0;
        for (DetalleVenta detalle : recibo.getDetalles()) {
            contenido.append(String.format("- %s x%d",
                            detalle.getProducto().getNombre(), detalle.getCantidad()))
                    .append(String.format(" - $%.2f c/u", detalle.getProducto().getPrecio()))
                    .append(String.format(" - $%.2f\n", detalle.getSubtotal()));
            subtotal += detalle.getSubtotal();
        }

        contenido.append("====================\n");
        contenido.append(String.format("Subtotal: $%.2f\n", subtotal));

        double iva = subtotal * IVA_PORCENTAJE;
        contenido.append(String.format("IVA (%.0f%%): $%.2f\n", IVA_PORCENTAJE * 100, iva));

        double total = subtotal + iva;
        contenido.append(String.format("TOTAL: $%.2f\n", total));
        contenido.append("====================\n");
        contenido.append("¡Gracias por su compra!\n");

        return contenido.toString();
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

    // ==================== MÉTODOS DE NAVEGACIÓN ====================

    /**
     * Se abre la pantalla de ventas.
     * Se usa Main para cambiar entre pantallas.
     */
    @FXML
    private void abrirVentas() {
        try {
            Main.cambiarVista("/fxml/ventas/ventas.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir ventas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Se abre la pantalla de pedidos.
     * Se manejan errores por si no encuentra el archivo.
     */
    @FXML
    private void abrirPedidos() {
        try {
            Main.cambiarVista("/fxml/pedidos/pedidos.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir pedidos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Se abre la pantalla de productos.
     * Se navega a la gestión de productos.
     */
    @FXML
    private void abrirProductos() {
        try {
            Main.cambiarVista("/fxml/productos/productos.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir productos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Se abre la pantalla de inventario.
     * Se va al control de stock y existencias.
     */
    @FXML
    private void abrirInventario() {
        try {
            Main.cambiarVista("/fxml/inventario/inventario.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir inventario: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Se abre la pantalla de clientes.
     * Se navega a la gestión de clientes.
     */
    @FXML
    private void abrirClientes() {
        try {
            Main.cambiarVista("/fxml/clientes/clientes.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir clientes: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Se abre la pantalla de recibos (esta misma).
     * No hace nada porque ya estamos aquí.
     */
    @FXML
    private void abrirRecibos() {
        // Ya estamos en recibos
    }

    /**
     * Se abre la pantalla de reportes.
     * Se navega a estadísticas y reportes.
     */
    @FXML
    private void abrirReportes() {
        try {
            Main.cambiarVista("/fxml/reportes/reportes.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir reportes: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}