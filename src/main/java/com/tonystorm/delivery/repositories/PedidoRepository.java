package com.tonystorm.delivery.repositories;

import com.tonystorm.delivery.models.pedido.PedidoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PedidoRepository extends JpaRepository<PedidoModel, UUID> {
}
