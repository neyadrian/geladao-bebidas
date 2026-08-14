package com.geladaobebidas.app.controllers;

import com.geladaobebidas.app.dto.RegistrarVendaRequest;
import com.geladaobebidas.app.entities.Venda;
import com.geladaobebidas.app.report.NotaVendaGenerator;
import com.geladaobebidas.app.services.VendaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    private final VendaService vendaService;
    private final NotaVendaGenerator notaVendaGenerator;

    public VendaController(VendaService vendaService, NotaVendaGenerator notaVendaGenerator) {
        this.vendaService = vendaService;
        this.notaVendaGenerator = notaVendaGenerator;
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

    @GetMapping("/{id}/nota")
    public ResponseEntity<byte[]> gerarNota(@PathVariable Long id) throws Exception {
        Venda venda = vendaService.buscarPorId(id);
        byte[] pdf = notaVendaGenerator.gerar(venda);

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "inline; filename=nota-venda-" + id + ".pdf")
                .body(pdf);
    }
}