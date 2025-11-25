package com.example.gestion_panaderia.controller;

import com.example.gestion_panaderia.model.Cliente;
import com.example.gestion_panaderia.repository.JsonRepository;
import com.example.gestion_panaderia.service.IClienteService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

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

    // Repositorio y Servicio
    private JsonRepository<Cliente> clienteRepository;
    private IClienteService clienteService;
    private ObservableList<Cliente> clientesList;
    private Cliente clienteSeleccionado;

    public ClienteController() {
        // Inicializar el repositorio JSON
        this.clienteRepository = new JsonRepository<>("clientes.json", Cliente.class);
    }

    @FXML
    public void initialize() {
        inicializar();
    }

    @Override
    public void inicializar() {
        inicializarTabla();
        configurarEventos();
        usuarioActual();
        cargarClientesDesdeJSON();
    }

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

        clientesList = FXCollections.observableArrayList();
        clientesTable.setItems(clientesList);
    }

    private void configurarEventos() {
        // Evento para seleccionar cliente de la tabla
        clientesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> seleccionarCliente(newValue)
        );
    }

    /**
     * Carga los clientes desde el archivo JSON usando el repositorio
     */
    private void cargarClientesDesdeJSON() {
        try {
            // Cargar clientes desde JSON
            List<Cliente> clientes = clienteRepository.cargar();

            // Si no hay clientes, agregar datos de ejemplo
            if (clientes.isEmpty()) {
                System.out.println("No hay clientes. Creando datos de ejemplo...");
                inicializarDatosEjemplo();
                clientes = clienteRepository.cargar();
            }

            // Limpiar la lista actual
            clientesList.clear();

            // Agregar todos los clientes cargados
            clientesList.addAll(clientes);

            System.out.println("Clientes cargados correctamente desde JSON: " + clientes.size());

        } catch (Exception e) {
            System.err.println("Error al cargar clientes desde JSON: " + e.getMessage());
            e.printStackTrace();
            mostrarAlerta("Error al cargar los clientes desde el archivo JSON");
        }
    }

    /**
     * Inicializa el archivo con datos de ejemplo
     */
    private void inicializarDatosEjemplo() {
        List<Cliente> clientesEjemplo = new java.util.ArrayList<>();

        Cliente c1 = new Cliente("C001", "María González", "maria@email.com", "", "555-1234", "");
        c1.setPreferencias("Cliente frecuente, prefiere pan dulce");
        c1.setCalificacion(Cliente.CalificacionCliente.FELIZ);

        Cliente c2 = new Cliente("C002", "Carlos López", "carlos@email.com", "", "555-5678", "");
        c2.setPreferencias("Compra regularmente para eventos");
        c2.setCalificacion(Cliente.CalificacionCliente.NEUTRAL);

        Cliente c3 = new Cliente("C003", "Ana Martínez", "ana@email.com", "", "555-9012", "");
        c3.setPreferencias("Prefiere productos sin azúcar");
        c3.setCalificacion(Cliente.CalificacionCliente.TRISTE);

        clientesEjemplo.add(c1);
        clientesEjemplo.add(c2);
        clientesEjemplo.add(c3);

        clienteRepository.guardar(clientesEjemplo);
        System.out.println("Datos de ejemplo creados exitosamente");
    }

    /**
     * Refresca la tabla cargando nuevamente desde el JSON
     */
    @FXML
    private void refrescarTabla() {
        cargarClientesDesdeJSON();
        limpiarFormulario();
        mostrarMensaje("Actualización", "Lista de clientes actualizada desde el archivo");
    }

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

    @FXML
    private void asignarFeliz() {
        asignarCalificacion(Cliente.CalificacionCliente.FELIZ);
    }

    @FXML
    private void asignarNeutral() {
        asignarCalificacion(Cliente.CalificacionCliente.NEUTRAL);
    }

    @FXML
    private void asignarTriste() {
        asignarCalificacion(Cliente.CalificacionCliente.TRISTE);
    }

    private void asignarCalificacion(Cliente.CalificacionCliente calificacion) {
        if (clienteSeleccionado != null) {
            clienteSeleccionado.setCalificacion(calificacion);
            actualizarCalificacionLabel(calificacion);

            // Actualizar en el repositorio
            List<Cliente> clientes = clienteRepository.cargar();
            for (int i = 0; i < clientes.size(); i++) {
                if (clientes.get(i).getId().equals(clienteSeleccionado.getId())) {
                    clientes.set(i, clienteSeleccionado);
                    break;
                }
            }
            clienteRepository.guardar(clientes);

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

            // Cargar lista actual
            List<Cliente> clientes = clienteRepository.cargar();

            // Verificar que no exista el ID
            boolean existe = clientes.stream()
                    .anyMatch(c -> c.getId().equals(nuevoCliente.getId()));

            if (existe) {
                mostrarAlerta("El ID del cliente ya existe");
                return;
            }

            // Agregar y guardar
            clientes.add(nuevoCliente);
            clienteRepository.guardar(clientes);

            // Agregar a la lista visible
            clientesList.add(nuevoCliente);

            // Guardar en el servicio si está disponible
            if (clienteService != null) {
                clienteService.agregarCliente(nuevoCliente);
            }

            limpiarFormulario();
            mostrarMensaje("Operación Exitosa", "Cliente agregado exitosamente");
        }
    }

    @FXML
    private void guardarCliente() {
        if (clienteSeleccionado != null && validarFormulario()) {
            clienteSeleccionado.setNombre(txtNombre.getText());
            clienteSeleccionado.setTelefono(txtTelefono.getText());
            clienteSeleccionado.setUsuario(txtEmail.getText());
            clienteSeleccionado.setPreferencias(txtPreferencias.getText());

            // Actualizar en el repositorio
            List<Cliente> clientes = clienteRepository.cargar();
            for (int i = 0; i < clientes.size(); i++) {
                if (clientes.get(i).getId().equals(clienteSeleccionado.getId())) {
                    clientes.set(i, clienteSeleccionado);
                    break;
                }
            }
            clienteRepository.guardar(clientes);

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

    @FXML
    private void eliminarCliente() {
        if (clienteSeleccionado != null) {
            // Confirmar eliminación
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Está seguro de eliminar este cliente?");
            confirmacion.setContentText("Cliente: " + clienteSeleccionado.getNombre() + "\nID: " + clienteSeleccionado.getId());

            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                // Eliminar del repositorio
                clienteRepository.eliminar(clienteSeleccionado.getId());

                // Eliminar del servicio si está disponible
                if (clienteService != null) {
                    clienteService.eliminarCliente(clienteSeleccionado.getId());
                }

                // Eliminar de la lista visible
                clientesList.remove(clienteSeleccionado);
                limpiarFormulario();
                mostrarMensaje("Operación Exitosa", "Cliente eliminado exitosamente");
            }
        } else {
            mostrarAlerta("Seleccione un cliente para eliminar");
        }
    }

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

    private void mostrarMensaje(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void usuarioActual() {
        if (usuarioActual != null) {
            usuarioActual.setText("Administrador");
        }
    }

    // Métodos para cambiar entre ventanas
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
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
            stage.setTitle(titulo);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cambiar de ventana: " + rutaFXML);
        }
    }

    // Setter para inyección de dependencias
    public void setClienteService(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Setter para inyección del repositorio (opcional)
    public void setClienteRepository(JsonRepository<Cliente> clienteRepository) {
        this.clienteRepository = clienteRepository;
    }
}