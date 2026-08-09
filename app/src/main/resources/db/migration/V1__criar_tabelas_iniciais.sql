CREATE DATABASE IF NOT EXISTS banco_geladao_bebidas;
USE banco_geladao_bebidas;

CREATE TABLE cliente (
    id_cliente BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_cliente VARCHAR(50) NOT NULL,
    endereco_cliente VARCHAR(50) NOT NULL,
    telefone_cliente VARCHAR(50) NOT NULL
);

CREATE TABLE produto (
    id_produto BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_produto VARCHAR(50) NOT NULL,
    preco_produto DECIMAL(15, 2) NOT NULL,
    quantidade_produto INT NOT NULL,
    volume_produto INT NOT NULL,
    categoria_produto TINYINT NOT NULL,
    unidade_volume_produto VARCHAR(20) NOT NULL,
    tipo_embalagem_produto VARCHAR(20) NOT NULL
);

CREATE TABLE usuario (
    id_usuario BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_usuario VARCHAR(50) NOT NULL,
    cpf VARCHAR(50) NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(60) NOT NULL
);

CREATE TABLE venda (
    id_venda BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_venda DATETIME NOT NULL,
    valor_total_venda DECIMAL(15, 2) NOT NULL,
    id_cliente BIGINT,
    id_usuario BIGINT NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);