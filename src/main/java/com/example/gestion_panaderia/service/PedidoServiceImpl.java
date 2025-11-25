package com.example.gestion_panaderia.service;

import com.example.gestion_panaderia.model.Pedido;
import com.example.gestion_panaderia.repository.IRepository;

import java.util.List;
import java.util.stream.Collectors;

public class PedidoServiceImpl implements IPedidoService {

    private final IRepository<Pedido> pedidoRepository;

    public PedidoServiceImpl(IRepository<Pedido> pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public void agregarPedido(Pedido pedido) {
        List<Pedido> pedidos = pedidoRepository.cargar();
        pedidos.add(pedido);
        pedidoRepository.guardar(pedidos);
    }

    @Override
    public void actualizarPedido(Pedido pedido) {
        List<Pedido> pedidos = pedidoRepository.cargar();
        pedidos = pedidos.stream()
                .map(p -> p.getId().equals(pedido.getId()) ? pedido : p)
                .collect(Collectors.toList());
        pedidoRepository.guardar(pedidos);
    }

    @Override
    public void eliminarPedido(String id) {
        List<Pedido> pedidos = pedidoRepository.cargar();
        pedidos = pedidos.stream()
                .filter(p -> !p.getId().equals(id))
                .collect(Collectors.toList());
        pedidoRepository.guardar(pedidos);
    }

    @Override
    public Pedido buscarPedidoPorId(String id) {
        return pedidoRepository.cargar().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Pedido> listarPedidos() {
        return pedidoRepository.cargar();
    }

    @Override
    public List<Pedido> buscarPedidosPorEstado(String estado) {
        return listarPedidos().stream()
                .filter(pedido -> pedido.getEstado().equalsIgnoreCase(estado))
                .collect(Collectors.toList());
    }

    @Override
    public List<Pedido> buscarPedidosPorCliente(String cliente) {
        return listarPedidos().stream()
                .filter(pedido -> pedido.getCliente().toLowerCase().contains(cliente.toLowerCase()))
                .collect(Collectors.toList());
    }
}