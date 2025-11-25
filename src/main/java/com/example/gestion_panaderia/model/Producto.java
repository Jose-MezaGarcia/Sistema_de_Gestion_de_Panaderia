package com.example.gestion_panaderia.model;

import java.util.Objects;

public class Producto {
    private String id;
    private String codigo;
    private String nombre;
    private double precio;
    private int stock;
    private Categoria categoria;
    private String descripcion;
    private boolean activo;
    private String fechaCreacion;
    private String fechaActualizacion;

    // Constructores
    public Producto(String codigo, String nombre, double precio, int stock, Categoria categoria) {
        this.activo = true;
        this.fechaCreacion = java.time.LocalDateTime.now().toString();
    }

    public Producto(String id, String codigo, String nombre, double precio, int stock, Categoria categoria) {
        this(codigo, nombre, precio, stock, categoria);
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }

    public Producto(String id, String codigo, String nombre, double precio, int stock, Categoria categoria, String descripcion) {
        this(id, codigo, nombre, precio, stock, categoria);
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.fechaActualizacion = java.time.LocalDateTime.now().toString();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
        this.fechaActualizacion = java.time.LocalDateTime.now().toString();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        this.fechaActualizacion = java.time.LocalDateTime.now().toString();
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
        this.fechaActualizacion = java.time.LocalDateTime.now().toString();
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
        this.fechaActualizacion = java.time.LocalDateTime.now().toString();
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
        this.fechaActualizacion = java.time.LocalDateTime.now().toString();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
        this.fechaActualizacion = java.time.LocalDateTime.now().toString();
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
        this.fechaActualizacion = java.time.LocalDateTime.now().toString();
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    // Métodos de negocio
    public void aumentarStock(int cantidad) {
        if (cantidad > 0) {
            this.stock += cantidad;
            this.fechaActualizacion = java.time.LocalDateTime.now().toString();
        }
    }

    public void disminuirStock(int cantidad) {
        if (cantidad > 0 && this.stock >= cantidad) {
            this.stock -= cantidad;
            this.fechaActualizacion = java.time.LocalDateTime.now().toString();
        }
    }

    public boolean tieneStockSuficiente(int cantidad) {
        return this.stock >= cantidad;
    }

    public double calcularPrecioTotal(int cantidad) {
        return this.precio * cantidad;
    }

    public String obtenerEstadoStock() {
        if (stock == 0) {
            return "Sin Stock";
        } else if (stock < 5) {
            return "Stock Bajo";
        } else {
            return "Stock Óptimo";
        }
    }

    // Métodos equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return Objects.equals(id, producto.id) &&
                Objects.equals(codigo, producto.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigo);
    }

    // Método toString
    @Override
    public String toString() {
        return "Producto{" +
                "id='" + id + '\'' +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", stock=" + stock +
                ", categoria=" + (categoria != null ? categoria.getNombre() : "null") +
                ", descripcion='" + descripcion + '\'' +
                ", activo=" + activo +
                ", fechaCreacion='" + fechaCreacion + '\'' +
                ", fechaActualizacion='" + fechaActualizacion + '\'' +
                '}';
    }

    // Método para validar el producto
    public boolean esValido() {
        return id != null && !id.trim().isEmpty() &&
                codigo != null && !codigo.trim().isEmpty() &&
                nombre != null && !nombre.trim().isEmpty() &&
                precio >= 0 &&
                stock >= 0 &&
                categoria != null;
    }

    // Método para clonar el producto
    public Producto clonar() {
        Producto clon = new Producto(codigo, nombre, precio, stock, categoria);
        clon.id = this.id;
        clon.codigo = this.codigo;
        clon.nombre = this.nombre;
        clon.precio = this.precio;
        clon.stock = this.stock;
        clon.categoria = this.categoria != null ? this.categoria.clonar() : null;
        clon.descripcion = this.descripcion;
        clon.activo = this.activo;
        clon.fechaCreacion = this.fechaCreacion;
        clon.fechaActualizacion = this.fechaActualizacion;
        return clon;
    }

    public void actualizarStock(int i) {
    }
}