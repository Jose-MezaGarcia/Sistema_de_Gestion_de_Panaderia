package com.example.gestion_panaderia.controller;

import com.example.gestion_panaderia.Main;
import com.example.gestion_panaderia.model.*;
import com.example.gestion_panaderia.service.*;
import com.example.gestion_panaderia.repository.JsonRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InventarioController implements IController {

    // ==================== SERVICIOS ====================
    private IProductoService productoService;

    // ==================== COMPONENTES FXML ====================

    // Búsqueda y filtros
    @FXML private TextField txtBuscarInventario;
    @FXML private ComboBox<String> cmbCategoriaInventario;
    @FXML private ComboBox<String> cmbEstadoStock;

    // Indicadores de stock
    @FXML private Label lblStockOptimo;
    @FXML private Label lblStockBajo;
    @FXML private Label lblSinStock;
    @FXML private Label lblTotalProductos;

    // Tabla de inventario
    @FXML private TableView<Producto> tableInventario;
    @FXML private TableColumn<Producto, String> colCodigoInv;
    @FXML private TableColumn<Producto, String> colProductoInv;
    @FXML private TableColumn<Producto, String> colCategoriaInv;
    @FXML private TableColumn<Producto, Integer> colStockActual;
    @FXML private TableColumn<Producto, Integer> colStockMinimo;
    @FXML private TableColumn<Producto, String> colEstadoInv;

    // Botones de acción
    @FXML private Button btnBuscarInventario;
    @FXML private Button btnAgregarProducto;
    @FXML private Button btnEditarInventario;
    @FXML private Button btnActualizarStock;
    @FXML private Button btnEliminarProducto;
    @FXML private Button btnExportarInventario;

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
    private ObservableList<Producto> inventarioData;
    private FilteredList<Producto> inventarioFiltrado;

    // ==================== CONSTANTES ====================
    private static final int STOCK_MINIMO = 10;
    private static final int STOCK_BAJO_LIMITE = 5;

    // ==================== MÉTODOS DE INICIALIZACIÓN ====================

    /**
     * Se inicializa el controlador al cargar la vista
     */
    @FXML
    public void initialize() {
        inicializar();
    }

    /**
     * Se inicializan los servicios, datos y listas filtradas
     * Aquí se prepara el repositorio y la colección observable
     */
    @Override
    public void inicializar() {
        // Inicializar servicios con inyección de dependencias
        JsonRepository<Producto> productoRepo = new JsonRepository<>("materias_primas.json", Producto.class);
        productoService = new ProductoServiceImpl(productoRepo);

        // Inicializar datos
        inventarioData = FXCollections.observableArrayList();
        inventarioFiltrado = new FilteredList<>(inventarioData);

        // Configurar componentes
        configurarControles();
        configurarTabla();
        configurarEventos();
        configurarNavegacion();

        // Cargar datos iniciales
        cargarInventario();
        actualizarEstadisticas();

        usuarioActual.setText("Administrador");
    }

    /**
     * Se configuran los controles de la vista
     * Aquí se llenan los ComboBox de categorías y estado de stock
     */
    private void configurarControles() {
        // Configurar ComboBox de categorías para materias primas
        List<String> categoriasMateriasPrimas = List.of(
                "Todas",
                "Harinas",
                "Azúcares y Endulzantes",
                "Lácteos",
                "Huevos",
                "Grasas y Aceites",
                "Levaduras",
                "Frutos Secos",
                "Frutas",
                "Chocolates",
                "Especias",
                "Conservantes"
        );

        cmbCategoriaInventario.setItems(FXCollections.observableArrayList(categoriasMateriasPrimas));
        cmbCategoriaInventario.setValue("Todas");

        // Configurar ComboBox de estado de stock
        cmbEstadoStock.setItems(FXCollections.observableArrayList(
                "Todos", "Óptimo", "Bajo", "Sin Stock"
        ));
        cmbEstadoStock.setValue("Todos");
    }

    /**
     * Se configura la tabla de inventario
     * Se definen las columnas y se aplica estilo según estado de stock
     */
    private void configurarTabla() {
        colCodigoInv.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProductoInv.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoriaInv.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getCategoria().getNombre()));
        colStockActual.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colStockMinimo.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(STOCK_MINIMO).asObject());

        colEstadoInv.setCellValueFactory(cellData -> {
            int stock = cellData.getValue().getStock();
            String estado = determinarEstadoStock(stock);
            return new javafx.beans.property.SimpleStringProperty(estado);
        });

        tableInventario.setItems(inventarioFiltrado);

        // Configurar estilo de celdas según estado de stock
        colEstadoInv.setCellFactory(column -> new TableCell<Producto, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item) {
                        case "Óptimo":
                            setStyle("-fx-text-fill: #28a745; -fx-font-weight: bold;");
                            break;
                        case "Bajo":
                            setStyle("-fx-text-fill: #ffc107; -fx-font-weight: bold;");
                            break;
                        case "Sin Stock":
                            setStyle("-fx-text-fill: #dc3545; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });
    }

    /**
     * Se configuran los eventos de la vista
     * Aquí se definen las acciones de búsqueda y de inventario
     */
    private void configurarEventos() {
        // Búsqueda y filtros
        btnBuscarInventario.setOnAction(event -> buscarInventario());
        txtBuscarInventario.setOnAction(event -> buscarInventario());
        txtBuscarInventario.textProperty().addListener((obs, oldVal, newVal) -> buscarInventario());
        cmbCategoriaInventario.setOnAction(event -> buscarInventario());
        cmbEstadoStock.setOnAction(event -> buscarInventario());

        // Acciones de inventario
        btnAgregarProducto.setOnAction(event -> mostrarDialogoAgregarProducto());
        btnEditarInventario.setOnAction(event -> mostrarDialogoEditarProducto());
        btnActualizarStock.setOnAction(event -> actualizarStock());
        btnEliminarProducto.setOnAction(event -> eliminarProducto());
        btnExportarInventario.setOnAction(event -> exportarInventario());
    }

    /**
     * Se configuran los botones de navegación
     * Aquí se definen las acciones para abrir cada ventana
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
     * Se busca en el inventario según texto, categoría y estado seleccionados
     * Se filtra la lista observable con los criterios
     */
    @FXML
    private void buscarInventario() {
        String textoBusqueda = txtBuscarInventario.getText().toLowerCase();
        String categoriaSeleccionada = cmbCategoriaInventario.getValue();
        String estadoSeleccionado = cmbEstadoStock.getValue();

        inventarioFiltrado.setPredicate(producto -> {
            boolean coincideBusqueda = textoBusqueda.isEmpty() ||
                    producto.getNombre().toLowerCase().contains(textoBusqueda) ||
                    producto.getId().toLowerCase().contains(textoBusqueda);

            boolean coincideCategoria = categoriaSeleccionada.equals("Todas") ||
                    producto.getCategoria().getNombre().equals(categoriaSeleccionada);

            boolean coincideEstado = estadoSeleccionado.equals("Todos") ||
                    determinarEstadoStock(producto.getStock()).equals(estadoSeleccionado);

            return coincideBusqueda && coincideCategoria && coincideEstado;
        });
    }

    /**
     * Se muestra un diálogo para agregar una nueva materia prima
     * Se construye un formulario con código, nombre, precio, stock y categoría
     * Se valida la entrada y se crea un producto si todo está correcto
     * Se guarda en el servicio y se actualiza el inventario
     */
    private void mostrarDialogoAgregarProducto() {
        Dialog<Producto> dialog = new Dialog<>();
        dialog.setTitle("Agregar Materia Prima");
        dialog.setHeaderText("Ingrese los datos de la nueva materia prima");

        // Configurar botones
        ButtonType agregarButtonType = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(agregarButtonType, ButtonType.CANCEL);

        // Crear formulario
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtCodigo = new TextField();
        txtCodigo.setPromptText("MP001");
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Harina de Trigo");
        TextField txtPrecio = new TextField();
        txtPrecio.setPromptText("25.50");
        TextField txtStock = new TextField();
        txtStock.setPromptText("100");

        ComboBox<String> cbCategoria = new ComboBox<>();
        cbCategoria.getItems().addAll(
                "Harinas",
                "Azúcares y Endulzantes",
                "Lácteos",
                "Huevos",
                "Grasas y Aceites",
                "Levaduras",
                "Frutos Secos",
                "Frutas",
                "Chocolates",
                "Especias",
                "Conservantes"
        );
        cbCategoria.setPromptText("Seleccione categoría");

        grid.add(new Label("Código:"), 0, 0);
        grid.add(txtCodigo, 1, 0);
        grid.add(new Label("Nombre:"), 0, 1);
        grid.add(txtNombre, 1, 1);
        grid.add(new Label("Precio por Kg/Lt:"), 0, 2);
        grid.add(txtPrecio, 1, 2);
        grid.add(new Label("Stock (Kg/Lt):"), 0, 3);
        grid.add(txtStock, 1, 3);
        grid.add(new Label("Categoría:"), 0, 4);
        grid.add(cbCategoria, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Convertir resultado
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == agregarButtonType) {
                try {
                    String codigo = txtCodigo.getText().trim();
                    String nombre = txtNombre.getText().trim();
                    double precio = Double.parseDouble(txtPrecio.getText().trim());
                    int stock = Integer.parseInt(txtStock.getText().trim());
                    String categoriaNombre = cbCategoria.getValue();

                    if (codigo.isEmpty() || nombre.isEmpty() || categoriaNombre == null) {
                        throw new IllegalArgumentException("Todos los campos son obligatorios");
                    }

                    if (precio < 0 || stock < 0) {
                        throw new IllegalArgumentException("Precio y stock no pueden ser negativos");
                    }

                    Categoria categoria = new Categoria(categoriaNombre);
                    return new Producto(codigo, nombre, precio, stock, categoria);
                } catch (NumberFormatException e) {
                    mostrarAlerta("Error", "Precio y stock deben ser números válidos", Alert.AlertType.ERROR);
                    return null;
                } catch (IllegalArgumentException e) {
                    mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
                    return null;
                }
            }
            return null;
        });

        Optional<Producto> result = dialog.showAndWait();
        result.ifPresent(producto -> {
            productoService.agregarProducto(producto);
            cargarInventario();
            actualizarEstadisticas();
            mostrarAlerta("Éxito", "Materia prima agregada correctamente", Alert.AlertType.INFORMATION);
        });
    }

    /**
     * Se muestra un diálogo para editar una materia prima seleccionada
     * Se cargan los datos actuales en el formulario
     * Se valida la entrada y se actualiza el producto si todo está correcto
     * Se guarda en el servicio y se refresca la tabla
     */
    private void mostrarDialogoEditarProducto() {
        Producto productoSeleccionado = tableInventario.getSelectionModel().getSelectedItem();
        if (productoSeleccionado == null) {
            mostrarAlerta("Error", "Seleccione una materia prima para editar", Alert.AlertType.ERROR);
            return;
        }

        Dialog<Producto> dialog = new Dialog<>();
        dialog.setTitle("Editar Materia Prima");
        dialog.setHeaderText("Editando: " + productoSeleccionado.getNombre());

        // Configurar botones
        ButtonType editarButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(editarButtonType, ButtonType.CANCEL);

        // Crear formulario con datos actuales
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtCodigo = new TextField(productoSeleccionado.getId());
        txtCodigo.setEditable(false);
        TextField txtNombre = new TextField(productoSeleccionado.getNombre());
        TextField txtPrecio = new TextField(String.valueOf(productoSeleccionado.getPrecio()));
        TextField txtStock = new TextField(String.valueOf(productoSeleccionado.getStock()));

        ComboBox<String> cbCategoria = new ComboBox<>();
        cbCategoria.getItems().addAll(
                "Harinas",
                "Azúcares y Endulzantes",
                "Lácteos",
                "Huevos",
                "Grasas y Aceites",
                "Levaduras",
                "Frutos Secos",
                "Frutas",
                "Chocolates",
                "Especias",
                "Conservantes"
        );
        cbCategoria.setValue(productoSeleccionado.getCategoria().getNombre());

        grid.add(new Label("Código:"), 0, 0);
        grid.add(txtCodigo, 1, 0);
        grid.add(new Label("Nombre:"), 0, 1);
        grid.add(txtNombre, 1, 1);
        grid.add(new Label("Precio por Kg/Lt:"), 0, 2);
        grid.add(txtPrecio, 1, 2);
        grid.add(new Label("Stock (Kg/Lt):"), 0, 3);
        grid.add(txtStock, 1, 3);
        grid.add(new Label("Categoría:"), 0, 4);
        grid.add(cbCategoria, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Convertir resultado
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == editarButtonType) {
                try {
                    String nombre = txtNombre.getText().trim();
                    double precio = Double.parseDouble(txtPrecio.getText().trim());
                    int stock = Integer.parseInt(txtStock.getText().trim());
                    String categoriaNombre = cbCategoria.getValue();

                    if (nombre.isEmpty() || categoriaNombre == null) {
                        throw new IllegalArgumentException("Todos los campos son obligatorios");
                    }

                    if (precio < 0 || stock < 0) {
                        throw new IllegalArgumentException("Precio y stock no pueden ser negativos");
                    }

                    productoSeleccionado.setNombre(nombre);
                    productoSeleccionado.setPrecio(precio);
                    productoSeleccionado.setStock(stock);
                    productoSeleccionado.setCategoria(new Categoria(categoriaNombre));

                    return productoSeleccionado;
                } catch (NumberFormatException e) {
                    mostrarAlerta("Error", "Precio y stock deben ser números válidos", Alert.AlertType.ERROR);
                    return null;
                } catch (IllegalArgumentException e) {
                    mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
                    return null;
                }
            }
            return null;
        });// aqui llegue x2

        Optional<Producto> result = dialog.showAndWait();
        result.ifPresent(producto -> {
            productoService.actualizarProducto(producto);
            tableInventario.refresh();
            actualizarEstadisticas();
            mostrarAlerta("Éxito", "Materia prima actualizada correctamente", Alert.AlertType.INFORMATION);
        });
    }

    /**
     * Se actualiza el stock de la materia prima seleccionada
     * Se pide el nuevo valor y se valida que no sea negativo
     * Se guarda en el servicio y se refresca la tabla
     */
    @FXML
    private void actualizarStock() {
        Producto productoSeleccionado = tableInventario.getSelectionModel().getSelectedItem();
        if (productoSeleccionado == null) {
            mostrarAlerta("Error", "Seleccione una materia prima para actualizar stock", Alert.AlertType.ERROR);
            return;
        }

        TextInputDialog dialog = new TextInputDialog(String.valueOf(productoSeleccionado.getStock()));
        dialog.setTitle("Actualizar Stock");
        dialog.setHeaderText("Actualizar stock de: " + productoSeleccionado.getNombre());
        dialog.setContentText("Nuevo stock (Kg/Lt):");

        dialog.showAndWait().ifPresent(nuevoStock -> {
            try {
                int stock = Integer.parseInt(nuevoStock);
                if (stock < 0) {
                    mostrarAlerta("Error", "El stock no puede ser negativo", Alert.AlertType.ERROR);
                    return;
                }

                productoSeleccionado.setStock(stock);
                productoService.actualizarProducto(productoSeleccionado);
                tableInventario.refresh();
                actualizarEstadisticas();
                mostrarAlerta("Éxito", "Stock actualizado correctamente", Alert.AlertType.INFORMATION);
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "Ingrese un número válido", Alert.AlertType.ERROR);
            }
        });
    }

    /**
     * Se elimina la materia prima seleccionada
     * Se pide confirmación antes de quitarla de la lista
     * Se actualizan las estadísticas después de eliminar
     */
    @FXML
    private void eliminarProducto() {
        Producto productoSeleccionado = tableInventario.getSelectionModel().getSelectedItem();
        if (productoSeleccionado == null) {
            mostrarAlerta("Error", "Seleccione una materia prima para eliminar", Alert.AlertType.ERROR);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro de eliminar: " + productoSeleccionado.getNombre() + "?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");

        if (confirmacion.showAndWait().get() == ButtonType.OK) {
            inventarioData.remove(productoSeleccionado);
            // En un sistema real, llamaríamos a: productoService.eliminarProducto(productoSeleccionado.getId());
            actualizarEstadisticas();
            mostrarAlerta("Éxito", "Materia prima eliminada correctamente", Alert.AlertType.INFORMATION);
        }
    }

    /**
     * Se exporta el inventario
     * Por ahora solo muestra un mensaje informativo
     */
    @FXML
    private void exportarInventario() {
        mostrarAlerta("Información", "Funcionalidad de exportación a Excel en desarrollo",
                Alert.AlertType.INFORMATION);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Se cargan los productos desde el servicio
     * Se llenan los datos en la lista observable
     */
    private void cargarInventario() {
        try {
            List<Producto> productos = productoService.listarProductos();
            inventarioData.setAll(productos);
        } catch (Exception e) {
            System.err.println("Error al cargar inventario: " + e.getMessage());
            mostrarAlerta("Error", "No se pudieron cargar las materias primas", Alert.AlertType.ERROR);
        }
    }

    /**
     * Se actualizan las estadísticas de stock
     * Se cuentan productos óptimos, bajos, sin stock y el total
     */
    private void actualizarEstadisticas() {
        long stockOptimo = inventarioData.stream()
                .filter(p -> p.getStock() > STOCK_BAJO_LIMITE)
                .count();
        long stockBajo = inventarioData.stream()
                .filter(p -> p.getStock() > 0 && p.getStock() <= STOCK_BAJO_LIMITE)
                .count();
        long sinStock = inventarioData.stream()
                .filter(p -> p.getStock() == 0)
                .count();
        long totalProductos = inventarioData.size();

        lblStockOptimo.setText(String.valueOf(stockOptimo));
        lblStockBajo.setText(String.valueOf(stockBajo));
        lblSinStock.setText(String.valueOf(sinStock));
        lblTotalProductos.setText(String.valueOf(totalProductos));
    }


    /**
     * Se determina el estado del stock según el número
     * Devuelve Óptimo, Bajo o Sin Stock
     */
    private String determinarEstadoStock(int stock) {
        if (stock == 0) {
            return "Sin Stock";
        } else if (stock <= STOCK_BAJO_LIMITE) {
            return "Bajo";
        } else {
            return "Óptimo";
        }
    }

    /**
     * Se muestra una alerta en pantalla
     * Se usa para errores, información o confirmaciones
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
     * Se abre la ventana de ventas
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
     * Se abre la ventana de pedidos
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
     * Se abre la ventana de productos
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
     * Se abre la ventana de inventario
     * Aquí no hace nada porque ya estamos en inventario
     */
    @FXML
    private void abrirInventario() {
        // Ya estamos en inventario
    }

    /**
     * Se abre la ventana de clientes
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
     * Se abre la ventana de recibos
     */
    @FXML
    private void abrirRecibos() {
        try {
            Main.cambiarVista("/fxml/recibos/recibos.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir recibos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Se abre la ventana de reportes
     */
    @FXML
    private void abrirReportes() {
        try {
            Main.cambiarVista("/fxml/reportes/reportes.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir reportes: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Se deja preparado el método para agregar producto desde acción
     */
    public void agregarProducto(ActionEvent actionEvent) {
    }

    /**
     * Se deja preparado el método para editar inventario desde acción
     */
    public void editarInventario(ActionEvent actionEvent) {
    }
}