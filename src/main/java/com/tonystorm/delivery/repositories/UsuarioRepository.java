package com.tonystorm.delivery.repositories;

import com.tonystorm.delivery.models.usuario.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, UUID> {
}
