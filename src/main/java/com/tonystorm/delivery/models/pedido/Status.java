package com.tonystorm.delivery.models.pedido;

import jakarta.persistence.Embeddable;

@Embeddable
public enum Status {
    ANDAMENTO,
    FINALIZADO
}
