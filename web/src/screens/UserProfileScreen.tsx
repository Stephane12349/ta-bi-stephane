import React, { useState } from "react";
import { useMarket } from "../context/MarketContext";
import { User, Phone, MapPin, Building, ShieldCheck, Save, Wifi } from "lucide-react";

export const UserProfileScreen: React.FC = () => {
  const { currentUser, updateProfile, switchRole } = useMarket();

  const [name, setName] = useState(currentUser.fullName);
  const [business, setBusiness] = useState(currentUser.businessName);
  const [commune, setCommune] = useState(currentUser.commune);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    updateProfile(name, business, commune);
  };

  return (
    <div style={{ maxWidth: 600, margin: "0 auto", padding: "16px 16px 90px 16px" }}>
      {/* Carte En-tête */}
      <div style={{
        backgroundColor: "var(--color-primary)",
        color: "#FFFFFF",
        borderRadius: 14,
        padding: 20,
        display: "flex",
        alignItems: "center",
        gap: 14,
        marginBottom: 20
      }}>
        <div style={{
          width: 50,
          height: 50,
          borderRadius: "50%",
          backgroundColor: "#FFFFFF",
          color: "var(--color-primary)",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          fontSize: 22,
          fontWeight: 900
        }}>
          {currentUser.fullName.charAt(0)}
        </div>
        <div>
          <h2 style={{ fontSize: 18, fontWeight: 800 }}>{currentUser.fullName}</h2>
          <div style={{ fontSize: 12, color: "#D4EFDF" }}>{currentUser.phone}</div>
          <span style={{
            backgroundColor: "var(--color-accent)",
            color: "#000",
            fontSize: 10,
            fontWeight: 800,
            padding: "2px 8px",
            borderRadius: 10,
            display: "inline-block",
            marginTop: 4
          }}>
            RÔLE : {currentUser.role}
          </span>
        </div>
      </div>

      {/* Formulaire Profil */}
      <form onSubmit={handleSubmit} style={{
        backgroundColor: "#FFFFFF",
        borderRadius: 12,
        padding: 16,
        border: "1px solid var(--color-border)",
        display: "flex",
        flexDirection: "column",
        gap: 12,
        marginBottom: 20
      }}>
        <h3 style={{ fontSize: 14, fontWeight: 800 }}>Mes Informations Commerciales</h3>

        <div>
          <label style={{ fontSize: 11, fontWeight: 700, display: "block", marginBottom: 4 }}>Nom complet</label>
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid var(--color-border)", fontSize: 13 }}
          />
        </div>

        <div>
          <label style={{ fontSize: 11, fontWeight: 700, display: "block", marginBottom: 4 }}>Nom de mon commerce / boutique</label>
          <input
            type="text"
            value={business}
            onChange={(e) => setBusiness(e.target.value)}
            style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid var(--color-border)", fontSize: 13 }}
          />
        </div>

        <div>
          <label style={{ fontSize: 11, fontWeight: 700, display: "block", marginBottom: 4 }}>Commune ou Ville en Côte d'Ivoire</label>
          <input
            type="text"
            value={commune}
            onChange={(e) => setCommune(e.target.value)}
            style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid var(--color-border)", fontSize: 13 }}
          />
        </div>

        <button
          type="submit"
          style={{
            backgroundColor: "var(--color-primary)",
            color: "#FFF",
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
          <Save size={15} /> Mettre à jour mon profil
        </button>
      </form>

      {/* Mode Économiseur de Données */}
      <div style={{
        backgroundColor: "#FFFFFF",
        borderRadius: 12,
        padding: 16,
        border: "1px solid var(--color-border)",
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center"
      }}>
        <div>
          <div style={{ fontSize: 13, fontWeight: 700, display: "flex", alignItems: "center", gap: 6 }}>
            <Wifi size={16} color="var(--color-primary)" /> Mode Bas Débit (Côte d'Ivoire)
          </div>
          <div style={{ fontSize: 11, color: "var(--color-text-muted)" }}>
            Compresse les images et privilégie le cache local hors-ligne.
          </div>
        </div>
        <span style={{ fontSize: 11, fontWeight: 800, color: "#27AE60" }}>ACTIF</span>
      </div>
    </div>
  );
};
