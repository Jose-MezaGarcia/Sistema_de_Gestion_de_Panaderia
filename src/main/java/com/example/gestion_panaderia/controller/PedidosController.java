package com.example.gestion_panaderia.controller;

import com.example.gestion_panaderia.Main;
import com.example.gestion_panaderia.model.Pedido;
import com.example.gestion_panaderia.service.IPedidoService;
import com.example.gestion_panaderia.service.PedidoServiceImpl;
import com.example.gestion_panaderia.repository.IRepository;
import com.example.gestion_panaderia.repository.JsonRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controlador de la vista de pedidos
 * Aquí se manejan los formularios, la tabla, las estadísticas y las acciones sobre pedidos
 */
public class PedidosController implements IController {

    // ==================== SERVICIOS ====================
    private IPedidoService pedidoService;

    // ==================== COMPONENTES FXML ====================
    @FXML private Button btnVentas;
    @FXML private Button btnPedidos;
    @FXML private Button btnProductos;
    @FXML private Button btnInventario;
    @FXML private Button btnClientes;
    @FXML private Button btnRecibos;
    @FXML private Button btnReportes;
    @FXML private Label usuarioActual;

    // Formulario
    @FXML private TextField txtTituloPedido;
    @FXML private TextField txtClientePedido;
    @FXML private DatePicker dpFechaEntrega;
    @FXML private ComboBox<String> cbHoraEntrega;
    @FXML private TextArea txtDescripcionPedido;
    @FXML private ComboBox<String> cbEstadoPedido;
    @FXML private TextField txtTelefonoCliente;
    @FXML private Button btnAgregarPedido;
    @FXML private Button btnLimpiarFormulario;

    // Búsqueda y tabla
    @FXML private TextField txtBuscarPedidos;
    @FXML private ComboBox<String> cbFiltrarEstado;
    @FXML private TableView<Pedido> tablaPedidos;
    @FXML private TableColumn<Pedido, String> colIdPedido;
    @FXML private TableColumn<Pedido, String> colTituloPedido;
    @FXML private TableColumn<Pedido, String> colClientePedido;
    @FXML private TableColumn<Pedido, String> colFechaEntrega;
    @FXML private TableColumn<Pedido, String> colHoraEntrega;
    @FXML private TableColumn<Pedido, String> colDescripcion;
    @FXML private TableColumn<Pedido, String> colEstadoPedido;
    @FXML private TableColumn<Pedido, String> colTelefono;
    @FXML private TableColumn<Pedido, String> colAcciones;

    // Estadísticas
    @FXML private Label lblTotalPedidos;
    @FXML private Label lblPedidosPendientes;
    @FXML private Label lblPedidosCompletados;
    @FXML private Label lblPedidosProceso;

    // Botones de acción
    @FXML private Button btnEditarPedido;
    @FXML private Button btnCambiarEstado;
    @FXML private Button btnEliminarPedido;
    @FXML private Button btnImprimirPedido;

    // ==================== DATOS ====================
    private ObservableList<Pedido> pedidosData;
    private FilteredList<Pedido> pedidosFiltrados;
    private ObservableList<String> estados;
    private ObservableList<String> horas;

    // ==================== INICIALIZACIÓN ====================

    /**
     * Se ejecuta automáticamente al cargar la vista
     */
    @FXML
    public void initialize() {
        inicializar();
    }

    /**
     * Se inicializan los servicios, datos y componentes de la vista
     */
    @Override
    public void inicializar() {
        // Se inicializa el servicio con repositorio JSON
        IRepository<Pedido> pedidoRepo = new JsonRepository<>("pedidos.json", Pedido.class);
        pedidoService = new PedidoServiceImpl(pedidoRepo);

        // Se inicializan las listas de datos
        pedidosData = FXCollections.observableArrayList();
        pedidosFiltrados = new FilteredList<>(pedidosData);

        // Se configuran los componentes de la vista
        configurarOpciones();
        configurarTabla();
        configurarEventos();
        configurarNavegacion();

        // Se cargan los pedidos desde el archivo JSON
        cargarPedidos();
        actualizarEstadisticas();

        // Se muestra el usuario actual
        usuarioActual.setText("Administrador");
    }
}

/**
 * Se configuran las opciones de estado y horas disponibles
 * Se llenan los ComboBox con valores predefinidos
 */
private void configurarOpciones() {
    // Estados posibles para pedidos
    estados = FXCollections.observableArrayList(
            "PENDIENTE", "EN PROCESO", "COMPLETADO", "CANCELADO"
    );
    cbEstadoPedido.setItems(estados);

    // Filtros de estado
    ObservableList<String> filtrosEstado = FXCollections.observableArrayList("TODOS");
    filtrosEstado.addAll(estados);
    cbFiltrarEstado.setItems(filtrosEstado);

    // Horas disponibles (8:00 AM - 8:00 PM, cada 30 min)
    horas = FXCollections.observableArrayList();
    for (int hora = 8; hora <= 20; hora++) {
        for (int minuto = 0; minuto < 60; minuto += 30) {
            horas.add(String.format("%02d:%02d", hora, minuto));
        }
    }
    cbHoraEntrega.setItems(horas);
}

/**
 * Se configuran las columnas de la tabla de pedidos
 * Se formatean fechas y horas para que sean legibles
 */
private void configurarTabla() {
    colIdPedido.setCellValueFactory(new PropertyValueFactory<>("id"));
    colTituloPedido.setCellValueFactory(new PropertyValueFactory<>("titulo"));
    colClientePedido.setCellValueFactory(new PropertyValueFactory<>("cliente"));

    // Formato legible para fechas
    colFechaEntrega.setCellValueFactory(cellData -> {
        LocalDate fecha = cellData.getValue().getFechaEntregaAsLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return new javafx.beans.property.SimpleStringProperty(
                fecha != null ? fecha.format(formatter) : "--");
    });

    // Formato legible para horas
    colHoraEntrega.setCellValueFactory(cellData -> {
        LocalTime hora = cellData.getValue().getHoraEntregaAsLocalTime();
        return new javafx.beans.property.SimpleStringProperty(
                hora != null ? hora.format(DateTimeFormatter.ofPattern("HH:mm")) : "--");
    });

    colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
    colEstadoPedido.setCellValueFactory(new PropertyValueFactory<>("estado"));
    colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

    // Columna de acciones (editar y eliminar)
    configurarColumnaAcciones();

    tablaPedidos.setItems(pedidosFiltrados);
}

/**
 * Se configuran los botones de acción dentro de la tabla
 * Cada fila tendrá botones para editar y eliminar
 */
private void configurarColumnaAcciones() {
    colAcciones.setCellFactory(param -> new TableCell<Pedido, String>() {
        private final Button btnEliminar = new Button("🗑️");
        private final Button btnEditar = new Button("✏️");

        {
            btnEliminar.setOnAction(event -> {
                Pedido pedido = getTableView().getItems().get(getIndex());
                eliminarPedido(pedido);
            });

            btnEditar.setOnAction(event -> {
                Pedido pedido = getTableView().getItems().get(getIndex());
                editarPedido(pedido);
            });

            // Estilos de botones
            btnEliminar.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
            btnEditar.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                HBox botones = new HBox(5, btnEditar, btnEliminar);
                setGraphic(botones);
            }
        }
    });
}

/**
 * Se configuran los eventos de los botones y campos
 * Incluye formulario, búsqueda, filtros y acciones de tabla
 */
private void configurarEventos() {
    // Formulario
    btnAgregarPedido.setOnAction(event -> agregarPedido());
    btnLimpiarFormulario.setOnAction(event -> limpiarFormulario());

    // Búsqueda y filtros
    txtBuscarPedidos.textProperty().addListener((obs, oldVal, newVal) -> filtrarPedidos());
    cbFiltrarEstado.setOnAction(event -> filtrarPedidos());

    // Acciones de tabla
    btnEditarPedido.setOnAction(event -> editarPedidoSeleccionado());
    btnEliminarPedido.setOnAction(event -> eliminarPedidoSeleccionado());
    btnCambiarEstado.setOnAction(event -> cambiarEstadoPedido());
    btnImprimirPedido.setOnAction(event -> imprimirPedido());
}

/**
 * Se configuran los botones de navegación entre vistas
 */
private void configurarNavegacion() {
    btnVentas.setOnAction(event -> abrirVentas());
    btnPedidos.setOnAction(event -> {}); // Ya estamos en pedidos
    btnProductos.setOnAction(event -> abrirProductos());
    btnInventario.setOnAction(event -> abrirInventario());
    btnClientes.setOnAction(event -> abrirClientes());
    btnRecibos.setOnAction(event -> abrirRecibos());
    btnReportes.setOnAction(event -> abrirReportes());
}

// ==================== MÉTODOS DE NEGOCIO ====================

/**
 * Se cargan los pedidos desde el servicio
 * Si ocurre un error se muestra una alerta
 */
private void cargarPedidos() {
    try {
        List<Pedido> pedidos = pedidoService.listarPedidos();
        pedidosData.setAll(pedidos);
        System.out.println("✅ Pedidos cargados desde JSON: " + pedidos.size());
    } catch (Exception e) {
        System.err.println("❌ Error al cargar pedidos: " + e.getMessage());
        e.printStackTrace();
        mostrarAlerta("Error", "No se pudieron cargar los pedidos: " + e.getMessage(), Alert.AlertType.ERROR);
    }
}


/**
 * Se agrega un nuevo pedido desde el formulario
 * Se valida, se crea el objeto y se guarda en JSON
 */
@FXML
private void agregarPedido() {
    try {
        // Validar campos
        if (!validarFormulario()) {
            return;
        }

        // Crear nuevo pedido
        Pedido pedido = new Pedido();
        pedido.setId(generarIdPedido());
        pedido.setTitulo(txtTituloPedido.getText().trim());
        pedido.setCliente(txtClientePedido.getText().trim());

        // Usar métodos especiales para fechas/horas
        pedido.setFechaEntregaFromLocalDate(dpFechaEntrega.getValue());

        // Convertir hora de string a LocalTime y luego guardar como String
        String horaStr = cbHoraEntrega.getValue();
        if (horaStr != null) {
            pedido.setHoraEntregaFromLocalTime(LocalTime.parse(horaStr));
        }

        pedido.setDescripcion(txtDescripcionPedido.getText().trim());
        pedido.setEstado(cbEstadoPedido.getValue());
        pedido.setTelefono(txtTelefonoCliente.getText().trim());

        // Guardar pedido en JSON
        pedidoService.agregarPedido(pedido);
        pedidosData.add(pedido);

        // Limpiar y actualizar
        limpiarFormulario();
        actualizarEstadisticas();

        mostrarAlerta("Éxito", "Pedido agregado correctamente", Alert.AlertType.INFORMATION);

    } catch (Exception e) {
        System.err.println("❌ Error al agregar pedido: " + e.getMessage());
        e.printStackTrace();
        mostrarAlerta("Error", "No se pudo agregar el pedido: " + e.getMessage(),
                Alert.AlertType.ERROR);
    }
}

/**
 * Se valida el formulario de pedido
 * Si falta algún campo obligatorio se muestra alerta
 */
private boolean validarFormulario() {
    if (txtTituloPedido.getText().trim().isEmpty()) {
        mostrarAlerta("Error", "El título del pedido es obligatorio", Alert.AlertType.ERROR);
        txtTituloPedido.requestFocus();
        return false;
    }
    if (txtClientePedido.getText().trim().isEmpty()) {
        mostrarAlerta("Error", "El cliente es obligatorio", Alert.AlertType.ERROR);
        txtClientePedido.requestFocus();
        return false;
    }
    if (dpFechaEntrega.getValue() == null) {
        mostrarAlerta("Error", "La fecha de entrega es obligatoria", Alert.AlertType.ERROR);
        dpFechaEntrega.requestFocus();
        return false;
    }
    if (cbHoraEntrega.getValue() == null) {
        mostrarAlerta("Error", "La hora de entrega es obligatoria", Alert.AlertType.ERROR);
        cbHoraEntrega.requestFocus();
        return false;
    }
    if (cbEstadoPedido.getValue() == null) {
        mostrarAlerta("Error", "El estado es obligatorio", Alert.AlertType.ERROR);
        cbEstadoPedido.requestFocus();
        return false;
    }
    return true;
}

/**
 * Se genera un id único para el pedido
 */
private String generarIdPedido() {
    return "PED-" + System.currentTimeMillis();
}

/**
 * Se limpia el formulario de pedido
 * Se restauran los valores y el botón de agregar
 */
private void limpiarFormulario() {
    txtTituloPedido.clear();
    txtClientePedido.clear();
    dpFechaEntrega.setValue(null);
    cbHoraEntrega.setValue(null);
    txtDescripcionPedido.clear();
    cbEstadoPedido.setValue(null);
    txtTelefonoCliente.clear();

    // Restaurar botón si estaba en modo edición
    btnAgregarPedido.setText("➕ Agregar Pedido");
    btnAgregarPedido.setOnAction(event -> agregarPedido());
}

/**
 * Se filtran los pedidos según búsqueda y estado
 */
private void filtrarPedidos() {
    String textoBusqueda = txtBuscarPedidos.getText().toLowerCase();
    String estadoFiltro = cbFiltrarEstado.getValue();

    pedidosFiltrados.setPredicate(pedido -> {
        // Filtro por texto de búsqueda
        if (!textoBusqueda.isEmpty()) {
            boolean coincideTitulo = pedido.getTitulo().toLowerCase().contains(textoBusqueda);
            boolean coincideCliente = pedido.getCliente().toLowerCase().contains(textoBusqueda);
            boolean coincideDescripcion = pedido.getDescripcion().toLowerCase().contains(textoBusqueda);

            if (!coincideTitulo && !coincideCliente && !coincideDescripcion) {
                return false;
            }
        }

        // Filtro por estado
        if (estadoFiltro != null && !estadoFiltro.equals("TODOS")) {
            if (!pedido.getEstado().equals(estadoFiltro)) {
                return false;
            }
        }

        return true;
    });
}

/**
 * Se actualizan las estadísticas de pedidos
 * Se muestran totales, pendientes, completados y en proceso
 */
private void actualizarEstadisticas() {
    int total = pedidosData.size();
    int pendientes = (int) pedidosData.stream()
            .filter(p -> "PENDIENTE".equals(p.getEstado()))
            .count();
    int completados = (int) pedidosData.stream()
            .filter(p -> "COMPLETADO".equals(p.getEstado()))
            .count();
    int enProceso = (int) pedidosData.stream()
            .filter(p -> "EN PROCESO".equals(p.getEstado()))
            .count();

    lblTotalPedidos.setText(String.valueOf(total));
    lblPedidosPendientes.setText(String.valueOf(pendientes));
    lblPedidosCompletados.setText(String.valueOf(completados));
    lblPedidosProceso.setText(String.valueOf(enProceso));
}

/**
 * Se edita el pedido seleccionado en la tabla
 * Si no hay selección se muestra alerta
 */
private void editarPedidoSeleccionado() {
    Pedido pedido = tablaPedidos.getSelectionModel().getSelectedItem();
    if (pedido != null) {
        editarPedido(pedido);
    } else {
        mostrarAlerta("Error", "Seleccione un pedido para editar", Alert.AlertType.ERROR);
    }
}

/**
 * Se llena el formulario con los datos del pedido
 * Se cambia el botón a modo "Actualizar"
 */
private void editarPedido(Pedido pedido) {
    txtTituloPedido.setText(pedido.getTitulo());
    txtClientePedido.setText(pedido.getCliente());

    // Usar métodos especiales para fechas/horas
    dpFechaEntrega.setValue(pedido.getFechaEntregaAsLocalDate());

    LocalTime hora = pedido.getHoraEntregaAsLocalTime();
    if (hora != null) {
        cbHoraEntrega.setValue(hora.format(DateTimeFormatter.ofPattern("HH:mm")));
    }

    txtDescripcionPedido.setText(pedido.getDescripcion());
    cbEstadoPedido.setValue(pedido.getEstado());
    txtTelefonoCliente.setText(pedido.getTelefono());

    // Cambiar el botón a "Actualizar"
    btnAgregarPedido.setText("🔄 Actualizar");
    btnAgregarPedido.setOnAction(event -> actualizarPedido(pedido));
}

/**
 * Se actualiza un pedido con los datos del formulario
 * Se guardan los cambios en JSON y se refresca la tabla
 */
private void actualizarPedido(Pedido pedido) {
    try {
        pedido.setTitulo(txtTituloPedido.getText().trim());
        pedido.setCliente(txtClientePedido.getText().trim());

        // Usar métodos especiales para fechas/horas
        pedido.setFechaEntregaFromLocalDate(dpFechaEntrega.getValue());

        String horaStr = cbHoraEntrega.getValue();
        if (horaStr != null) {
            pedido.setHoraEntregaFromLocalTime(LocalTime.parse(horaStr));
        }

        pedido.setDescripcion(txtDescripcionPedido.getText().trim());
        pedido.setEstado(cbEstadoPedido.getValue());
        pedido.setTelefono(txtTelefonoCliente.getText().trim());

        // Guardar cambios en JSON
        pedidoService.actualizarPedido(pedido);

        // Actualizar tabla y limpiar formulario
        cargarPedidos();
        limpiarFormulario();

        mostrarAlerta("Éxito", "Pedido actualizado correctamente", Alert.AlertType.INFORMATION);

    } catch (Exception e) {
        System.err.println("❌ Error al actualizar pedido: " + e.getMessage());
        e.printStackTrace();
        mostrarAlerta("Error", "No se pudo actualizar el pedido: " + e.getMessage(),
                Alert.AlertType.ERROR);
    }
}

/**
 * Se elimina el pedido seleccionado en la tabla
 * Si no hay selección se muestra alerta
 */
private void eliminarPedidoSeleccionado() {
    Pedido pedido = tablaPedidos.getSelectionModel().getSelectedItem();
    if (pedido != null) {
        eliminarPedido(pedido);
    } else {
        mostrarAlerta("Error", "Seleccione un pedido para eliminar", Alert.AlertType.ERROR);
    }
}

/**
 * Se elimina un pedido con confirmación
 * Se actualizan estadísticas y se muestra alerta
 */
private void eliminarPedido(Pedido pedido) {
    Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
    confirmacion.setTitle("Confirmar eliminación");
    confirmacion.setHeaderText("¿Está seguro de eliminar el pedido: " + pedido.getTitulo() + "?");
    confirmacion.setContentText("Esta acción no se puede deshacer.");

    if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
        try {
            pedidoService.eliminarPedido(pedido.getId());
            pedidosData.remove(pedido);
            actualizarEstadisticas();
            mostrarAlerta("Éxito", "Pedido eliminado correctamente", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            System.err.println("❌ Error al eliminar pedido: " + e.getMessage());
            mostrarAlerta("Error", "No se pudo eliminar el pedido: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }
}

/**
 * Se cambia el estado del pedido seleccionado
 * Se abre un diálogo para elegir el nuevo estado
 */
private void cambiarEstadoPedido() {
    Pedido pedido = tablaPedidos.getSelectionModel().getSelectedItem();
    if (pedido == null) {
        mostrarAlerta("Error", "Seleccione un pedido para cambiar estado", Alert.AlertType.ERROR);
        return;
    }

    ChoiceDialog<String> dialog = new ChoiceDialog<>(pedido.getEstado(), estados);
    dialog.setTitle("Cambiar Estado");
    dialog.setHeaderText("Seleccione el nuevo estado para: " + pedido.getTitulo());
    dialog.setContentText("Estado:");

    dialog.showAndWait().ifPresent(nuevoEstado -> {
        pedido.setEstado(nuevoEstado);
        try {
            pedidoService.actualizarPedido(pedido);
            cargarPedidos();
            actualizarEstadisticas();
            mostrarAlerta("Éxito", "Estado actualizado correctamente", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            System.err.println("❌ Error al cambiar estado: " + e.getMessage());
            mostrarAlerta("Error", "No se pudo actualizar el estado", Alert.AlertType.ERROR);
        }
    });
}


/**
 * Se imprime el pedido seleccionado
 * Si no hay selección se muestra alerta
 */
private void imprimirPedido() {
    Pedido pedido = tablaPedidos.getSelectionModel().getSelectedItem();
    if (pedido == null) {
        mostrarAlerta("Error", "Seleccione un pedido para imprimir", Alert.AlertType.ERROR);
        return;
    }

    // Simular impresión
    String contenido = generarContenidoPedido(pedido);
    System.out.println("=== PEDIDO IMPRESO ===\n" + contenido);

    mostrarAlerta("Éxito", "Pedido enviado a impresión", Alert.AlertType.INFORMATION);
}

/**
 * Se genera el contenido del pedido en formato texto
 * Incluye datos básicos y se devuelve como String
 */
private String generarContenidoPedido(Pedido pedido) {
    DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate fecha = pedido.getFechaEntregaAsLocalDate();

    StringBuilder sb = new StringBuilder();
    sb.append("🍞 DULCE TENTACIÓN - PEDIDO\n");
    sb.append("==============================\n");
    sb.append("ID: ").append(pedido.getId()).append("\n");
    sb.append("Título: ").append(pedido.getTitulo()).append("\n");
    sb.append("Cliente: ").append(pedido.getCliente()).append("\n");
    sb.append("Fecha Entrega: ").append(fecha != null ? fecha.format(fechaFormatter) : "--").append("\n");
    sb.append("Hora: ").append(pedido.getHoraEntrega() != null ? pedido.getHoraEntrega() : "--").append("\n");
    sb.append("Teléfono: ").append(pedido.getTelefono()).append("\n");
    sb.append("Estado: ").append(pedido.getEstado()).append("\n");
    sb.append("Descripción:\n").append(pedido.getDescripcion()).append("\n");
    sb.append("==============================\n");

    return sb.toString();
}

/**
 * Se muestra una alerta en pantalla
 * @param titulo título de la alerta
 * @param mensaje contenido del mensaje
 * @param tipo tipo de alerta (información, error, etc.)
 */
private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
    Alert alert = new Alert(tipo);
    alert.setTitle(titulo);
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();
}

// ==================== NAVEGACIÓN ====================

/** Se abre la vista de ventas */
@FXML
private void abrirVentas() {
    try {
        Main.cambiarVista("/fxml/ventas/ventas.fxml");
    } catch (Exception e) {
        mostrarAlerta("Error", "No se pudo abrir ventas", Alert.AlertType.ERROR);
    }
}

/** Se abre la vista de productos */
@FXML
private void abrirProductos() {
    try {
        Main.cambiarVista("/fxml/productos/productos.fxml");
    } catch (Exception e) {
        mostrarAlerta("Error", "No se pudo abrir productos", Alert.AlertType.ERROR);
    }
}

/** Se abre la vista de inventario */
@FXML
private void abrirInventario() {
    try {
        Main.cambiarVista("/fxml/inventario/inventario.fxml");
    } catch (Exception e) {
        mostrarAlerta("Error", "No se pudo abrir inventario", Alert.AlertType.ERROR);
    }
}

/** Se abre la vista de clientes */
@FXML
private void abrirClientes() {
    try {
        Main.cambiarVista("/fxml/clientes/clientes.fxml");
    } catch (Exception e) {
        mostrarAlerta("Error", "No se pudo abrir clientes", Alert.AlertType.ERROR);
    }
}

/** Se abre la vista de recibos */
@FXML
private void abrirRecibos() {
    try {
        Main.cambiarVista("/fxml/recibos/recibos.fxml");
    } catch (Exception e) {
        mostrarAlerta("Error", "No se pudo abrir recibos", Alert.AlertType.ERROR);
    }
}

/** Se abre la vista de reportes */
@FXML
private void abrirReportes() {
    try {
        Main.cambiarVista("/fxml/reportes/reportes.fxml");
    } catch (Exception e) {
        mostrarAlerta("Error", "No se pudo abrir reportes", Alert.AlertType.ERROR);
    }
}
}