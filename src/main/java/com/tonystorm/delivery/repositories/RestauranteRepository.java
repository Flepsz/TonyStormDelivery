package com.tonystorm.delivery.repositories;

import com.tonystorm.delivery.models.restaurante.RestauranteModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RestauranteRepository extends JpaRepository<RestauranteModel, UUID> {
    RestauranteModel findByNome(String nome);
}
