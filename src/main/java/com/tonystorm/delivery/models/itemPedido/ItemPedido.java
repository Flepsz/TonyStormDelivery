package com.tonystorm.delivery.models.itemPedido;

import com.tonystorm.delivery.models.comida.ComidaModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private ComidaModel comida;

    private int quantidade;

    private Double subTotal;
}
