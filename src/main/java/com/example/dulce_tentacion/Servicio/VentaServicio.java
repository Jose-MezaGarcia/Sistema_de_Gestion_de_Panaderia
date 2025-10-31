package com.example.dulce_tentacion.Servicio;

import com.example.dulce_tentacion.Modelo.DetalleVenta;
import com.example.dulce_tentacion.Modelo.Empleado;
import com.example.dulce_tentacion.Modelo.Venta;
import com.example.dulce_tentacion.Repositorio.IRepositorio;

import java.util.List;

/**
 * Implementación de la lógica de negocio para ventas.
 * Depende del repositorio de ventas (IRepositorio<Venta>).
 */
public class VentaServicio implements IVentaServicio {

    private final IRepositorio<Venta> ventaRepo; // Depende del paquete Repositorio

    public VentaServicio(IRepositorio<Venta> ventaRepo) {
        this.ventaRepo = ventaRepo;
    }

    @Override
    public Venta registrarVenta(List<DetalleVenta> detalles, Empleado vendedor) {
        // Crear objeto Venta, calcular total, guardar en ventaRepo
        return null;
    }

    @Override
    public List<Venta> listarVentas() {
        // Implementar lectura de todas las ventas desde el repositorio
        return null;
    }
}
