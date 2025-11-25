package com.example.gestion_panaderia.model;

/**
 * Interfaz para objetos que pueden registrar ventas
 * Aquí se define lo que se hace al vender
 */
public interface ISeller {

    /**
     * Se registra una venta
     * @param v venta realizada
     */
    void registrarVenta(Venta v);
}
