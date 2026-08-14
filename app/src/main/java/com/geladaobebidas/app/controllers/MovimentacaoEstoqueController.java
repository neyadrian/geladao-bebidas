package com.geladaobebidas.app.controllers;

import com.geladaobebidas.app.entities.MovimentacaoEstoque;
import com.geladaobebidas.app.services.MovimentacaoEstoqueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimentacoes-estoque")
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService  movimentacaoEstoqueService;

    public MovimentacaoEstoqueController(MovimentacaoEstoqueService movimentacaoEstoqueService) {
        this.movimentacaoEstoqueService = movimentacaoEstoqueService;
    }

    @PostMapping
    public ResponseEntity<MovimentacaoEstoque> salvar(@RequestBody MovimentacaoEstoque movimentacaoEstoque) {
        movimentacaoEstoqueService.salvar(movimentacaoEstoque);
        return  ResponseEntity.status(HttpStatus.CREATED).body(movimentacaoEstoque);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimentacaoEstoque> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(movimentacaoEstoqueService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<MovimentacaoEstoque>> listarTodas() {
        List<MovimentacaoEstoque> movimentacaoEstoque = movimentacaoEstoqueService.listarTodas();
        return ResponseEntity.ok().body(movimentacaoEstoque);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        movimentacaoEstoqueService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}