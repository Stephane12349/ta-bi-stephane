import React, { useState } from "react";
import { useMarket } from "../context/MarketContext";
import { Store, Plus, Package, Check, X, Trash2, Eye, KeyRound } from "lucide-react";
import { Product } from "../types";

export const WholesalerDashboard: React.FC = () => {
  const {
    currentUser,
    products,
    orders,
    categories,
    addProduct,
    deleteProduct,
    toggleStock,
    updateOrderStatus,
    showNotice,
  } = useMarket();

  const [addModalOpen, setAddModalOpen] = useState(false);
  const [pinInputs, setPinInputs] = useState<Record<string, string>>({});

  // Formulaire d'ajout
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [categoryId, setCategoryId] = useState(categories[0]?.id || "cat_cosmetics");
  const [packaging, setPackaging] = useState("Carton de 24 pièces");
  const [minOrderQuantity, setMinOrderQuantity] = useState(2);
  const [basePrice, setBasePrice] = useState(25000);
  const [stockQuantity, setStockQuantity] = useState(50);
  const [tierPrice1, setTierPrice1] = useState(22500);

  const myOrders = orders; // En mode simulation, affiche toutes les commandes ou celles associées

  const handleAddSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim()) return;

    addProduct({
      name,
      description: description || "Article de gros certifié disponible au magasin.",
      categoryId,
      sellerId: currentUser.id,
      sellerName: currentUser.businessName || currentUser.fullName,
      sellerSector: currentUser.commune || "Adjamé Forum",
      images: ["https://images.unsplash.com/photo-1584917865442-de89df76afd3?w=500"],
      packaging,
      minOrderQuantity,
      basePrice,
      priceTiers: [
        { minQuantity: minOrderQuantity, maxQuantity: minOrderQuantity + 4, unitPriceFcfa: basePrice, label: `${minOrderQuantity} à ${minOrderQuantity + 4} ctn` },
        { minQuantity: minOrderQuantity + 5, maxQuantity: null, unitPriceFcfa: tierPrice1, label: `${minOrderQuantity + 5}+ ctn (Tarif Dépôt)` },
      ],
      stockStatus: stockQuantity > 0 ? "IN_STOCK" : "OUT_OF_STOCK",
      stockQuantity,
      isVerifiedSeller: true,
      isPromoted: false,
    });

    setAddModalOpen(false);
    setName("");
    setDescription("");
  };

  const handleVerifyPin = (orderId: string, expectedPin: string) => {
    const input = pinInputs[orderId] || "";
    if (input.trim() === expectedPin) {
      updateOrderStatus(orderId, "COMPLETED");
      showNotice(`Code PIN validé ! Marchandise remise.`);
    } else {
      showNotice(`Code PIN erroné ! Veuillez redemander le code au client.`);
    }
  };

  return (
    <div style={{ maxWidth: 900, margin: "0 auto", padding: "16px 16px 90px 16px" }}>
      {/* En-tête Espace Grossiste */}
      <div style={{
        backgroundColor: "var(--color-secondary)",
        color: "#FFFFFF",
        borderRadius: 14,
        padding: 18,
        marginBottom: 20
      }}>
        <div style={{ fontSize: 11, fontWeight: 800, textTransform: "uppercase", color: "var(--color-accent)" }}>
          ESPACE GESTION MAGASINIER
        </div>
        <h2 style={{ fontSize: 20, fontWeight: 900, margin: "4px 0" }}>
          {currentUser.businessName || "Ma Boutique Adjamé"}
        </h2>
        <div style={{ fontSize: 12, color: "#FDEDEC" }}>
          Gérant : {currentUser.fullName} • Statut : <strong>Boutique Partenaire Certifiée</strong>
        </div>
      </div>

      {/* Cartes KPI */}
      <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(130px, 1fr))", gap: 10, marginBottom: 20 }}>
        <div style={{ backgroundColor: "#FFFFFF", padding: 12, borderRadius: 10, border: "1px solid var(--color-border)", textAlign: "center" }}>
          <div style={{ fontSize: 22, fontWeight: 900, color: "var(--color-primary)" }}>{products.length}</div>
          <div style={{ fontSize: 11, color: "var(--color-text-muted)" }}>Articles au catalogue</div>
        </div>
        <div style={{ backgroundColor: "#FFFFFF", padding: 12, borderRadius: 10, border: "1px solid var(--color-border)", textAlign: "center" }}>
          <div style={{ fontSize: 22, fontWeight: 900, color: "var(--color-secondary)" }}>{myOrders.length}</div>
          <div style={{ fontSize: 11, color: "var(--color-text-muted)" }}>Commandes reçues</div>
        </div>
        <div style={{ backgroundColor: "#FFFFFF", padding: 12, borderRadius: 10, border: "1px solid var(--color-border)", textAlign: "center" }}>
          <div style={{ fontSize: 22, fontWeight: 900, color: "#27AE60" }}>
            {myOrders.reduce((acc, o) => acc + o.totalAmount, 0).toLocaleString()} F
          </div>
          <div style={{ fontSize: 11, color: "var(--color-text-muted)" }}>Volume d'affaires</div>
        </div>
      </div>

      {/* Section Gestion des Commandes Reçues */}
      <div style={{ marginBottom: 28 }}>
        <h3 style={{ fontSize: 16, fontWeight: 800, marginBottom: 12 }}>
          Commandes de Retrait Magasin ({myOrders.length})
        </h3>

        <div style={{ display: "flex", flexDirection: "column", gap: 10 }}>
          {myOrders.map((o) => (
            <div
              key={o.id}
              style={{
                backgroundColor: "#FFFFFF",
                borderRadius: 10,
                padding: 14,
                border: "1px solid var(--color-border)",
                display: "flex",
                flexDirection: "column",
                gap: 8
              }}
            >
              <div style={{ display: "flex", justifyContent: "space-between" }}>
                <span style={{ fontWeight: 800, color: "var(--color-primary)" }}>{o.id}</span>
                <span style={{ fontSize: 12, fontWeight: 700, color: "var(--color-secondary)" }}>
                  {o.totalAmount.toLocaleString()} FCFA
                </span>
              </div>
              <div style={{ fontSize: 12 }}>
                Client : <strong>{o.buyerName}</strong> ({o.buyerPhone})
              </div>

              {/* Transition de statut */}
              <div style={{ display: "flex", gap: 8, alignItems: "center", marginTop: 4 }}>
                {o.status === "PENDING" && (
                  <button
                    onClick={() => updateOrderStatus(o.id, "CONFIRMED")}
                    style={{ backgroundColor: "var(--color-primary)", color: "#FFF", border: "none", padding: "6px 12px", borderRadius: 6, fontSize: 11, fontWeight: 700 }}
                  >
                    Confirmer la réservation
                  </button>
                )}

                {o.status === "CONFIRMED" && (
                  <button
                    onClick={() => updateOrderStatus(o.id, "READY")}
                    style={{ backgroundColor: "#2980B9", color: "#FFF", border: "none", padding: "6px 12px", borderRadius: 6, fontSize: 11, fontWeight: 700 }}
                  >
                    Marquer prête pour retrait
                  </button>
                )}

                {o.status === "READY" && (
                  <div style={{ display: "flex", gap: 6, width: "100%" }}>
                    <input
                      type="text"
                      placeholder="Code PIN client..."
                      value={pinInputs[o.id] || ""}
                      onChange={(e) => setPinInputs({ ...pinInputs, [o.id]: e.target.value })}
                      style={{ flex: 1, padding: "6px 8px", borderRadius: 6, border: "1px solid var(--color-border)", fontSize: 12 }}
                    />
                    <button
                      onClick={() => handleVerifyPin(o.id, o.pickupPinCode)}
                      style={{ backgroundColor: "#27AE60", color: "#FFF", border: "none", padding: "6px 12px", borderRadius: 6, fontSize: 11, fontWeight: 700 }}
                    >
                      Délivrer (PIN)
                    </button>
                  </div>
                )}

                {o.status === "COMPLETED" && (
                  <span style={{ fontSize: 12, fontWeight: 800, color: "#27AE60" }}>
                    ✓ Colis délivré avec succès
                  </span>
                )}
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Section Gestion du Catalogue */}
      <div>
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 12 }}>
          <h3 style={{ fontSize: 16, fontWeight: 800 }}>Mon Catalogue de Gros</h3>
          <button
            onClick={() => setAddModalOpen(true)}
            style={{
              backgroundColor: "var(--color-primary)",
              color: "#FFFFFF",
              border: "none",
              borderRadius: 8,
              padding: "8px 14px",
              fontSize: 12,
              fontWeight: 800,
              display: "flex",
              alignItems: "center",
              gap: 6
            }}
          >
            <Plus size={15} /> Publier un Arrivage
          </button>
        </div>

        <div style={{ display: "flex", flexDirection: "column", gap: 10 }}>
          {products.map((p) => (
            <div
              key={p.id}
              style={{
                backgroundColor: "#FFFFFF",
                borderRadius: 10,
                padding: 12,
                border: "1px solid var(--color-border)",
                display: "flex",
                gap: 12,
                alignItems: "center"
              }}
            >
              <img src={p.images[0]} alt={p.name} style={{ width: 55, height: 55, borderRadius: 6, objectFit: "cover" }} />
              <div style={{ flex: 1 }}>
                <h4 style={{ fontSize: 13, fontWeight: 700 }}>{p.name}</h4>
                <div style={{ fontSize: 11, color: "var(--color-text-muted)" }}>
                  {p.basePrice.toLocaleString()} F / ctn • MOQ: {p.minOrderQuantity}
                </div>
              </div>

              <div style={{ display: "flex", gap: 6 }}>
                <button
                  onClick={() => toggleStock(p.id)}
                  style={{
                    padding: "5px 8px",
                    borderRadius: 6,
                    border: "none",
                    backgroundColor: p.stockStatus === "IN_STOCK" ? "#EAFAF1" : "#FDEDEC",
                    color: p.stockStatus === "IN_STOCK" ? "#27AE60" : "#C0392B",
                    fontSize: 11,
                    fontWeight: 700
                  }}
                >
                  {p.stockStatus === "IN_STOCK" ? "En stock" : "Rupture"}
                </button>
                <button
                  onClick={() => deleteProduct(p.id)}
                  style={{ background: "none", border: "none", color: "#BDC3C7", padding: 4 }}
                >
                  <Trash2 size={16} />
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Modal Ajout Produit */}
      {addModalOpen && (
        <div style={{
          position: "fixed",
          inset: 0,
          backgroundColor: "rgba(0,0,0,0.6)",
          zIndex: 70,
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          padding: 16
        }}>
          <form
            onSubmit={handleAddSubmit}
            style={{
              backgroundColor: "#FFFFFF",
              borderRadius: 16,
              padding: 20,
              width: "100%",
              maxWidth: 500,
              maxHeight: "90vh",
              overflowY: "auto"
            }}
          >
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 14 }}>
              <h3 style={{ fontSize: 16, fontWeight: 800 }}>Publier un Arrivage au Carton</h3>
              <button type="button" onClick={() => setAddModalOpen(false)} style={{ background: "none", border: "none" }}>
                <X size={18} />
              </button>
            </div>

            <div style={{ display: "flex", flexDirection: "column", gap: 10 }}>
              <div>
                <label style={{ fontSize: 11, fontWeight: 700, display: "block", marginBottom: 4 }}>Nom de l'article *</label>
                <input
                  type="text"
                  required
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  placeholder="Ex: Savon Kanza Éclaircissant"
                  style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid var(--color-border)", fontSize: 13 }}
                />
              </div>

              <div>
                <label style={{ fontSize: 11, fontWeight: 700, display: "block", marginBottom: 4 }}>Catégorie *</label>
                <select
                  value={categoryId}
                  onChange={(e) => setCategoryId(e.target.value)}
                  style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid var(--color-border)", fontSize: 13 }}
                >
                  {categories.map((c) => (
                    <option key={c.id} value={c.id}>{c.name}</option>
                  ))}
                </select>
              </div>

              <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 10 }}>
                <div>
                  <label style={{ fontSize: 11, fontWeight: 700, display: "block", marginBottom: 4 }}>Conditionnement *</label>
                  <input
                    type="text"
                    value={packaging}
                    onChange={(e) => setPackaging(e.target.value)}
                    placeholder="Ex: Carton de 24"
                    style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid var(--color-border)", fontSize: 13 }}
                  />
                </div>
                <div>
                  <label style={{ fontSize: 11, fontWeight: 700, display: "block", marginBottom: 4 }}>MOQ (Min. cartons) *</label>
                  <input
                    type="number"
                    min="1"
                    value={minOrderQuantity}
                    onChange={(e) => setMinOrderQuantity(Number(e.target.value))}
                    style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid var(--color-border)", fontSize: 13 }}
                  />
                </div>
              </div>

              <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 10 }}>
                <div>
                  <label style={{ fontSize: 11, fontWeight: 700, display: "block", marginBottom: 4 }}>Prix de base (FCFA) *</label>
                  <input
                    type="number"
                    value={basePrice}
                    onChange={(e) => setBasePrice(Number(e.target.value))}
                    style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid var(--color-border)", fontSize: 13 }}
                  />
                </div>
                <div>
                  <label style={{ fontSize: 11, fontWeight: 700, display: "block", marginBottom: 4 }}>Tarif volume (+5 cartons)</label>
                  <input
                    type="number"
                    value={tierPrice1}
                    onChange={(e) => setTierPrice1(Number(e.target.value))}
                    style={{ width: "100%", padding: 8, borderRadius: 6, border: "1px solid var(--color-border)", fontSize: 13 }}
                  />
                </div>
              </div>

              <button
                type="submit"
                style={{
                  backgroundColor: "var(--color-primary)",
                  color: "#FFF",
                  border: "none",
                  borderRadius: 8,
                  padding: "12px 0",
                  fontSize: 13,
                  fontWeight: 800,
                  marginTop: 10
                }}
              >
                Mettre en vente sur Adjamé Market
              </button>
            </div>
          </form>
        </div>
      )}
    </div>
  );
};
