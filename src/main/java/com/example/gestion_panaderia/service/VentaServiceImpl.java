package com.example.gestion_panaderia.service;

import com.example.gestion_panaderia.model.Cliente;
import com.example.gestion_panaderia.model.DetalleVenta;
import com.example.gestion_panaderia.model.Empleado;
import com.example.gestion_panaderia.model.Venta;
import com.example.gestion_panaderia.repository.IRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class VentaServiceImpl implements IVentaService {

    private IRepository<Venta> ventaRepo;

    public VentaServiceImpl(IRepository<Venta> ventaRepo) {
        this.ventaRepo = ventaRepo;
    }

    /**
     * Registra una venta con cliente opcional
     */
    @Override
    public Venta registrarVenta(List<DetalleVenta> detalles, Empleado vendedor) {
        return registrarVenta(detalles, vendedor, null);
    }

    /**
     * Registra una venta con cliente y aplica descuentos
     */
    public Venta registrarVenta(List<DetalleVenta> detalles, Empleado vendedor, Cliente cliente) {
        String id = "V-" + UUID.randomUUID().toString().substring(0, 8);
        LocalDateTime fecha = LocalDateTime.now();

        // Crear la venta con cliente (puede ser null)
        Venta venta = new Venta(id, fecha, vendedor, cliente, detalles);

        // Cargar ventas existentes
        List<Venta> ventas = ventaRepo.cargar();
        ventas.add(venta);

        // Guardar en el repositorio
        ventaRepo.guardar(ventas);

        return venta;
    }

    @Override
    public List<Venta> listarVentas() {
        return ventaRepo.cargar();
    }

    /**
     * Busca una venta por su ID
     */
    public Venta buscarPorId(String id) {
        List<Venta> ventas = ventaRepo.cargar();
        return ventas.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtiene las ventas de un cliente específico
     */
    public List<Venta> obtenerVentasPorCliente(String clienteId) {
        List<Venta> ventas = ventaRepo.cargar();
        return ventas.stream()
                .filter(v -> v.getCliente() != null && v.getCliente().getId().equals(clienteId))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Calcula el total de ventas en un rango de fechas
     */
    public double calcularTotalVentas(LocalDateTime inicio, LocalDateTime fin) {
        List<Venta> ventas = ventaRepo.cargar();
        return ventas.stream()
                .filter(v -> v.getFecha().isAfter(inicio) && v.getFecha().isBefore(fin))
                .mapToDouble(Venta::getTotal)
                .sum();
    }
}