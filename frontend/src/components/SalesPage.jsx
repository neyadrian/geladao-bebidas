import { useEffect, useState } from "react";
import { apiGet, apiSend, downloadPdf, ApiError, formatBRL, formatDate } from "../api.js";
import { Banner, EmptyState, Loading } from "./Bits.jsx";
import Pagination, { usePagination } from "./Pagination.jsx";

let itemKeySeq = 1;
function newItem() {
  return { key: itemKeySeq++, produtoId: "", quantidade: "1" };
}

export default function SalesPage({ auth }) {
  const [vendas, setVendas] = useState([]);
  const [clientes, setClientes] = useState([]);
  const [usuarios, setUsuarios] = useState([]);
  const [produtos, setProdutos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [ok, setOk] = useState("");

  const [clienteId, setClienteId] = useState("");
  const [usuarioId, setUsuarioId] = useState("");
  const [formaPagamento, setFormaPagamento] = useState("DINHEIRO");
  const [itens, setItens] = useState([newItem()]);
  const [saving, setSaving] = useState(false);
  const [downloadingId, setDownloadingId] = useState(null);
  const [page, setPage] = useState(1);

  async function load() {
    setLoading(true);
    setError("");
    try {
      const [v, c, u, p] = await Promise.all([
        apiGet("/vendas", auth),
        apiGet("/clientes", auth),
        apiGet("/usuarios", auth),
        apiGet("/produtos", auth),
      ]);
      setVendas((v || []).sort((a, b) => (a.dataVenda < b.dataVenda ? 1 : -1)));
      setClientes(c || []);
      setUsuarios(u || []);
      setProdutos(p || []);
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Não foi possível carregar os dados.");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  function updateItem(key, field, value) {
    setItens((list) => list.map((it) => (it.key === key ? { ...it, [field]: value } : it)));
  }

  function addItem() {
    setItens((list) => [...list, newItem()]);
  }

  function removeItem(key) {
    setItens((list) => (list.length > 1 ? list.filter((it) => it.key !== key) : list));
  }

  function estimatedTotal() {
    return itens.reduce((sum, it) => {
      const produto = produtos.find((p) => String(p.idProduto) === String(it.produtoId));
      const qty = parseInt(it.quantidade, 10) || 0;
      if (!produto) return sum;
      return sum + produto.precoProduto * qty;
    }, 0);
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setOk("");

    if (!clienteId || !usuarioId) {
      setError("Selecione o cliente e o vendedor.");
      return;
    }
    const validItens = itens
      .filter((it) => it.produtoId && parseInt(it.quantidade, 10) > 0)
      .map((it) => ({ produtoId: parseInt(it.produtoId, 10), quantidade: parseInt(it.quantidade, 10) }));

    if (validItens.length === 0) {
      setError("Adicione ao menos um item com produto e quantidade.");
      return;
    }

    setSaving(true);
    try {
      await apiSend("POST", "/vendas", auth, { clienteId: parseInt(clienteId, 10), usuarioId: parseInt(usuarioId, 10), formaPagamento, itens: validItens });
      setOk("Venda registrada com sucesso.");
      setClienteId("");
      setFormaPagamento("DINHEIRO");
      setItens([newItem()]);
      await load();
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Não foi possível registrar a venda.");
    } finally {
      setSaving(false);
    }
  }

  async function handleNota(id) {
    setDownloadingId(id);
    setError("");
    try {
      await downloadPdf(`/vendas/${id}/nota`, auth, `nota-venda-${id}.pdf`);
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Não foi possível gerar a nota.");
    } finally {
      setDownloadingId(null);
    }
  }

  async function handlePayFiado(id) {
    if (!window.confirm("Confirmar o pagamento deste fiado?")) return;
    setError("");
    try {
      await apiSend("PUT", `/vendas/${id}/pagar-fiado`, auth);
      setOk("Pagamento registrado com sucesso!");
      await load();
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Não foi possível registrar o pagamento.");
    }
  }

  return (
    <div>
      <header className="page-head">
        <div>
          <span className="eyebrow">Balcão</span>
          <h1>Vendas</h1>
          <p>Registre um pedido e baixe a nota em PDF.</p>
        </div>
      </header>

      <Banner type="error">{error}</Banner>
      <Banner type="ok">{ok}</Banner>

      <section className="card card-pad" style={{ marginBottom: 24 }}>
        <h2 style={{ fontSize: 17, marginBottom: 16 }}>Nova venda</h2>
        <form onSubmit={handleSubmit}>
          <div className="field-grid">
            <div className="field">
              <label htmlFor="clienteId">Cliente</label>
              <select id="clienteId" value={clienteId} onChange={(e) => setClienteId(e.target.value)}>
                <option value="">Selecione…</option>
                {clientes.map((c) => (
                  <option key={c.idCliente} value={c.idCliente}>
                    {c.nomeCliente}
                  </option>
                ))}
              </select>
            </div>
            <div className="field">
              <label htmlFor="usuarioId">Vendedor</label>
              <select id="usuarioId" value={usuarioId} onChange={(e) => setUsuarioId(e.target.value)}>
                <option value="">Selecione…</option>
                {usuarios.map((u) => (
                  <option key={u.idUsuario} value={u.idUsuario}>
                    {u.nomeUsuario}
                  </option>
                ))}
              </select>
            </div>
            <div className="field">
              <label htmlFor="formaPagamento">Pagamento</label>
              <select id="formaPagamento" value={formaPagamento} onChange={(e) => setFormaPagamento(e.target.value)}>
                <option value="DINHEIRO">Dinheiro</option>
                <option value="PIX">Pix</option>
                <option value="CARTAO">Cartão</option>
                <option value="FIADO">Fiado</option>
              </select>
            </div>
          </div>

          <div style={{ marginTop: 20 }}>
            <label style={{ fontSize: 13, fontWeight: 600, color: "var(--ink-700)" }}>Itens</label>
            <div style={{ display: "flex", flexDirection: "column", gap: 10, marginTop: 8 }}>
              {itens.map((it) => (
                <div key={it.key} style={{ display: "flex", gap: 10, alignItems: "flex-end", flexWrap: "wrap" }}>
                  <div className="field" style={{ flex: "2 1 220px" }}>
                    <select value={it.produtoId} onChange={(e) => updateItem(it.key, "produtoId", e.target.value)}>
                      <option value="">Produto…</option>
                      {produtos.map((p) => (
                        <option key={p.idProduto} value={p.idProduto}>
                          {p.nomeProduto} — {formatBRL(p.precoProduto)}
                        </option>
                      ))}
                    </select>
                  </div>
                  <div className="field" style={{ flex: "0 1 100px" }}>
                    <input
                      type="number"
                      min="1"
                      value={it.quantidade}
                      onChange={(e) => updateItem(it.key, "quantidade", e.target.value)}
                      placeholder="Qtd."
                    />
                  </div>
                  <button
                    type="button"
                    className="btn btn-danger btn-sm"
                    onClick={() => removeItem(it.key)}
                    disabled={itens.length === 1}
                    aria-label="Remover item"
                  >
                    Remover
                  </button>
                </div>
              ))}
            </div>
            <button type="button" className="btn btn-ghost btn-sm" style={{ marginTop: 10 }} onClick={addItem}>
              + Adicionar item
            </button>
          </div>

          <div className="form-actions" style={{ alignItems: "center", justifyContent: "space-between" }}>
            <div className="numeric" style={{ fontSize: 15, color: "var(--ink-700)" }}>
              Total estimado: <strong>{formatBRL(estimatedTotal())}</strong>
            </div>
            <button className="btn btn-primary" type="submit" disabled={saving}>
              {saving ? "Registrando…" : "Registrar venda"}
            </button>
          </div>
        </form>
      </section>

      <section className="card">
        <div className="card-head">
          <h2>Últimas vendas</h2>
          <span className="count">{vendas.length}</span>
        </div>
        {loading ? (
          <Loading />
        ) : vendas.length === 0 ? (
          <EmptyState glyph="🛒" title="Nenhuma venda registrada" hint="Registre a primeira venda acima." />
        ) : (
          (() => {
            const { pageItems, totalPages, safePage, pageSize } = usePagination(vendas, page);
            return (
              <>
                <div className="table-wrap">
                  <table>
                    <thead>
                      <tr>
                        <th>Venda</th>
                        <th>Cliente</th>
                        <th>Data</th>
                        <th>Total</th>
                        <th>Pagamento</th>
                        <th>Status</th>
                        <th></th>
                      </tr>
                    </thead>
                    <tbody>
                      {pageItems.map((v) => (
                        <tr key={v.idVenda}>
                          <td className="numeric">#{v.idVenda}</td>
                          <td>{v.cliente?.nomeCliente || "—"}</td>
                          <td>{formatDate(v.dataVenda)}</td>
                          <td className="numeric">{formatBRL(v.valorTotalVenda)}</td>
                          <td>
                            {v.formaPagamento === "FIADO" ? <span className="badge badge-warning">Fiado</span> : <span className="badge badge-default">{v.formaPagamento || "—"}</span>}
                          </td>
                          <td>
                            {v.statusPagamento === "PENDENTE" ? (
                              <span style={{ color: "var(--red-600)", fontWeight: 600 }}>Pendente</span>
                            ) : (
                              <span style={{ color: "var(--green-600)", fontWeight: 600 }}>Pago</span>
                            )}
                          </td>
                          <td>
                            <div className="row-actions">
                              {v.statusPagamento === "PENDENTE" && (
                                <button
                                  className="btn btn-primary btn-sm"
                                  onClick={() => handlePayFiado(v.idVenda)}
                                >
                                  Pagar
                                </button>
                              )}
                              <button
                                className="btn btn-gold btn-sm"
                                onClick={() => handleNota(v.idVenda)}
                                disabled={downloadingId === v.idVenda}
                              >
                                {downloadingId === v.idVenda ? "Gerando…" : "Baixar nota"}
                              </button>
                            </div>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
                <Pagination
                  page={safePage}
                  totalPages={totalPages}
                  total={vendas.length}
                  pageSize={pageSize}
                  onChange={setPage}
                />
              </>
            );
          })()
        )}
      </section>
    </div>
  );
}
