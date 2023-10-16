package com.tonystorm.delivery.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ComidaDto(
        @NotBlank
        String nome,

        @NotNull
        Double preco) {
}
