package com.tonystorm.delivery.controllers;

import com.tonystorm.delivery.dtos.ComidaDto;
import com.tonystorm.delivery.dtos.RestauranteDto;
import com.tonystorm.delivery.models.comida.ComidaModel;
import com.tonystorm.delivery.models.restaurante.RestauranteModel;
import com.tonystorm.delivery.repositories.ComidaRepository;
import com.tonystorm.delivery.repositories.RestauranteRepository;
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
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ComidaRepository comidaRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<?> cadastrarRestaurante(@RequestBody @Valid RestauranteDto restauranteDto) {
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
    public ResponseEntity<ComidaModel> cadastrarComida(@PathVariable(value = "id") UUID id,
                                                       @RequestBody @Valid ComidaDto comidaDto) {
        Optional<RestauranteModel> restauranteOptional = restauranteRepository.findById(id);

        if (restauranteOptional.isPresent()) {
            RestauranteModel restaurante = restauranteOptional.get();

            var comida = new ComidaModel();
            BeanUtils.copyProperties(comidaDto, comida);

            comida.setRestaurante(restaurante);

            ComidaModel novaComida = comidaRepository.save(comida);

            return ResponseEntity.status(HttpStatus.CREATED).body(comidaRepository.save(comida));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<RestauranteModel>> buscarTodosRestaurantes() {
        List<RestauranteModel> restauranteList = restauranteRepository.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(restauranteList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarUmRestaurtante(@PathVariable(value = "id") UUID id) {
        Optional<RestauranteModel> restaurante0 = restauranteRepository.findById(id);
        if (restaurante0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Restaurante não encontrado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(restaurante0.get());
    }

    @GetMapping("/{id}/comidas")
    public ResponseEntity<List<ComidaModel>> getComidasDoRestaurante(@PathVariable(value = "id") UUID id) {
        Optional<RestauranteModel> restauranteOptional = restauranteRepository.findById(id);

        if (restauranteOptional.isPresent()) {
            RestauranteModel restaurante = restauranteOptional.get();
            List<ComidaModel> comidas = restaurante.getComidas();
            return ResponseEntity.status(HttpStatus.OK).body(comidas);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Object> deletarRestaurante(@PathVariable(value = "id") UUID id) {
        Optional<RestauranteModel> restaurante0 = restauranteRepository.findById(id);
        if (restaurante0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Restaurante não encontrado.");
        }
        restauranteRepository.delete(restaurante0.get());
        return ResponseEntity.status(HttpStatus.OK).body("Restaurante deletado com sucesso!");
    }

    @DeleteMapping("/{idRestaurante}/comidas/{idComida}")
    @Transactional
    public ResponseEntity<Object> deletarComida(@PathVariable("idRestaurante") UUID idRestaurante, @PathVariable("idComida") UUID idComida) {
        Optional<RestauranteModel> restaurante0 = restauranteRepository.findById(idRestaurante);
        if (restaurante0.isPresent()) {
            var restaurante = restaurante0.get();

            ComidaModel comidaToDelete = null;
            for (ComidaModel comida : restaurante.getComidas()) {
                if (comida.getIdComida().equals(idComida)) {
                    comidaToDelete = comida;
                    break;
                }
            }
            if (comidaToDelete != null) {
                restaurante.getComidas().remove(comidaToDelete);
                restauranteRepository.save(restaurante);
                return ResponseEntity.status(HttpStatus.OK).body("Comida removida com sucesso");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comida não encontrada.");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Restaurante não encontrado.");
    }
}
