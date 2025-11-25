package com.example.gestion_panaderia.model;

import java.util.Objects;

public class Categoria {
    private String id;
    private String nombre;
    private String descripcion;
    private boolean activa;
    private String fechaCreacion;

    // Constructores
    public Categoria() {
        this.activa = true;
        this.fechaCreacion = java.time.LocalDateTime.now().toString();
    }

    public Categoria(String nombre) {
        this();
        this.id = generarId();
        this.nombre = nombre;
    }

    public Categoria(String id, String nombre) {
        this();
        this.id = id;
        this.nombre = nombre;
    }

    public Categoria(String id, String nombre, String descripcion) {
        this(id, nombre);
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    // Método para generar ID automático
    private String generarId() {
        return "CAT_" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Métodos equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return Objects.equals(id, categoria.id) &&
                Objects.equals(nombre, categoria.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre);
    }

    // Método toString
    @Override
    public String toString() {
        return "Categoria{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", activa=" + activa +
                ", fechaCreacion='" + fechaCreacion + '\'' +
                '}';
    }

    // Método para validar la categoría
    public boolean esValida() {
        return nombre != null && !nombre.trim().isEmpty();
    }

    // Método para clonar la categoría
    public Categoria clonar() {
        Categoria clon = new Categoria();
        clon.id = this.id;
        clon.nombre = this.nombre;
        clon.descripcion = this.descripcion;
        clon.activa = this.activa;
        clon.fechaCreacion = this.fechaCreacion;
        return clon;
    }

    // Métodos estáticos para categorías predefinidas
    public static Categoria crearCategoriaGalletas() {
        return new Categoria("CAT_GALLETAS", "GALLETAS", "Productos de galletas y bizcochos");
    }

    public static Categoria crearCategoriaPasteles() {
        return new Categoria("CAT_PASTELES", "PASTELES", "Pasteles y tortas de diferentes sabores");
    }

    public static Categoria crearCategoriaPanes() {
        return new Categoria("CAT_PANES", "PANES", "Panadería tradicional y especialidades");
    }

    public static Categoria crearCategoriaPostres() {
        return new Categoria("CAT_POSTRES", "POSTRES", "Postres y dulces variados");
    }

    public static java.util.List<Categoria> obtenerCategoriasPredefinidas() {
        java.util.List<Categoria> categorias = new java.util.ArrayList<>();
        categorias.add(crearCategoriaGalletas());
        categorias.add(crearCategoriaPasteles());
        categorias.add(crearCategoriaPanes());
        categorias.add(crearCategoriaPostres());
        return categorias;
    }
}