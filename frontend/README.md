# Geladão Bebidas — Painel (frontend)

Painel web em React para o sistema de estoque e vendas do Geladão Bebidas.

## Rodando localmente

```bash
npm install
npm run dev
```

Abre em http://localhost:5173.

Por padrão a API é esperada em `http://localhost:8080`. Para apontar para outro endereço,
copie `.env.example` para `.env` e ajuste `VITE_API_URL`.

## Login

Use o login e senha de um usuário já cadastrado no backend (tabela `usuario`).
Se ainda não tiver nenhum, cadastre um via `POST /usuarios` (rota livre de autenticação).

## Build de produção

```bash
npm run build
```

Gera os arquivos estáticos em `dist/`, prontos para publicar em qualquer servidor
de arquivos estáticos (Nginx, Vercel, Netlify, etc).
