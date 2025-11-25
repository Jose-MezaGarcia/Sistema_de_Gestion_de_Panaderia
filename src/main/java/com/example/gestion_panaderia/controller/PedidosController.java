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

    @FXML
    public void initialize() {
        inicializar();
    }

    @Override
    public void inicializar() {
        // Inicializar servicios con JSON
        IRepository<Pedido> pedidoRepo = new JsonRepository<>("pedidos.json", Pedido.class);
        pedidoService = new PedidoServiceImpl(pedidoRepo);

        // Inicializar datos
        pedidosData = FXCollections.observableArrayList();
        pedidosFiltrados = new FilteredList<>(pedidosData);

        // Configurar componentes
        configurarOpciones();
        configurarTabla();
        configurarEventos();
        configurarNavegacion();

        // Cargar datos desde JSON
        cargarPedidos();
        actualizarEstadisticas();

        usuarioActual.setText("Administrador");
    }

    private void configurarOpciones() {
        // Estados posibles para pedidos (notas)
        estados = FXCollections.observableArrayList(
                "PENDIENTE", "EN PROCESO", "COMPLETADO", "CANCELADO"
        );
        cbEstadoPedido.setItems(estados);

        // Filtros de estado
        ObservableList<String> filtrosEstado = FXCollections.observableArrayList("TODOS");
        filtrosEstado.addAll(estados);
        cbFiltrarEstado.setItems(filtrosEstado);

        // Horas disponibles (8:00 AM - 8:00 PM)
        horas = FXCollections.observableArrayList();
        for (int hora = 8; hora <= 20; hora++) {
            for (int minuto = 0; minuto < 60; minuto += 30) {
                horas.add(String.format("%02d:%02d", hora, minuto));
            }
        }
        cbHoraEntrega.setItems(horas);
    }

    private void configurarTabla() {
        // Configurar columnas
        colIdPedido.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTituloPedido.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colClientePedido.setCellValueFactory(new PropertyValueFactory<>("cliente"));

        // CORRECCIÓN: Usar formato legible para fechas
        colFechaEntrega.setCellValueFactory(cellData -> {
            LocalDate fecha = cellData.getValue().getFechaEntregaAsLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return new javafx.beans.property.SimpleStringProperty(
                    fecha != null ? fecha.format(formatter) : "--");
        });

        // CORRECCIÓN: Usar formato legible para horas
        colHoraEntrega.setCellValueFactory(cellData -> {
            LocalTime hora = cellData.getValue().getHoraEntregaAsLocalTime();
            return new javafx.beans.property.SimpleStringProperty(
                    hora != null ? hora.format(DateTimeFormatter.ofPattern("HH:mm")) : "--");
        });

        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colEstadoPedido.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        // Configurar columna de acciones
        configurarColumnaAcciones();

        tablaPedidos.setItems(pedidosFiltrados);
    }

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

    @FXML
    private void agregarPedido() {
        try {
            // Validar campos
            if (!validarFormulario()) {
                return;
            }

            // Crear nuevo pedido (nota)
            Pedido pedido = new Pedido();
            pedido.setId(generarIdPedido());
            pedido.setTitulo(txtTituloPedido.getText().trim());
            pedido.setCliente(txtClientePedido.getText().trim());

            // CORRECCIÓN: Usar métodos especiales para fechas/horas
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

    private String generarIdPedido() {
        return "PED-" + System.currentTimeMillis();
    }

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

    private void editarPedidoSeleccionado() {
        Pedido pedido = tablaPedidos.getSelectionModel().getSelectedItem();
        if (pedido != null) {
            editarPedido(pedido);
        } else {
            mostrarAlerta("Error", "Seleccione un pedido para editar", Alert.AlertType.ERROR);
        }
    }

    private void editarPedido(Pedido pedido) {
        // Llenar formulario con datos del pedido
        txtTituloPedido.setText(pedido.getTitulo());
        txtClientePedido.setText(pedido.getCliente());

        // CORRECCIÓN: Usar métodos especiales para fechas/horas
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

    private void actualizarPedido(Pedido pedido) {
        try {
            // Actualizar datos del pedido
            pedido.setTitulo(txtTituloPedido.getText().trim());
            pedido.setCliente(txtClientePedido.getText().trim());

            // CORRECCIÓN: Usar métodos especiales para fechas/horas
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

            // Actualizar tabla
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

    private void eliminarPedidoSeleccionado() {
        Pedido pedido = tablaPedidos.getSelectionModel().getSelectedItem();
        if (pedido != null) {
            eliminarPedido(pedido);
        } else {
            mostrarAlerta("Error", "Seleccione un pedido para eliminar", Alert.AlertType.ERROR);
        }
    }

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

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // ==================== NAVEGACIÓN ====================

    @FXML
    private void abrirVentas() {
        try {
            Main.cambiarVista("/fxml/ventas/ventas.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir ventas", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void abrirProductos() {
        try {
            Main.cambiarVista("/fxml/productos/productos.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir productos", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void abrirInventario() {
        try {
            Main.cambiarVista("/fxml/inventario/inventario.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir inventario", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void abrirClientes() {
        try {
            Main.cambiarVista("/fxml/clientes/clientes.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir clientes", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void abrirRecibos() {
        try {
            Main.cambiarVista("/fxml/recibos/recibos.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir recibos", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void abrirReportes() {
        try {
            Main.cambiarVista("/fxml/reportes/reportes.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir reportes", Alert.AlertType.ERROR);
        }
    }
}