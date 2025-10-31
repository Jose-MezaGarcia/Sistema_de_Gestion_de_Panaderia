package com.example.gestion_panaderia.Servicio;

import com.example.gestion_panaderia.Repositorio.ILeerRepo;
import com.example.gestion_panaderia.Repositorio.IEscribirRepo;
import com.example.gestion_panaderia.modelo.DetalleVenta;
import com.example.gestion_panaderia.modelo.Empleado;
import com.example.gestion_panaderia.modelo.Venta;

import java.util.List;
import java.util.UUID;

/**
 * Implementa las operaciones del servicio de ventas.
 */
public class VentaServicio implements IVentaServicio {

    private final ILeerRepo<Venta> lector;
    private final IEscribirRepo<Venta> escritor;

    public VentaServicio(ILeerRepo<Venta> lector, IEscribirRepo<Venta> escritor) {
        this.lector = lector;
        this.escritor = escritor;
    }

    @Override
    public Venta registrarVenta(List<DetalleVenta> detalles, Empleado vendedor) {
        String id = UUID.randomUUID().toString();
        Venta nuevaVenta = new Venta(id, vendedor);

        for (DetalleVenta detalle : detalles) {
            nuevaVenta.agregarDetalle(detalle);
        }

        nuevaVenta.calcularTotal();

        List<Venta> ventas = lector.leer();
        ventas.add(nuevaVenta);
        escritor.escribir(ventas);

        System.out.println("Venta registrada correctamente. Total: $" + nuevaVenta.getTotal());
        return nuevaVenta;
    }

    @Override
    public List<Venta> listarVentas() {
        return lector.leer();
    }
}
