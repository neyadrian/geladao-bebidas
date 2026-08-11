package com.geladaobebidas.app.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item_venda")
    private Long idItemVenda;

    @Column(name = "quantidade_item")
    private Integer quantidadeItem;

    @Column(name = "preco_unitario_item")
    private BigDecimal precoUnitarioItem;

    @ManyToOne
    @JoinColumn(name = "id_venda", nullable = false)
    private Venda venda;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;
}
