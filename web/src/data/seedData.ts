import { Category, Product, WholesalerShop } from "../types";

export const SEED_CATEGORIES: Category[] = [
  {
    id: "cat_cosmetics",
    name: "Cosmétiques & Beauté",
    slug: "cosmetiques",
    iconKey: "sparkles",
    description: "Soins corporels, savons de teint, mèches brésiliennes et parfumerie au carton.",
    productCount: 420
  },
  {
    id: "cat_textile",
    name: "Plein Textile & Pagnes",
    slug: "textile-pagnes",
    iconKey: "shirt",
    description: "Pagnes Wax hollandais, Woodin, Uniwax, tissus basin riche et prêt-à-porter en ballot.",
    productCount: 650
  },
  {
    id: "cat_electronics",
    name: "Téléphonie & Électronique",
    slug: "electronique-telecom",
    iconKey: "smartphone",
    description: "Smartphones, accessoires de charge, enceintes Bluetooth, panneaux solaires portatifs.",
    productCount: 380
  },
  {
    id: "cat_shoes",
    name: "Chaussures & Maroquinerie",
    slug: "chaussures-sacs",
    iconKey: "footprints",
    description: "Sandales en gros, baskets tendance, sacs à main dame et valises de voyage.",
    productCount: 290
  },
  {
    id: "cat_agro",
    name: "Vivrier & Agroalimentaire",
    slug: "vivrier-agro",
    iconKey: "utensils",
    description: "Riz parfumé 50kg, bidons d'huile 25L, pâtes alimentaires, lait en poudre au carton.",
    productCount: 510
  },
  {
    id: "cat_hardware",
    name: "Quincaillerie & Outillage",
    slug: "quincaillerie-bricolage",
    iconKey: "wrench",
    description: "Matériaux de construction, cadenas de sécurité, petit outillage professionnel.",
    productCount: 210
  }
];

export const SEED_SHOPS: WholesalerShop[] = [
  {
    id: "seller_elhadj_oumar",
    name: "Éts El Hadj Oumar & Frères",
    description: "Grand importateur direct de textile et pagnes au Forum des Marchés d'Adjamé depuis 1998.",
    ownerName: "El Hadj Oumar Traoré",
    phone: "+2250708091011",
    whatsapp: "+2250708091011",
    address: "Forum des Marchés, Hall B, Box 14-16",
    marketSector: "Forum d'Adjamé",
    landmarks: "Face pharmacie Mirador, couloir B, 2ème escalier",
    hours: "07h30 - 18h00 (Fermé dimanche)",
    isVerified: true,
    verificationBadge: "CERTIFIED_IMPORTATEUR",
    rating: 4.9,
    reviewCount: 142,
    transactionCount: 480,
    coverImageUrl: "https://images.unsplash.com/photo-1578575437130-527eed3abbec?w=600"
  },
  {
    id: "seller_fanta_cosmetics",
    name: "Fanta Beauté Distribution",
    description: "Spécialiste du gros cosmétique et capillaire au carrefour Roxy. Arrivages Dubaï et Nigeria.",
    ownerName: "Mme Fanta Koné",
    phone: "+2250505121416",
    whatsapp: "+2250505121416",
    address: "Adjamé Roxy, Rue du Cinéma",
    marketSector: "Adjamé Roxy",
    landmarks: "À côté du glacier Roxy, devant le hangar à mèches",
    hours: "08h00 - 18h30 (Tous les jours)",
    isVerified: true,
    verificationBadge: "TERRAIN_VERIFIED",
    rating: 4.8,
    reviewCount: 98,
    transactionCount: 310,
    coverImageUrl: "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?w=600"
  },
  {
    id: "seller_dallas_tech",
    name: "Dallas High-Tech Import",
    description: "Fournisseur en gros d'accessoires de téléphonie, câbles, écouteurs sans fil et powerbanks.",
    ownerName: "M. Ibrahim Doumbia",
    phone: "+2250102030405",
    whatsapp: "+2250102030405",
    address: "Black Market Adjamé, Allée des chargeurs",
    marketSector: "Black Market",
    landmarks: "Face Grande Mosquée d'Adjamé, 3ème magasin après le kiosque Orange",
    hours: "08h00 - 19h00 (Tous les jours)",
    isVerified: true,
    verificationBadge: "TERRAIN_VERIFIED",
    rating: 4.7,
    reviewCount: 76,
    transactionCount: 220,
    coverImageUrl: "https://images.unsplash.com/photo-1519389950473-47ba0277781c?w=600"
  }
];

export const SEED_PRODUCTS: Product[] = [
  {
    id: "prod_savon_kanza",
    name: "Savon Kanza Éclaircissant Anti-Taches",
    description: "Savon de beauté clarifiant très recherché par les revendeuses. Vendu exclusivement au carton d'origine.",
    categoryId: "cat_cosmetics",
    sellerId: "seller_fanta_cosmetics",
    sellerName: "Fanta Beauté Distribution",
    sellerSector: "Adjamé Roxy",
    images: ["https://images.unsplash.com/photo-1608248597359-00958189c445?w=500"],
    packaging: "Carton de 48 pièces",
    minOrderQuantity: 3,
    basePrice: 24000,
    priceTiers: [
      { minQuantity: 3, maxQuantity: 9, unitPriceFcfa: 22000, label: "3 à 9 cartons" },
      { minQuantity: 10, maxQuantity: 24, unitPriceFcfa: 20000, label: "10 à 24 cartons" },
      { minQuantity: 25, maxQuantity: null, unitPriceFcfa: 18500, label: "25+ cartons (Grossiste)" }
    ],
    stockStatus: "IN_STOCK",
    stockQuantity: 65,
    isVerifiedSeller: true,
    isPromoted: true
  },
  {
    id: "prod_pagne_woodin",
    name: "Pagne Imprimé Woodin Prestige 6 Yards",
    description: "Ballot original scellé de pagnes tissés 100% coton. Motifs tendance 2026 très prisés pour cérémonies.",
    categoryId: "cat_textile",
    sellerId: "seller_elhadj_oumar",
    sellerName: "Éts El Hadj Oumar & Frères",
    sellerSector: "Forum d'Adjamé",
    images: ["https://images.unsplash.com/photo-1607083206869-4c7672e72a8a?w=500"],
    packaging: "Ballot de 10 pièces",
    minOrderQuantity: 2,
    basePrice: 105000,
    priceTiers: [
      { minQuantity: 2, maxQuantity: 5, unitPriceFcfa: 98000, label: "2 à 5 ballots" },
      { minQuantity: 6, maxQuantity: null, unitPriceFcfa: 92000, label: "6+ ballots (Prix Conteneur)" }
    ],
    stockStatus: "IN_STOCK",
    stockQuantity: 40,
    isVerifiedSeller: true,
    isPromoted: true
  },
  {
    id: "prod_chargeur_rapide",
    name: "Chargeur Rapide 25W Fast-Charge Type-C",
    description: "Boîte complète de chargeurs haute compatibilité pour smartphones Samsung, Infinix, Tecno et Xiaomi.",
    categoryId: "cat_electronics",
    sellerId: "seller_dallas_tech",
    sellerName: "Dallas High-Tech Import",
    sellerSector: "Black Market",
    images: ["https://images.unsplash.com/photo-1583863788434-e58a36330cf0?w=500"],
    packaging: "Paquet de 50 unités",
    minOrderQuantity: 2,
    basePrice: 35000,
    priceTiers: [
      { minQuantity: 2, maxQuantity: 4, unitPriceFcfa: 32000, label: "2 à 4 paquets" },
      { minQuantity: 5, maxQuantity: null, unitPriceFcfa: 28500, label: "5+ paquets (Tarif Dépôt)" }
    ],
    stockStatus: "IN_STOCK",
    stockQuantity: 120,
    isVerifiedSeller: true,
    isPromoted: false
  },
  {
    id: "prod_creme_carotone",
    name: "Crème Clarifiante Carotone Maxi 330ml",
    description: "Format économique très demandé en demi-gros par les boutiques de quartier de Yopougon et Abobo.",
    categoryId: "cat_cosmetics",
    sellerId: "seller_fanta_cosmetics",
    sellerName: "Fanta Beauté Distribution",
    sellerSector: "Adjamé Roxy",
    images: ["https://images.unsplash.com/photo-1556228720-195a672e8a03?w=500"],
    packaging: "Carton de 24 flacons",
    minOrderQuantity: 2,
    basePrice: 19500,
    priceTiers: [
      { minQuantity: 2, maxQuantity: 5, unitPriceFcfa: 18000, label: "2 à 5 cartons" },
      { minQuantity: 6, maxQuantity: null, unitPriceFcfa: 16500, label: "6+ cartons" }
    ],
    stockStatus: "IN_STOCK",
    stockQuantity: 30,
    isVerifiedSeller: true,
    isPromoted: false
  }
];
