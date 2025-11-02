package com.example.gestion_panaderia.service;

import com.example.gestion_panaderia.model.DetalleVenta;
import com.example.gestion_panaderia.model.Empleado;
import com.example.gestion_panaderia.model.Venta;
import java.util.List;

public interface IVentaService {
    Venta registrarVenta(List<DetalleVenta> detalles, Empleado vendedor);
    List<Venta> listarVentas();
}
