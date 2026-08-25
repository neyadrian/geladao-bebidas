package com.geladaobebidas.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class RegistrarVendaRequest {

    private Long clienteId;
    private Long usuarioId;
    private List<ItemVendaRequest> itens;
    private String formaPagamento;
}
