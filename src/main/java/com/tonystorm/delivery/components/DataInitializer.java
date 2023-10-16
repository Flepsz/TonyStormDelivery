package com.tonystorm.delivery.components;

import com.tonystorm.delivery.models.comida.ComidaModel;
import com.tonystorm.delivery.models.pedido.PedidoModel;
import com.tonystorm.delivery.models.restaurante.Localizacao;
import com.tonystorm.delivery.models.restaurante.RestauranteModel;
import com.tonystorm.delivery.models.usuario.Endereco;
import com.tonystorm.delivery.models.usuario.UsuarioModel;
import com.tonystorm.delivery.repositories.ComidaRepository;
import com.tonystorm.delivery.repositories.PedidoRepository;
import com.tonystorm.delivery.repositories.RestauranteRepository;
import com.tonystorm.delivery.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final ComidaRepository comidaRepository;
    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;


    @Autowired
    public DataInitializer(ComidaRepository comidaRepository, RestauranteRepository restauranteRepository,
                           UsuarioRepository usuarioRepository, PedidoRepository pedidoRepository) {
        this.comidaRepository = comidaRepository;
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        var rCalabreso = new RestauranteModel();
        rCalabreso.setNome("Calabreso's Churrascaria");
        rCalabreso.setSenha("696969");
        rCalabreso.setCNPJ("10446214000140");
        rCalabreso.setLocalizacao(new Localizacao(43.99, 55.23));
        restauranteRepository.save(rCalabreso);

        List<ComidaModel> comidas = new ArrayList<>();

        var pizza = new ComidaModel();
        pizza.setNome("Pizza");
        pizza.setPreco(10.99);
        pizza.setRestaurante(rCalabreso);
        comidas.add(pizza);

        var hamburger = new ComidaModel();
        hamburger.setNome("Hamburguer");
        hamburger.setPreco(5.99);
        hamburger.setRestaurante(rCalabreso);
        comidas.add(hamburger);

        comidaRepository.save(pizza);
        comidaRepository.save(hamburger);

        var usuario = new UsuarioModel();
        usuario.setNome("Ludmillo");
        usuario.setSenha("linguico123");
        usuario.setCPF("98619782088");
        usuario.setEndereco(new Endereco(22.32, 55.44));

        usuarioRepository.save(usuario);

        var pedido1 = new PedidoModel();
        pedido1.setUsuario(usuario);
        pedido1.setComidas(comidas);

        pedidoRepository.save(pedido1);
    }
}