package com.tonystorm.delivery.repositories;

import com.tonystorm.delivery.models.comida.ComidaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ComidaRepository extends JpaRepository<ComidaModel, UUID> {
}
