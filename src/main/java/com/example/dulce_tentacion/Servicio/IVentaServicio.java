package com.example.gestion_panaderia.Servicio;

import com.example.gestion_panaderia.modelo.DetalleVenta;
import com.example.gestion_panaderia.modelo.Empleado;
import com.example.gestion_panaderia.modelo.Venta;

import java.util.List;

/**
 * Define las operaciones del servicio de ventas.
 */
public interface IVentaServicio {

    Venta registrarVenta(List<DetalleVenta> detalles, Empleado vendedor);

    List<Venta> listarVentas();
}
