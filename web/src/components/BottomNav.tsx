import React from "react";
import { useMarket } from "../context/MarketContext";
import { Home, Search, Heart, ReceiptText, Store, ShieldCheck, User, Package } from "lucide-react";

export const BottomNav: React.FC = () => {
  const { currentScreen, navigate, currentUser } = useMarket();

  const role = currentUser.role;

  return (
    <nav style={{
      position: "fixed",
      bottom: 0,
      left: 0,
      right: 0,
      zIndex: 40,
      backgroundColor: "#FFFFFF",
      borderTop: "1px solid var(--color-border)",
      display: "flex",
      justifyContent: "space-around",
      alignItems: "center",
      padding: "6px 0 10px 0",
      boxShadow: "0 -2px 10px rgba(0,0,0,0.05)"
    }}>
      {role === "BUYER" && (
        <>
          <button
            onClick={() => navigate("home")}
            style={{
              background: "none",
              border: "none",
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              gap: 2,
              color: currentScreen === "home" ? "var(--color-primary)" : "var(--color-text-muted)",
              fontSize: 11,
              fontWeight: currentScreen === "home" ? 700 : 500
            }}
          >
            <Home size={20} />
            <span>Accueil</span>
          </button>

          <button
            onClick={() => navigate("search")}
            style={{
              background: "none",
              border: "none",
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              gap: 2,
              color: currentScreen === "search" ? "var(--color-primary)" : "var(--color-text-muted)",
              fontSize: 11,
              fontWeight: currentScreen === "search" ? 700 : 500
            }}
          >
            <Search size={20} />
            <span>Rechercher</span>
          </button>

          <button
            onClick={() => navigate("favorites")}
            style={{
              background: "none",
              border: "none",
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              gap: 2,
              color: currentScreen === "favorites" ? "var(--color-primary)" : "var(--color-text-muted)",
              fontSize: 11,
              fontWeight: currentScreen === "favorites" ? 700 : 500
            }}
          >
            <Heart size={20} />
            <span>Favoris</span>
          </button>

          <button
            onClick={() => navigate("orders")}
            style={{
              background: "none",
              border: "none",
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              gap: 2,
              color: currentScreen === "orders" ? "var(--color-primary)" : "var(--color-text-muted)",
              fontSize: 11,
              fontWeight: currentScreen === "orders" ? 700 : 500
            }}
          >
            <ReceiptText size={20} />
            <span>Commandes</span>
          </button>

          <button
            onClick={() => navigate("profile")}
            style={{
              background: "none",
              border: "none",
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              gap: 2,
              color: currentScreen === "profile" ? "var(--color-primary)" : "var(--color-text-muted)",
              fontSize: 11,
              fontWeight: currentScreen === "profile" ? 700 : 500
            }}
          >
            <User size={20} />
            <span>Profil</span>
          </button>
        </>
      )}

      {role === "WHOLESALER" && (
        <>
          <button
            onClick={() => navigate("wholesaler_dashboard")}
            style={{
              background: "none",
              border: "none",
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              gap: 2,
              color: currentScreen === "wholesaler_dashboard" ? "var(--color-secondary)" : "var(--color-text-muted)",
              fontSize: 11,
              fontWeight: currentScreen === "wholesaler_dashboard" ? 700 : 500
            }}
          >
            <Store size={20} />
            <span>Dashboard</span>
          </button>

          <button
            onClick={() => navigate("wholesaler_catalog")}
            style={{
              background: "none",
              border: "none",
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              gap: 2,
              color: currentScreen === "wholesaler_catalog" ? "var(--color-secondary)" : "var(--color-text-muted)",
              fontSize: 11,
              fontWeight: currentScreen === "wholesaler_catalog" ? 700 : 500
            }}
          >
            <Package size={20} />
            <span>Catalogue</span>
          </button>

          <button
            onClick={() => navigate("wholesaler_orders")}
            style={{
              background: "none",
              border: "none",
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              gap: 2,
              color: currentScreen === "wholesaler_orders" ? "var(--color-secondary)" : "var(--color-text-muted)",
              fontSize: 11,
              fontWeight: currentScreen === "wholesaler_orders" ? 700 : 500
            }}
          >
            <ReceiptText size={20} />
            <span>Commandes</span>
          </button>

          <button
            onClick={() => navigate("profile")}
            style={{
              background: "none",
              border: "none",
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              gap: 2,
              color: currentScreen === "profile" ? "var(--color-secondary)" : "var(--color-text-muted)",
              fontSize: 11,
              fontWeight: currentScreen === "profile" ? 700 : 500
            }}
          >
            <User size={20} />
            <span>Profil</span>
          </button>
        </>
      )}

      {role === "ADMIN" && (
        <>
          <button
            onClick={() => navigate("admin_dashboard")}
            style={{
              background: "none",
              border: "none",
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              gap: 2,
              color: currentScreen === "admin_dashboard" ? "#B7950B" : "var(--color-text-muted)",
              fontSize: 11,
              fontWeight: currentScreen === "admin_dashboard" ? 700 : 500
            }}
          >
            <ShieldCheck size={20} />
            <span>Supervision</span>
          </button>

          <button
            onClick={() => navigate("admin_kyc")}
            style={{
              background: "none",
              border: "none",
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              gap: 2,
              color: currentScreen === "admin_kyc" ? "#B7950B" : "var(--color-text-muted)",
              fontSize: 11,
              fontWeight: currentScreen === "admin_kyc" ? 700 : 500
            }}
          >
            <Store size={20} />
            <span>Boutiques KYC</span>
          </button>

          <button
            onClick={() => navigate("profile")}
            style={{
              background: "none",
              border: "none",
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              gap: 2,
              color: currentScreen === "profile" ? "#B7950B" : "var(--color-text-muted)",
              fontSize: 11,
              fontWeight: currentScreen === "profile" ? 700 : 500
            }}
          >
            <User size={20} />
            <span>Admin</span>
          </button>
        </>
      )}
    </nav>
  );
};
