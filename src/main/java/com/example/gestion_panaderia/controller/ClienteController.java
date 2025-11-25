package com.example.gestion_panaderia.controller;

import com.example.gestion_panaderia.model.Cliente;
import com.example.gestion_panaderia.service.IClienteService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controlador de clientes
 * Aquí se maneja la vista de clientes, su tabla y las acciones del formulario
 */
public class ClienteController implements IController {

    // Referencias FXML
    @FXML public Label usuarioActual;
    @FXML public Label calificacionActualLabel;

    // Tabla de clientes
    @FXML private TableView<Cliente> clientesTable;
    @FXML private TableColumn<Cliente, String> colId;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, Cliente.CalificacionCliente> colCalificacion;

    // Campos del formulario
    @FXML private TextField txtNombre;
    @FXML private TextField txtId;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;
    @FXML private TextArea txtPreferencias;

    // Botones de calificación
    @FXML private Button btnFeliz;
    @FXML private Button btnNeutral;
    @FXML private Button btnTriste;

    // Botones de navegación
    @FXML private Button btnVentas;
    @FXML private Button btnPedidos;
    @FXML private Button btnProductos;
    @FXML private Button btnInventario;
    @FXML private Button btnClientes;
    @FXML private Button btnRecibos;
    @FXML private Button btnReportes;

    // Servicio
    private IClienteService clienteService;
    private ObservableList<Cliente> clientesList;
    private Cliente clienteSeleccionado;

    /**
     * Se inicializa el controlador al cargar la vista
     */
    @FXML
    public void initialize() {
        inicializar();
    }

    /**
     * Se inicializan los componentes principales
     * Aquí se prepara la tabla, los eventos y se cargan los clientes
     */
    @Override
    public void inicializar() {
        inicializarTabla();
        configurarEventos();
        usuarioActual();
        cargarClientes();
    }

    /**
     * Se configura la tabla de clientes
     * Se definen las columnas y se personaliza la celda de calificación
     */
    private void inicializarTabla() {
        // Configurar columnas de la tabla
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCalificacion.setCellValueFactory(new PropertyValueFactory<>("calificacion"));

        // Celda personalizada para la calificación
        colCalificacion.setCellFactory(column -> new TableCell<Cliente, Cliente.CalificacionCliente>() {
            @Override
            protected void updateItem(Cliente.CalificacionCliente calificacion, boolean empty) {
                super.updateItem(calificacion, empty);
                if (empty || calificacion == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(calificacion.getEmoji() + " " + (int)(calificacion.getDescuento() * 100) + "%");

                    // Aplicar estilos según la calificación
                    switch (calificacion) {
                        case FELIZ:
                            setStyle("-fx-text-fill: #2E8B57; -fx-font-weight: bold;");
                            break;
                        case NEUTRAL:
                            setStyle("-fx-text-fill: #FF8C00; -fx-font-weight: bold;");
                            break;
                        case TRISTE:
                            setStyle("-fx-text-fill: #DC143C; -fx-font-weight: bold;");
                            break;
                    }
                }
            }
        });

        /**
         * Se inicializa la lista de clientes y se conecta con la tabla
         */
        clientesList = FXCollections.observableArrayList();
        clientesTable.setItems(clientesList);
    }

    /**
     * Se configuran los eventos de la tabla
     * Aquí se detecta cuando se selecciona un cliente
     */
    private void configurarEventos() {
        // Evento para seleccionar cliente de la tabla
        clientesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> seleccionarCliente(newValue)
        );
    }

    /**
     * Se cargan clientes de ejemplo para pruebas
     */
    private void cargarClientes() {
        // Datos de ejemplo para pruebas
        clientesList.addAll(
                crearClienteEjemplo("C001", "María González", "555-1234", "maria@email.com", Cliente.CalificacionCliente.FELIZ),
                crearClienteEjemplo("C002", "Carlos López", "555-5678", "carlos@email.com", Cliente.CalificacionCliente.NEUTRAL),
                crearClienteEjemplo("C003", "Ana Martínez", "555-9012", "ana@email.com", Cliente.CalificacionCliente.TRISTE)
        );
    }

    /**
     * Se crea un cliente de ejemplo con datos básicos y calificación
     */
    private Cliente crearClienteEjemplo(String id, String nombre, String telefono, String email, Cliente.CalificacionCliente calificacion) {
        Cliente cliente = new Cliente(id, nombre, email, "", telefono, "");
        cliente.setPreferencias("Cliente frecuente");
        cliente.setCalificacion(calificacion);
        return cliente;
    }

    /**
     * Se selecciona un cliente de la tabla
     * Se llenan los campos del formulario y se actualiza la calificación
     */
    private void seleccionarCliente(Cliente cliente) {
        this.clienteSeleccionado = cliente;

        if (cliente != null) {
            // Llenar formulario con datos del cliente
            txtId.setText(cliente.getId());
            txtNombre.setText(cliente.getNombre());
            txtTelefono.setText(cliente.getTelefono());
            txtEmail.setText(cliente.getUsuario());
            if (cliente.getPreferencias() != null) {
                txtPreferencias.setText(cliente.getPreferencias());
            } else {
                txtPreferencias.setText("");
            }

            // Actualizar calificación actual
            actualizarCalificacionLabel(cliente.getCalificacion());
        } else {
            limpiarFormulario();
        }
    }

    /**
     * Se asigna calificación FELIZ al cliente seleccionado
     */
    @FXML
    private void asignarFeliz() {
        asignarCalificacion(Cliente.CalificacionCliente.FELIZ);
    }

    /**
     * Se asigna calificación NEUTRAL al cliente seleccionado
     */
    @FXML
    private void asignarNeutral() {
        asignarCalificacion(Cliente.CalificacionCliente.NEUTRAL);
    }

    /**
     * Se asigna calificación TRISTE al cliente seleccionado
     */
    @FXML
    private void asignarTriste() {
        asignarCalificacion(Cliente.CalificacionCliente.TRISTE);
    }

    /**
     * Se asigna la calificación al cliente seleccionado
     * Se actualiza la tabla y se guarda en el servicio si está disponible
     */
    private void asignarCalificacion(Cliente.CalificacionCliente calificacion) {
        if (clienteSeleccionado != null) {
            clienteSeleccionado.setCalificacion(calificacion);
            actualizarCalificacionLabel(calificacion);

            // Actualizar la tabla
            clientesTable.refresh();

            // Guardar en el servicio si está disponible
            if (clienteService != null) {
                clienteService.actualizarCalificacion(clienteSeleccionado.getId(), calificacion);
            }

            mostrarMensaje("Calificación actualizada",
                    "Cliente: " + clienteSeleccionado.getNombre() +
                            "\nCalificación: " + calificacion.getEmoji() +
                            "\nDescuento aplicable: " + (int)(calificacion.getDescuento() * 100) + "%");
        } else {
            mostrarAlerta("Seleccione un cliente primero");
        }
    }

    /**
     * Se cambia el color del label según la calificación
     * Si no hay calificación se muestra "Sin calificar"
     */
    private void actualizarCalificacionLabel(Cliente.CalificacionCliente calificacion) {
        if (calificacion != null) {
            String texto = calificacion.getEmoji() + " - " + (int)(calificacion.getDescuento() * 100) + "% descuento";
            calificacionActualLabel.setText(texto);

            // Cambiar color según calificación
            switch (calificacion) {
                case FELIZ:
                    calificacionActualLabel.setStyle("-fx-text-fill: #2E8B57; -fx-font-weight: bold;");
                    break;
                case NEUTRAL:
                    calificacionActualLabel.setStyle("-fx-text-fill: #FF8C00; -fx-font-weight: bold;");
                    break;
                case TRISTE:
                    calificacionActualLabel.setStyle("-fx-text-fill: #DC143C; -fx-font-weight: bold;");
                    break;
            }
        } else {
            calificacionActualLabel.setText("Sin calificar");
            calificacionActualLabel.setStyle("-fx-text-fill: #5c4033;");
        }
    }

    /**
     * Se agrega un cliente nuevo desde el formulario
     * Se guarda en la lista y en el servicio si está disponible
     */
    @FXML
    private void agregarCliente() {
        if (validarFormulario()) {
            Cliente nuevoCliente = new Cliente(
                    txtId.getText(),
                    txtNombre.getText(),
                    txtEmail.getText(),
                    "",
                    txtTelefono.getText(),
                    ""
            );
            nuevoCliente.setPreferencias(txtPreferencias.getText());
            nuevoCliente.setCalificacion(Cliente.CalificacionCliente.NEUTRAL);

            clientesList.add(nuevoCliente);

            // Guardar en el servicio si está disponible
            if (clienteService != null) {
                clienteService.agregarCliente(nuevoCliente);
            }

            limpiarFormulario();
            mostrarMensaje("Operación Exitosa", "Cliente actualizado exitosamente");
        }
    }

    /**
     * Se guarda la información del cliente seleccionado
     * Se actualiza en la tabla y en el servicio
     */
    @FXML
    private void guardarCliente() {
        if (clienteSeleccionado != null && validarFormulario()) {
            clienteSeleccionado.setNombre(txtNombre.getText());
            clienteSeleccionado.setTelefono(txtTelefono.getText());
            clienteSeleccionado.setUsuario(txtEmail.getText());
            clienteSeleccionado.setPreferencias(txtPreferencias.getText());

            // Guardar en el servicio si está disponible
            if (clienteService != null) {
                clienteService.actualizarCliente(clienteSeleccionado);
            }

            clientesTable.refresh();
            mostrarMensaje("Operación Exitosa", "Cliente actualizado exitosamente");
        } else {
            mostrarAlerta("Seleccione un cliente para editar");
        }
    }

    /**
     * Se elimina el cliente seleccionado
     * Se quita de la lista y del servicio
     */
    @FXML
    private void eliminarCliente() {
        if (clienteSeleccionado != null) {
            // Eliminar del servicio si está disponible
            if (clienteService != null) {
                clienteService.eliminarCliente(clienteSeleccionado.getId());
            }

            clientesList.remove(clienteSeleccionado);
            limpiarFormulario();
            mostrarMensaje("Operación Exitosa", "Cliente actualizado exitosamente");
        } else {
            mostrarAlerta("Seleccione un cliente para eliminar");
        }
    }

    /**
     * Se limpia el formulario y se reinicia la selección
     */
    @FXML
    private void limpiarFormulario() {
        txtId.clear();
        txtNombre.clear();
        txtTelefono.clear();
        txtEmail.clear();
        txtPreferencias.clear();
        clienteSeleccionado = null;
        calificacionActualLabel.setText("Sin calificar");
        calificacionActualLabel.setStyle("-fx-text-fill: #5c4033;");
        clientesTable.getSelectionModel().clearSelection();
    }

    /**
     * Se valida el formulario antes de guardar o agregar
     * Se revisa que ID y nombre no estén vacíos y que el ID no exista
     */
    private boolean validarFormulario() {
        if (txtId.getText().isEmpty() || txtNombre.getText().isEmpty()) {
            mostrarAlerta("ID y Nombre son campos obligatorios");
            return false;
        }

        // Verificar si el ID ya existe (solo para nuevo cliente)
        if (clienteSeleccionado == null) {
            String nuevoId = txtId.getText();
            boolean idExiste = clientesList.stream()
                    .anyMatch(cliente -> cliente.getId().equals(nuevoId));

            if (idExiste) {
                mostrarAlerta("El ID del cliente ya existe");
                return false;
            }
        }

        return true;
    }

    /**
     * Se muestra un mensaje informativo en pantalla
     */
    private void mostrarMensaje(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Se muestra una alerta de advertencia en pantalla
     */
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Se muestra el usuario actual en la vista
     */
    private void usuarioActual() {
        if (usuarioActual != null) {
            usuarioActual.setText("Administrador");
        }
    }

    /**
     * Se abre la ventana de ventas
     */
    // Métodos para cambiar entre ventanas
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

    /**
     * Se cambia de ventana según la ruta y el título
     */
    private void cambiarVentana(javafx.event.ActionEvent event, String rutaFXML, String titulo) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource(rutaFXML));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
            stage.setTitle(titulo);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cambiar de ventana: " + rutaFXML);
        }
    }

    /**
     * Se inyecta el servicio de clientes
     */
    public void setClienteService(IClienteService clienteService) {
        this.clienteService = clienteService;
    }
}