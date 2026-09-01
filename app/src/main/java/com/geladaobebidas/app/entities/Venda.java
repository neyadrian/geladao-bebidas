package com.geladaobebidas.app.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venda")
    private Long idVenda;

    @Column(name = "data_venda")
    private LocalDateTime dataVenda;

    @Column(name = "valor_total_venda")
    private BigDecimal valorTotalVenda;

    @Column(name = "valor_total_lucro")
    private BigDecimal valorTotalLucro;

    @Column(name = "forma_pagamento")
    private String formaPagamento;

    @Column(name = "status_pagamento")
    private String statusPagamento;

    @Column(name = "percentual_desconto")
    private BigDecimal percentualDesconto;

    @Column(name = "valor_desconto")
    private BigDecimal valorDesconto;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
}