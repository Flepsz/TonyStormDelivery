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
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private UsuarioModel usuario;

    @ManyToMany
    @JoinTable(
            name = "pedido_comida",
            joinColumns = @JoinColumn(name = "pedido_id"),
            inverseJoinColumns = @JoinColumn(name = "comida_id")
    )
    private List<ComidaModel> comidas;

    private Double precoTotal;

    private Double distancia;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Enumerated(EnumType.STRING)
    private Status status;

    public void setAndamento() {
        this.status = Status.ANDAMENTO;
    }

    public void setFinalizado() {
        this.status = Status.FINALIZADO;
    }
}
