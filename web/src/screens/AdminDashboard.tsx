import React from "react";
import { useMarket } from "../context/MarketContext";
import { ShieldCheck, CheckCircle2, AlertTriangle, Store } from "lucide-react";

export const AdminDashboard: React.FC = () => {
  const { shops, reports, orders, toggleShopVerification } = useMarket();

  return (
    <div style={{ maxWidth: 900, margin: "0 auto", padding: "16px 16px 90px 16px" }}>
      <div style={{
        backgroundColor: "var(--color-primary)",
        color: "#FFFFFF",
        borderRadius: 14,
        padding: 18,
        marginBottom: 20
      }}>
        <div style={{ fontSize: 11, fontWeight: 800, textTransform: "uppercase", color: "var(--color-accent)" }}>
          BACK-OFFICE & SUPERVISION ADJAMÉ
        </div>
        <h2 style={{ fontSize: 20, fontWeight: 900, margin: "4px 0" }}>
          Supervision des Opérations Terrain
        </h2>
        <div style={{ fontSize: 12, color: "#D4EFDF" }}>
          Validation physique des boutiques KYC et arbitrage des litiges de retrait.
        </div>
      </div>

      {/* Validation KYC Boutiques */}
      <div style={{ marginBottom: 28 }}>
        <h3 style={{ fontSize: 16, fontWeight: 800, marginBottom: 12, display: "flex", alignItems: "center", gap: 6 }}>
          <Store size={18} color="var(--color-primary)" />
          Certification & Audit Terrain des Grossistes
        </h3>

        <div style={{ display: "flex", flexDirection: "column", gap: 10 }}>
          {shops.map((shop) => (
            <div
              key={shop.id}
              style={{
                backgroundColor: "#FFFFFF",
                borderRadius: 10,
                padding: 14,
                border: "1px solid var(--color-border)",
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center"
              }}
            >
              <div>
                <div style={{ display: "flex", alignItems: "center", gap: 6 }}>
                  <strong style={{ fontSize: 14 }}>{shop.name}</strong>
                  {shop.isVerified ? (
                    <span style={{ fontSize: 10, backgroundColor: "#EAFAF1", color: "#27AE60", fontWeight: 800, padding: "2px 6px", borderRadius: 4 }}>
                      CERTIFIÉ
                    </span>
                  ) : (
                    <span style={{ fontSize: 10, backgroundColor: "#FDEDEC", color: "#C0392B", fontWeight: 800, padding: "2px 6px", borderRadius: 4 }}>
                      EN ATTENTE
                    </span>
                  )}
                </div>
                <div style={{ fontSize: 12, color: "var(--color-text-muted)", marginTop: 2 }}>
                  {shop.marketSector} • Gérant : {shop.ownerName} ({shop.phone})
                </div>
              </div>

              <button
                onClick={() => toggleShopVerification(shop.id)}
                style={{
                  backgroundColor: shop.isVerified ? "#C0392B" : "var(--color-primary)",
                  color: "#FFF",
                  border: "none",
                  borderRadius: 6,
                  padding: "6px 12px",
                  fontSize: 11,
                  fontWeight: 700
                }}
              >
                {shop.isVerified ? "Révoquer certification" : "Valider sur le terrain"}
              </button>
            </div>
          ))}
        </div>
      </div>

      {/* Traitement des Signalements & Litiges */}
      <div>
        <h3 style={{ fontSize: 16, fontWeight: 800, marginBottom: 12, display: "flex", alignItems: "center", gap: 6 }}>
          <AlertTriangle size={18} color="#C0392B" />
          Signalements & Litiges Ouverts ({reports.length})
        </h3>

        {reports.length === 0 ? (
          <div style={{ backgroundColor: "#FFFFFF", borderRadius: 10, padding: 20, textAlign: "center", color: "var(--color-text-muted)" }}>
            Aucun signalement d'anomalie en attente. Toutes les boutiques sont conformes.
          </div>
        ) : (
          <div style={{ display: "flex", flexDirection: "column", gap: 10 }}>
            {reports.map((r) => (
              <div
                key={r.id}
                style={{
                  backgroundColor: "#FDEDEC",
                  border: "1px solid #FADBD8",
                  borderRadius: 10,
                  padding: 12
                }}
              >
                <div style={{ display: "flex", justifyContent: "space-between", fontSize: 12, fontWeight: 700 }}>
                  <span style={{ color: "#78281F" }}>Boutique ciblée : {r.shopName}</span>
                  <span style={{ color: "#C0392B" }}>{r.reason}</span>
                </div>
                <div style={{ fontSize: 12, marginTop: 4, color: "#2C3E50" }}>
                  "{r.description}" — Émis par {r.reporterName}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};
