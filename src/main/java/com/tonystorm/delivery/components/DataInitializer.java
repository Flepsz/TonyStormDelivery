package com.tonystorm.delivery.components;

import com.tonystorm.delivery.models.comida.ComidaModel;
import com.tonystorm.delivery.models.itemPedido.ItemPedido;
import com.tonystorm.delivery.models.pedido.PedidoModel;
import com.tonystorm.delivery.models.pedido.Status;
import com.tonystorm.delivery.models.restaurante.Localizacao;
import com.tonystorm.delivery.models.restaurante.RestauranteModel;
import com.tonystorm.delivery.models.usuario.Endereco;
import com.tonystorm.delivery.models.usuario.UsuarioModel;
import com.tonystorm.delivery.repositories.ComidaRepository;
import com.tonystorm.delivery.repositories.PedidoRepository;
import com.tonystorm.delivery.repositories.RestauranteRepository;
import com.tonystorm.delivery.repositories.UsuarioRepository;
import com.tonystorm.delivery.services.CalculadoraPedidoService;
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
    private final CalculadoraPedidoService calculadoraPedidoService;
    private boolean initialized = false;


    @Autowired
    public DataInitializer(ComidaRepository comidaRepository, RestauranteRepository restauranteRepository,
                           UsuarioRepository usuarioRepository, PedidoRepository pedidoRepository,
                           CalculadoraPedidoService calculadoraPedidoService) {
        this.comidaRepository = comidaRepository;
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
        this.pedidoRepository = pedidoRepository;
        this.calculadoraPedidoService = calculadoraPedidoService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (initialized) {
            return;
        }
        var rCalabreso = new RestauranteModel();
        rCalabreso.setNome("Calabreso");
        rCalabreso.setSenha("6969");
        rCalabreso.setCnpj("10446214000140");
        rCalabreso.setLocalizacao(new Localizacao(43.99, 55.23));
        restauranteRepository.save(rCalabreso);

        var bbRestaurante = new RestauranteModel();
        bbRestaurante.setNome("BB");
        bbRestaurante.setSenha("123");
        bbRestaurante.setCnpj("30229906000143");
        bbRestaurante.setLocalizacao(new Localizacao(23.99, 33.53));
        restauranteRepository.save(bbRestaurante);

        List<ComidaModel> comidas = new ArrayList<>();

        var pizza = new ComidaModel();
        pizza.setNome("Pizza");
        pizza.setPreco(32.99);
        pizza.setRestaurante(rCalabreso);
        comidas.add(pizza);

        var hamburger = new ComidaModel();
        hamburger.setNome("Hamburguer");
        hamburger.setPreco(14.89);
        hamburger.setRestaurante(rCalabreso);
        comidas.add(hamburger);

        var coracao = new ComidaModel();
        coracao.setNome("Calma coração");
        coracao.setPreco(8.99);
        coracao.setRestaurante(bbRestaurante);

        comidaRepository.save(coracao);
        comidaRepository.save(pizza);
        comidaRepository.save(hamburger);

        var usuario = new UsuarioModel();
        usuario.setNome("Ludmillo");
        usuario.setSenha("linguico123");
        usuario.setCpf("98619782088");
        usuario.setEndereco(new Endereco(22.32, 55.44));

        usuarioRepository.save(usuario);

        var itemPedido = new ItemPedido();
        itemPedido.setComida(pizza);
        itemPedido.setQuantidade(3);

        List<ItemPedido> itensPedidos = new ArrayList<>();
        itensPedidos.add(itemPedido);

        var pedido1 = new PedidoModel();
        pedido1.setUsuario(usuario);
        pedido1.setItensPedido(itensPedidos);
        pedido1.setAndamento();

        pedido1 = calculadoraPedidoService.calcularPedido(pedido1);

        pedidoRepository.save(pedido1);

        initialized = true;
    }
}