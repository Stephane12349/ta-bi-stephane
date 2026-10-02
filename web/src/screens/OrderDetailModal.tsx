import React from "react";
import { useMarket } from "../context/MarketContext";
import { X, MapPin, Phone, MessageCircle, ShieldCheck, CheckCircle2 } from "lucide-react";

export const OrderDetailModal: React.FC = () => {
  const { selectedOrderId, orders, shops, navigate } = useMarket();

  const order = orders.find((o) => o.id === selectedOrderId);
  const shop = order ? shops.find((s) => s.id === order.sellerId) : null;

  if (!order) return null;

  const handleCall = () => {
    if (shop) window.location.href = `tel:${shop.phone}`;
  };

  const handleWhatsApp = () => {
    if (shop) {
      const clean = shop.whatsapp.replace(/\D/g, "");
      const msg = encodeURIComponent(`Bonjour ${shop.name}, je vous contacte concernant ma commande ${order.id}.`);
      window.open(`https://api.whatsapp.com/send?phone=${clean}&text=${msg}`, "_blank");
    }
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
      onClick={() => navigate("orders")}
    >
      <div
        onClick={(e) => e.stopPropagation()}
        style={{
          backgroundColor: "#FFFFFF",
          width: "100%",
          maxWidth: 600,
          maxHeight: "90vh",
          borderTopLeftRadius: 20,
          borderTopRightRadius: 20,
          overflowY: "auto",
          padding: 20,
          position: "relative"
        }}
      >
        <button
          onClick={() => navigate("orders")}
          style={{
            position: "absolute",
            top: 14,
            right: 14,
            width: 32,
            height: 32,
            borderRadius: "50%",
            border: "none",
            backgroundColor: "#F4F6F6",
            display: "flex",
            alignItems: "center",
            justifyContent: "center"
          }}
        >
          <X size={16} />
        </button>

        <div style={{ display: "flex", alignItems: "center", gap: 8, marginBottom: 4 }}>
          <h2 style={{ fontSize: 18, fontWeight: 900, color: "var(--color-primary)" }}>{order.id}</h2>
          <span style={{
            backgroundColor: "#FEF9E7",
            color: "#B7950B",
            fontSize: 11,
            fontWeight: 800,
            padding: "2px 8px",
            borderRadius: 12
          }}>
            {order.status}
          </span>
        </div>

        <div style={{ fontSize: 12, color: "var(--color-text-muted)", marginBottom: 16 }}>
          Passée le {new Date(order.createdAt).toLocaleDateString("fr-FR", { hour: "2-digit", minute: "2-digit" })}
        </div>

        {/* Code PIN de Retrait Magasin */}
        {order.status !== "COMPLETED" && (
          <div style={{
            backgroundColor: "#FBEEE6",
            border: "1px dashed #E67E22",
            borderRadius: 12,
            padding: 14,
            textAlign: "center",
            marginBottom: 16
          }}>
            <div style={{ fontSize: 11, fontWeight: 700, color: "#78281F", textTransform: "uppercase" }}>
              Code de Retrait Magasin Sécurisé (PIN)
            </div>
            <div style={{ fontSize: 32, fontWeight: 900, color: "#900C3F", letterSpacing: 4, margin: "4px 0" }}>
              {order.pickupPinCode}
            </div>
            <div style={{ fontSize: 11, color: "#78281F" }}>
              Présentez ce code au magasinier à Adjamé après inspection de vos colis.
            </div>
          </div>
        )}

        {/* Détails du Vendeur */}
        <div style={{
          backgroundColor: "#F8F9FA",
          borderRadius: 10,
          padding: 12,
          border: "1px solid var(--color-border)",
          marginBottom: 16
        }}>
          <div style={{ fontSize: 11, fontWeight: 700, color: "var(--color-primary)" }}>FOURNISSEUR :</div>
          <div style={{ fontSize: 14, fontWeight: 800 }}>{order.sellerName}</div>
          <div style={{ fontSize: 12, color: "var(--color-text-muted)" }}>{order.sellerSector}</div>

          {shop && (
            <div style={{ fontSize: 11, marginTop: 4, color: "#2C3E50" }}>
              📍 <strong>Repère :</strong> {shop.landmarks}
            </div>
          )}

          <div style={{ display: "flex", gap: 8, marginTop: 10 }}>
            <button
              onClick={handleCall}
              style={{
                flex: 1,
                padding: "6px 0",
                borderRadius: 6,
                border: "1px solid var(--color-border)",
                backgroundColor: "#FFF",
                fontSize: 12,
                fontWeight: 700,
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                gap: 4
              }}
            >
              <Phone size={13} /> Appeler
            </button>
            <button
              onClick={handleWhatsApp}
              style={{
                flex: 1,
                padding: "6px 0",
                borderRadius: 6,
                border: "none",
                backgroundColor: "var(--color-whatsapp)",
                color: "#FFF",
                fontSize: 12,
                fontWeight: 700,
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                gap: 4
              }}
            >
              <MessageCircle size={13} /> WhatsApp
            </button>
          </div>
        </div>

        {/* Articles commandés */}
        <div style={{ marginBottom: 16 }}>
          <h4 style={{ fontSize: 13, fontWeight: 800, marginBottom: 8 }}>Articles de la commande :</h4>
          <div style={{ display: "flex", flexDirection: "column", gap: 6 }}>
            {order.items.map((it, idx) => (
              <div
                key={idx}
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  padding: "8px 10px",
                  backgroundColor: "#FFFFFF",
                  border: "1px solid var(--color-border)",
                  borderRadius: 6,
                  fontSize: 12
                }}
              >
                <div>
                  <div style={{ fontWeight: 700 }}>{it.productName}</div>
                  <div style={{ fontSize: 10, color: "var(--color-text-muted)" }}>{it.packaging} × {it.quantity} ctn</div>
                </div>
                <div style={{ fontWeight: 800, color: "var(--color-secondary)" }}>
                  {it.subtotal.toLocaleString()} FCFA
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Total & Mode de Livraison */}
        <div style={{ borderTop: "1px solid var(--color-border)", paddingTop: 12, display: "flex", justifyContent: "space-between", alignItems: "center" }}>
          <div>
            <div style={{ fontSize: 11, color: "var(--color-text-muted)" }}>Mode : {order.deliveryType === "CLICK_AND_COLLECT" ? "Retrait magasin" : "Gare car"}</div>
            {order.notes && <div style={{ fontSize: 11, color: "#7F8C8D" }}>Note : {order.notes}</div>}
          </div>
          <div style={{ fontSize: 18, fontWeight: 900, color: "var(--color-primary)" }}>
            {order.totalAmount.toLocaleString()} FCFA
          </div>
        </div>
      </div>
    </div>
  );
};
