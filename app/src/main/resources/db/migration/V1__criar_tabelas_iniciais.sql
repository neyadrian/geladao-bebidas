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
    id_cliente BIGINT NOT NULL,
    id_usuario BIGINT NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE item_venda (
    id_item_venda BIGINT AUTO_INCREMENT PRIMARY KEY,
    quantidade_item INT NOT NULL,
    preco_unitario_item DECIMAL(15, 2) NOT NULL,
    id_venda BIGINT NOT NUll,
    id_produto BIGINT NOT NULL,
    FOREIGN KEY (id_venda) REFERENCES venda(id_venda),
    FOREIGN KEY (id_produto) REFERENCES produto(id_produto)
);

CREATE TABLE movimentacao_estoque (
    id_movimentacao BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_movimentacao VARCHAR(20) NOT NULL,
    quantidade_movimentacao INT NOT NULL,
    data_movimentacao DATETIME NOT NULL,
    motivo_movimentacao VARCHAR(200),
    id_produto BIGINT NOT NULL,
    FOREIGN KEY (id_produto) REFERENCES produto(id_produto)
);