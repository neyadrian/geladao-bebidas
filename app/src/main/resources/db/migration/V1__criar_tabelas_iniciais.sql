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
)
