import { useEffect, useState } from "react";
import {
  apiGet,
  apiSend,
  apiDelete,
  ApiError,
  CATEGORIAS,
  CATEGORIA_LABELS,
  UNIDADES_VOLUME,
  TIPOS_EMBALAGEM,
  formatBRL,
} from "../api.js";
import { Banner, EmptyState, Loading } from "./Bits.jsx";

const emptyForm = {
  nomeProduto: "",
  precoProduto: "",
  quantidadeProduto: "",
  categoriaProduto: "0",
  volumeProduto: "",
  unidadeVolumeProduto: "ML",
  tipoEmbalagemProduto: "UNIDADE",
};

export default function ProductsPage({ auth }) {
  const [produtos, setProdutos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [form, setForm] = useState(emptyForm);
  const [saving, setSaving] = useState(false);

  async function load() {
    setLoading(true);
    setError("");
    try {
      const data = await apiGet("/produtos", auth);
      setProdutos(data || []);
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Não foi possível carregar os produtos.");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  function updateField(key, value) {
    setForm((f) => ({ ...f, [key]: value }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setSaving(true);
    setError("");
    try {
      await apiSend("POST", "/produtos", auth, {
        nomeProduto: form.nomeProduto.trim(),
        precoProduto: parseFloat(form.precoProduto),
        quantidadeProduto: parseInt(form.quantidadeProduto, 10),
        categoriaProduto: parseInt(form.categoriaProduto, 10),
        volumeProduto: parseInt(form.volumeProduto, 10),
        unidadeVolumeProduto: form.unidadeVolumeProduto,
        tipoEmbalagemProduto: form.tipoEmbalagemProduto,
      });
      setForm(emptyForm);
      await load();
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Não foi possível cadastrar o produto.");
    } finally {
      setSaving(false);
    }
  }

  async function handleDelete(id) {
    if (!window.confirm("Excluir este produto?")) return;
    try {
      await apiDelete(`/produtos/${id}`, auth);
      setProdutos((list) => list.filter((p) => p.idProduto !== id));
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Não foi possível excluir o produto.");
    }
  }

  return (
    <div>
      <header className="page-head">
        <div>
          <span className="eyebrow">Estoque</span>
          <h1>Produtos</h1>
          <p>Cadastre bebidas e acompanhe a quantidade disponível.</p>
        </div>
      </header>

      <Banner>{error}</Banner>

      <section className="card card-pad" style={{ marginBottom: 24 }}>
        <h2 style={{ fontSize: 17, marginBottom: 16 }}>Novo produto</h2>
        <form onSubmit={handleSubmit}>
          <div className="field-grid">
            <div className="field">
              <label htmlFor="nomeProduto">Nome</label>
              <input
                id="nomeProduto"
                required
                value={form.nomeProduto}
                onChange={(e) => updateField("nomeProduto", e.target.value)}
                placeholder="Cerveja Skol Lata"
              />
            </div>
            <div className="field">
              <label htmlFor="categoriaProduto">Categoria</label>
              <select
                id="categoriaProduto"
                value={form.categoriaProduto}
                onChange={(e) => updateField("categoriaProduto", e.target.value)}
              >
                {CATEGORIAS.map((cat, idx) => (
                  <option key={cat} value={idx}>
                    {CATEGORIA_LABELS[cat]}
                  </option>
                ))}
              </select>
            </div>
            <div className="field">
              <label htmlFor="precoProduto">Preço (R$)</label>
              <input
                id="precoProduto"
                required
                type="number"
                min="0"
                step="0.01"
                value={form.precoProduto}
                onChange={(e) => updateField("precoProduto", e.target.value)}
                placeholder="6,50"
              />
            </div>
            <div className="field">
              <label htmlFor="quantidadeProduto">Qtd. em estoque</label>
              <input
                id="quantidadeProduto"
                required
                type="number"
                min="0"
                step="1"
                value={form.quantidadeProduto}
                onChange={(e) => updateField("quantidadeProduto", e.target.value)}
                placeholder="50"
              />
            </div>
            <div className="field">
              <label htmlFor="volumeProduto">Volume</label>
              <input
                id="volumeProduto"
                required
                type="number"
                min="0"
                step="1"
                value={form.volumeProduto}
                onChange={(e) => updateField("volumeProduto", e.target.value)}
                placeholder="350"
              />
            </div>
            <div className="field">
              <label htmlFor="unidadeVolumeProduto">Unidade</label>
              <select
                id="unidadeVolumeProduto"
                value={form.unidadeVolumeProduto}
                onChange={(e) => updateField("unidadeVolumeProduto", e.target.value)}
              >
                {UNIDADES_VOLUME.map((u) => (
                  <option key={u} value={u}>
                    {u === "ML" ? "ml" : "litro"}
                  </option>
                ))}
              </select>
            </div>
            <div className="field">
              <label htmlFor="tipoEmbalagemProduto">Embalagem</label>
              <select
                id="tipoEmbalagemProduto"
                value={form.tipoEmbalagemProduto}
                onChange={(e) => updateField("tipoEmbalagemProduto", e.target.value)}
              >
                {TIPOS_EMBALAGEM.map((t) => (
                  <option key={t} value={t}>
                    {t.charAt(0) + t.slice(1).toLowerCase()}
                  </option>
                ))}
              </select>
            </div>
          </div>
          <div className="form-actions">
            <button className="btn btn-primary" type="submit" disabled={saving}>
              {saving ? "Salvando…" : "Cadastrar produto"}
            </button>
          </div>
        </form>
      </section>

      <section className="card">
        <div className="card-head">
          <h2>Produtos cadastrados</h2>
          <span className="count">{produtos.length}</span>
        </div>
        {loading ? (
          <Loading />
        ) : produtos.length === 0 ? (
          <EmptyState glyph="🍺" title="Nenhum produto ainda" hint="Cadastre o primeiro produto acima." />
        ) : (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Produto</th>
                  <th>Categoria</th>
                  <th>Embalagem</th>
                  <th>Preço</th>
                  <th>Estoque</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {produtos.map((p) => (
                  <tr key={p.idProduto}>
                    <td>
                      <strong>{p.nomeProduto}</strong>
                      <div style={{ fontSize: 12, color: "var(--ink-500)" }}>
                        {p.volumeProduto} {p.unidadeVolumeProduto === "ML" ? "ml" : "L"}
                      </div>
                    </td>
                    <td>
                      <span className="tag">{CATEGORIA_LABELS[CATEGORIAS[p.categoriaProduto]] || "—"}</span>
                    </td>
                    <td>{p.tipoEmbalagemProduto}</td>
                    <td className="numeric">{formatBRL(p.precoProduto)}</td>
                    <td className="numeric">{p.quantidadeProduto}</td>
                    <td>
                      <button className="btn btn-danger btn-sm" onClick={() => handleDelete(p.idProduto)}>
                        Excluir
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>
    </div>
  );
}
