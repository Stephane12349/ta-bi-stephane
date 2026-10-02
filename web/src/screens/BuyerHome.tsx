import React from "react";
import { useMarket } from "../context/MarketContext";
import { ProductCard, ShopCard } from "../components/Cards";
import { Search, Flame, ShieldCheck, MapPin, ArrowRight } from "lucide-react";

export const BuyerHome: React.FC = () => {
  const {
    products,
    shops,
    categories,
    navigate,
    setSelectedSector,
    setSelectedCategory,
  } = useMarket();

  const verifiedShops = shops.filter((s) => s.isVerified);
  const featuredProducts = products.slice(0, 4);

  const sectors = [
    { id: "Forum", name: "Forum d'Adjamé", badge: "Textile & Gros" },
    { id: "Roxy", name: "Adjamé Roxy", badge: "Cosmétiques & Mode" },
    { id: "Black", name: "Black Market", badge: "Téléphonie & Tech" },
    { id: "Gouro", name: "Marché Gouro", badge: "Vivrier & Épices" },
  ];

  return (
    <div style={{ maxWidth: 1100, margin: "0 auto", padding: "16px 16px 90px 16px" }}>
      {/* Bannière Hero Adjamé Market */}
      <div
        style={{
          background: "linear-gradient(135deg, #1B4D3E 0%, #2C6E59 100%)",
          borderRadius: 16,
          padding: 20,
          color: "#FFFFFF",
          boxShadow: "0 6px 16px rgba(27, 77, 62, 0.25)",
          marginBottom: 20,
        }}
      >
        <div style={{ display: "inline-block", backgroundColor: "var(--color-accent)", color: "#000", fontSize: 10, fontWeight: 800, padding: "2px 8px", borderRadius: 12, marginBottom: 8 }}>
          DIRECT GROSSISTES ABIDJAN
        </div>
        <h2 style={{ fontSize: 22, fontWeight: 900, lineHeight: 1.2, margin: "4px 0 8px 0" }}>
          Trouvez vos fournisseurs certifiés à Adjamé
        </h2>
        <p style={{ fontSize: 12, color: "#D4EFDF", marginBottom: 14, maxWidth: 500 }}>
          Commandez directement au carton et au ballot sans tourner en rond au Forum. Tarifs dégressifs et retrait sécurisé par code PIN.
        </p>

        {/* Barre de Recherche Rapide */}
        <div
          onClick={() => navigate("search")}
          style={{
            backgroundColor: "#FFFFFF",
            borderRadius: 10,
            padding: "10px 14px",
            display: "flex",
            alignItems: "center",
            gap: 10,
            color: "#7F8C8D",
            cursor: "pointer",
            boxShadow: "0 2px 6px rgba(0,0,0,0.1)"
          }}
        >
          <Search size={18} color="var(--color-primary)" />
          <span style={{ fontSize: 13 }}>Rechercher un savon, pagne, chargeur au carton...</span>
        </div>
      </div>

      {/* Raccourcis Secteurs d'Adjamé */}
      <div style={{ marginBottom: 24 }}>
        <h3 style={{ fontSize: 15, fontWeight: 800, marginBottom: 10, display: "flex", alignItems: "center", gap: 6 }}>
          <MapPin size={16} color="var(--color-secondary)" />
          Explorer par Marché d'Adjamé
        </h3>
        <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(130px, 1fr))", gap: 10 }}>
          {sectors.map((sec) => (
            <div
              key={sec.id}
              onClick={() => {
                setSelectedSector(sec.id);
                navigate("search");
              }}
              style={{
                backgroundColor: "#FFFFFF",
                border: "1px solid var(--color-border)",
                borderRadius: 10,
                padding: 10,
                cursor: "pointer",
                textAlign: "center",
                boxShadow: "0 1px 3px rgba(0,0,0,0.02)"
              }}
            >
              <div style={{ fontSize: 13, fontWeight: 800, color: "var(--color-primary)" }}>{sec.name}</div>
              <div style={{ fontSize: 10, color: "var(--color-secondary)", fontWeight: 600, marginTop: 2 }}>{sec.badge}</div>
            </div>
          ))}
        </div>
      </div>

      {/* Catégories de Gros */}
      <div style={{ marginBottom: 24 }}>
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 10 }}>
          <h3 style={{ fontSize: 15, fontWeight: 800 }}>Rayons Grossistes</h3>
          <button
            onClick={() => navigate("search")}
            style={{ background: "none", border: "none", color: "var(--color-primary)", fontSize: 12, fontWeight: 700 }}
          >
            Tout voir
          </button>
        </div>

        <div style={{ display: "flex", gap: 10, overflowX: "auto", paddingBottom: 6 }}>
          {categories.map((cat) => (
            <div
              key={cat.id}
              onClick={() => {
                setSelectedCategory(cat.id);
                navigate("search");
              }}
              style={{
                minWidth: 120,
                backgroundColor: "#FFFFFF",
                borderRadius: 10,
                padding: 12,
                border: "1px solid var(--color-border)",
                textAlign: "center",
                cursor: "pointer",
                flexShrink: 0
              }}
            >
              <div style={{ fontSize: 12, fontWeight: 700, color: "var(--color-text)" }}>{cat.name}</div>
              <div style={{ fontSize: 10, color: "var(--color-text-muted)", marginTop: 2 }}>{cat.productCount} articles</div>
            </div>
          ))}
        </div>
      </div>

      {/* Nouveaux Arrivages Conteneurs */}
      <div style={{ marginBottom: 28 }}>
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 12 }}>
          <h3 style={{ fontSize: 16, fontWeight: 800, display: "flex", alignItems: "center", gap: 6 }}>
            <Flame size={18} color="var(--color-secondary)" />
            Arrivages & Prix Dégressifs
          </h3>
          <button
            onClick={() => navigate("search")}
            style={{ background: "none", border: "none", color: "var(--color-primary)", fontSize: 12, fontWeight: 700 }}
          >
            Explorer le stock ({products.length})
          </button>
        </div>

        <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(220px, 1fr))", gap: 14 }}>
          {featuredProducts.map((p) => (
            <ProductCard key={p.id} product={p} />
          ))}
        </div>
      </div>

      {/* Grossistes Certifiés Terrain */}
      <div>
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 12 }}>
          <h3 style={{ fontSize: 16, fontWeight: 800, display: "flex", alignItems: "center", gap: 6 }}>
            <ShieldCheck size={18} color="var(--color-accent)" />
            Grossistes Vérifiés sur le Terrain
          </h3>
        </div>

        <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(280px, 1fr))", gap: 14 }}>
          {verifiedShops.map((s) => (
            <ShopCard key={s.id} shop={s} />
          ))}
        </div>
      </div>
    </div>
  );
};
