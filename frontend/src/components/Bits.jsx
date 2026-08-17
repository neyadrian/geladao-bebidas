export function Banner({ type = "error", children }) {
  if (!children) return null;
  return <div className={`banner banner-${type === "error" ? "error" : "ok"}`}>{children}</div>;
}

export function EmptyState({ glyph, title, hint }) {
  return (
    <div className="empty-state">
      <div className="glyph" aria-hidden="true">
        {glyph}
      </div>
      <strong>{title}</strong>
      {hint && <span>{hint}</span>}
    </div>
  );
}

export function Loading({ label = "Carregando…" }) {
  return <div className="spinner-row">{label}</div>;
}
