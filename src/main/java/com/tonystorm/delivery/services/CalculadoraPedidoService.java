package com.tonystorm.delivery.services;

import com.tonystorm.delivery.models.comida.ComidaModel;
import com.tonystorm.delivery.models.itemPedido.ItemPedido;
import com.tonystorm.delivery.models.pedido.PedidoModel;
import com.tonystorm.delivery.models.pedido.Status;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalculadoraPedidoService {

    public PedidoModel calcularPedido(PedidoModel pedido) {
        if (pedido == null || pedido.getItensPedido() == null || pedido.getItensPedido().isEmpty()) {
            return pedido;
        }

        double precoTotal = pedido.getItensPedido().stream().mapToDouble(item -> {
            if (item.getSubTotal() != null) {
                return item.getSubTotal();
            } else {
                return 0.0;
            }
        }).sum();

        var localizacaoRestaurante = pedido.getItensPedido().get(0).getComida().getRestaurante().getLocalizacao();
        var enderecoUsuario = pedido.getUsuario().getEndereco();

        var distancia = new DistanciaPedidoService(localizacaoRestaurante, enderecoUsuario).calcular();

        pedido.setPrecoTotal(precoTotal);
        pedido.setDistancia(distancia);

        return pedido;
    }
}
