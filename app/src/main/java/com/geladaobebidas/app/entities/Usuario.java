package com.geladaobebidas.app.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nome_usuario")
    private String nomeUsuario;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "senha")
    private String senha;
}
