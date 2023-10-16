package com.tonystorm.delivery.models.pedidoComida;

import com.tonystorm.delivery.models.comida.ComidaModel;
import com.tonystorm.delivery.models.pedido.PedidoModel;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

@Embeddable
public class PedidoComidaKey implements Serializable {
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private PedidoModel pedido;

    @ManyToOne
    @JoinColumn(name = "comida_id")
    private ComidaModel comida;

}