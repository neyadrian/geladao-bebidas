package com.geladaobebidas.app.entities;

import com.geladaobebidas.app.enums.TipoMovimentacao;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimentacao")
    private Long idMovimentacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimentacao")
    private TipoMovimentacao tipoMovimentacao;

    @Column(name = "quantidade_movimentacao")
    private Integer quantidadeMovimentacao;

    @Column(name = "data_movimentacao")
    private LocalDateTime dataMovimentacao;

    @Column(name = "motivo_movimentacao")
    private String motivoMovimentacao;

    @ManyToOne
    @JoinColumn(name = "id_produto")
    private Produto produto;
}
