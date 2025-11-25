package com.example.gestion_panaderia.service;

import com.example.gestion_panaderia.model.Pedido;
import java.util.List;

public interface IPedidoService {
    void agregarPedido(Pedido pedido);
    void actualizarPedido(Pedido pedido);
    void eliminarPedido(String id);
    Pedido buscarPedidoPorId(String id);
    List<Pedido> listarPedidos();
    List<Pedido> buscarPedidosPorEstado(String estado);
    List<Pedido> buscarPedidosPorCliente(String cliente);
}