package com.geladaobebidas.app.controllers;

import com.geladaobebidas.app.dto.RegistrarVendaRequest;
import com.geladaobebidas.app.entities.Venda;
import com.geladaobebidas.app.services.VendaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @PostMapping
    public ResponseEntity<Venda> registrarVenda(@RequestBody RegistrarVendaRequest request) {
        Venda venda = vendaService.registrarVenda(
                request.getClienteId(),
                request.getUsuarioId(),
                request.getItens()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(venda);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venda> buscarVendaPorId(@PathVariable Long id) {
        Venda venda = vendaService.buscarPorId(id);
        return ResponseEntity.ok(venda);
    }

    @GetMapping
    public ResponseEntity<List<Venda>> listarTodas() {
        List<Venda> vendas = vendaService.listarTodas();
        return ResponseEntity.ok(vendas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        vendaService.excluir(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
