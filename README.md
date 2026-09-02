# 🍻 Geladão Bebidas - Sistema de Gestão e PDV

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![React](https://img.shields.io/badge/React-18-61DAFB?style=for-the-badge&logo=react&logoColor=black)

**Geladão Bebidas** é uma aplicação Full-Stack desenvolvida para gerenciar o estoque, as vendas e a parte financeira de uma distribuidora de bebidas. O principal foco do projeto reside em sua **arquitetura de Back-End**, implementando lógicas robustas de transações, segurança e regras de negócio.

---

## 💻 Arquitetura e Tecnologias (Back-End)

A API foi construída seguindo as melhores práticas do ecosistema Spring, focando em segurança, manutenibilidade e integridade de dados.

- **Linguagem:** Java 17
- **Framework:** Spring Boot 3
- **Persistência:** Spring Data JPA / Hibernate
- **Banco de Dados:** MySQL 8.0
- **Versionamento de Banco:** Flyway Migrations
- **Segurança:** Spring Security + JWT (JSON Web Tokens) com BCrypt
- **Geração de Relatórios:** iTextPDF (Geração de PDFs em tempo real)
- **Containerização:** Docker e Docker Compose

---

## ⚙️ Principais Funcionalidades do Motor (API)

O Back-End atua como o cérebro da operação, garantindo que o sistema nunca chegue a um estado inválido:

### 1. Transações e Trava de Estoque
Ao realizar uma venda, o sistema processa a transação em cadeia. Ele calcula o total, aplica descontos proporcionais no lucro e executa a **baixa automática do estoque**. 
- Existe uma trava de segurança em nível de serviço (`VendaService`): caso a quantidade de itens na venda supere o estoque disponível no MySQL, a transação é revertida (`rollback`) e uma Exceção Customizada é disparada.

### 2. Controle Financeiro Avançado (Lucro e Fiado)
- **Cálculo de Lucro Líquido:** A API salva não só o total da venda, mas o `valor_total_lucro`, subtraindo o preço de custo do preço de venda de cada item (mesmo com a aplicação de descontos).
- **Controle de Dívidas (Fiado):** Implementação do fluxo de Status da Venda. Vendas pagas no "Fiado" nascem como `PENDENTE`, exigindo uma chamada posterior ao endpoint `PUT /vendas/{id}/pagar-fiado` para registrar a quitação.

### 3. Segurança e Tratamento Global de Erros
- **Stateless Auth:** Toda requisição (exceto login) exige um token JWT válido, com validação injetada através do `JwtAuthFilter`.
- **GlobalExceptionHandler:** Todos os erros de negócio (`RecursoNaoEncontradoException`, erros de estoque, etc.) são interceptados globalmente pela API e traduzidos para respostas HTTP limpas e JSON formatados (`400 Bad Request`, `404 Not Found`), garantindo que o Front-End receba a mensagem amigável e não um *Stack Trace* sujo.

### 4. Flyway Migrations
O banco de dados é versionado de forma rigorosa. Todas as alterações na estrutura (criação de tabelas e adição de colunas como a de "Desconto") são feitas exclusivamente através de scripts `.sql` controlados pelo Flyway.

### 5. PDF Generator
O Back-End possui um motor próprio (`NotaVendaGenerator`) que "desenha" bytes de PDF em memória e realiza o streaming binário via endpoint (`GET /vendas/{id}/nota`), gerando recibos instantâneos sem salvar arquivos temporários no disco do servidor.

---

## 🎨 O Front-End

Apesar do foco estar no Back-End, a aplicação conta com um Front-End completo desenvolvido em **React** com **Vite**, consumindo a API Rest.
- Interface responsiva com design "Mobile-First" e menu hambúrguer dinâmico.
- Componentes modulares, uso massivo de Hooks e paginação via Front-End.

---

## 🚀 Como Rodar o Projeto Localmente

Você precisará do **Docker** e do **Docker Compose** instalados na sua máquina.

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/geladao-bebidas.git
cd geladao-bebidas
```

2. Suba o Banco de Dados e a API Java através do Docker Compose:
```bash
cd app
docker-compose up -d --build
```
> O Docker irá baixar o MySQL, subir o banco de dados e, em seguida, compilar e executar o Back-End Java automaticamente na porta `8080`.

3. Inicie o Front-End (Em outro terminal):
```bash
cd frontend
npm install
npm run dev
```

Acesse a interface gráfica através do link gerado pelo Vite (geralmente `http://localhost:5173`).

---
Desenvolvido como projeto prático para Portfólio de Back-End. 👨‍💻
