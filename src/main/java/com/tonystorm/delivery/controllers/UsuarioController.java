package com.tonystorm.delivery.controllers;

import com.tonystorm.delivery.dtos.PedidoDto;
import com.tonystorm.delivery.dtos.UsuarioDto;
import com.tonystorm.delivery.models.comida.ComidaModel;
import com.tonystorm.delivery.models.pedido.PedidoModel;
import com.tonystorm.delivery.models.usuario.UsuarioModel;
import com.tonystorm.delivery.repositories.ComidaRepository;
import com.tonystorm.delivery.repositories.PedidoRepository;
import com.tonystorm.delivery.repositories.UsuarioRepository;
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
    public ResponseEntity<UsuarioModel> criarUsuario(@RequestBody @Valid UsuarioDto usuarioDto) {
        var usuario = new UsuarioModel();
        BeanUtils.copyProperties(usuarioDto, usuario);
        UsuarioModel novoUsuario = usuarioRepository.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @PostMapping("/{id}/pedidos")
    @Transactional
    public ResponseEntity<?> criarPedido(@PathVariable(value = "id") UUID id,
                                                   @RequestBody @Valid PedidoDto pedidoDto) {
        Optional<UsuarioModel> usuarioOptional = usuarioRepository.findById(id);

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

            Double precoTotal = comidas.stream().mapToDouble(ComidaModel::getPreco).sum();

            var pedido = new PedidoModel();
            BeanUtils.copyProperties(pedidoDto, pedido);

            pedido.setUsuario(usuario);
            pedido.setPrecoTotal(precoTotal);
            pedido.setComidas(comidas);

            PedidoModel novoPedido = pedidoRepository.save(pedido);

            return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
