import React, { useState } from "react";
import { useMarket } from "../context/MarketContext";
import { ProductCard } from "../components/Cards";
import { X, Phone, MessageCircle, MapPin, ShieldCheck, Heart, FileText, Save } from "lucide-react";

export const ShopDetailModal: React.FC = () => {
  const {
    selectedShopId,
    shops,
    products,
    navigate,
    sellerNotes,
    saveSellerNote,
    favoriteShopIds,
    toggleFavoriteShop,
  } = useMarket();

  const shop = shops.find((s) => s.id === selectedShopId);
  const shopProducts = products.filter((p) => p.sellerId === selectedShopId);

  const [note, setNote] = useState<string>(
    shop ? sellerNotes[shop.id] || "" : ""
  );

  if (!shop) return null;

  const isFav = favoriteShopIds.includes(shop.id);

  const handleWhatsApp = () => {
    const cleanPhone = shop.whatsapp.replace(/\D/g, "");
    const msg = encodeURIComponent(`Bonjour ${shop.name}, je consulte votre boutique sur Adjamé Market.`);
    window.open(`https://api.whatsapp.com/send?phone=${cleanPhone}&text=${msg}`, "_blank");
  };

  const handleCall = () => {
    window.location.href = `tel:${shop.phone}`;
  };

  return (
    <div
      style={{
        position: "fixed",
        inset: 0,
        backgroundColor: "rgba(0,0,0,0.6)",
        zIndex: 60,
        display: "flex",
        justifyContent: "center",
        alignItems: "flex-end",
        backdropFilter: "blur(2px)",
      }}
      onClick={() => navigate("home")}
    >
      <div
        onClick={(e) => e.stopPropagation()}
        style={{
          backgroundColor: "#FFFFFF",
          width: "100%",
          maxWidth: 650,
          maxHeight: "92vh",
          borderTopLeftRadius: 20,
          borderTopRightRadius: 20,
          overflowY: "auto",
          paddingBottom: 24,
          position: "relative"
        }}
      >
        <button
          onClick={() => navigate("home")}
          style={{
            position: "absolute",
            top: 14,
            right: 14,
            zIndex: 10,
            width: 34,
            height: 34,
            borderRadius: "50%",
            backgroundColor: "rgba(0,0,0,0.5)",
            color: "#FFF",
            border: "none",
            display: "flex",
            alignItems: "center",
            justifyContent: "center"
          }}
        >
          <X size={18} />
        </button>

        {/* En-tête Boutique */}
        <div style={{
          backgroundColor: "var(--color-primary)",
          color: "#FFFFFF",
          padding: "24px 18px",
          borderTopLeftRadius: 20,
          borderTopRightRadius: 20
        }}>
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start" }}>
            <div>
              <span style={{
                backgroundColor: "var(--color-accent)",
                color: "#000",
                fontSize: 10,
                fontWeight: 800,
                padding: "2px 8px",
                borderRadius: 4
              }}>
                BOUTIQUE VÉRIFIÉE TERRAIN
              </span>
              <h2 style={{ fontSize: 20, fontWeight: 900, margin: "8px 0 4px 0" }}>{shop.name}</h2>
              <div style={{ fontSize: 12, color: "#D4EFDF" }}>Gérant : {shop.ownerName} • {shop.hours}</div>
            </div>

            <button
              onClick={() => toggleFavoriteShop(shop.id)}
              style={{ background: "none", border: "none", color: isFav ? "#E74C3C" : "#FFF" }}
            >
              <Heart size={22} fill={isFav ? "#E74C3C" : "none"} />
            </button>
          </div>

          <div style={{ display: "flex", alignItems: "center", gap: 6, fontSize: 12, color: "#E8F8F5", marginTop: 12 }}>
            <MapPin size={16} color="var(--color-secondary)" />
            <span>{shop.address} ({shop.marketSector})</span>
          </div>

          <div style={{
            fontSize: 11,
            backgroundColor: "rgba(255,255,255,0.15)",
            padding: "6px 10px",
            borderRadius: 6,
            marginTop: 8,
            color: "#FFF"
          }}>
            📍 <strong>Repère :</strong> {shop.landmarks}
          </div>

          {/* Boutons Appel direct & WhatsApp */}
          <div style={{ display: "flex", gap: 10, marginTop: 14 }}>
            <button
              onClick={handleCall}
              style={{
                flex: 1,
                backgroundColor: "#FFFFFF",
                color: "var(--color-primary)",
                border: "none",
                borderRadius: 8,
                padding: "10px 0",
                fontSize: 13,
                fontWeight: 800,
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                gap: 6
              }}
            >
              <Phone size={15} /> Appeler
            </button>
            <button
              onClick={handleWhatsApp}
              style={{
                flex: 1,
                backgroundColor: "var(--color-whatsapp)",
                color: "#FFFFFF",
                border: "none",
                borderRadius: 8,
                padding: "10px 0",
                fontSize: 13,
                fontWeight: 800,
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                gap: 6
              }}
            >
              <MessageCircle size={15} /> WhatsApp
            </button>
          </div>
        </div>

        {/* Section Notes Privées Acheteur */}
        <div style={{ padding: "16px 16px 0 16px" }}>
          <div style={{
            backgroundColor: "#F8F9FA",
            border: "1px solid var(--color-border)",
            borderRadius: 10,
            padding: 12
          }}>
            <div style={{ display: "flex", alignItems: "center", gap: 6, fontSize: 12, fontWeight: 800, color: "var(--color-primary)" }}>
              <FileText size={15} />
              <span>Ma note personnelle privée sur ce grossiste :</span>
            </div>
            <textarea
              value={note}
              onChange={(e) => setNote(e.target.value)}
              placeholder="Ex: Demander le commis Salif pour 5% de remise au carton..."
              style={{
                width: "100%",
                borderRadius: 6,
                border: "1px solid var(--color-border)",
                padding: 8,
                fontSize: 12,
                marginTop: 6,
                resize: "none",
                height: 55,
                outline: "none"
              }}
            />
            <div style={{ textAlign: "right", marginTop: 6 }}>
              <button
                onClick={() => saveSellerNote(shop.id, note)}
                style={{
                  backgroundColor: "var(--color-primary)",
                  color: "#FFF",
                  border: "none",
                  borderRadius: 6,
                  padding: "5px 12px",
                  fontSize: 11,
                  fontWeight: 700,
                  display: "inline-flex",
                  alignItems: "center",
                  gap: 4
                }}
              >
                <Save size={12} /> Enregistrer ma note
              </button>
            </div>
          </div>
        </div>

        {/* Catalogue de la Boutique */}
        <div style={{ padding: 16 }}>
          <h3 style={{ fontSize: 15, fontWeight: 800, marginBottom: 12 }}>
            Articles disponibles en magasin ({shopProducts.length})
          </h3>

          <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(200px, 1fr))", gap: 12 }}>
            {shopProducts.map((p) => (
              <ProductCard key={p.id} product={p} />
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};
