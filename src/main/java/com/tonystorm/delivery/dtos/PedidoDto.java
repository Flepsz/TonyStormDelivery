package com.tonystorm.delivery.dtos;

import com.tonystorm.delivery.models.itemPedido.ItemPedido;
import com.tonystorm.delivery.models.pedido.Status;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import javax.persistence.Enumerated;
import java.util.List;
import java.util.UUID;

public record PedidoDto(
        @NotNull
        @NotEmpty
        List<ItemPedido> itensPedido
) {
        public List<ItemPedido> getItensPedido() {
                return itensPedido;
        }
}
