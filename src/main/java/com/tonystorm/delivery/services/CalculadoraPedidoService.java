package com.tonystorm.delivery.services;

import com.tonystorm.delivery.models.comida.ComidaModel;
import com.tonystorm.delivery.models.pedido.PedidoModel;
import com.tonystorm.delivery.models.pedido.Status;
import org.springframework.stereotype.Service;

@Service
public class CalculadoraPedidoService {

    public PedidoModel calcularPedido(PedidoModel pedido) {
        double precoTotal = pedido.getComidas().stream().mapToDouble(ComidaModel::getPreco).sum();

        var localizacaoRestaurante = pedido.getComidas().get(0).getRestaurante().getLocalizacao();
        var enderecoUsuario = pedido.getUsuario().getEndereco();

        var distancia = new DistanciaPedidoService(localizacaoRestaurante, enderecoUsuario).calcular();

        pedido.setPrecoTotal(precoTotal);
        pedido.setDistancia(distancia);
        pedido.setStatus(Status.ANDAMENTO);

        return pedido;
    }
}
