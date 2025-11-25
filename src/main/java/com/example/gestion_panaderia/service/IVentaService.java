package com.example.gestion_panaderia.service;

import com.example.gestion_panaderia.model.Cliente;
import com.example.gestion_panaderia.model.DetalleVenta;
import com.example.gestion_panaderia.model.Empleado;
import com.example.gestion_panaderia.model.Venta;

import java.time.LocalDateTime;
import java.util.List;

public interface IVentaService {

    /**
     * Registra una venta sin cliente
     */
    Venta registrarVenta(List<DetalleVenta> detalles, Empleado vendedor);

    /**
     * Registra una venta con cliente (aplica descuentos)
     */
    Venta registrarVenta(List<DetalleVenta> detalles, Empleado vendedor, Cliente cliente);

    /**
     * Lista todas las ventas
     */
    List<Venta> listarVentas();

    /**
     * Busca una venta por su ID
     */
    Venta buscarPorId(String id);

    /**
     * Obtiene las ventas de un cliente específico
     */
    List<Venta> obtenerVentasPorCliente(String clienteId);

    /**
     * Calcula el total de ventas en un rango de fechas
     */
    double calcularTotalVentas(LocalDateTime inicio, LocalDateTime fin);
}