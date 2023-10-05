package com.tonystorm.delivery.models.usuario;

import com.tonystorm.delivery.models.pedido.PedidoModel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

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
    private UUID idUsuario;
    @OneToMany(mappedBy = "usuario")
    private List<PedidoModel> pedidos;
    @Length (min = 3, max = 45)
    private String nome;
    @Column(unique=true, length = 11)
    @Length (min = 11, max = 11)
    private String CPF;
    private Endereco endereco;
}
