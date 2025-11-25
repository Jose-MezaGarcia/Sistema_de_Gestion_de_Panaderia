
package com.example.gestion_panaderia.service;

import com.example.gestion_panaderia.model.Cliente;
import java.util.List;

public interface IClienteService {
    void agregarCliente(Cliente cliente);
    void actualizarCliente(Cliente cliente);
    void eliminarCliente(String id);
    Cliente buscarPorId(String id);
    List<Cliente> listarClientes();
    void actualizarCalificacion(String clienteId, Cliente.CalificacionCliente calificacion);
}
