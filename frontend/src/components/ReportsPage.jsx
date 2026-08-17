import { useState } from "react";
import { downloadPdf, ApiError } from "../api.js";
import { Banner } from "./Bits.jsx";

const MESES = [
  "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
  "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro",
];

export default function ReportsPage({ auth }) {
  const now = new Date();
  const [ano, setAno] = useState(String(now.getFullYear()));
  const [mes, setMes] = useState(String(now.getMonth() + 1));
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [ok, setOk] = useState("");

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setOk("");
    setLoading(true);
    try {
      await downloadPdf(
        `/vendas/relatorio-mensal?ano=${ano}&mes=${mes}`,
        auth,
        `relatorio-${mes}-${ano}.pdf`
      );
      setOk(`Relatório de ${MESES[parseInt(mes, 10) - 1]}/${ano} baixado.`);
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Não foi possível gerar o relatório.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div>
      <header className="page-head">
        <div>
          <span className="eyebrow">Fechamento</span>
          <h1>Relatórios</h1>
          <p>Baixe o resumo de vendas de um mês em PDF.</p>
        </div>
      </header>

      <Banner type="error">{error}</Banner>
      <Banner type="ok">{ok}</Banner>

      <section className="card card-pad">
        <h2 style={{ fontSize: 17, marginBottom: 16 }}>Relatório mensal</h2>
        <form onSubmit={handleSubmit}>
          <div className="field-grid" style={{ maxWidth: 360 }}>
            <div className="field">
              <label htmlFor="mes">Mês</label>
              <select id="mes" value={mes} onChange={(e) => setMes(e.target.value)}>
                {MESES.map((nome, idx) => (
                  <option key={nome} value={idx + 1}>
                    {nome}
                  </option>
                ))}
              </select>
            </div>
            <div className="field">
              <label htmlFor="ano">Ano</label>
              <input
                id="ano"
                type="number"
                value={ano}
                onChange={(e) => setAno(e.target.value)}
                min="2000"
                max="2100"
              />
            </div>
          </div>
          <div className="form-actions" style={{ justifyContent: "flex-start" }}>
            <button className="btn btn-primary" type="submit" disabled={loading}>
              {loading ? "Gerando…" : "Gerar e baixar PDF"}
            </button>
          </div>
        </form>
      </section>
    </div>
  );
}
