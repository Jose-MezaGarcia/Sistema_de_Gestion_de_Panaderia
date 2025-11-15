package com.example.gestion_panaderia.model;

/**
 * Representa un producto dentro del sistema de gestión de panadería.
 * Cada producto tiene un identificador, nombre, precio, stock disponible
 * y pertenece a una categoría específica.
 *
 * Esta clase forma parte del modelo de datos y se utiliza para
 * gestionar el inventario y las ventas de productos.
 *
 * Ejemplo: Pan dulce, con precio y stock disponible.
 *
 * @author ¿?
 * @version ¿?
 */
public class Producto {

    /** Identificador único del producto */
    private String id;

    /** Nombre del producto */
    private String nombre;

    /** Precio unitario del producto */
    private double precio;

    /** Cantidad disponible en stock */
    private int stock;

    /** Categoría a la que pertenece el producto */
    private Categoria categoria;

    /** Constructor vacío requerido por frameworks y librerías de persistencia */
    public Producto() {}

    /**
     * Constructor que inicializa un producto con todos sus datos.
     *
     * @param id Identificador único del producto
     * @param nombre Nombre del producto
     * @param precio Precio unitario
     * @param stock Cantidad disponible en inventario
     * @param categoria Categoría del producto
     */
    public Producto(String id, String nombre, double precio, int stock, Categoria categoria) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }

    /** @return identificador del producto */
    public String getId() { return id; }

    /** @param id nuevo identificador del producto */
    public void setId(String id) { this.id = id; }

    /** @return nombre del producto */
    public String getNombre() { return nombre; }

    /** @param nombre nuevo nombre del producto */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /** @return precio unitario del producto */
    public double getPrecio() { return precio; }

    /** @param precio nuevo precio unitario */
    public void setPrecio(double precio) { this.precio = precio; }

    /** @return cantidad disponible en stock */
    public int getStock() { return stock; }

    /** @param stock nueva cantidad disponible */
    public void setStock(int stock) { this.stock = stock; }

    /** @return categoría del producto */
    public Categoria getCategoria() { return categoria; }

    /** @param categoria nueva categoría del producto */
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    /**
     * Actualiza el stock del producto sumando la cantidad indicada.
     * Puede ser positiva (entrada de inventario) o negativa (venta).
     *
     * @param cantidad cantidad a modificar en el stock
     */
    public void actualizarStock(int cantidad) {
        this.stock += cantidad;
    }

    /**
     * Devuelve una representación en texto del producto,
     * incluyendo nombre, precio y stock disponible.
     *
     * @return descripción del producto en formato legible
     */
    @Override
    public String toString() {
        return nombre + " - $" + precio + " (Stock: " + stock + ")";
    }
}
