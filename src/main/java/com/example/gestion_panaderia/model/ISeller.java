package com.example.gestion_panaderia.model;

/**
 * Interfaz que define el comportamiento relacionado con la venta de productos
 * dentro del sistema de gestión de panadería.
 *
 * Las clases que implementen esta interfaz deben proporcionar una implementación
 * concreta del método {@link #registrarVenta(Venta)}, que permite registrar una venta.
 *
 * Ejemplo de implementación: {@link Empleado}, donde un empleado puede registrar ventas.
 *
 * @author ¿?
 * @version ¿?
 */
public interface ISeller {

    /**
     * Método que debe implementarse para registrar una venta.
     * @param v Venta que se desea registrar
     */
    void registrarVenta(Venta v);
}
