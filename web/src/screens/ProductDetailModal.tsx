import React, { useState } from "react";
import { useMarket } from "../context/MarketContext";
import { X, MessageCircle, ShoppingBag, ShieldCheck, MapPin, Store, AlertTriangle } from "lucide-react";

export const ProductDetailModal: React.FC = () => {
  const {
    selectedProductId,
    products,
    shops,
    navigate,
    openShopDetail,
    addToCart,
    submitReport,
  } = useMarket();

  const product = products.find((p) => p.id === selectedProductId);
  const shop = product ? shops.find((s) => s.id === product.sellerId) : null;

  const [quantity, setQuantity] = useState<number>(product ? product.minOrderQuantity : 1);
  const [reportOpen, setReportOpen] = useState(false);
  const [reportReason, setReportReason] = useState("PRIX_TROMPEUR");
  const [reportDesc, setReportDesc] = useState("");

  if (!product) return null;

  // Calcul du prix selon palier
  const calculateTierPrice = (qty: number): number => {
    const matchingTier = [...product.priceTiers]
      .reverse()
      .find((t) => qty >= t.minQuantity && (t.maxQuantity === null || qty <= t.maxQuantity));
    return matchingTier ? matchingTier.unitPriceFcfa : product.basePrice;
  };

  const currentUnitPrice = calculateTierPrice(quantity);
  const totalAmount = currentUnitPrice * quantity;

  const handleWhatsApp = () => {
    const cleanPhone = (shop?.whatsapp || "+2250708091011").replace(/\D/g, "");
    const msg = encodeURIComponent(
      `Bonjour ${product.sellerName}, j'ai vu votre article "${product.name}" sur Adjamé Market. Je souhaite commander ${quantity} carton(s) au prix de ${currentUnitPrice.toLocaleString()} FCFA/ctn (Total: ${totalAmount.toLocaleString()} FCFA). Est-ce disponible en magasin ?`
    );
    window.open(`https://api.whatsapp.com/send?phone=${cleanPhone}&text=${msg}`, "_blank");
  };

  const handleReport = () => {
    submitReport(product.sellerId, product.sellerName, reportReason, reportDesc || "Signalement fiche produit");
    setReportOpen(false);
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
          maxWidth: 600,
          maxHeight: "92vh",
          borderTopLeftRadius: 20,
          borderTopRightRadius: 20,
          overflowY: "auto",
          paddingBottom: 24,
          position: "relative"
        }}
      >
        {/* Bouton Fermer */}
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

        {/* Photo Principale */}
        <div style={{ height: 240, width: "100%", backgroundColor: "#ECEFF1" }}>
          <img
            src={product.images[0]}
            alt={product.name}
            style={{ width: "100%", height: "100%", objectFit: "cover" }}
          />
        </div>

        <div style={{ padding: 16 }}>
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 8 }}>
            <span style={{
              backgroundColor: "var(--color-primary-container)",
              color: "var(--color-primary)",
              fontSize: 11,
              fontWeight: 800,
              padding: "3px 8px",
              borderRadius: 6
            }}>
              {product.sellerSector}
            </span>
            <span style={{ backgroundColor: "#F4F6F6", color: "#2C3E50", fontSize: 11, fontWeight: 700, padding: "3px 8px", borderRadius: 6 }}>
              {product.packaging}
            </span>
          </div>

          <h2 style={{ fontSize: 18, fontWeight: 800, color: "var(--color-text)", margin: "8px 0" }}>
            {product.name}
          </h2>

          <div style={{ display: "flex", alignItems: "baseline", gap: 6, margin: "10px 0" }}>
            <span style={{ fontSize: 24, fontWeight: 900, color: "var(--color-secondary)" }}>
              {currentUnitPrice.toLocaleString()} FCFA
            </span>
            <span style={{ fontSize: 13, color: "var(--color-text-muted)" }}>/ carton</span>
          </div>

          <div style={{ fontSize: 11, color: "var(--color-text-muted)", marginBottom: 14 }}>
            MOQ imposé par le grossiste : min. <strong>{product.minOrderQuantity} carton(s)</strong>
          </div>

          {/* Grille des Paliers Dégressifs */}
          {product.priceTiers.length > 0 && (
            <div style={{
              backgroundColor: "#F8F9FA",
              border: "1px solid var(--color-border)",
              borderRadius: 10,
              padding: 12,
              marginBottom: 16
            }}>
              <div style={{ fontSize: 12, fontWeight: 800, color: "var(--color-primary)", marginBottom: 8 }}>
                Tarifs dégressifs grossiste d'Adjamé :
              </div>
              <div style={{ display: "flex", flexDirection: "column", gap: 6 }}>
                {product.priceTiers.map((tier, idx) => {
                  const isActive = quantity >= tier.minQuantity && (tier.maxQuantity === null || quantity <= tier.maxQuantity);
                  return (
                    <div
                      key={idx}
                      style={{
                        display: "flex",
                        justifyContent: "space-between",
                        padding: "6px 10px",
                        borderRadius: 6,
                        backgroundColor: isActive ? "var(--color-primary-container)" : "transparent",
                        fontWeight: isActive ? 800 : 500,
                        color: isActive ? "var(--color-primary)" : "var(--color-text)",
                        fontSize: 12
                      }}
                    >
                      <span>{tier.label}</span>
                      <span>{tier.unitPriceFcfa.toLocaleString()} FCFA / ctn {isActive && "✓"}</span>
                    </div>
                  );
                })}
              </div>
            </div>
          )}

          {/* Sélecteur de Quantité */}
          <div style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            padding: 12,
            backgroundColor: "#FFFFFF",
            border: "1px solid var(--color-border)",
            borderRadius: 10,
            marginBottom: 16
          }}>
            <div>
              <div style={{ fontSize: 11, color: "var(--color-text-muted)" }}>Quantité commandée :</div>
              <div style={{ fontSize: 16, fontWeight: 800, color: "var(--color-primary)" }}>
                Total : {totalAmount.toLocaleString()} FCFA
              </div>
            </div>

            <div style={{ display: "flex", alignItems: "center", gap: 12 }}>
              <button
                onClick={() => setQuantity(Math.max(product.minOrderQuantity, quantity - 1))}
                disabled={quantity <= product.minOrderQuantity}
                style={{
                  width: 36,
                  height: 36,
                  borderRadius: 6,
                  border: "1px solid var(--color-border)",
                  backgroundColor: "#F4F6F6",
                  fontWeight: 800,
                  fontSize: 16
                }}
              >
                -
              </button>
              <span style={{ fontSize: 16, fontWeight: 800 }}>{quantity}</span>
              <button
                onClick={() => setQuantity(quantity + 1)}
                style={{
                  width: 36,
                  height: 36,
                  borderRadius: 6,
                  border: "1px solid var(--color-border)",
                  backgroundColor: "#F4F6F6",
                  fontWeight: 800,
                  fontSize: 16
                }}
              >
                +
              </button>
            </div>
          </div>

          {/* Boutique du Vendeur */}
          {shop && (
            <div
              onClick={() => openShopDetail(shop.id)}
              style={{
                border: "1px solid var(--color-border)",
                borderRadius: 10,
                padding: 12,
                cursor: "pointer",
                marginBottom: 16,
                backgroundColor: "#FFFFFF"
              }}
            >
              <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                <div>
                  <div style={{ display: "flex", alignItems: "center", gap: 6 }}>
                    <Store size={15} color="var(--color-primary)" />
                    <strong style={{ fontSize: 13 }}>{shop.name}</strong>
                    {shop.isVerified && <ShieldCheck size={14} color="var(--color-accent)" />}
                  </div>
                  <div style={{ fontSize: 11, color: "var(--color-text-muted)", marginTop: 2 }}>
                    {shop.address} ({shop.marketSector})
                  </div>
                </div>
                <span style={{ fontSize: 11, color: "var(--color-primary)", fontWeight: 700 }}>
                  Voir boutique →
                </span>
              </div>
            </div>
          )}

          {/* Description */}
          <div style={{ marginBottom: 20 }}>
            <h4 style={{ fontSize: 13, fontWeight: 800, marginBottom: 4 }}>Description du produit :</h4>
            <p style={{ fontSize: 12, color: "#566573", lineHeight: 1.5 }}>{product.description}</p>
          </div>

          {/* Boutons d'Action Fixes */}
          <div style={{ display: "flex", gap: 10 }}>
            <button
              onClick={handleWhatsApp}
              style={{
                flex: 1,
                backgroundColor: "var(--color-whatsapp)",
                color: "#FFFFFF",
                border: "none",
                borderRadius: 8,
                padding: "12px 0",
                fontSize: 13,
                fontWeight: 800,
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                gap: 6
              }}
            >
              <MessageCircle size={16} /> Négocier sur WhatsApp
            </button>

            <button
              onClick={() => {
                addToCart(product, quantity);
                navigate("cart");
              }}
              style={{
                flex: 1,
                backgroundColor: "var(--color-primary)",
                color: "#FFFFFF",
                border: "none",
                borderRadius: 8,
                padding: "12px 0",
                fontSize: 13,
                fontWeight: 800,
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                gap: 6
              }}
            >
              <ShoppingBag size={16} /> Ajouter au Panier
            </button>
          </div>

          {/* Lien Signalement Litige */}
          <div style={{ textAlign: "center", marginTop: 14 }}>
            <button
              onClick={() => setReportOpen(!reportOpen)}
              style={{ background: "none", border: "none", color: "#95A5A6", fontSize: 11, display: "inline-flex", alignItems: "center", gap: 4 }}
            >
              <AlertTriangle size={12} /> Signaler un problème avec ce produit
            </button>

            {reportOpen && (
              <div style={{ marginTop: 10, padding: 10, backgroundColor: "#FDEDEC", borderRadius: 8, textAlign: "left" }}>
                <div style={{ fontSize: 11, fontWeight: 700, color: "#C0392B", marginBottom: 6 }}>
                  Motif du signalement :
                </div>
                <select
                  value={reportReason}
                  onChange={(e) => setReportReason(e.target.value)}
                  style={{ width: "100%", padding: 6, fontSize: 12, borderRadius: 6, marginBottom: 6 }}
                >
                  <option value="PRIX_TROMPEUR">Prix en magasin différent de l'app</option>
                  <option value="PRODUIT_NON_CONFORME">Produit contrefait / abîmé</option>
                  <option value="BOUTIQUE_INTROUVABLE">Boutique introuvable</option>
                </select>
                <button
                  onClick={handleReport}
                  style={{
                    backgroundColor: "#C0392B",
                    color: "#FFF",
                    border: "none",
                    borderRadius: 6,
                    padding: "6px 12px",
                    fontSize: 11,
                    fontWeight: 700
                  }}
                >
                  Envoyer à la modération
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};
