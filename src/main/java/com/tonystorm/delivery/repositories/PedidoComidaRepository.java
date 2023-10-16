package com.tonystorm.delivery.repositories;

import com.tonystorm.delivery.models.pedidoComida.PedidoComidaModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PedidoComidaRepository extends JpaRepository<PedidoComidaModel, UUID> {
    @Transactional
    void deleteByComidaId(UUID comidaId);
}
