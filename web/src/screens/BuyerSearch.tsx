import React from "react";
import { useMarket } from "../context/MarketContext";
import { ProductCard } from "../components/Cards";
import { Search, X, Filter } from "lucide-react";

export const BuyerSearch: React.FC = () => {
  const {
    products,
    categories,
    searchQuery,
    setSearchQuery,
    selectedCategory,
    setSelectedCategory,
    selectedSector,
    setSelectedSector,
    verifiedOnly,
    setVerifiedOnly,
  } = useMarket();

  const filtered = products.filter((p) => {
    const q = searchQuery.toLowerCase().trim();
    const matchQ =
      !q ||
      p.name.toLowerCase().includes(q) ||
      p.description.toLowerCase().includes(q) ||
      p.sellerName.toLowerCase().includes(q);

    const matchCat = !selectedCategory || p.categoryId === selectedCategory;
    const matchSec = !selectedSector || p.sellerSector.toLowerCase().includes(selectedSector.toLowerCase());
    const matchVer = !verifiedOnly || p.isVerifiedSeller;

    return matchQ && matchCat && matchSec && matchVer;
  });

  const sectors = ["Forum", "Roxy", "Black", "Gouro"];

  return (
    <div style={{ maxWidth: 1100, margin: "0 auto", padding: "16px 16px 90px 16px" }}>
      {/* Barre de Recherche */}
      <div style={{
        display: "flex",
        alignItems: "center",
        backgroundColor: "#FFFFFF",
        borderRadius: 10,
        padding: "8px 12px",
        border: "1px solid var(--color-border)",
        boxShadow: "0 2px 6px rgba(0,0,0,0.05)",
        marginBottom: 14
      }}>
        <Search size={18} color="var(--color-primary)" style={{ marginRight: 8 }} />
        <input
          type="text"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          placeholder="Rechercher par article, marque ou grossiste..."
          style={{
            flex: 1,
            border: "none",
            outline: "none",
            fontSize: 14
          }}
          autoFocus
        />
        {searchQuery && (
          <button
            onClick={() => setSearchQuery("")}
            style={{ background: "none", border: "none", color: "#95A5A6" }}
          >
            <X size={16} />
          </button>
        )}
      </div>

      {/* Filtres Rapides (Secteurs & Catégories) */}
      <div style={{ display: "flex", gap: 8, overflowX: "auto", paddingBottom: 8, marginBottom: 14 }}>
        <button
          onClick={() => { setSelectedCategory(null); setSelectedSector(null); setVerifiedOnly(false); }}
          style={{
            whiteSpace: "nowrap",
            padding: "5px 12px",
            borderRadius: 20,
            fontSize: 11,
            fontWeight: 700,
            border: "1px solid var(--color-border)",
            backgroundColor: !selectedCategory && !selectedSector && !verifiedOnly ? "var(--color-primary)" : "#FFFFFF",
            color: !selectedCategory && !selectedSector && !verifiedOnly ? "#FFFFFF" : "var(--color-text)"
          }}
        >
          Tous
        </button>

        <button
          onClick={() => setVerifiedOnly(!verifiedOnly)}
          style={{
            whiteSpace: "nowrap",
            padding: "5px 12px",
            borderRadius: 20,
            fontSize: 11,
            fontWeight: 700,
            border: "1px solid var(--color-border)",
            backgroundColor: verifiedOnly ? "var(--color-accent)" : "#FFFFFF",
            color: verifiedOnly ? "#000" : "var(--color-text)"
          }}
        >
          ✓ Vendeurs Vérifiés
        </button>

        {sectors.map((s) => (
          <button
            key={s}
            onClick={() => setSelectedSector(selectedSector === s ? null : s)}
            style={{
              whiteSpace: "nowrap",
              padding: "5px 12px",
              borderRadius: 20,
              fontSize: 11,
              fontWeight: 700,
              border: "1px solid var(--color-border)",
              backgroundColor: selectedSector === s ? "var(--color-secondary)" : "#FFFFFF",
              color: selectedSector === s ? "#FFFFFF" : "var(--color-text)"
            }}
          >
            {s}
          </button>
        ))}

        {categories.map((c) => (
          <button
            key={c.id}
            onClick={() => setSelectedCategory(selectedCategory === c.id ? null : c.id)}
            style={{
              whiteSpace: "nowrap",
              padding: "5px 12px",
              borderRadius: 20,
              fontSize: 11,
              fontWeight: 700,
              border: "1px solid var(--color-border)",
              backgroundColor: selectedCategory === c.id ? "var(--color-primary)" : "#FFFFFF",
              color: selectedCategory === c.id ? "#FFFFFF" : "var(--color-text)"
            }}
          >
            {c.name}
          </button>
        ))}
      </div>

      {/* Résumé Résultats */}
      <div style={{ fontSize: 13, color: "var(--color-text-muted)", marginBottom: 14 }}>
        <strong>{filtered.length}</strong> offre(s) de gros trouvée(s)
      </div>

      {/* Grille de Produits */}
      {filtered.length === 0 ? (
        <div style={{ textAlign: "center", padding: "40px 20px", color: "var(--color-text-muted)" }}>
          <p style={{ fontSize: 16, fontWeight: 700 }}>Aucun produit grossiste ne correspond.</p>
          <p style={{ fontSize: 12, marginTop: 4 }}>Essayez d'élargir vos filtres ou de chercher un autre mot-clé.</p>
        </div>
      ) : (
        <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(220px, 1fr))", gap: 14 }}>
          {filtered.map((p) => (
            <ProductCard key={p.id} product={p} />
          ))}
        </div>
      )}
    </div>
  );
};
