export const API_BASE = import.meta.env.VITE_API_URL || "http://localhost:8080";

export class ApiError extends Error {
  constructor(status, message) {
    super(message);
    this.status = status;
  }
}

function authHeader(auth) {
  if (!auth?.token) return {};
  return { Authorization: `Bearer ${auth.token}` };
}

export async function login(login, senha) {
  const res = await fetch(`${API_BASE}/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ login, senha }),
  });
  const body = await res.json().catch(() => null);
  if (!res.ok) {
    throw new ApiError(res.status, body?.message || "Login ou senha incorretos.");
  }
  return { token: body.token, login };
}

async function handle(res) {
  if (res.status === 204) return null;
  const isJson = res.headers.get("content-type")?.includes("application/json");
  const body = isJson ? await res.json().catch(() => null) : await res.text().catch(() => "");
  if (!res.ok) {
    if (res.status === 401) {
      window.dispatchEvent(new Event("geladao-auth-expired"));
    }
    const message =
      res.status === 401
        ? "Sessão expirada. Faça login novamente."
        : (isJson && body?.message) || (typeof body === "string" && body) || `Erro ${res.status}`;
    throw new ApiError(res.status, message);
  }
  return body;
}

export async function apiGet(path, auth) {
  const res = await fetch(`${API_BASE}${path}`, { headers: { ...authHeader(auth) } });
  return handle(res);
}

export async function apiSend(method, path, auth, payload) {
  const res = await fetch(`${API_BASE}${path}`, {
    method,
    headers: { "Content-Type": "application/json", ...authHeader(auth) },
    body: payload !== undefined ? JSON.stringify(payload) : undefined,
  });
  return handle(res);
}

export async function apiDelete(path, auth) {
  const res = await fetch(`${API_BASE}${path}`, {
    method: "DELETE",
    headers: { ...authHeader(auth) },
  });
  return handle(res);
}

// Downloads a PDF endpoint as a blob and triggers a save/open in a new tab.
export async function downloadPdf(path, auth, filename) {
  const res = await fetch(`${API_BASE}${path}`, { headers: { ...authHeader(auth) } });
  if (!res.ok) {
    if (res.status === 401) {
      window.dispatchEvent(new Event("geladao-auth-expired"));
    }
    throw new ApiError(res.status, res.status === 401 ? "Sessão expirada, entre novamente." : `Erro ${res.status}`);
  }
  const blob = await res.blob();
  const url = URL.createObjectURL(blob);
  const a = document.createElement("a");
  a.href = url;
  a.download = filename;
  document.body.appendChild(a);
  a.click();
  a.remove();
  setTimeout(() => URL.revokeObjectURL(url), 4000);
}

export const CATEGORIAS = [
  "CERVEJA",
  "REFRIGERANTE",
  "AGUA",
  "ENERGETICO",
  "DESTILADO",
  "VINHOS",
  "WHISKY",
  "VODKA",
  "OUTROS",
];

export const CATEGORIA_LABELS = {
  CERVEJA: "Cerveja",
  REFRIGERANTE: "Refrigerante",
  AGUA: "Água",
  ENERGETICO: "Energético",
  DESTILADO: "Destilado",
  VINHOS: "Vinho",
  WHISKY: "Whisky",
  VODKA: "Vodka",
  OUTROS: "Outros",
};

export const UNIDADES_VOLUME = ["ML", "LITRO"];
export const TIPOS_EMBALAGEM = ["FARDO", "CAIXA", "UNIDADE"];

export function formatBRL(value) {
  const n = typeof value === "number" ? value : parseFloat(value);
  if (Number.isNaN(n)) return "—";
  return n.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
}

export function formatDate(iso) {
  if (!iso) return "—";
  const d = new Date(iso);
  if (Number.isNaN(d.getTime())) return iso;
  return d.toLocaleString("pt-BR", { day: "2-digit", month: "2-digit", year: "numeric", hour: "2-digit", minute: "2-digit" });
}
