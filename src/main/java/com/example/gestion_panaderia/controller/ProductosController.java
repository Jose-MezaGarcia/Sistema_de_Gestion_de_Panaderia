package com.example.gestion_panaderia.controller;

import com.example.gestion_panaderia.model.Producto;
import com.example.gestion_panaderia.model.Categoria;
import com.example.gestion_panaderia.service.IProductoService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.util.List;
import java.util.Optional;

public class ProductosController implements IController {

    @FXML public Label usuarioActual;

    @FXML private TextField txtBuscarProducto;
    @FXML private ComboBox<String> cmbCategoriaProducto;
    @FXML private ComboBox<String> cmbEstadoStockProducto;
    @FXML private Button btnBuscarProducto, btnLimpiarFiltros;

    @FXML private Label lblTotalGalletas, lblTotalPasteles, lblTotalPanes, lblTotalProductos;

    @FXML private TableView<Producto> tableProductos;
    @FXML private TableColumn<Producto, String> colId;
    @FXML private TableColumn<Producto, String> colCodigo;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;
    @FXML private TableColumn<Producto, String> colEstado;

    @FXML private Button btnAgregarProducto, btnEditarProducto, btnEliminarProducto, btnActualizarStock, btnExportarProductos;

    private IProductoService productoService;
    private ObservableList<Producto> productosObservable;
    private FilteredList<Producto> productosFiltrados;

    @FXML
    public void initialize() {
        inicializar();
    }

    @Override
    public void inicializar() {
        System.out.println("=== INICIALIZANDO CONTROLADOR DE PRODUCTOS ===");

        inicializarComponentes();
        usuarioActual();
        configurarTabla();

        diagnosticarServicioCompleto();

        cargarDatos();
        configurarFiltros();
        actualizarEstadisticas();
    }

    private void diagnosticarServicioCompleto() {
        System.out.println("DIAGNOSTICO COMPLETO DEL SERVICIO");
        System.out.println("   - productoService: " + (productoService != null ? "PRESENTE" : "NULL"));

        if (productoService == null) {
            System.out.println("   SERVICIO NO INYECTADO - configurando directamente...");
            configurarServicioDirectamente();
        } else {
            System.out.println("   SERVICIO INYECTADO CORRECTAMENTE");
            verificarDatosJSON();
        }
    }

    private void configurarServicioDirectamente() {
        try {
            System.out.println("   CREANDO SERVICIO DIRECTAMENTE...");

            com.example.gestion_panaderia.repository.IRepository<Producto> productoRepository =
                    new com.example.gestion_panaderia.repository.JsonRepository<>("productos.json", Producto.class);

            productoService = new com.example.gestion_panaderia.service.ProductoServiceImpl(productoRepository);

            System.out.println("   SERVICIO CREADO DIRECTAMENTE");
            verificarDatosJSON();

        } catch (Exception e) {
            System.err.println("   ERROR CREANDO SERVICIO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void verificarDatosJSON() {
        if (productoService != null) {
            try {
                System.out.println("   VERIFICANDO DATOS DEL JSON...");
                List<Producto> productos = productoService.listarProductos();
                System.out.println("   PRODUCTOS EN JSON: " + productos.size());

                if (productos.isEmpty()) {
                    System.out.println("   EL JSON ESTA VACIO O NO SE ENCONTRARON PRODUCTOS");
                    System.out.println("   VERIFICA QUE productos.json TENGA DATOS VALIDOS");
                } else {
                    System.out.println("   JSON CARGADO CORRECTAMENTE");
                    for (Producto p : productos) {
                        System.out.println("      - " + p.getId() + " | " + p.getNombre() + " | $" + p.getPrecio() + " | Stock: " + p.getStock());
                    }
                }

            } catch (Exception e) {
                System.err.println("   ERROR ACCEDIENDO AL JSON: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void inicializarComponentes() {
        cmbCategoriaProducto.getItems().addAll("Todas", "GALLETAS", "PASTELES", "PANES");
        cmbCategoriaProducto.setValue("Todas");

        cmbEstadoStockProducto.getItems().addAll("Todos", "Stock Optimo", "Stock Bajo", "Sin Stock");
        cmbEstadoStockProducto.setValue("Todos");

        tableProductos.setPlaceholder(new Label("No se encontraron productos"));
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(cellData -> {
            Categoria categoria = cellData.getValue().getCategoria();
            return new SimpleStringProperty(categoria != null ? categoria.getNombre() : "Sin categoria");
        });
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colEstado.setCellValueFactory(cellData -> {
            Producto producto = cellData.getValue();
            return new SimpleStringProperty(obtenerEstadoStock(producto.getStock()));
        });

        colPrecio.setCellFactory(column -> new TableCell<Producto, Double>() {
            @Override
            protected void updateItem(Double precio, boolean empty) {
                super.updateItem(precio, empty);
                if (empty || precio == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", precio));
                }
            }
        });

        colEstado.setCellFactory(column -> new TableCell<Producto, String>() {
            @Override
            protected void updateItem(String estado, boolean empty) {
                super.updateItem(estado, empty);
                if (empty || estado == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(estado);
                    switch (estado) {
                        case "Stock Optimo": setStyle("-fx-text-fill: #28a745; -fx-font-weight: bold;"); break;
                        case "Stock Bajo": setStyle("-fx-text-fill: #ffc107; -fx-font-weight: bold;"); break;
                        case "Sin Stock": setStyle("-fx-text-fill: #dc3545; -fx-font-weight: bold;"); break;
                        default: setStyle("");
                    }
                }
            }
        });
    }

    private void cargarDatos() {
        System.out.println("CARGANDO DATOS EN LA TABLA...");

        if (productoService != null) {
            try {
                List<Producto> productos = productoService.listarProductos();
                System.out.println("   PRODUCTOS A MOSTRAR: " + productos.size());

                if (productos.isEmpty()) {
                    System.out.println("   NO HAY PRODUCTOS - CARGANDO DATOS DE EJEMPLO");
                    cargarDatosEjemplo();
                } else {
                    productosObservable = FXCollections.observableArrayList(productos);
                    tableProductos.setItems(productosObservable);
                    System.out.println("   TABLA ACTUALIZADA CON " + productosObservable.size() + " PRODUCTOS");
                }

            } catch (Exception e) {
                System.err.println("   ERROR CARGANDO DATOS: " + e.getMessage());
                cargarDatosEjemplo();
            }
        } else {
            System.err.println("   SERVICIO NO DISPONIBLE - CARGANDO DATOS DE EJEMPLO");
            cargarDatosEjemplo();
        }
    }

    private void cargarDatosEjemplo() {
        System.out.println("CARGANDO DATOS DE EJEMPLO...");

        productosObservable = FXCollections.observableArrayList();

        productosObservable.add(new Producto("1", "G001", "Galletas Chocolate", 2.50, 20, new Categoria("GALLETAS")));
        productosObservable.add(new Producto("2", "G002", "Galletas Avena", 2.00, 15, new Categoria("GALLETAS")));
        productosObservable.add(new Producto("3", "P001", "Pastel Chocolate", 25.00, 8, new Categoria("PASTELES")));
        productosObservable.add(new Producto("4", "P002", "Pastel Fresa", 22.00, 6, new Categoria("PASTELES")));
        productosObservable.add(new Producto("5", "B001", "Pan Blanco", 1.50, 15, new Categoria("PANES")));
        productosObservable.add(new Producto("6", "B002", "Pan Integral", 2.00, 12, new Categoria("PANES")));

        tableProductos.setItems(productosObservable);
        System.out.println("   DATOS DE EJEMPLO CARGADOS: " + productosObservable.size() + " PRODUCTOS");
    }

    private String obtenerEstadoStock(int stock) {
        if (stock == 0) return "Sin Stock";
        if (stock < 5) return "Stock Bajo";
        return "Stock Optimo";
    }

    private void configurarFiltros() {
        if (productosObservable != null) {
            productosFiltrados = new FilteredList<>(productosObservable);

            txtBuscarProducto.textProperty().addListener((observable, oldValue, newValue) -> aplicarFiltros());
            cmbCategoriaProducto.valueProperty().addListener((observable, oldValue, newValue) -> aplicarFiltros());
            cmbEstadoStockProducto.valueProperty().addListener((observable, oldValue, newValue) -> aplicarFiltros());

            SortedList<Producto> sortedData = new SortedList<>(productosFiltrados);
            sortedData.comparatorProperty().bind(tableProductos.comparatorProperty());
            tableProductos.setItems(sortedData);
        }
    }

    private void aplicarFiltros() {
        if (productosFiltrados != null) {
            productosFiltrados.setPredicate(producto -> {
                String textoBusqueda = txtBuscarProducto.getText().toLowerCase();
                if (!textoBusqueda.isEmpty()) {
                    String nombre = producto.getNombre().toLowerCase();
                    String codigo = producto.getCodigo().toLowerCase();
                    if (!nombre.contains(textoBusqueda) && !codigo.contains(textoBusqueda)) {
                        return false;
                    }
                }

                String categoriaFiltro = cmbCategoriaProducto.getValue();
                if (!"Todas".equals(categoriaFiltro)) {
                    String categoriaProducto = producto.getCategoria().getNombre();
                    if (!categoriaFiltro.equals(categoriaProducto)) {
                        return false;
                    }
                }

                String estadoFiltro = cmbEstadoStockProducto.getValue();
                if (!"Todos".equals(estadoFiltro)) {
                    String estadoProducto = obtenerEstadoStock(producto.getStock());
                    if (!estadoFiltro.equals(estadoProducto)) {
                        return false;
                    }
                }

                return true;
            });
            actualizarEstadisticas();
        }
    }

    private void actualizarEstadisticas() {
        if (productosObservable != null) {
            long totalGalletas = productosObservable.stream()
                    .filter(p -> p.getCategoria() != null && "GALLETAS".equals(p.getCategoria().getNombre()))
                    .count();
            long totalPasteles = productosObservable.stream()
                    .filter(p -> p.getCategoria() != null && "PASTELES".equals(p.getCategoria().getNombre()))
                    .count();
            long totalPanes = productosObservable.stream()
                    .filter(p -> p.getCategoria() != null && "PANES".equals(p.getCategoria().getNombre()))
                    .count();

            lblTotalGalletas.setText(String.valueOf(totalGalletas));
            lblTotalPasteles.setText(String.valueOf(totalPasteles));
            lblTotalPanes.setText(String.valueOf(totalPanes));
            lblTotalProductos.setText(String.valueOf(productosObservable.size()));
        }
    }

    @FXML
    private void buscarProducto() { aplicarFiltros(); }

    @FXML
    private void limpiarFiltros() {
        txtBuscarProducto.clear();
        cmbCategoriaProducto.setValue("Todas");
        cmbEstadoStockProducto.setValue("Todos");
        aplicarFiltros();
    }

    @FXML
    private void agregarProducto() {
        mostrarDialogoProducto(null);
    }

    @FXML
    private void editarProducto() {
        Producto productoSeleccionado = tableProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            mostrarDialogoProducto(productoSeleccionado);
        } else {
            mostrarAlerta("Seleccione un producto para editar");
        }
    }

    @FXML
    private void eliminarProducto() {
        Producto productoSeleccionado = tableProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminacion");
            confirmacion.setHeaderText("Eliminar producto");
            confirmacion.setContentText("¿Esta seguro de que desea eliminar el producto: " + productoSeleccionado.getNombre() + "?");

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                try {
                    if (productoService != null) {
                        productoService.eliminarProducto(productoSeleccionado.getId());
                    }
                    productosObservable.remove(productoSeleccionado);
                    actualizarEstadisticas();
                    mostrarMensaje("Producto eliminado", "El producto se ha eliminado correctamente.");
                } catch (Exception e) {
                    mostrarAlerta("Error al eliminar producto: " + e.getMessage());
                }
            }
        } else {
            mostrarAlerta("Seleccione un producto para eliminar");
        }
    }

    @FXML
    private void actualizarStock() {
        Producto productoSeleccionado = tableProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            TextInputDialog dialog = new TextInputDialog(String.valueOf(productoSeleccionado.getStock()));
            dialog.setTitle("Actualizar Stock");
            dialog.setHeaderText("Actualizar stock de: " + productoSeleccionado.getNombre());
            dialog.setContentText("Nuevo stock:");

            Optional<String> resultado = dialog.showAndWait();
            if (resultado.isPresent()) {
                try {
                    int nuevoStock = Integer.parseInt(resultado.get());
                    productoSeleccionado.setStock(nuevoStock);

                    if (productoService != null) {
                        productoService.actualizarProducto(productoSeleccionado);
                    }

                    tableProductos.refresh();
                    actualizarEstadisticas();
                    mostrarMensaje("Stock actualizado", "El stock se ha actualizado correctamente.");
                } catch (NumberFormatException e) {
                    mostrarAlerta("Por favor ingrese un numero valido para el stock");
                }
            }
        } else {
            mostrarAlerta("Seleccione un producto para actualizar stock");
        }
    }

    @FXML
    private void exportarProductos() {
        mostrarMensaje("Exportar", "Funcionalidad de exportacion en desarrollo...");
    }

    private void mostrarDialogoProducto(Producto producto) {
        Dialog<Producto> dialog = new Dialog<>();
        dialog.setTitle(producto == null ? "Agregar Producto" : "Editar Producto");
        dialog.setHeaderText(producto == null ? "Complete los datos del nuevo producto" :
                "Edite los datos del producto: " + producto.getNombre());

        ButtonType botonGuardar = new ButtonType(producto == null ? "Agregar" : "Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(botonGuardar, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField txtId = new TextField();
        TextField txtCodigo = new TextField();
        TextField txtNombre = new TextField();
        ComboBox<Categoria> cmbCategoria = new ComboBox<>();
        TextField txtPrecio = new TextField();
        TextField txtStock = new TextField();

        cmbCategoria.getItems().addAll(
                new Categoria("GALLETAS"),
                new Categoria("PASTELES"),
                new Categoria("PANES")
        );

        cmbCategoria.setConverter(new StringConverter<Categoria>() {
            @Override
            public String toString(Categoria categoria) {
                return categoria != null ? categoria.getNombre() : "";
            }

            @Override
            public Categoria fromString(String string) {
                return cmbCategoria.getItems().stream()
                        .filter(cat -> cat.getNombre().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        if (producto != null) {
            txtId.setText(producto.getId());
            txtCodigo.setText(producto.getCodigo());
            txtNombre.setText(producto.getNombre());
            cmbCategoria.setValue(producto.getCategoria());
            txtPrecio.setText(String.valueOf(producto.getPrecio()));
            txtStock.setText(String.valueOf(producto.getStock()));
            txtId.setDisable(true);
        } else {
            txtId.setPromptText("ID unico");
            txtCodigo.setPromptText("Codigo del producto");
            txtNombre.setPromptText("Nombre del producto");
            txtPrecio.setPromptText("0.00");
            txtStock.setPromptText("0");
        }

        grid.add(new Label("ID:"), 0, 0);
        grid.add(txtId, 1, 0);
        grid.add(new Label("Codigo:"), 0, 1);
        grid.add(txtCodigo, 1, 1);
        grid.add(new Label("Nombre:"), 0, 2);
        grid.add(txtNombre, 1, 2);
        grid.add(new Label("Categoria:"), 0, 3);
        grid.add(cmbCategoria, 1, 3);
        grid.add(new Label("Precio:"), 0, 4);
        grid.add(txtPrecio, 1, 4);
        grid.add(new Label("Stock:"), 0, 5);
        grid.add(txtStock, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == botonGuardar) {
                try {
                    String id = txtId.getText();
                    String codigo = txtCodigo.getText();
                    String nombre = txtNombre.getText();
                    Categoria categoria = cmbCategoria.getValue();
                    double precio = Double.parseDouble(txtPrecio.getText());
                    int stock = Integer.parseInt(txtStock.getText());

                    if (id.isEmpty() || codigo.isEmpty() || nombre.isEmpty() || categoria == null) {
                        mostrarAlerta("Todos los campos son obligatorios");
                        return null;
                    }

                    Producto nuevoProducto = new Producto(id, codigo, nombre, precio, stock, categoria);

                    if (producto == null) {
                        // Agregar nuevo producto
                        if (productoService != null) {
                            productoService.agregarProducto(nuevoProducto);
                        }
                        productosObservable.add(nuevoProducto);
                        mostrarMensaje("Producto agregado", "El producto se ha agregado correctamente.");
                    } else {
                        // Editar producto existente
                        if (productoService != null) {
                            productoService.actualizarProducto(nuevoProducto);
                        }
                        int index = productosObservable.indexOf(producto);
                        if (index >= 0) {
                            productosObservable.set(index, nuevoProducto);
                        }
                        mostrarMensaje("Producto actualizado", "El producto se ha actualizado correctamente.");
                    }

                    actualizarEstadisticas();
                    return nuevoProducto;

                } catch (NumberFormatException e) {
                    mostrarAlerta("Por favor ingrese valores validos para precio y stock");
                } catch (Exception e) {
                    mostrarAlerta("Error al guardar producto: " + e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
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

    // Metodos de navegacion
    @FXML
    private void abrirVentas(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/ventas/ventas.fxml", "Dulce Tentacion - Ventas");
    }

    @FXML
    private void abrirPedidos(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/pedidos/pedidos.fxml", "Dulce Tentacion - Pedidos");
    }

    @FXML
    private void abrirProductos(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/productos/productos.fxml", "Dulce Tentacion - Productos");
    }

    @FXML
    private void abrirInventario(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/inventario/inventario.fxml", "Dulce Tentacion - Inventario");
    }

    @FXML
    private void abrirClientes(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/clientes/clientes.fxml", "Dulce Tentacion - Clientes");
    }

    @FXML
    private void abrirRecibos(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/recibos/recibos.fxml", "Dulce Tentacion - Recibos");
    }

    @FXML
    private void abrirReportes(javafx.event.ActionEvent event) {
        cambiarVentana(event, "/fxml/reportes/reportes.fxml", "Dulce Tentacion - Reportes");
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

    public void setProductoService(IProductoService productoService) {
        this.productoService = productoService;
        System.out.println("SERVICIO RECIBIDO EN setProductoService");
    }
}