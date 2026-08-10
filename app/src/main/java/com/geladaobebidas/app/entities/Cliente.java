package com.geladaobebidas.app.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data // anotação do lombok que gera automaticamente Getters e Setters, Equals e HashCode
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "nome_cliente")
    private String nomeCliente;

    @Column(name = "endereco_cliente")
    private String enderecoCliente;

    @Column(name = "telefone_cliente")
    private String telefoneCliente;

}
