package com.example.gestion_panaderia.controller;

/**
 * Interfaz base para los controladores de la aplicación.
 * Define el método de inicialización que debe implementarse
 * en cada controlador para configurar la vista y sus eventos.
 *
 * Esta interfaz permite estandarizar el ciclo de vida de los controladores
 * en el sistema de punto de venta "Dulce Tentación".
 */
public interface IController {
    /**
     * Método que debe implementarse para inicializar la vista.
     * Se utiliza para configurar componentes, eventos y datos iniciales.
     */
    void inicializar();
}
