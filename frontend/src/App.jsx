import { useEffect, useState } from "react";
import Login from "./components/Login.jsx";
import Navbar from "./components/Navbar.jsx";
import ProductsPage from "./components/ProductsPage.jsx";
import ClientsPage from "./components/ClientsPage.jsx";
import SalesPage from "./components/SalesPage.jsx";
import ReportsPage from "./components/ReportsPage.jsx";

const PAGES = {
  produtos: ProductsPage,
  clientes: ClientsPage,
  vendas: SalesPage,
  relatorios: ReportsPage,
};

const STORAGE_KEY = "geladao-auth";

function loadStoredAuth() {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) return null;
    const parsed = JSON.parse(raw);
    if (parsed?.token && parsed?.login) return parsed;
    return null;
  } catch {
    return null;
  }
}

export default function App() {
  const [auth, setAuth] = useState(() => loadStoredAuth());
  const [page, setPage] = useState("produtos");

  function handleLogin(newAuth) {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(newAuth));
    setAuth(newAuth);
  }

  function handleLogout() {
    localStorage.removeItem(STORAGE_KEY);
    setAuth(null);
  }

  // If any request ever comes back 401 (token expired or invalid),
  // the "geladao-auth-expired" event is fired — log out automatically.
  useEffect(() => {
    function onExpired() {
      handleLogout();
    }
    window.addEventListener("geladao-auth-expired", onExpired);
    return () => window.removeEventListener("geladao-auth-expired", onExpired);
  }, []);

  if (!auth) {
    return <Login onLogin={handleLogin} />;
  }

  const Page = PAGES[page] ?? ProductsPage;

  return (
    <div className="app-shell pt-16">
      <Navbar page={page} onNavigate={setPage} auth={auth} onLogout={handleLogout} />
      <main className="main">
        <Page auth={auth} />
      </main>
    </div>
  );
}

