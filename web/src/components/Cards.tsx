import React from "react";
import { Product, WholesalerShop, Order } from "../types";
import { useMarket } from "../context/MarketContext";
import {
  Heart,
  Phone,
  MessageCircle,
  MapPin,
  ShieldCheck,
  Star,
  CheckCircle2,
  Clock,
  Package,
} from "lucide-react";

export const ProductCard: React.FC<{ product: Product }> = ({ product }) => {
  const {
    openProductDetail,
    favoriteProductIds,
    toggleFavoriteProduct,
    addToCart,
  } = useMarket();

  const isFav = favoriteProductIds.includes(product.id);

  return (
    <div
      style={{
        backgroundColor: "#FFFFFF",
        borderRadius: 12,
        overflow: "hidden",
        border: "1px solid var(--color-border)",
        display: "flex",
        flexDirection: "column",
        boxShadow: "0 2px 6px rgba(0,0,0,0.04)",
        position: "relative"
      }}
    >
      {/* Image & Badges */}
      <div
        onClick={() => openProductDetail(product.id)}
        style={{ position: "relative", height: 140, cursor: "pointer", backgroundColor: "#ECEFF1" }}
      >
        <img
          src={product.images[0]}
          alt={product.name}
          style={{ width: "100%", height: "100%", objectFit: "cover" }}
          loading="lazy"
        />

        {/* Conditionnement */}
        <span
          style={{
            position: "absolute",
            bottom: 6,
            left: 6,
            backgroundColor: "rgba(0,0,0,0.75)",
            color: "#FFFFFF",
            fontSize: 10,
            fontWeight: 700,
            padding: "2px 6px",
            borderRadius: 4
          }}
        >
          {product.packaging}
        </span>

        {/* Bouton Favori */}
        <button
          onClick={(e) => {
            e.stopPropagation();
            toggleFavoriteProduct(product.id);
          }}
          style={{
            position: "absolute",
            top: 6,
            right: 6,
            width: 30,
            height: 30,
            borderRadius: "50%",
            backgroundColor: "rgba(255,255,255,0.9)",
            border: "none",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            color: isFav ? "#E74C3C" : "#7F8C8D"
          }}
        >
          <Heart size={16} fill={isFav ? "#E74C3C" : "none"} />
        </button>
      </div>

      {/* Contenu */}
      <div
        onClick={() => openProductDetail(product.id)}
        style={{ padding: 10, flex: 1, display: "flex", flexDirection: "column", cursor: "pointer" }}
      >
        <div style={{ fontSize: 10, color: "var(--color-primary)", fontWeight: 700, textTransform: "uppercase" }}>
          {product.sellerSector}
        </div>

        <h4 style={{
          fontSize: 13,
          fontWeight: 700,
          color: "var(--color-text)",
          margin: "4px 0",
          lineHeight: 1.3,
          display: "-webkit-box",
          WebkitLineClamp: 2,
          WebkitBoxOrient: "vertical",
          overflow: "hidden"
        }}>
          {product.name}
        </h4>

        {/* Prix de base & Paliers */}
        <div style={{ marginTop: "auto", paddingTop: 6 }}>
          <div style={{ display: "flex", alignItems: "baseline", gap: 4 }}>
            <span style={{ fontSize: 16, fontWeight: 800, color: "var(--color-secondary)" }}>
              {product.basePrice.toLocaleString()} F
            </span>
            <span style={{ fontSize: 11, color: "var(--color-text-muted)" }}>/ carton</span>
          </div>

          <div style={{ fontSize: 10, color: "var(--color-text-muted)", marginTop: 2 }}>
            MOQ : min. {product.minOrderQuantity} carton(s)
          </div>

          {product.priceTiers.length > 0 && (
            <div style={{
              fontSize: 10,
              backgroundColor: "var(--color-primary-container)",
              color: "var(--color-primary)",
              fontWeight: 700,
              padding: "2px 6px",
              borderRadius: 4,
              marginTop: 6,
              display: "inline-block"
            }}>
              Dès {product.priceTiers[0].minQuantity} ctn : {product.priceTiers[0].unitPriceFcfa.toLocaleString()} F
            </div>
          )}
        </div>
      </div>

      {/* Bouton Rapide Ajouter au Panier */}
      <div style={{ padding: "0 10px 10px 10px" }}>
        <button
          onClick={(e) => {
            e.stopPropagation();
            addToCart(product, product.minOrderQuantity);
          }}
          style={{
            width: "100%",
            backgroundColor: "var(--color-primary)",
            color: "#FFFFFF",
            border: "none",
            borderRadius: 6,
            padding: "8px 0",
            fontSize: 11,
            fontWeight: 700
          }}
        >
          Commander ({product.minOrderQuantity} ctn)
        </button>
      </div>
    </div>
  );
};

export const ShopCard: React.FC<{ shop: WholesalerShop }> = ({ shop }) => {
  const { openShopDetail, favoriteShopIds, toggleFavoriteShop } = useMarket();
  const isFav = favoriteShopIds.includes(shop.id);

  const handleWhatsApp = (e: React.MouseEvent) => {
    e.stopPropagation();
    const clean = shop.whatsapp.replace(/\D/g, "");
    const msg = encodeURIComponent(`Bonjour ${shop.name}, je vous contacte depuis Adjamé Market pour vos prix de gros.`);
    window.open(`https://api.whatsapp.com/send?phone=${clean}&text=${msg}`, "_blank");
  };

  const handleCall = (e: React.MouseEvent) => {
    e.stopPropagation();
    window.location.href = `tel:${shop.phone}`;
  };

  return (
    <div
      onClick={() => openShopDetail(shop.id)}
      style={{
        backgroundColor: "#FFFFFF",
        borderRadius: 12,
        padding: 14,
        border: "1px solid var(--color-border)",
        boxShadow: "0 2px 6px rgba(0,0,0,0.04)",
        cursor: "pointer",
        display: "flex",
        flexDirection: "column",
        gap: 8
      }}
    >
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start" }}>
        <div>
          <div style={{ display: "flex", alignItems: "center", gap: 6 }}>
            <h3 style={{ fontSize: 15, fontWeight: 800, color: "var(--color-text)" }}>{shop.name}</h3>
            {shop.isVerified && (
              <span style={{
                backgroundColor: "var(--color-accent)",
                color: "#000",
                fontSize: 9,
                fontWeight: 800,
                padding: "1px 6px",
                borderRadius: 4
              }}>
                VÉRIFIÉ TERRAIN
              </span>
            )}
          </div>
          <div style={{ fontSize: 11, color: "var(--color-text-muted)", marginTop: 2 }}>
            Gérant : {shop.ownerName}
          </div>
        </div>

        <button
          onClick={(e) => {
            e.stopPropagation();
            toggleFavoriteShop(shop.id);
          }}
          style={{ background: "none", border: "none", color: isFav ? "#E74C3C" : "#BDC3C7" }}
        >
          <Heart size={18} fill={isFav ? "#E74C3C" : "none"} />
        </button>
      </div>

      <div style={{ display: "flex", alignItems: "center", gap: 4, fontSize: 11, color: "var(--color-text-muted)" }}>
        <MapPin size={13} color="var(--color-secondary)" />
        <span>{shop.address} ({shop.marketSector})</span>
      </div>

      <div style={{
        fontSize: 11,
        backgroundColor: "#F4F6F6",
        padding: "4px 8px",
        borderRadius: 6,
        color: "#2C3E50"
      }}>
        📍 <strong>Repère :</strong> {shop.landmarks}
      </div>

      {/* Actions Directes : Appel & WhatsApp */}
      <div style={{ display: "flex", gap: 8, marginTop: 4 }}>
        <button
          onClick={handleCall}
          style={{
            flex: 1,
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            gap: 6,
            padding: "8px 0",
            borderRadius: 6,
            border: "1px solid var(--color-border)",
            backgroundColor: "#FFFFFF",
            fontSize: 12,
            fontWeight: 700,
            color: "var(--color-text)"
          }}
        >
          <Phone size={14} /> Appeler
        </button>
        <button
          onClick={handleWhatsApp}
          style={{
            flex: 1,
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            gap: 6,
            padding: "8px 0",
            borderRadius: 6,
            border: "none",
            backgroundColor: "var(--color-whatsapp)",
            fontSize: 12,
            fontWeight: 700,
            color: "#FFFFFF"
          }}
        >
          <MessageCircle size={14} /> WhatsApp
        </button>
      </div>
    </div>
  );
};

export const OrderCard: React.FC<{ order: Order }> = ({ order }) => {
  const { openOrderDetail } = useMarket();

  const statusColors: Record<string, { bg: string; text: string }> = {
    PENDING: { bg: "#FEF9E7", text: "#B7950B" },
    CONFIRMED: { bg: "#E8F8F5", text: "#16A085" },
    PREPARING: { bg: "#EBF5FB", text: "#2980B9" },
    READY: { bg: "#EAFAF1", text: "#27AE60" },
    COMPLETED: { bg: "#EAFAF1", text: "#27AE60" },
    CANCELLED: { bg: "#FDEDEC", text: "#C0392B" },
    REJECTED: { bg: "#FDEDEC", text: "#C0392B" },
  };

  const statusLabel: Record<string, string> = {
    PENDING: "En attente grossiste",
    CONFIRMED: "Confirmée",
    PREPARING: "En préparation",
    READY: "Prête au magasin",
    COMPLETED: "Retirée & Clôturée",
    CANCELLED: "Annulée",
    REJECTED: "Refusée",
  };

  const conf = statusColors[order.status] || { bg: "#F4F6F6", text: "#333" };

  return (
    <div
      onClick={() => openOrderDetail(order.id)}
      style={{
        backgroundColor: "#FFFFFF",
        borderRadius: 12,
        padding: 14,
        border: "1px solid var(--color-border)",
        boxShadow: "0 2px 6px rgba(0,0,0,0.04)",
        cursor: "pointer",
        display: "flex",
        flexDirection: "column",
        gap: 6
      }}
    >
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <span style={{ fontWeight: 800, color: "var(--color-primary)", fontSize: 15 }}>
          {order.id}
        </span>
        <span style={{
          backgroundColor: conf.bg,
          color: conf.text,
          fontSize: 11,
          fontWeight: 700,
          padding: "3px 8px",
          borderRadius: 12
        }}>
          {statusLabel[order.status] || order.status}
        </span>
      </div>

      <div style={{ fontSize: 13, fontWeight: 600 }}>
        Grossiste : {order.sellerName} ({order.sellerSector})
      </div>

      <div style={{ fontSize: 12, color: "var(--color-text-muted)" }}>
        {order.items.length} article(s) • Total : <strong style={{ color: "var(--color-secondary)" }}>{order.totalAmount.toLocaleString()} FCFA</strong>
      </div>

      {order.status !== "COMPLETED" && order.status !== "CANCELLED" && (
        <div style={{
          marginTop: 4,
          padding: "6px 10px",
          backgroundColor: "#FBEEE6",
          borderRadius: 6,
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center"
        }}>
          <span style={{ fontSize: 11, color: "#78281F", fontWeight: 600 }}>Code retrait magasin :</span>
          <span style={{ fontSize: 14, fontWeight: 900, color: "#900C3F", letterSpacing: 2 }}>{order.pickupPinCode}</span>
        </div>
      )}
    </div>
  );
};
