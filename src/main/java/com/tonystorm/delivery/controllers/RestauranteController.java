package com.tonystorm.delivery.controllers;

import com.tonystorm.delivery.dtos.ComidaDto;
import com.tonystorm.delivery.dtos.RestauranteDto;
import com.tonystorm.delivery.models.comida.ComidaModel;
import com.tonystorm.delivery.models.pedido.PedidoModel;
import com.tonystorm.delivery.models.restaurante.RestauranteModel;
import com.tonystorm.delivery.repositories.ComidaRepository;
import com.tonystorm.delivery.repositories.PedidoComidaRepository;
import com.tonystorm.delivery.repositories.PedidoRepository;
import com.tonystorm.delivery.repositories.RestauranteRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ComidaRepository comidaRepository;

    @Autowired
    private PedidoComidaRepository pedidoComidaRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<Object> postRestaurante(@RequestBody @Valid RestauranteDto restauranteDto) {
        if (restauranteRepository.findByNome(restauranteDto.nome()) != null) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body("Restaurante com o mesmo nome já existe.");
        }

        var restaurante = new RestauranteModel();
        BeanUtils.copyProperties(restauranteDto, restaurante);

        RestauranteModel novoRestaurante = restauranteRepository.save(restaurante);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoRestaurante);
    }

    @PostMapping("/{id}/comidas")
    @Transactional
    public ResponseEntity<ComidaModel> postComida(@PathVariable(value = "id") UUID id,
                                                       @RequestBody @Valid ComidaDto comidaDto) {
        Optional<RestauranteModel> restauranteOptional = restauranteRepository.findById(id);

        if (restauranteOptional.isPresent()) {
            RestauranteModel restaurante = restauranteOptional.get();

            var comida = new ComidaModel();
            BeanUtils.copyProperties(comidaDto, comida);

            comida.setRestaurante(restaurante);

            ComidaModel novaComida = comidaRepository.save(comida);

            return ResponseEntity.status(HttpStatus.CREATED).body(comidaRepository.save(comida));
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<RestauranteModel>> getAllRestaurantes() {
        List<RestauranteModel> restauranteList = restauranteRepository.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(restauranteList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneRestaurante(@PathVariable(value = "id") UUID id) {
        Optional<RestauranteModel> restaurante0 = restauranteRepository.findById(id);

        if (restaurante0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Restaurante não encontrado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(restaurante0.get());
    }

    @GetMapping("/{id}/comidas")
    public ResponseEntity<List<ComidaModel>> getAllComidas(@PathVariable(value = "id") UUID id) {
        Optional<RestauranteModel> restauranteOptional = restauranteRepository.findById(id);

        if (restauranteOptional.isPresent()) {
            var restaurante = restauranteOptional.get();
            List<ComidaModel> comidas = restaurante.getComidas();

            return ResponseEntity.status(HttpStatus.OK).body(comidas);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{idRestaurante}/comidas/{idComida}")
    public ResponseEntity<Object> getUmaComidaById(@PathVariable("idRestaurante") UUID idRestaurante,
                                                   @PathVariable("idComida") UUID idComida) {
        Optional<RestauranteModel> restaurante0 = restauranteRepository.findById(idRestaurante);
        Optional<ComidaModel> comida0 = comidaRepository.findById(idComida);

        if (restaurante0.isPresent() && comida0.isPresent()) {
            var comida = comida0.get();

            return ResponseEntity.status(HttpStatus.OK).body(comida);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Restaurante ou comida não encontrada.");
    }

    @GetMapping("/{id}/pedidos")
    public ResponseEntity<List<PedidoModel>> getPedidosFromRestaurante(@PathVariable(value = "id") UUID id) {
        Optional<RestauranteModel> restaurante0 = restauranteRepository.findById(id);

        if (restaurante0.isPresent()) {
            List<PedidoModel> todosPedidos = pedidoRepository.findAll();

            List<PedidoModel> pedidosDoRestaurante = new ArrayList<>();
            for (PedidoModel pedido : todosPedidos) {
                if (pedido.getItensPedido().get(0).getComida().getRestaurante().getId().equals(id)) {
                    pedidosDoRestaurante.add(pedido);
                }
            }

            return ResponseEntity.status(HttpStatus.OK).body(pedidosDoRestaurante);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Object> deletarRestaurante(@PathVariable(value = "id") UUID id) {
        Optional<RestauranteModel> restaurante0 = restauranteRepository.findById(id);

        if (restaurante0.isPresent()) {
            var restaurante = restaurante0.get();
            restauranteRepository.delete(restaurante);

            return ResponseEntity.status(HttpStatus.OK).body("Restaurante deletado com sucesso!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Restaurante não encontrado.");
    }

    @DeleteMapping("/{idRestaurante}/comidas/{idComida}")
    @Transactional
    public ResponseEntity<Object> deleteComida(@PathVariable("idRestaurante") UUID idRestaurante,
                                                @PathVariable("idComida") UUID idComida) {
        Optional<RestauranteModel> restaurante0 = restauranteRepository.findById(idRestaurante);
        Optional<ComidaModel> comida0 = comidaRepository.findById(idComida);

        if (restaurante0.isPresent() && comida0.isPresent()) {
                var comida = comida0.get();
                removerReferenciasEmPedidoComida(idComida);

                if (comida.getRestaurante().getId().equals(idRestaurante)) {
                    comidaRepository.delete(comida);

                    return ResponseEntity.status(HttpStatus.OK).body("Comida removida com sucesso");
                }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Restaurante ou comida não encontrada.");
    }

    private void removerReferenciasEmPedidoComida(UUID idComida) {
        pedidoComidaRepository.deleteByComidaId(idComida);
    }
}
