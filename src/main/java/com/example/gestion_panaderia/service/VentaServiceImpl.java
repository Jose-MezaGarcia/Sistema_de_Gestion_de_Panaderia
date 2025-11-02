package com.example.gestion_panaderia.service;

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
    
    @Override
    public Venta registrarVenta(List<DetalleVenta> detalles, Empleado vendedor) {
        String id = "V-" + UUID.randomUUID().toString().substring(0, 8);
        LocalDateTime fecha = LocalDateTime.now();
        
        Venta venta = new Venta(id, fecha, vendedor, detalles);
        
        List<Venta> ventas = ventaRepo.cargar();
        ventas.add(venta);
        ventaRepo.guardar(ventas);
        
        return venta;
    }
    
    @Override
    public List<Venta> listarVentas() {
        return ventaRepo.cargar();
    }
}
