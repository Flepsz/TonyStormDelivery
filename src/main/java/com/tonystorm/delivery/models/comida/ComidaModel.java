package com.tonystorm.delivery.models.comida;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tonystorm.delivery.models.restaurante.RestauranteModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "Comidas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComidaModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "idRestaurante")
    private RestauranteModel restaurante;

    private String nome;

    private Double preco;
}