package com.geladaobebidas.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class RegistrarVendaRequest {

    private Long clientId;
    private Long usuarioId;
    private List<ItemVendaRequest> itemVendaRequests;
}
