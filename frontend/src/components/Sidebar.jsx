import { useState } from "react";

const NAV_ITEMS = [
  { key: "produtos", label: "Produtos", icon: "🍺" },
  { key: "clientes", label: "Clientes", icon: "🧾" },
  { key: "vendas", label: "Vendas", icon: "🛒" },
  { key: "relatorios", label: "Relatórios", icon: "📊" },
];

export default function Sidebar({ page, onNavigate, auth, onLogout }) {
  const [isOpen, setIsOpen] = useState(false);
  const initial = auth?.login?.[0]?.toUpperCase() || "?";

  function handleNavigate(key) {
    onNavigate(key);
    setIsOpen(false);
  }

  return (
    <>
      <header className="mobile-header">
        <button className="hamburger-btn" onClick={() => setIsOpen(!isOpen)} aria-label="Menu">
          ☰
        </button>
        <div className="mobile-logo">
          <img src="/logo.png" alt="Logo Geladão" />
          <span>Geladão</span>
        </div>
      </header>

      <nav className={`sidebar ${isOpen ? "open" : ""}`} aria-label="Navegação principal">
        <div className="sidebar-brand">
          <img src="/logo.png" alt="" />
          <div className="brand-text">
            <span className="name">Geladão</span>
            <span className="sub">Estoque &amp; Vendas</span>
          </div>
          <button className="close-btn" onClick={() => setIsOpen(false)}>✕</button>
        </div>

        <ul className="nav-list">
          {NAV_ITEMS.map((item) => (
            <li key={item.key}>
              <button
                className={`nav-item ${page === item.key ? "active" : ""}`}
                onClick={() => handleNavigate(item.key)}
                aria-current={page === item.key ? "page" : undefined}
              >
                <span className="icon" aria-hidden="true">
                  {item.icon}
                </span>
                {item.label}
              </button>
            </li>
          ))}
        </ul>

        <div className="sidebar-footer">
          <div className="sidebar-user">
            <div className="avatar">{initial}</div>
            <div className="who">
              <div className="n">{auth?.login}</div>
              <div className="r">Conectado</div>
            </div>
          </div>
          <button className="logout-btn" onClick={onLogout}>
            Sair
          </button>
        </div>
      </nav>
      {isOpen && <div className="sidebar-overlay" onClick={() => setIsOpen(false)}></div>}
    </>
  );
}
