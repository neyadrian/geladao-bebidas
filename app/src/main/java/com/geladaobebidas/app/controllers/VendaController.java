package com.geladaobebidas.app.controllers;

import com.geladaobebidas.app.dto.RegistrarVendaRequest;
import com.geladaobebidas.app.entities.Venda;
import com.geladaobebidas.app.services.VendaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                request.getClientId(),
                request.getUsuarioId(),
                request.getItens()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(venda);
    }
}
