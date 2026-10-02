import React, { useState } from "react";
import { useMarket } from "../context/MarketContext";
import { UserRole } from "../types";
import {
  ShoppingBag,
  Bell,
  Search,
  Store,
  ShieldCheck,
  User,
  ChevronDown,
  Sparkles,
} from "lucide-react";

export const Header: React.FC = () => {
  const {
    currentUser,
    switchRole,
    cartItems,
    notifications,
    navigate,
    currentScreen,
    searchQuery,
    setSearchQuery,
  } = useMarket();

  const [roleMenuOpen, setRoleMenuOpen] = useState(false);

  const unreadNotifs = notifications.filter((n) => !n.isRead).length;

  return (
    <header style={{
      position: "sticky",
      top: 0,
      zIndex: 40,
      backgroundColor: "var(--color-primary)",
      color: "#FFFFFF",
      boxShadow: "0 2px 8px rgba(0,0,0,0.15)"
    }}>
      <div style={{
        maxWidth: 1100,
        margin: "0 auto",
        padding: "10px 16px",
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        gap: 12
      }}>
        {/* Logo & Titre */}
        <div
          onClick={() => navigate("home")}
          style={{ display: "flex", alignItems: "center", gap: 8, cursor: "pointer" }}
        >
          <div style={{
            width: 38,
            height: 38,
            borderRadius: 8,
            backgroundColor: "#FFFFFF",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            fontWeight: 900,
            fontSize: 20,
            color: "var(--color-primary)"
          }}>
            A
          </div>
          <div>
            <div style={{ fontWeight: 800, fontSize: 17, letterSpacing: -0.3, lineHeight: 1.1 }}>
              Adjamé Market
            </div>
            <div style={{ fontSize: 10, color: "var(--color-accent)", fontWeight: 600 }}>
              GROSSISTES B2B CI
            </div>
          </div>
        </div>

        {/* Sélecteur de Rôles Interactif */}
        <div style={{ position: "relative" }}>
          <button
            onClick={() => setRoleMenuOpen(!roleMenuOpen)}
            style={{
              display: "flex",
              alignItems: "center",
              gap: 6,
              padding: "5px 10px",
              borderRadius: 20,
              border: "1px solid rgba(255,255,255,0.25)",
              backgroundColor: currentUser.role === "BUYER" ? "#16A085" : currentUser.role === "WHOLESALER" ? "var(--color-secondary)" : "var(--color-accent)",
              color: currentUser.role === "ADMIN" ? "#000" : "#FFF",
              fontSize: 11,
              fontWeight: 700,
              textTransform: "uppercase"
            }}
          >
            {currentUser.role === "BUYER" && <User size={13} />}
            {currentUser.role === "WHOLESALER" && <Store size={13} />}
            {currentUser.role === "ADMIN" && <ShieldCheck size={13} />}
            <span>{currentUser.role === "BUYER" ? "Acheteur" : currentUser.role === "WHOLESALER" ? "Grossiste" : "Admin"}</span>
            <ChevronDown size={12} />
          </button>

          {roleMenuOpen && (
            <div
              style={{
                position: "absolute",
                top: "120%",
                right: 0,
                backgroundColor: "#FFFFFF",
                color: "#1C2833",
                borderRadius: 10,
                boxShadow: "0 8px 24px rgba(0,0,0,0.2)",
                padding: 6,
                minWidth: 190,
                zIndex: 50,
                border: "1px solid #E5E8E8"
              }}
            >
              <div style={{ fontSize: 10, fontWeight: 700, color: "#7F8C8D", padding: "6px 8px" }}>
                BASCULE ESPACE
              </div>
              <button
                onClick={() => { switchRole("BUYER"); setRoleMenuOpen(false); }}
                style={{
                  width: "100%",
                  textAlign: "left",
                  padding: "8px 10px",
                  borderRadius: 6,
                  border: "none",
                  backgroundColor: currentUser.role === "BUYER" ? "#E8F8F5" : "transparent",
                  color: "#16A085",
                  fontWeight: 600,
                  fontSize: 12,
                  display: "flex",
                  alignItems: "center",
                  gap: 8
                }}
              >
                <User size={14} /> Espace Acheteur (Revendeur)
              </button>
              <button
                onClick={() => { switchRole("WHOLESALER"); setRoleMenuOpen(false); }}
                style={{
                  width: "100%",
                  textAlign: "left",
                  padding: "8px 10px",
                  borderRadius: 6,
                  border: "none",
                  backgroundColor: currentUser.role === "WHOLESALER" ? "#FEF5E7" : "transparent",
                  color: "var(--color-secondary)",
                  fontWeight: 600,
                  fontSize: 12,
                  display: "flex",
                  alignItems: "center",
                  gap: 8
                }}
              >
                <Store size={14} /> Espace Grossiste (Boutique)
              </button>
              <button
                onClick={() => { switchRole("ADMIN"); setRoleMenuOpen(false); }}
                style={{
                  width: "100%",
                  textAlign: "left",
                  padding: "8px 10px",
                  borderRadius: 6,
                  border: "none",
                  backgroundColor: currentUser.role === "ADMIN" ? "#FEF9E7" : "transparent",
                  color: "#B7950B",
                  fontWeight: 600,
                  fontSize: 12,
                  display: "flex",
                  alignItems: "center",
                  gap: 8
                }}
              >
                <ShieldCheck size={14} /> Espace Admin (Supervision)
              </button>
            </div>
          )}
        </div>

        {/* Actions : Panier, Notifs, Profil */}
        <div style={{ display: "flex", alignItems: "center", gap: 10 }}>
          {currentUser.role === "BUYER" && (
            <button
              onClick={() => navigate("cart")}
              style={{
                position: "relative",
                background: "rgba(255,255,255,0.15)",
                border: "none",
                borderRadius: "50%",
                width: 38,
                height: 38,
                color: "#FFFFFF",
                display: "flex",
                alignItems: "center",
                justifyContent: "center"
              }}
              title="Panier d'achats"
            >
              <ShoppingBag size={18} />
              {cartItems.length > 0 && (
                <span style={{
                  position: "absolute",
                  top: -2,
                  right: -2,
                  backgroundColor: "var(--color-secondary)",
                  color: "#FFFFFF",
                  fontSize: 10,
                  fontWeight: 800,
                  borderRadius: 10,
                  padding: "1px 5px",
                  border: "2px solid var(--color-primary)"
                }}>
                  {cartItems.length}
                </span>
              )}
            </button>
          )}

          <button
            onClick={() => navigate("notifications")}
            style={{
              position: "relative",
              background: "rgba(255,255,255,0.15)",
              border: "none",
              borderRadius: "50%",
              width: 38,
              height: 38,
              color: "#FFFFFF",
              display: "flex",
              alignItems: "center",
              justifyContent: "center"
            }}
            title="Notifications"
          >
            <Bell size={18} />
            {unreadNotifs > 0 && (
              <span style={{
                position: "absolute",
                top: -2,
                right: -2,
                backgroundColor: "#E74C3C",
                color: "#FFFFFF",
                fontSize: 10,
                fontWeight: 800,
                borderRadius: 10,
                padding: "1px 5px"
              }}>
                {unreadNotifs}
              </span>
            )}
          </button>
        </div>
      </div>
    </header>
  );
};
