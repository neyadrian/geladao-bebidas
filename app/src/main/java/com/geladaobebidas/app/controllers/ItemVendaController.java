package com.geladaobebidas.app.controllers;

import com.geladaobebidas.app.entities.ItemVenda;
import com.geladaobebidas.app.services.ItemVendaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itens-venda")
public class ItemVendaController {

    private final ItemVendaService itemVendaService;

    public ItemVendaController(ItemVendaService itemVendaService) {
        this.itemVendaService = itemVendaService;
    }

    @PostMapping
    public ResponseEntity<ItemVenda> criarItemVenda(@RequestBody ItemVenda itemVenda) {
        itemVendaService.salvar(itemVenda);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemVenda);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemVenda> buscarItemVendaPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(itemVendaService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ItemVenda>> listarTodos() {
        List<ItemVenda> itemVendas = itemVendaService.listarTodos();
        return ResponseEntity.ok().body(itemVendas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirItemVendaPorId(@PathVariable Long id) {
        itemVendaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}