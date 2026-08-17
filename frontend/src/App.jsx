import { useState } from "react";
import Login from "./components/Login.jsx";
import Sidebar from "./components/Sidebar.jsx";
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

export default function App() {
  const [auth, setAuth] = useState(null);
  const [page, setPage] = useState("produtos");

  if (!auth) {
    return <Login onLogin={setAuth} />;
  }

  const Page = PAGES[page] ?? ProductsPage;

  return (
    <div className="app-shell">
      <Sidebar page={page} onNavigate={setPage} auth={auth} onLogout={() => setAuth(null)} />
      <main className="main">
        <Page auth={auth} />
      </main>
    </div>
  );
}
