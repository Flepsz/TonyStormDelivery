package com.tonystorm.delivery.controllers;

import com.tonystorm.delivery.dtos.PedidoDto;
import com.tonystorm.delivery.dtos.UsuarioDto;
import com.tonystorm.delivery.models.comida.ComidaModel;
import com.tonystorm.delivery.models.pedido.PedidoModel;
import com.tonystorm.delivery.models.pedido.Status;
import com.tonystorm.delivery.models.restaurante.RestauranteModel;
import com.tonystorm.delivery.models.usuario.UsuarioModel;
import com.tonystorm.delivery.repositories.ComidaRepository;
import com.tonystorm.delivery.repositories.PedidoRepository;
import com.tonystorm.delivery.repositories.UsuarioRepository;
import com.tonystorm.delivery.services.DistanciaPedidoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComidaRepository comidaRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<UsuarioModel> postUsuario(@RequestBody @Valid UsuarioDto usuarioDto) {
        var usuario = new UsuarioModel();
        BeanUtils.copyProperties(usuarioDto, usuario);
        UsuarioModel novoUsuario = usuarioRepository.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @PostMapping("/{id}/pedidos")
    @Transactional
    public ResponseEntity<?> postPedido(@PathVariable(value = "id") UUID idUsuario,
                                                   @RequestBody @Valid PedidoDto pedidoDto) {
        Optional<UsuarioModel> usuarioOptional = usuarioRepository.findById(idUsuario);

        if (usuarioOptional.isPresent()) {
            UsuarioModel usuario = usuarioOptional.get();
            List<UUID> comidasIds = pedidoDto.getComidas();

            if (comidasIds.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A lista de IDs de comidas está vazia");
            }

            List<ComidaModel> comidas = comidaRepository.findAllById(comidasIds);

            if (comidas.size() != comidasIds.size()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nem todas as comidas foram encontradas com os IDs especificados");
            }

            RestauranteModel restaurante = comidas.get(0).getRestaurante();

            var distancia = new DistanciaPedidoService(restaurante.getLocalizacao(), usuario.getEndereco()).calcular();

            Double precoTotal = comidas.stream().mapToDouble(ComidaModel::getPreco).sum();

            var pedido = new PedidoModel();
            BeanUtils.copyProperties(pedidoDto, pedido);

            pedido.setUsuario(usuario);
            pedido.setPrecoTotal(precoTotal);
            pedido.setComidas(comidas);
            pedido.setDistancia(distancia);

            PedidoModel novoPedido = pedidoRepository.save(pedido);

            return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("{idUsuario}/pedidos/{idPedido}/andamento")
    @Transactional
    public ResponseEntity<PedidoModel> setAndamentoPedido(@PathVariable("idUsuario") UUID idUsuario,
                                                          @PathVariable("idPedido") UUID idPedido) {
        Optional<UsuarioModel> usuario0 = usuarioRepository.findById(idUsuario);
        Optional<PedidoModel> pedido0 = pedidoRepository.findById(idPedido);

        if (usuario0.isPresent() && pedido0.isPresent()) {
            var pedido = pedido0.get();

            if (pedido.getUsuario().getId().equals(idUsuario)) {
                pedido.setAndamento();
                pedidoRepository.save(pedido);

                return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("{idUsuario}/pedidos/{idPedido}/finalizado")
    @Transactional
    public ResponseEntity<PedidoModel> setFinalizadoPedido(@PathVariable("idUsuario") UUID idUsuario,
                                                          @PathVariable("idPedido") UUID idPedido) {
        Optional<UsuarioModel> usuario0 = usuarioRepository.findById(idUsuario);
        Optional<PedidoModel> pedido0 = pedidoRepository.findById(idPedido);

        if (usuario0.isPresent() && pedido0.isPresent()) {
            var pedido = pedido0.get();

            if (pedido.getUsuario().getId().equals(idUsuario)) {
                pedido.setFinalizado();
                pedidoRepository.save(pedido);

                return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<UsuarioModel>> getUsuarios() {
        List<UsuarioModel> usuarioList = usuarioRepository.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(usuarioList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioModel> getUsuarioByID(@PathVariable(value = "id") UUID idUsuario) {
        Optional<UsuarioModel> usuario0 = usuarioRepository.findById(idUsuario);
        if (usuario0.isPresent()) {
            var usuario = usuario0.get();
            return ResponseEntity.status(HttpStatus.OK).body(usuario);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/pedidos")
    public ResponseEntity<List<PedidoModel>> getAllPedidosByUserID(@PathVariable(value = "id") UUID idUsuario) {
        Optional<UsuarioModel> usuario0 = usuarioRepository.findById(idUsuario);
        if (usuario0.isPresent()) {
            var usuario = usuario0.get();
            List<PedidoModel> pedidos = usuario.getPedidos();
            return ResponseEntity.status(HttpStatus.OK).body(pedidos);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{idUsuario}/pedidos/{idPedido}")
    public ResponseEntity<PedidoModel> getPedidoByID(@PathVariable("idUsuario") UUID idUsuario,
                                                     @PathVariable("idPedido") UUID idPedido) {
        Optional<UsuarioModel> usuario0 = usuarioRepository.findById(idUsuario);
        Optional<PedidoModel> pedido0 = pedidoRepository.findById(idPedido);

        if (usuario0.isPresent() && pedido0.isPresent()) {
            var pedido = pedido0.get();

            if (pedido.getUsuario().getId().equals(idUsuario)) {
                return ResponseEntity.status(HttpStatus.OK).body(pedido);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Object> deleteUsuario(@PathVariable(value = "id") UUID id) {
        Optional<UsuarioModel> usuario0 = usuarioRepository.findById(id);
        if (usuario0.isPresent()) {
            var usuario = usuario0.get();

            usuarioRepository.delete(usuario);
            return ResponseEntity.status(HttpStatus.OK).body("Usuário deletado com sucesso.");
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{idUsuario}/pedidos/{idPedido}")
    @Transactional
    public ResponseEntity<Object> deletePedido(@PathVariable("idUsuario") UUID idUsuario,
                                               @PathVariable("idPedido") UUID isPedido) {
        Optional<UsuarioModel> usuario0 = usuarioRepository.findById(idUsuario);
        Optional<PedidoModel> pedido0 = pedidoRepository.findById(isPedido);

        if (usuario0.isPresent() && pedido0.isPresent()) {
            var pedido = pedido0.get();

            if (pedido.getUsuario().getId().equals(idUsuario)) {
                pedidoRepository.delete(pedido);
                return ResponseEntity.status(HttpStatus.OK).body("Pedido deletado com sucesso.");
            }
        }

        return ResponseEntity.notFound().build();
    }
}
