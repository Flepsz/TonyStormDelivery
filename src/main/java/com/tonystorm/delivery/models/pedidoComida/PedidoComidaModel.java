package com.tonystorm.delivery.models.pedidoComida;


import com.tonystorm.delivery.models.comida.ComidaModel;
import com.tonystorm.delivery.models.pedido.PedidoModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "pedido_comida")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoComidaModel {
    @EmbeddedId
    private PedidoComidaKey id;

    @ManyToOne
    @MapsId("pedidoId")
    @JoinColumn(name = "pedido_id")
    private PedidoModel pedido;

    @ManyToOne
    @MapsId("comidaId")
    @JoinColumn(name = "comida_id")
    private ComidaModel comida;
}
