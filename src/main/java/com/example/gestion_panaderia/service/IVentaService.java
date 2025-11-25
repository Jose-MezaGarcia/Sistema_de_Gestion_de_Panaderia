package com.example.gestion_panaderia.service;

import com.example.gestion_panaderia.model.Cliente;
import com.example.gestion_panaderia.model.DetalleVenta;
import com.example.gestion_panaderia.model.Empleado;
import com.example.gestion_panaderia.model.Venta;

import java.time.LocalDateTime;
import java.util.List;

public interface IVentaService {

    Venta registrarVenta(List<DetalleVenta> detalles, Empleado vendedor);
    Venta registrarVenta(List<DetalleVenta> detalles, Empleado vendedor, Cliente cliente);
    List<Venta> listarVentas();
    Venta buscarPorId(String id);
    List<Venta> obtenerVentasPorCliente(String clienteId);
    double calcularTotalVentas(LocalDateTime inicio, LocalDateTime fin);

}