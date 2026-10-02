import React, { useState } from "react";
import { useMarket } from "../context/MarketContext";
import { OrderCard } from "../components/Cards";
import { ReceiptText, CheckCircle2 } from "lucide-react";

export const OrdersScreen: React.FC = () => {
  const { orders } = useMarket();
  const [filter, setFilter] = useState<"ALL" | "ACTIVE" | "DONE">("ALL");

  const filtered = orders.filter((o) => {
    if (filter === "ACTIVE") return o.status !== "COMPLETED" && o.status !== "CANCELLED";
    if (filter === "DONE") return o.status === "COMPLETED" || o.status === "CANCELLED";
    return true;
  });

  return (
    <div style={{ maxWidth: 800, margin: "0 auto", padding: "16px 16px 90px 16px" }}>
      <h2 style={{ fontSize: 18, fontWeight: 800, marginBottom: 14 }}>Historique de Mes Commandes</h2>

      {/* Onglets Filtre */}
      <div style={{ display: "flex", gap: 8, marginBottom: 16 }}>
        <button
          onClick={() => setFilter("ALL")}
          style={{
            flex: 1,
            padding: "8px 0",
            borderRadius: 8,
            border: "1px solid var(--color-border)",
            backgroundColor: filter === "ALL" ? "var(--color-primary)" : "#FFFFFF",
            color: filter === "ALL" ? "#FFFFFF" : "var(--color-text)",
            fontWeight: 700,
            fontSize: 12
          }}
        >
          Toutes ({orders.length})
        </button>
        <button
          onClick={() => setFilter("ACTIVE")}
          style={{
            flex: 1,
            padding: "8px 0",
            borderRadius: 8,
            border: "1px solid var(--color-border)",
            backgroundColor: filter === "ACTIVE" ? "var(--color-primary)" : "#FFFFFF",
            color: filter === "ACTIVE" ? "#FFFFFF" : "var(--color-text)",
            fontWeight: 700,
            fontSize: 12
          }}
        >
          En cours
        </button>
        <button
          onClick={() => setFilter("DONE")}
          style={{
            flex: 1,
            padding: "8px 0",
            borderRadius: 8,
            border: "1px solid var(--color-border)",
            backgroundColor: filter === "DONE" ? "var(--color-primary)" : "#FFFFFF",
            color: filter === "DONE" ? "#FFFFFF" : "var(--color-text)",
            fontWeight: 700,
            fontSize: 12
          }}
        >
          Terminées
        </button>
      </div>

      {filtered.length === 0 ? (
        <div style={{ textAlign: "center", padding: "40px 16px", color: "var(--color-text-muted)" }}>
          <ReceiptText size={32} style={{ margin: "0 auto 10px auto", display: "block" }} />
          <p style={{ fontWeight: 700 }}>Aucune commande dans cette section.</p>
        </div>
      ) : (
        <div style={{ display: "flex", flexDirection: "column", gap: 12 }}>
          {filtered.map((order) => (
            <OrderCard key={order.id} order={order} />
          ))}
        </div>
      )}
    </div>
  );
};
