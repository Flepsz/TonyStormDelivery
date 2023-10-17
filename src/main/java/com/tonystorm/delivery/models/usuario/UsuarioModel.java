package com.tonystorm.delivery.models.usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tonystorm.delivery.models.pedido.PedidoModel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Usuarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioModel implements Serializable {
    private static final long serialVersionID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<PedidoModel> pedidos;

    private String nome;

    private String senha;

    @CPF
    private String cpf;

    private Endereco endereco;
}
