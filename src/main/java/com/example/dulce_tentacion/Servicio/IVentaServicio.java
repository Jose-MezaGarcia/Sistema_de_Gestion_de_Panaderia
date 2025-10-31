package com.example.dulce_tentacion.Servicio;

import com.example.dulce_tentacion.Modelo.DetalleVenta;
import com.example.dulce_tentacion.Modelo.Empleado;
import com.example.dulce_tentacion.Modelo.Venta;

import java.util.List;

/**
 * Interfaz que define las operaciones de gestión de ventas.
 */
public interface IVentaServicio {

    /**
     * Registra una nueva venta.
     *
     * @param detalles lista de productos vendidos.
     * @param vendedor empleado que realizó la venta.
     * @return el objeto Venta generado.
     */
    Venta registrarVenta(List<DetalleVenta> detalles, Empleado vendedor);

    /**
     * Retorna todas las ventas registradas.
     * @return lista de ventas.
     */
    List<Venta> listarVentas();
}
