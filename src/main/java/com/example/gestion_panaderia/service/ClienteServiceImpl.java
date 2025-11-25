// ClienteServiceImpl.java
package com.example.gestion_panaderia.service;

import com.example.gestion_panaderia.model.Cliente;
import com.example.gestion_panaderia.repository.IRepository;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteServiceImpl implements IClienteService {

    private IRepository<Cliente> clienteRepository;

    public ClienteServiceImpl(IRepository<Cliente> clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public void agregarCliente(Cliente cliente) {
        List<Cliente> clientes = clienteRepository.cargar();
        clientes.add(cliente);
        clienteRepository.guardar(clientes);
    }

    @Override
    public void actualizarCliente(Cliente clienteActualizado) {
        List<Cliente> clientes = clienteRepository.cargar();
        clientes = clientes.stream()
                .map(c -> c.getId().equals(clienteActualizado.getId()) ? clienteActualizado : c)
                .collect(Collectors.toList());
        clienteRepository.guardar(clientes);
    }

    @Override
    public void eliminarCliente(String id) {
        List<Cliente> clientes = clienteRepository.cargar();
        clientes = clientes.stream()
                .filter(c -> !c.getId().equals(id))
                .collect(Collectors.toList());
        clienteRepository.guardar(clientes);
    }

    @Override
    public Cliente buscarPorId(String id) {
        return clienteRepository.cargar().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Cliente> listarClientes() {
        return clienteRepository.cargar();
    }

    @Override
    public void actualizarCalificacion(String clienteId, Cliente.CalificacionCliente calificacion) {
        Cliente cliente = buscarPorId(clienteId);
        if (cliente != null) {
            cliente.setCalificacion(calificacion);
            actualizarCliente(cliente);
        }
    }
}
