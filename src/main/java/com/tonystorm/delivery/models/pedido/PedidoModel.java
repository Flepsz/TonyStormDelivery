package com.tonystorm.delivery.models.pedido;

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
    private List<ComidaModel> comidas;
    private Double precoTotal;

    @Enumerated(EnumType.STRING)
    private Status status;
}
