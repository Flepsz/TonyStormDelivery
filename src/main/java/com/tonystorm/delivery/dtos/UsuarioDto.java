package com.tonystorm.delivery.dtos;

import com.tonystorm.delivery.models.usuario.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioDto(
        @NotBlank
        String nome,
        @NotBlank
        String senha,
        @NotBlank
        String cpf,
        @NotNull
        Endereco endereco) {


}
