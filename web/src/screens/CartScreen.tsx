import React, { useState } from "react";
import { useMarket } from "../context/MarketContext";
import { Trash2, ShoppingBag, ArrowRight, ShieldCheck, CheckCircle2 } from "lucide-react";

export const CartScreen: React.FC = () => {
  const {
    cartItems,
    updateCartQty,
    removeFromCart,
    clearCart,
    cartTotal,
    placeOrder,
    navigate,
  } = useMarket();

  const [deliveryType, setDeliveryType] = useState<"CLICK_AND_COLLECT" | "GARE_EXPEDITION">("CLICK_AND_COLLECT");
  const [notes, setNotes] = useState("");

  if (cartItems.length === 0) {
    return (
      <div style={{ maxWidth: 600, margin: "0 auto", padding: "60px 16px", textAlign: "center" }}>
        <div style={{
          width: 70,
          height: 70,
          borderRadius: "50%",
          backgroundColor: "#F4F6F6",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          margin: "0 auto 16px auto",
          color: "var(--color-primary)"
        }}>
          <ShoppingBag size={32} />
        </div>
        <h3 style={{ fontSize: 18, fontWeight: 800 }}>Votre panier de gros est vide</h3>
        <p style={{ fontSize: 13, color: "var(--color-text-muted)", marginTop: 4, marginBottom: 20 }}>
          Ajoutez des articles au carton directement depuis les boutiques d'Adjamé.
        </p>
        <button
          onClick={() => navigate("search")}
          style={{
            backgroundColor: "var(--color-primary)",
            color: "#FFF",
            border: "none",
            borderRadius: 8,
            padding: "10px 20px",
            fontSize: 13,
            fontWeight: 700
          }}
        >
          Découvrir les arrivages de gros
        </button>
      </div>
    );
  }

  const handlePlaceOrder = () => {
    placeOrder(deliveryType, notes);
  };

  return (
    <div style={{ maxWidth: 800, margin: "0 auto", padding: "16px 16px 90px 16px" }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 16 }}>
        <h2 style={{ fontSize: 18, fontWeight: 800 }}>Mon Panier de Gros ({cartItems.length})</h2>
        <button
          onClick={clearCart}
          style={{ background: "none", border: "none", color: "#C0392B", fontSize: 12, fontWeight: 700 }}
        >
          Vider le panier
        </button>
      </div>

      {/* Liste des Articles du Panier */}
      <div style={{ display: "flex", flexDirection: "column", gap: 12, marginBottom: 20 }}>
        {cartItems.map((item) => (
          <div
            key={item.product.id}
            style={{
              backgroundColor: "#FFFFFF",
              borderRadius: 12,
              padding: 12,
              border: "1px solid var(--color-border)",
              display: "flex",
              gap: 12,
              alignItems: "center"
            }}
          >
            <img
              src={item.product.images[0]}
              alt={item.product.name}
              style={{ width: 65, height: 65, borderRadius: 8, objectFit: "cover" }}
            />
            <div style={{ flex: 1 }}>
              <div style={{ fontSize: 10, color: "var(--color-primary)", fontWeight: 700 }}>
                {item.product.sellerName}
              </div>
              <h4 style={{ fontSize: 13, fontWeight: 700, margin: "2px 0" }}>{item.product.name}</h4>
              <div style={{ fontSize: 11, color: "var(--color-text-muted)" }}>
                {item.unitPrice.toLocaleString()} F / ctn • Sous-total : <strong>{item.totalPrice.toLocaleString()} F</strong>
              </div>
            </div>

            <div style={{ display: "flex", alignItems: "center", gap: 8 }}>
              <div style={{ display: "flex", alignItems: "center", border: "1px solid var(--color-border)", borderRadius: 6, backgroundColor: "#F8F9FA" }}>
                <button
                  onClick={() => updateCartQty(item.product.id, item.quantity - 1)}
                  style={{ width: 28, height: 28, border: "none", background: "none", fontWeight: 800 }}
                >
                  -
                </button>
                <span style={{ fontSize: 12, fontWeight: 800, padding: "0 6px" }}>{item.quantity}</span>
                <button
                  onClick={() => updateCartQty(item.product.id, item.quantity + 1)}
                  style={{ width: 28, height: 28, border: "none", background: "none", fontWeight: 800 }}
                >
                  +
                </button>
              </div>

              <button
                onClick={() => removeFromCart(item.product.id)}
                style={{ background: "none", border: "none", color: "#BDC3C7", padding: 4 }}
              >
                <Trash2 size={16} />
              </button>
            </div>
          </div>
        ))}
      </div>

      {/* Mode de Retrait / Expédition */}
      <div style={{ backgroundColor: "#FFFFFF", borderRadius: 12, padding: 16, border: "1px solid var(--color-border)", marginBottom: 16 }}>
        <h3 style={{ fontSize: 14, fontWeight: 800, marginBottom: 10 }}>Mode de livraison :</h3>

        <div style={{ display: "flex", flexDirection: "column", gap: 8 }}>
          <label style={{
            display: "flex",
            alignItems: "center",
            gap: 10,
            padding: 10,
            borderRadius: 8,
            border: `1px solid ${deliveryType === "CLICK_AND_COLLECT" ? "var(--color-primary)" : "var(--color-border)"}`,
            backgroundColor: deliveryType === "CLICK_AND_COLLECT" ? "var(--color-primary-container)" : "#FFFFFF",
            cursor: "pointer"
          }}>
            <input
              type="radio"
              name="delivery"
              checked={deliveryType === "CLICK_AND_COLLECT"}
              onChange={() => setDeliveryType("CLICK_AND_COLLECT")}
            />
            <div>
              <div style={{ fontSize: 13, fontWeight: 700 }}>Retrait direct au magasin (Click & Collect Adjamé)</div>
              <div style={{ fontSize: 11, color: "var(--color-text-muted)" }}>Gratuit • Retrait avec votre code PIN sécurisé sous 48h</div>
            </div>
          </label>

          <label style={{
            display: "flex",
            alignItems: "center",
            gap: 10,
            padding: 10,
            borderRadius: 8,
            border: `1px solid ${deliveryType === "GARE_EXPEDITION" ? "var(--color-primary)" : "var(--color-border)"}`,
            backgroundColor: deliveryType === "GARE_EXPEDITION" ? "var(--color-primary-container)" : "#FFFFFF",
            cursor: "pointer"
          }}>
            <input
              type="radio"
              name="delivery"
              checked={deliveryType === "GARE_EXPEDITION"}
              onChange={() => setDeliveryType("GARE_EXPEDITION")}
            />
            <div>
              <div style={{ fontSize: 13, fontWeight: 700 }}>Dépôt en gare de car (Intérieur du pays : UTB, CTE, SBTA)</div>
              <div style={{ fontSize: 11, color: "var(--color-text-muted)" }}>Dépôt sécurisé par l'Agent Relais Adjamé au guichet bagages</div>
            </div>
          </label>
        </div>

        <div style={{ marginTop: 12 }}>
          <label style={{ fontSize: 12, fontWeight: 600, display: "block", marginBottom: 4 }}>
            Instructions particulières pour le grossiste :
          </label>
          <input
            type="text"
            value={notes}
            onChange={(e) => setNotes(e.target.value)}
            placeholder="Ex: Mon livreur passe demain vers 11h..."
            style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid var(--color-border)", fontSize: 12 }}
          />
        </div>
      </div>

      {/* Récapitulatif Total & Validation */}
      <div style={{ backgroundColor: "#FFFFFF", borderRadius: 12, padding: 16, border: "1px solid var(--color-border)" }}>
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 14 }}>
          <span style={{ fontSize: 15, fontWeight: 700 }}>Total de la commande :</span>
          <span style={{ fontSize: 22, fontWeight: 900, color: "var(--color-secondary)" }}>
            {cartTotal.toLocaleString()} FCFA
          </span>
        </div>

        <button
          onClick={handlePlaceOrder}
          style={{
            width: "100%",
            backgroundColor: "var(--color-primary)",
            color: "#FFFFFF",
            border: "none",
            borderRadius: 8,
            padding: "14px 0",
            fontSize: 14,
            fontWeight: 800,
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            gap: 8
          }}
        >
          Valider ma commande grossiste <ArrowRight size={16} />
        </button>

        <div style={{ fontSize: 11, color: "var(--color-text-muted)", textAlign: "center", marginTop: 10 }}>
          🔒 Paiement et inspection du carton directement en boutique avec votre code PIN.
        </div>
      </div>
    </div>
  );
};
