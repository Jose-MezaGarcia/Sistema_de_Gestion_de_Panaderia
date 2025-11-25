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

/**
 * Se controla la pantalla de reportes y estadísticas.
 * Se generan reportes de ventas, productos más vendidos y análisis por categorías.
 * También se muestran gráficas y resúmenes de los datos.
 */
public class ReportesController implements IController {

    // ==================== SERVICIOS ====================
    /**
     * Se usa para obtener los datos de ventas.
     * Se conecta al archivo JSON donde se guardan las ventas.
     */
    private IVentaService ventaService;

    /**
     * Se usa para obtener información de productos.
     * Se necesita para filtrar por categorías y productos.
     */
    private IProductoService productoService;

    // ==================== COMPONENTES FXML ====================

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

    // ==================== DATOS ====================
    /**
     * Se guardan las ventas para mostrar en el detalle.
     * Se actualiza cuando se aplican filtros.
     */
    private ObservableList<Venta> ventasData;

    /**
     * Se guardan los productos para el top de más vendidos.
     * Se calcula automáticamente al generar reportes.
     */
    private ObservableList<ProductoReporte> productosReporteData;

    /**
     * Se guardan las categorías con sus totales de venta.
     * Se usa para mostrar distribución por categorías.
     */
    private ObservableList<CategoriaReporte> categoriasReporteData;

    // ==================== MÉTODOS DE INICIALIZACIÓN ====================

    @FXML
    public void initialize() {
        inicializar();
    }

    /**
     * Se prepara el controlador para usarse.
     * Se inicializan servicios, se configuran componentes y se carga el primer reporte.
     */
    @Override
    public void inicializar() {
        // Se conecta con los archivos JSON de ventas y productos
        JsonRepository<Venta> ventaRepo = new JsonRepository<>("ventas.json", Venta.class);
        JsonRepository<Producto> productoRepo = new JsonRepository<>("productos.json", Producto.class);

        ventaService = new VentaServiceImpl(ventaRepo);
        productoService = new ProductoServiceImpl(productoRepo);

        // Se preparan las listas para mostrar datos
        ventasData = FXCollections.observableArrayList();
        productosReporteData = FXCollections.observableArrayList();
        categoriasReporteData = FXCollections.observableArrayList();

        // Se configuran todos los componentes visuales
        configurarControles();
        configurarTablas();
        configurarEventos();
        configurarNavegacion();

        // Se genera el reporte inicial con datos por defecto
        generarReporte();

        // Se muestra el usuario actual en pantalla
        usuarioActual.setText("Administrador");
    }

    /**
     * Se configuran los combobox y filtros.
     * Se llenan con opciones y se establecen valores por defecto.
     */
    private void configurarControles() {
        // Se configuran los tipos de reporte disponibles
        cbTipoReporte.setItems(FXCollections.observableArrayList(
                "Ventas Diarias", "Ventas Semanales", "Ventas Mensuales",
                "Productos Más Vendidos", "Ventas por Categoría", "Reporte General"
        ));
        cbTipoReporte.setValue("Reporte General");

        // Se configuran los periodos de tiempo
        cbPeriodo.setItems(FXCollections.observableArrayList(
                "Hoy", "Esta Semana", "Este Mes", "Este Año", "Personalizado"
        ));
        cbPeriodo.setValue("Este Mes");

        // Se establecen fechas por defecto (mes actual)
        dpFechaInicio.setValue(LocalDate.now().withDayOfMonth(1));
        dpFechaFin.setValue(LocalDate.now());

        // Se cargan categorías y productos para los filtros
        cargarCategorias();
        cargarProductosFiltro();
    }

    /**
     * Se cargan las categorías desde los productos.
     * Se usan para filtrar reportes por categoría.
     */
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

    /**
     * Se cargan los nombres de productos para filtrar.
     * Se obtienen todos los productos del sistema.
     */
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

    /**
     * Se configuran las tablas de reportes.
     * Se define qué datos van en cada columna y cómo formatearlos.
     */
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

    /**
     * Se configuran los eventos de botones y combobox.
     * Se define qué pasa cuando se interactúa con los controles.
     */
    private void configurarEventos() {
        // Botones principales
        btnGenerar.setOnAction(event -> generarReporte());
        btnLimpiar.setOnAction(event -> limpiarFiltros());
        btnExportarExcel.setOnAction(event -> exportarExcel());
        btnImprimir.setOnAction(event -> imprimirReporte());

        // Cambio de periodo - se actualizan las fechas automáticamente
        cbPeriodo.setOnAction(event -> actualizarFechasPorPeriodo());
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
     * Se genera el reporte con los filtros actuales.
     * Se validan fechas, se filtran ventas y se actualizan todos los componentes.
     */
    @FXML
    private void generarReporte() {
        try {
            LocalDate fechaInicio = dpFechaInicio.getValue();
            LocalDate fechaFin = dpFechaFin.getValue();

            // Se validan que las fechas estén seleccionadas
            if (fechaInicio == null || fechaFin == null) {
                mostrarAlerta("Error", "Seleccione las fechas", Alert.AlertType.ERROR);
                return;
            }

            // Se valida que la fecha inicio no sea posterior a la fecha fin
            if (fechaInicio.isAfter(fechaFin)) {
                mostrarAlerta("Error", "La fecha de inicio no puede ser posterior a la fecha fin", Alert.AlertType.ERROR);
                return;
            }

            // Se filtran las ventas por el rango de fechas
            List<Venta> ventasFiltradas = filtrarVentasPorFecha(fechaInicio, fechaFin);
            ventasData.setAll(ventasFiltradas);

            // Se actualizan todos los componentes con los datos filtrados
            actualizarResumenes(ventasFiltradas);
            generarGrafica(ventasFiltradas);
            generarTopProductos(ventasFiltradas);
            generarVentasPorCategoria(ventasFiltradas);

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al generar reporte: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Se filtran las ventas por rango de fechas.
     * Solo se incluyen ventas entre fechaInicio y fechaFin (inclusive).
     *
     * @param fechaInicio Se incluyen ventas desde esta fecha
     * @param fechaFin Se incluyen ventas hasta esta fecha
     * @return Se obtienen las ventas que cumplen con el filtro de fecha
     */
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

    /**
     * Se actualizan las fechas automáticamente según el periodo seleccionado.
     * Se usa cuando se cambia el combobox de periodo.
     */
    private void actualizarFechasPorPeriodo() {
        String periodo = cbPeriodo.getValue();
        LocalDate hoy = LocalDate.now();

        switch (periodo) {
            case "Hoy":
                dpFechaInicio.setValue(hoy);
                dpFechaFin.setValue(hoy);
                break;
            case "Esta Semana":
                // Se calcula el lunes de esta semana
                dpFechaInicio.setValue(hoy.minusDays(hoy.getDayOfWeek().getValue() - 1));
                dpFechaFin.setValue(hoy);
                break;
            case "Este Mes":
                // Se toma desde el día 1 del mes actual
                dpFechaInicio.setValue(hoy.withDayOfMonth(1));
                dpFechaFin.setValue(hoy);
                break;
            case "Este Año":
                // Se toma desde el día 1 del año actual
                dpFechaInicio.setValue(hoy.withDayOfYear(1));
                dpFechaFin.setValue(hoy);
                break;
        }
    }

    /**
     * Se actualizan las tarjetas de resumen con los totales.
     * Se calculan total de ventas, cantidad, promedio y producto más vendido.
     *
     * @param ventas Se usan estas ventas para calcular los resúmenes
     */
    private void actualizarResumenes(List<Venta> ventas) {
        double totalVentas = ventas.stream().mapToDouble(Venta::getTotal).sum();
        int cantidadVentas = ventas.size();
        double promedioVenta = cantidadVentas > 0 ? totalVentas / cantidadVentas : 0;

        // Se actualizan las tarjetas con los valores calculados
        lblTotalVentas.setText(String.format("$%.2f", totalVentas));
        lblCantidadVentas.setText(String.valueOf(cantidadVentas));
        lblPromedioVenta.setText(String.format("$%.2f", promedioVenta));
        lblTotalRegistros.setText(String.valueOf(cantidadVentas));
        lblTotalGeneral.setText(String.format("$%.2f", totalVentas));

        // Se actualiza el periodo mostrado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        lblPeriodoVentas.setText(dpFechaInicio.getValue().format(formatter) + " - " +
                dpFechaFin.getValue().format(formatter));
        lblPeriodoCantidad.setText("Transacciones realizadas");

        // Se busca el producto más vendido
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

    /**
     * Se genera la gráfica de barras con ventas por día.
     * Se agrupan las ventas por fecha y se muestran en la gráfica.
     *
     * @param ventas Se usan estas ventas para generar la gráfica
     */
    private void generarGrafica(List<Venta> ventas) {
        chartVentas.getData().clear();

        if (ventas.isEmpty()) {
            return;
        }

        // Se agrupan ventas por día
        Map<LocalDate, Double> ventasPorDia = ventas.stream()
                .collect(Collectors.groupingBy(
                        venta -> venta.getFecha().toLocalDate(),
                        Collectors.summingDouble(Venta::getTotal)
                ));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Ventas por Día");

        // Se añaden los datos a la serie, ordenados por fecha
        ventasPorDia.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String fechaStr = entry.getKey().format(DateTimeFormatter.ofPattern("dd/MM"));
                    series.getData().add(new XYChart.Data<>(fechaStr, entry.getValue()));
                });

        chartVentas.getData().add(series);
    }

    /**
     * Se genera el top 10 de productos más vendidos.
     * Se calcula por cantidad vendida y se asigna ranking.
     *
     * @param ventas Se usan estas ventas para calcular el top
     */
    private void generarTopProductos(List<Venta> ventas) {
        Map<String, ProductoReporte> reporteProductos = new HashMap<>();

        // Se recorren todas las ventas y detalles para acumular cantidades
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

        // Se ordenan por cantidad vendida (descendente) y se toman los 10 primeros
        List<ProductoReporte> topProductos = reporteProductos.values().stream()
                .sorted((p1, p2) -> Integer.compare(p2.getCantidadVendida(), p1.getCantidadVendida()))
                .limit(10)
                .collect(Collectors.toList());

        // Se asigna el ranking (1, 2, 3, ...)
        for (int i = 0; i < topProductos.size(); i++) {
            topProductos.get(i).setRanking(i + 1);
        }

        productosReporteData.setAll(topProductos);
    }

    /**
     * Se generan las ventas agrupadas por categoría.
     * Se calcula el total por categoría y su porcentaje del total general.
     *
     * @param ventas Se usan estas ventas para calcular por categorías
     */
    private void generarVentasPorCategoria(List<Venta> ventas) {
        Map<String, Double> ventasPorCategoria = new HashMap<>();
        double totalGeneral = ventas.stream().mapToDouble(Venta::getTotal).sum();

        // Se acumulan los totales por categoría
        for (Venta venta : ventas) {
            for (DetalleVenta detalle : venta.getDetalles()) {
                String categoria = detalle.getProducto().getCategoria().getNombre();
                ventasPorCategoria.merge(categoria, detalle.getSubtotal(), Double::sum);
            }
        }

        // Se crean los reportes por categoría con porcentajes
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

    /**
     * Se limpian todos los filtros y se vuelve a los valores por defecto.
     * Se genera automáticamente el reporte con los filtros limpios.
     */
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

    /**
     * Se simula la exportación a Excel.
     * CORRECCIÓN: en desarrollo - se implementará en futura versión.
     */
    @FXML
    private void exportarExcel() {
        mostrarAlerta("Información", "Funcionalidad de exportación a Excel en desarrollo",
                Alert.AlertType.INFORMATION);
    }

    /**
     * Se simula la impresión del reporte.
     * CORRECCIÓN: en desarrollo - se implementará en futura versión.
     */
    @FXML
    private void imprimirReporte() {
        mostrarAlerta("Información", "Funcionalidad de impresión en desarrollo",
                Alert.AlertType.INFORMATION);
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
     * Se abre la pantalla de recibos.
     * Se navega a la gestión de recibos.
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
     * Se abre la pantalla de reportes (esta misma).
     * No hace nada porque ya estamos aquí.
     */
    @FXML
    private void abrirReportes() {
        // Ya estamos en reportes
    }

    // ==================== MÉTODOS AUXILIARES ====================

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

    // ==================== CLASES INTERNAS PARA REPORTES ====================

    /**
     * Se usa para representar los datos de productos en reportes.
     * Se guarda nombre, cantidad vendida e ingresos totales.
     */
    public static class ProductoReporte {
        private int ranking;
        private String nombreProducto;
        private int cantidadVendida;
        private double ingresoTotal;

        /**
         * Se crea el reporte de producto con datos iniciales.
         *
         * @param nombreProducto Se guarda el nombre del producto
         * @param cantidadVendida Se guarda la cantidad total vendida
         * @param ingresoTotal Se guarda el ingreso total generado
         */
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

    /**
     * Se usa para representar los datos de categorías en reportes.
     * Se guarda nombre de categoría, total vendido y porcentaje.
     */
    public static class CategoriaReporte {
        private String categoria;
        private double totalVendido;
        private String porcentaje;

        /**
         * Se crea el reporte de categoría con datos calculados.
         *
         * @param categoria Se guarda el nombre de la categoría
         * @param totalVendido Se guarda el total vendido en esta categoría
         * @param porcentaje Se guarda el porcentaje que representa del total
         */
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