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
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Pedidos")
@Getter
@Setter
@AllArgsConstructor
public class PedidoModel implements Serializable {

    public PedidoModel() {
        setAndamento();
    }

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

    private String status;

    public void setAndamento() {
        this.status = "Em Andamento";
    }

    public void setFinalizado() {
        this.status = "Finalizado";
    }
}
