import { useEffect, useState } from "react";
import { apiGet, apiSend, apiDelete, ApiError } from "../api.js";
import { Banner, EmptyState, Loading } from "./Bits.jsx";

const emptyForm = { nomeCliente: "", enderecoCliente: "", telefoneCliente: "" };

export default function ClientsPage({ auth }) {
  const [clientes, setClientes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [form, setForm] = useState(emptyForm);
  const [saving, setSaving] = useState(false);

  async function load() {
    setLoading(true);
    setError("");
    try {
      const data = await apiGet("/clientes", auth);
      setClientes(data || []);
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Não foi possível carregar os clientes.");
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
      await apiSend("POST", "/clientes", auth, {
        nomeCliente: form.nomeCliente.trim(),
        enderecoCliente: form.enderecoCliente.trim(),
        telefoneCliente: form.telefoneCliente.trim(),
      });
      setForm(emptyForm);
      await load();
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Não foi possível cadastrar o cliente.");
    } finally {
      setSaving(false);
    }
  }

  async function handleDelete(id) {
    if (!window.confirm("Excluir este cliente?")) return;
    try {
      await apiDelete(`/clientes/${id}`, auth);
      setClientes((list) => list.filter((c) => c.idCliente !== id));
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Não foi possível excluir o cliente.");
    }
  }

  return (
    <div>
      <header className="page-head">
        <div>
          <span className="eyebrow">Cadastro</span>
          <h1>Clientes</h1>
          <p>Guarde os dados de quem compra no Geladão.</p>
        </div>
      </header>

      <Banner>{error}</Banner>

      <section className="card card-pad" style={{ marginBottom: 24 }}>
        <h2 style={{ fontSize: 17, marginBottom: 16 }}>Novo cliente</h2>
        <form onSubmit={handleSubmit}>
          <div className="field-grid">
            <div className="field">
              <label htmlFor="nomeCliente">Nome</label>
              <input
                id="nomeCliente"
                required
                value={form.nomeCliente}
                onChange={(e) => updateField("nomeCliente", e.target.value)}
                placeholder="João da Silva"
              />
            </div>
            <div className="field">
              <label htmlFor="telefoneCliente">Telefone</label>
              <input
                id="telefoneCliente"
                required
                value={form.telefoneCliente}
                onChange={(e) => updateField("telefoneCliente", e.target.value)}
                placeholder="(88) 9 9999-9999"
              />
            </div>
            <div className="field" style={{ gridColumn: "1 / -1" }}>
              <label htmlFor="enderecoCliente">Endereço</label>
              <input
                id="enderecoCliente"
                required
                value={form.enderecoCliente}
                onChange={(e) => updateField("enderecoCliente", e.target.value)}
                placeholder="Rua, número, bairro"
              />
            </div>
          </div>
          <div className="form-actions">
            <button className="btn btn-primary" type="submit" disabled={saving}>
              {saving ? "Salvando…" : "Cadastrar cliente"}
            </button>
          </div>
        </form>
      </section>

      <section className="card">
        <div className="card-head">
          <h2>Clientes cadastrados</h2>
          <span className="count">{clientes.length}</span>
        </div>
        {loading ? (
          <Loading />
        ) : clientes.length === 0 ? (
          <EmptyState glyph="🧾" title="Nenhum cliente ainda" hint="Cadastre o primeiro cliente acima." />
        ) : (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Nome</th>
                  <th>Telefone</th>
                  <th>Endereço</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {clientes.map((c) => (
                  <tr key={c.idCliente}>
                    <td>
                      <strong>{c.nomeCliente}</strong>
                    </td>
                    <td>{c.telefoneCliente}</td>
                    <td>{c.enderecoCliente}</td>
                    <td>
                      <button className="btn btn-danger btn-sm" onClick={() => handleDelete(c.idCliente)}>
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
