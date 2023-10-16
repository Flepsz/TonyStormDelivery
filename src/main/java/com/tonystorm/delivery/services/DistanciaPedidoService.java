package com.tonystorm.delivery.services;

import com.tonystorm.delivery.models.restaurante.Localizacao;
import com.tonystorm.delivery.models.usuario.Endereco;
import org.springframework.stereotype.Service;

@Service
public class DistanciaPedidoService {
    private final Localizacao restaurante;
    private final Endereco usuario;

    public DistanciaPedidoService(Localizacao restaurante, Endereco usuario) {
        this.restaurante = restaurante;
        this.usuario = usuario;
    }

    public double calcular() {
        double x1 = restaurante.getX();
        double y1 = restaurante.getY();
        double x2 = usuario.getX();
        double y2 = usuario.getY();

        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
