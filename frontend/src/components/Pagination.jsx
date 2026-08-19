const PAGE_SIZE = 8;

export function usePagination(items, page) {
  const totalPages = Math.max(1, Math.ceil(items.length / PAGE_SIZE));
  const safePage = Math.min(page, totalPages);
  const start = (safePage - 1) * PAGE_SIZE;
  const pageItems = items.slice(start, start + PAGE_SIZE);
  return { pageItems, totalPages, safePage, pageSize: PAGE_SIZE };
}

export default function Pagination({ page, totalPages, total, pageSize, onChange }) {
  if (total === 0) return null;

  const start = (page - 1) * pageSize + 1;
  const end = Math.min(page * pageSize, total);

  const pages = [];
  for (let p = 1; p <= totalPages; p++) {
    if (p === 1 || p === totalPages || Math.abs(p - page) <= 1) {
      pages.push(p);
    } else if (pages[pages.length - 1] !== "…") {
      pages.push("…");
    }
  }

  return (
    <div className="pagination">
      <span className="info">
        {start}–{end} de {total}
      </span>
      <div className="controls">
        <button className="page-btn" onClick={() => onChange(page - 1)} disabled={page <= 1} aria-label="Página anterior">
          ‹
        </button>
        {pages.map((p, idx) =>
          p === "…" ? (
            <span key={`ellipsis-${idx}`} style={{ padding: "0 4px", color: "var(--ink-500)" }}>
              …
            </span>
          ) : (
            <button
              key={p}
              className={`page-btn ${p === page ? "active" : ""}`}
              onClick={() => onChange(p)}
              aria-current={p === page ? "page" : undefined}
            >
              {p}
            </button>
          )
        )}
        <button
          className="page-btn"
          onClick={() => onChange(page + 1)}
          disabled={page >= totalPages}
          aria-label="Próxima página"
        >
          ›
        </button>
      </div>
    </div>
  );
}
