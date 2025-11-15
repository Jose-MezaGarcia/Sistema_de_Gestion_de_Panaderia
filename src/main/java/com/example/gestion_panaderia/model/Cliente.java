package com.example.gestion_panaderia.model;

/**
 * Representa un cliente dentro del sistema de gestión de panadería.
 * Extiende la clase {@link Usuario} añadiendo información de contacto
 * como teléfono y dirección.
 *
 * Esta clase forma parte del modelo de datos y se utiliza para
 * registrar y gestionar la información de los clientes en el sistema.
 *
 * @author Pepe
 * @version 1.0
 */
public class Cliente extends Usuario {

    /** Número de teléfono del cliente */
    private String telefono;

    /** Dirección del cliente */
    private String direccion;

    /** Constructor vacío requerido por frameworks y librerías de persistencia */
    public Cliente() { super(); }

    /**
     * Constructor que inicializa un cliente con todos sus datos.
     *
     * @param id Identificador único del cliente
     * @param nombre Nombre completo del cliente
     * @param usuario Nombre de usuario para autenticación
     * @param password Contraseña del cliente
     * @param telefono Número de teléfono del cliente
     * @param direccion Dirección del cliente
     */
    public Cliente(String id, String nombre, String usuario, String password, String telefono, String direccion) {
        super(id, nombre, usuario, password);
        this.telefono = telefono;
        this.direccion = direccion;
    }

    /**
     * Obtiene el número de teléfono del cliente.
     * @return teléfono del cliente
     */
    public String getTelefono() { return telefono; }

    /**
     * Establece el número de teléfono del cliente.
     * @param telefono nuevo número de teléfono
     */
    public void setTelefono(String telefono) { this.telefono = telefono; }

    /**
     * Obtiene la dirección del cliente.
     * @return dirección del cliente
     */
    public String getDireccion() { return direccion; }

    /**
     * Establece la dirección del cliente.
     * @param direccion nueva dirección del cliente
     */
    public void setDireccion(String direccion) { this.direccion = direccion; }
}
