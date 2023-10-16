package com.tonystorm.delivery.models.restaurante;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tonystorm.delivery.models.comida.ComidaModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Restaurantes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestauranteModel implements Serializable {
    private static final long serialVersionID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idRestaurante;

    private String nome;

    @CNPJ
    private String CNPJ;

    private Localizacao localizacao;

    @JsonManagedReference
    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ComidaModel> comidas;
}
