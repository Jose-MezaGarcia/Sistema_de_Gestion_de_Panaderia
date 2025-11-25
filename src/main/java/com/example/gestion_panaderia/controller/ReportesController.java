package com.example.gestion_panaderia.controller;

import com.example.gestion_panaderia.Main;
import com.example.gestion_panaderia.model.*;
import com.example.gestion_panaderia.service.*;
import com.example.gestion_panaderia.repository.JsonRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ReportesController implements IController {

    private IVentaService ventaService;
    private IProductoService productoService;


    // Filtros
    @FXML private ComboBox<String> cbTipoReporte;
    @FXML private ComboBox<String> cbPeriodo;
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    @FXML private ComboBox<String> cbCategoria;
    @FXML private ComboBox<String> cbProductoFiltro;

    // Botones
    @FXML private Button btnGenerar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnExportarExcel;
    @FXML private Button btnImprimir;

    // Tarjetas de resumen
    @FXML private Label lblTotalVentas;
    @FXML private Label lblPeriodoVentas;
    @FXML private Label lblCantidadVentas;
    @FXML private Label lblPeriodoCantidad;
    @FXML private Label lblProductoTop;
    @FXML private Label lblCantidadTop;
    @FXML private Label lblPromedioVenta;
    @FXML private Label lblTotalRegistros;
    @FXML private Label lblTotalGeneral;

    // Gráfica
    @FXML private BarChart<String, Number> chartVentas;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;

    // Tablas
    @FXML private TableView<ProductoReporte> tablaTopProductos;
    @FXML private TableColumn<ProductoReporte, Integer> colRanking;
    @FXML private TableColumn<ProductoReporte, String> colProductoTop;
    @FXML private TableColumn<ProductoReporte, Integer> colCantidadVendida;
    @FXML private TableColumn<ProductoReporte, Double> colIngresoTotal;

    @FXML private TableView<CategoriaReporte> tablaCategorias;
    @FXML private TableColumn<CategoriaReporte, String> colCategoria;
    @FXML private TableColumn<CategoriaReporte, Double> colTotalVendido;
    @FXML private TableColumn<CategoriaReporte, String> colPorcentaje;

    @FXML private TableView<Venta> tablaVentasDetalle;
    @FXML private TableColumn<Venta, String> colFecha;
    @FXML private TableColumn<Venta, String> colTicket;
    @FXML private TableColumn<Venta, String> colCliente;
    @FXML private TableColumn<Venta, String> colProductos;
    @FXML private TableColumn<Venta, Integer> colCantidadItems;
    @FXML private TableColumn<Venta, String> colMetodoPago;
    @FXML private TableColumn<Venta, Double> colTotal;
    @FXML private TableColumn<Venta, String> colEstado;

    // Navegación
    @FXML private Button btnVentas;
    @FXML private Button btnPedidos;
    @FXML private Button btnProductos;
    @FXML private Button btnInventario;
    @FXML private Button btnClientes;
    @FXML private Button btnRecibos;
    @FXML private Button btnReportes;
    @FXML private Label usuarioActual;


    private ObservableList<Venta> ventasData;
    private ObservableList<ProductoReporte> productosReporteData;
    private ObservableList<CategoriaReporte> categoriasReporteData;



    @FXML
    public void initialize() {
        inicializar();
    }

    @Override
    public void inicializar() {
        // Inicializar servicios con inyección de dependencias
        JsonRepository<Venta> ventaRepo = new JsonRepository<>("ventas.json", Venta.class);
        JsonRepository<Producto> productoRepo = new JsonRepository<>("productos.json", Producto.class);

        ventaService = new VentaServiceImpl(ventaRepo);
        productoService = new ProductoServiceImpl(productoRepo);

        // Inicializar datos
        ventasData = FXCollections.observableArrayList();
        productosReporteData = FXCollections.observableArrayList();
        categoriasReporteData = FXCollections.observableArrayList();

        // Configurar componentes
        configurarControles();
        configurarTablas();
        configurarEventos();
        configurarNavegacion();

        // Generar reporte inicial
        generarReporte();

        usuarioActual.setText("Administrador");
    }

    private void configurarControles() {
        // Configurar tipos de reporte
        cbTipoReporte.setItems(FXCollections.observableArrayList(
                "Ventas Diarias", "Ventas Semanales", "Ventas Mensuales",
                "Productos Más Vendidos", "Ventas por Categoría", "Reporte General"
        ));
        cbTipoReporte.setValue("Reporte General");

        // Configurar periodos
        cbPeriodo.setItems(FXCollections.observableArrayList(
                "Hoy", "Esta Semana", "Este Mes", "Este Año", "Personalizado"
        ));
        cbPeriodo.setValue("Este Mes");

        // Configurar fechas por defecto
        dpFechaInicio.setValue(LocalDate.now().withDayOfMonth(1));
        dpFechaFin.setValue(LocalDate.now());

        // Cargar categorías y productos para filtros
        cargarCategorias();
        cargarProductosFiltro();
    }

    private void cargarCategorias() {
        try {
            List<Producto> productos = productoService.listarProductos();
            Set<String> categorias = productos.stream()
                    .map(p -> p.getCategoria().getNombre())
                    .collect(Collectors.toSet());

            List<String> categoriasList = new ArrayList<>(categorias);
            categoriasList.add(0, "Todas las categorías");
            cbCategoria.setItems(FXCollections.observableArrayList(categoriasList));
            cbCategoria.setValue("Todas las categorías");
        } catch (Exception e) {
            System.err.println("Error al cargar categorías: " + e.getMessage());
        }
    }

    private void cargarProductosFiltro() {
        try {
            List<Producto> productos = productoService.listarProductos();
            List<String> nombresProductos = productos.stream()
                    .map(Producto::getNombre)
                    .collect(Collectors.toList());

            List<String> productosList = new ArrayList<>(nombresProductos);
            productosList.add(0, "Todos los productos");
            cbProductoFiltro.setItems(FXCollections.observableArrayList(productosList));
            cbProductoFiltro.setValue("Todos los productos");
        } catch (Exception e) {
            System.err.println("Error al cargar productos: " + e.getMessage());
        }
    }

    private void configurarTablas() {
        // Tabla Top Productos
        colRanking.setCellValueFactory(new PropertyValueFactory<>("ranking"));
        colProductoTop.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colCantidadVendida.setCellValueFactory(new PropertyValueFactory<>("cantidadVendida"));
        colIngresoTotal.setCellValueFactory(new PropertyValueFactory<>("ingresoTotal"));
        tablaTopProductos.setItems(productosReporteData);

        // Tabla Categorías
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colTotalVendido.setCellValueFactory(new PropertyValueFactory<>("totalVendido"));
        colPorcentaje.setCellValueFactory(new PropertyValueFactory<>("porcentaje"));
        tablaCategorias.setItems(categoriasReporteData);

        // Tabla Detalle Ventas
        colFecha.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getFecha().format(formatter));
        });
        colTicket.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCliente.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty("Cliente General"));
        colProductos.setCellValueFactory(cellData -> {
            String productos = cellData.getValue().getDetalles().stream()
                    .map(d -> d.getProducto().getNombre())
                    .collect(Collectors.joining(", "));
            return new javafx.beans.property.SimpleStringProperty(productos);
        });
        colCantidadItems.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(
                        cellData.getValue().getDetalles().size()).asObject());
        colMetodoPago.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty("Efectivo"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colEstado.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty("Completada"));
        tablaVentasDetalle.setItems(ventasData);
    }

    private void configurarEventos() {
        // Botones principales
        btnGenerar.setOnAction(event -> generarReporte());
        btnLimpiar.setOnAction(event -> limpiarFiltros());
        btnExportarExcel.setOnAction(event -> exportarExcel());
        btnImprimir.setOnAction(event -> imprimirReporte());

        // Cambio de periodo
        cbPeriodo.setOnAction(event -> actualizarFechasPorPeriodo());
    }

    private void configurarNavegacion() {
        btnVentas.setOnAction(event -> abrirVentas());
        btnPedidos.setOnAction(event -> abrirPedidos());
        btnProductos.setOnAction(event -> abrirProductos());
        btnInventario.setOnAction(event -> abrirInventario());
        btnClientes.setOnAction(event -> abrirClientes());
        btnRecibos.setOnAction(event -> abrirRecibos());
        btnReportes.setOnAction(event -> abrirReportes());
    }

    @FXML
    private void generarReporte() {
        try {
            LocalDate fechaInicio = dpFechaInicio.getValue();
            LocalDate fechaFin = dpFechaFin.getValue();

            if (fechaInicio == null || fechaFin == null) {
                mostrarAlerta("Error", "Seleccione las fechas", Alert.AlertType.ERROR);
                return;
            }

            if (fechaInicio.isAfter(fechaFin)) {
                mostrarAlerta("Error", "La fecha de inicio no puede ser posterior a la fecha fin", Alert.AlertType.ERROR);
                return;
            }

            List<Venta> ventasFiltradas = filtrarVentasPorFecha(fechaInicio, fechaFin);
            ventasData.setAll(ventasFiltradas);

            actualizarResumenes(ventasFiltradas);
            generarGrafica(ventasFiltradas);
            generarTopProductos(ventasFiltradas);
            generarVentasPorCategoria(ventasFiltradas);

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al generar reporte: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private List<Venta> filtrarVentasPorFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        try {
            return ventaService.listarVentas().stream()
                    .filter(venta -> {
                        LocalDate fechaVenta = venta.getFecha().toLocalDate();
                        return !fechaVenta.isBefore(fechaInicio) && !fechaVenta.isAfter(fechaFin);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error al filtrar ventas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void actualizarFechasPorPeriodo() {
        String periodo = cbPeriodo.getValue();
        LocalDate hoy = LocalDate.now();

        switch (periodo) {
            case "Hoy":
                dpFechaInicio.setValue(hoy);
                dpFechaFin.setValue(hoy);
                break;
            case "Esta Semana":
                dpFechaInicio.setValue(hoy.minusDays(hoy.getDayOfWeek().getValue() - 1));
                dpFechaFin.setValue(hoy);
                break;
            case "Este Mes":
                dpFechaInicio.setValue(hoy.withDayOfMonth(1));
                dpFechaFin.setValue(hoy);
                break;
            case "Este Año":
                dpFechaInicio.setValue(hoy.withDayOfYear(1));
                dpFechaFin.setValue(hoy);
                break;
        }
    }

    private void actualizarResumenes(List<Venta> ventas) {
        double totalVentas = ventas.stream().mapToDouble(Venta::getTotal).sum();
        int cantidadVentas = ventas.size();
        double promedioVenta = cantidadVentas > 0 ? totalVentas / cantidadVentas : 0;

        lblTotalVentas.setText(String.format("$%.2f", totalVentas));
        lblCantidadVentas.setText(String.valueOf(cantidadVentas));
        lblPromedioVenta.setText(String.format("$%.2f", promedioVenta));
        lblTotalRegistros.setText(String.valueOf(cantidadVentas));
        lblTotalGeneral.setText(String.format("$%.2f", totalVentas));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        lblPeriodoVentas.setText(dpFechaInicio.getValue().format(formatter) + " - " +
                dpFechaFin.getValue().format(formatter));
        lblPeriodoCantidad.setText("Transacciones realizadas");

        // Producto más vendido
        if (!ventas.isEmpty()) {
            Map<String, Integer> ventasPorProducto = new HashMap<>();
            for (Venta venta : ventas) {
                for (DetalleVenta detalle : venta.getDetalles()) {
                    String nombreProducto = detalle.getProducto().getNombre();
                    ventasPorProducto.merge(nombreProducto, detalle.getCantidad(), Integer::sum);
                }
            }

            if (!ventasPorProducto.isEmpty()) {
                Map.Entry<String, Integer> topProducto = ventasPorProducto.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .get();

                lblProductoTop.setText(topProducto.getKey());
                lblCantidadTop.setText(topProducto.getValue() + " unidades");
            }
        }
    }

    private void generarGrafica(List<Venta> ventas) {
        chartVentas.getData().clear();

        if (ventas.isEmpty()) {
            return;
        }

        // Agrupar ventas por día
        Map<LocalDate, Double> ventasPorDia = ventas.stream()
                .collect(Collectors.groupingBy(
                        venta -> venta.getFecha().toLocalDate(),
                        Collectors.summingDouble(Venta::getTotal)
                ));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Ventas por Día");

        ventasPorDia.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String fechaStr = entry.getKey().format(DateTimeFormatter.ofPattern("dd/MM"));
                    series.getData().add(new XYChart.Data<>(fechaStr, entry.getValue()));
                });

        chartVentas.getData().add(series);
    }

    private void generarTopProductos(List<Venta> ventas) {
        Map<String, ProductoReporte> reporteProductos = new HashMap<>();

        for (Venta venta : ventas) {
            for (DetalleVenta detalle : venta.getDetalles()) {
                Producto producto = detalle.getProducto();
                String clave = producto.getId() + "|" + producto.getNombre();

                ProductoReporte reporte = reporteProductos.getOrDefault(clave,
                        new ProductoReporte(producto.getNombre(), 0, 0.0));

                reporte.setCantidadVendida(reporte.getCantidadVendida() + detalle.getCantidad());
                reporte.setIngresoTotal(reporte.getIngresoTotal() + detalle.getSubtotal());

                reporteProductos.put(clave, reporte);
            }
        }

        List<ProductoReporte> topProductos = reporteProductos.values().stream()
                .sorted((p1, p2) -> Integer.compare(p2.getCantidadVendida(), p1.getCantidadVendida()))
                .limit(10)
                .collect(Collectors.toList());

        // Asignar ranking
        for (int i = 0; i < topProductos.size(); i++) {
            topProductos.get(i).setRanking(i + 1);
        }

        productosReporteData.setAll(topProductos);
    }

    private void generarVentasPorCategoria(List<Venta> ventas) {
        Map<String, Double> ventasPorCategoria = new HashMap<>();
        double totalGeneral = ventas.stream().mapToDouble(Venta::getTotal).sum();

        for (Venta venta : ventas) {
            for (DetalleVenta detalle : venta.getDetalles()) {
                String categoria = detalle.getProducto().getCategoria().getNombre();
                ventasPorCategoria.merge(categoria, detalle.getSubtotal(), Double::sum);
            }
        }

        List<CategoriaReporte> categoriasReporte = ventasPorCategoria.entrySet().stream()
                .map(entry -> {
                    double porcentaje = totalGeneral > 0 ? (entry.getValue() / totalGeneral) * 100 : 0;
                    return new CategoriaReporte(entry.getKey(), entry.getValue(),
                            String.format("%.1f%%", porcentaje));
                })
                .sorted((c1, c2) -> Double.compare(c2.getTotalVendido(), c1.getTotalVendido()))
                .collect(Collectors.toList());

        categoriasReporteData.setAll(categoriasReporte);
    }

    @FXML
    private void limpiarFiltros() {
        cbTipoReporte.setValue("Reporte General");
        cbPeriodo.setValue("Este Mes");
        dpFechaInicio.setValue(LocalDate.now().withDayOfMonth(1));
        dpFechaFin.setValue(LocalDate.now());
        cbCategoria.setValue("Todas las categorías");
        cbProductoFiltro.setValue("Todos los productos");

        generarReporte();
    }

    @FXML
    private void exportarExcel() {
        mostrarAlerta("Información", "Funcionalidad de exportación a Excel en desarrollo",
                Alert.AlertType.INFORMATION);
    }

    @FXML
    private void imprimirReporte() {
        mostrarAlerta("Información", "Funcionalidad de impresión en desarrollo",
                Alert.AlertType.INFORMATION);
    }


    @FXML
    private void abrirVentas() {
        try {
            Main.cambiarVista("/fxml/ventas/ventas.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir ventas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void abrirPedidos() {
        try {
            Main.cambiarVista("/fxml/pedidos/pedidos.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir pedidos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void abrirProductos() {
        try {
            Main.cambiarVista("/fxml/productos/productos.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir productos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void abrirInventario() {
        try {
            Main.cambiarVista("/fxml/inventario/inventario.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir inventario: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void abrirClientes() {
        try {
            Main.cambiarVista("/fxml/clientes/clientes.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir clientes: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void abrirRecibos() {
        try {
            Main.cambiarVista("/fxml/recibos/recibos.fxml");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo abrir recibos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void abrirReportes() {
        // Ya estamos en reportes
    }



    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static class ProductoReporte {
        private int ranking;
        private String nombreProducto;
        private int cantidadVendida;
        private double ingresoTotal;

        public ProductoReporte(String nombreProducto, int cantidadVendida, double ingresoTotal) {
            this.nombreProducto = nombreProducto;
            this.cantidadVendida = cantidadVendida;
            this.ingresoTotal = ingresoTotal;
        }

        // Getters y Setters
        public int getRanking() { return ranking; }
        public void setRanking(int ranking) { this.ranking = ranking; }
        public String getNombreProducto() { return nombreProducto; }
        public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
        public int getCantidadVendida() { return cantidadVendida; }
        public void setCantidadVendida(int cantidadVendida) { this.cantidadVendida = cantidadVendida; }
        public double getIngresoTotal() { return ingresoTotal; }
        public void setIngresoTotal(double ingresoTotal) { this.ingresoTotal = ingresoTotal; }
    }

    public static class CategoriaReporte {
        private String categoria;
        private double totalVendido;
        private String porcentaje;

        public CategoriaReporte(String categoria, double totalVendido, String porcentaje) {
            this.categoria = categoria;
            this.totalVendido = totalVendido;
            this.porcentaje = porcentaje;
        }

        // Getters
        public String getCategoria() { return categoria; }
        public double getTotalVendido() { return totalVendido; }
        public String getPorcentaje() { return porcentaje; }
    }
}