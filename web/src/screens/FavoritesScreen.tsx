import React, { useState } from "react";
import { useMarket } from "../context/MarketContext";
import { ProductCard, ShopCard } from "../components/Cards";
import { Heart } from "lucide-react";

export const FavoritesScreen: React.FC = () => {
  const { products, shops, favoriteProductIds, favoriteShopIds } = useMarket();
  const [tab, setTab] = useState<"SHOPS" | "PRODUCTS">("SHOPS");

  const favShops = shops.filter((s) => favoriteShopIds.includes(s.id));
  const favProducts = products.filter((p) => favoriteProductIds.includes(p.id));

  return (
    <div style={{ maxWidth: 800, margin: "0 auto", padding: "16px 16px 90px 16px" }}>
      <h2 style={{ fontSize: 18, fontWeight: 800, marginBottom: 14 }}>Mes Favoris Enregistrés</h2>

      <div style={{ display: "flex", borderBottom: "2px solid var(--color-border)", marginBottom: 16 }}>
        <button
          onClick={() => setTab("SHOPS")}
          style={{
            flex: 1,
            padding: "10px 0",
            border: "none",
            background: "none",
            fontSize: 13,
            fontWeight: 800,
            color: tab === "SHOPS" ? "var(--color-primary)" : "var(--color-text-muted)",
            borderBottom: tab === "SHOPS" ? "3px solid var(--color-primary)" : "3px solid transparent",
            marginBottom: -2
          }}
        >
          Grossistes ({favShops.length})
        </button>

        <button
          onClick={() => setTab("PRODUCTS")}
          style={{
            flex: 1,
            padding: "10px 0",
            border: "none",
            background: "none",
            fontSize: 13,
            fontWeight: 800,
            color: tab === "PRODUCTS" ? "var(--color-primary)" : "var(--color-text-muted)",
            borderBottom: tab === "PRODUCTS" ? "3px solid var(--color-primary)" : "3px solid transparent",
            marginBottom: -2
          }}
        >
          Articles au carton ({favProducts.length})
        </button>
      </div>

      {tab === "SHOPS" ? (
        favShops.length === 0 ? (
          <div style={{ textAlign: "center", padding: "40px 16px", color: "var(--color-text-muted)" }}>
            <Heart size={32} style={{ margin: "0 auto 10px auto", display: "block" }} />
            <p style={{ fontWeight: 700 }}>Aucun grossiste enregistré dans vos favoris.</p>
          </div>
        ) : (
          <div style={{ display: "flex", flexDirection: "column", gap: 12 }}>
            {favShops.map((shop) => (
              <ShopCard key={shop.id} shop={shop} />
            ))}
          </div>
        )
      ) : favProducts.length === 0 ? (
        <div style={{ textAlign: "center", padding: "40px 16px", color: "var(--color-text-muted)" }}>
          <Heart size={32} style={{ margin: "0 auto 10px auto", display: "block" }} />
          <p style={{ fontWeight: 700 }}>Aucun article au carton dans vos favoris.</p>
        </div>
      ) : (
        <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(220px, 1fr))", gap: 14 }}>
          {favProducts.map((p) => (
            <ProductCard key={p.id} product={p} />
          ))}
        </div>
      )}
    </div>
  );
};
