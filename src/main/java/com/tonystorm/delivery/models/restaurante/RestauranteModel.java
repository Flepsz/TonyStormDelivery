package com.tonystorm.delivery.models.restaurante;

import com.tonystorm.delivery.models.comida.ComidaModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

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
    @Length(min = 3, max = 45)
    private String nome;

    @Column(unique=true, length = 11)
    @Length (min = 11, max = 11)
    private String CNPJ;
    private Localizacao localizacao;
    @OneToMany(mappedBy = "restaurante")
    private List<ComidaModel> comidas;
}
