package com.geladaobebidas.app.entities;

import com.geladaobebidas.app.enums.CategoriaProduto;
import com.geladaobebidas.app.enums.TipoEmbalagem;
import com.geladaobebidas.app.enums.UnidadeVolume;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    private Long idProduto;

    @Column(name = "nome_produto")
    private String nomeProduto;

    @Column(name = "preco_produto")
    private BigDecimal precoProduto;

    @Column(name = "preco_custo")
    private BigDecimal precoCusto;

    @Column(name = "quantidade_produto")
    private Integer quantidadeProduto;

    @Column(name = "estoque_minimo")
    private Integer estoqueMinimo;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "categoria_produto")
    private CategoriaProduto categoriaProduto;

    @Column(name = "volume_produto")
    private Integer volumeProduto;

    @Enumerated(EnumType.STRING)
    @Column(name = "unidade_volume_produto")
    private UnidadeVolume unidadeVolumeProduto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_embalagem_produto")
    private TipoEmbalagem tipoEmbalagemProduto;

    public boolean isEstoqueBaixo() {
        if (quantidadeProduto == null || estoqueMinimo == null) {
            return false;
        }
        return quantidadeProduto <= estoqueMinimo;
    }
    
    public BigDecimal getLucroUnitario() {
        if (precoProduto == null || precoCusto == null) {
            return BigDecimal.ZERO;
        }
        return precoProduto.subtract(precoCusto);
    }
}
