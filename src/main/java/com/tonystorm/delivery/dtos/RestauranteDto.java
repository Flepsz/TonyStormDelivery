package com.tonystorm.delivery.dtos;

import com.tonystorm.delivery.models.restaurante.Localizacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RestauranteDto(
        @NotBlank
        String nome,
        @NotBlank
        String senha,
        @NotBlank
        String cnpj,
        @NotNull
        Localizacao localizacao) {
        public RestauranteDto() {
                this(null, null, null, null);
        }

}
