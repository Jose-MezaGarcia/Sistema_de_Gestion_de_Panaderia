package com.example.gestion_panaderia.model;

/**
 * Interfaz que define el comportamiento relacionado con la visualización de reportes
 * dentro del sistema de gestión de panadería.
 *
 * Las clases que implementen esta interfaz deben proporcionar una implementación
 * concreta del método {@link #verReportes()}, que permite acceder o mostrar reportes.
 *
 * Ejemplo de implementación: {@link Empleado}, donde un empleado puede verificar reportes.
 *
 * @author ¿?
 * @version ¿?
 */
public interface IReportable {

    /**
     * Método que debe implementarse para visualizar reportes.
     * La implementación concreta dependerá de la clase que lo utilice.
     */
    void verReportes();
}
