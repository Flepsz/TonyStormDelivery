package com.tonystorm.delivery.models.pedido;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tonystorm.delivery.dtos.PedidoDto;
import com.tonystorm.delivery.models.comida.ComidaModel;
import com.tonystorm.delivery.models.usuario.UsuarioModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Pedidos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idPedido;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private UsuarioModel usuario;

    @ManyToMany
    @JoinTable(
            name = "pedidoComida",
            joinColumns = @JoinColumn(name = "pedidoId"),
            inverseJoinColumns = @JoinColumn(name = "comidaId")
    )
    private List<ComidaModel> comidas;

    private Double precoTotal;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Enumerated(EnumType.STRING)
    private Status status;


    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = Status.ANDAMENTO;
        }
    }
}
