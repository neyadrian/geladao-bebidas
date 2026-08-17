import { useState } from "react";
import { apiGet, ApiError } from "../api.js";

export default function Login({ onLogin }) {
  const [login, setLogin] = useState("");
  const [senha, setSenha] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  async function handleSubmit(e) {
    e.preventDefault();
    if (!login.trim() || !senha) {
      setError("Preencha login e senha.");
      return;
    }
    setLoading(true);
    setError("");
    const auth = { login: login.trim(), senha };
    try {
      // Any protected route confirms the credentials are valid.
      await apiGet("/produtos", auth);
      onLogin(auth);
    } catch (err) {
      if (err instanceof ApiError && err.status === 401) {
        setError("Login ou senha incorretos.");
      } else {
        setError("Não foi possível conectar ao servidor. Confira se a API está no ar.");
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="login-screen">
      <div className="login-card">
        <img src="/logo.png" alt="Geladão Bebidas" />
        <h1>Geladão Bebidas</h1>
        <span className="tagline">Painel de estoque e vendas</span>
        <form onSubmit={handleSubmit}>
          {error && <div className="banner banner-error">{error}</div>}
          <div className="field">
            <label htmlFor="login">Login</label>
            <input
              id="login"
              autoFocus
              value={login}
              onChange={(e) => setLogin(e.target.value)}
              placeholder="seu.login"
              autoComplete="username"
            />
          </div>
          <div className="field">
            <label htmlFor="senha">Senha</label>
            <input
              id="senha"
              type="password"
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              placeholder="••••••••"
              autoComplete="current-password"
            />
          </div>
          <button className="btn btn-primary" type="submit" disabled={loading}>
            {loading ? "Entrando…" : "Entrar"}
          </button>
        </form>
        <p className="login-foot">Acesso restrito à equipe do Geladão Bebidas.</p>
      </div>
    </div>
  );
}
