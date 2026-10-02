import React, { createContext, useContext, useState, useEffect } from "react";
import {
  UserProfile,
  UserRole,
  Category,
  Product,
  WholesalerShop,
  CartItem,
  Order,
  OrderStatus,
  AppNotification,
  ShopReport,
  PriceTier,
} from "../types";
import { SEED_CATEGORIES, SEED_PRODUCTS, SEED_SHOPS } from "../data/seedData";

interface MarketContextType {
  currentUser: UserProfile;
  switchRole: (role: UserRole) => void;
  updateProfile: (name: string, business: string, commune: string) => void;

  categories: Category[];
  products: Product[];
  shops: WholesalerShop[];

  currentScreen: string;
  navigate: (screen: string) => void;
  selectedProductId: string | null;
  openProductDetail: (id: string) => void;
  selectedShopId: string | null;
  openShopDetail: (id: string) => void;
  selectedOrderId: string | null;
  openOrderDetail: (id: string) => void;

  searchQuery: string;
  setSearchQuery: (q: string) => void;
  selectedCategory: string | null;
  setSelectedCategory: (cat: string | null) => void;
  selectedSector: string | null;
  setSelectedSector: (s: string | null) => void;
  verifiedOnly: boolean;
  setVerifiedOnly: (v: boolean) => void;

  cartItems: CartItem[];
  addToCart: (product: Product, quantity?: number) => void;
  updateCartQty: (productId: string, qty: number) => void;
  removeFromCart: (productId: string) => void;
  clearCart: () => void;
  cartTotal: number;

  orders: Order[];
  placeOrder: (deliveryType: "CLICK_AND_COLLECT" | "GARE_EXPEDITION", notes: string) => Order | null;
  updateOrderStatus: (orderId: string, status: OrderStatus) => void;

  favoriteProductIds: string[];
  favoriteShopIds: string[];
  toggleFavoriteProduct: (id: string) => void;
  toggleFavoriteShop: (id: string) => void;

  sellerNotes: Record<string, string>;
  saveSellerNote: (shopId: string, note: string) => void;

  reports: ShopReport[];
  submitReport: (shopId: string, shopName: string, reason: string, description: string) => void;

  notifications: AppNotification[];
  markNotificationAsRead: (id: string) => void;

  addProduct: (product: Omit<Product, "id">) => void;
  deleteProduct: (id: string) => void;
  toggleStock: (id: string) => void;
  toggleShopVerification: (shopId: string) => void;
  bannerNotice: string | null;
  showNotice: (msg: string) => void;
}

const MarketContext = createContext<MarketContextType | undefined>(undefined);

export const MarketProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [currentUser, setCurrentUser] = useState<UserProfile>(() => {
    const saved = localStorage.getItem("adjame_user");
    return saved ? JSON.parse(saved) : {
      id: "user_awa_traore",
      phone: "+2250701020304",
      fullName: "Awa Traoré",
      role: "BUYER",
      businessName: "Awa Chic Boutique",
      commune: "Yopougon Siporex",
      isVerified: true,
      dataSaverEnabled: false,
    };
  });

  const [categories] = useState<Category[]>(SEED_CATEGORIES);
  const [products, setProducts] = useState<Product[]>(() => {
    const saved = localStorage.getItem("adjame_products");
    return saved ? JSON.parse(saved) : SEED_PRODUCTS;
  });

  const [shops, setShops] = useState<WholesalerShop[]>(() => {
    const saved = localStorage.getItem("adjame_shops");
    return saved ? JSON.parse(saved) : SEED_SHOPS;
  });

  const [currentScreen, setCurrentScreen] = useState<string>("home");
  const [selectedProductId, setSelectedProductId] = useState<string | null>(null);
  const [selectedShopId, setSelectedShopId] = useState<string | null>(null);
  const [selectedOrderId, setSelectedOrderId] = useState<string | null>(null);

  const [searchQuery, setSearchQuery] = useState("");
  const [selectedCategory, setSelectedCategory] = useState<string | null>(null);
  const [selectedSector, setSelectedSector] = useState<string | null>(null);
  const [verifiedOnly, setVerifiedOnly] = useState(false);

  const [cartItems, setCartItems] = useState<CartItem[]>(() => {
    const saved = localStorage.getItem("adjame_cart");
    return saved ? JSON.parse(saved) : [];
  });

  const [orders, setOrders] = useState<Order[]>(() => {
    const saved = localStorage.getItem("adjame_orders");
    return saved ? JSON.parse(saved) : [
      {
        id: "CMD-9481",
        buyerId: "user_awa_traore",
        buyerName: "Awa Traoré",
        buyerPhone: "+2250701020304",
        sellerId: "seller_fanta_cosmetics",
        sellerName: "Fanta Beauté Distribution",
        sellerSector: "Adjamé Roxy",
        items: [
          {
            productId: "prod_savon_kanza",
            productName: "Savon Kanza Éclaircissant Anti-Taches",
            packaging: "Carton de 48 pièces",
            quantity: 5,
            unitPrice: 22000,
            subtotal: 110000,
          },
        ],
        totalAmount: 110000,
        status: "CONFIRMED",
        pickupPinCode: "4829",
        deliveryType: "CLICK_AND_COLLECT",
        notes: "Retrait prévu demain 10h par mon coursier.",
        createdAt: Date.now() - 3600000 * 4,
      },
    ];
  });

  const [favoriteProductIds, setFavoriteProductIds] = useState<string[]>(["prod_savon_kanza"]);
  const [favoriteShopIds, setFavoriteShopIds] = useState<string[]>(["seller_elhadj_oumar"]);
  const [sellerNotes, setSellerNotes] = useState<Record<string, string>>({
    seller_elhadj_oumar: "Demander le commis Salif pour 5% de remise au carton.",
  });

  const [reports, setReports] = useState<ShopReport[]>([]);
  const [notifications, setNotifications] = useState<AppNotification[]>([
    {
      id: "notif_1",
      title: "Arrivage Conteneur Forum",
      message: "El Hadj Oumar vient de recevoir 300 ballots de pagnes Woodin.",
      type: "ARRIVAGE",
      timestamp: Date.now() - 3600000,
      isRead: false,
    },
  ]);

  const [bannerNotice, setBannerNotice] = useState<string | null>(null);

  const showNotice = (msg: string) => {
    setBannerNotice(msg);
    setTimeout(() => setBannerNotice(null), 3500);
  };

  useEffect(() => {
    localStorage.setItem("adjame_user", JSON.stringify(currentUser));
  }, [currentUser]);

  useEffect(() => {
    localStorage.setItem("adjame_products", JSON.stringify(products));
  }, [products]);

  useEffect(() => {
    localStorage.setItem("adjame_shops", JSON.stringify(shops));
  }, [shops]);

  useEffect(() => {
    localStorage.setItem("adjame_cart", JSON.stringify(cartItems));
  }, [cartItems]);

  useEffect(() => {
    localStorage.setItem("adjame_orders", JSON.stringify(orders));
  }, [orders]);

  const switchRole = (role: UserRole) => {
    setCurrentUser((prev) => ({
      ...prev,
      role,
      fullName:
        role === "BUYER"
          ? "Awa Traoré (Revendeuse)"
          : role === "WHOLESALER"
          ? "El Hadj Oumar Traoré (Grossiste)"
          : "Jean-Luc Kouadio (Admin Ops)",
      businessName:
        role === "BUYER"
          ? "Awa Chic Boutique"
          : role === "WHOLESALER"
          ? "Éts El Hadj Oumar & Frères"
          : "Adjamé Market Direction",
    }));

    if (role === "BUYER") setCurrentScreen("home");
    else if (role === "WHOLESALER") setCurrentScreen("wholesaler_dashboard");
    else if (role === "ADMIN") setCurrentScreen("admin_dashboard");

    showNotice(`Mode basculé vers : ${role}`);
  };

  const updateProfile = (name: string, business: string, commune: string) => {
    setCurrentUser((prev) => ({
      ...prev,
      fullName: name,
      businessName: business,
      commune,
    }));
    showNotice("Profil mis à jour avec succès !");
  };

  const openProductDetail = (id: string) => {
    setSelectedProductId(id);
    setCurrentScreen("product_detail");
  };

  const openShopDetail = (id: string) => {
    setSelectedShopId(id);
    setCurrentScreen("shop_detail");
  };

  const openOrderDetail = (id: string) => {
    setSelectedOrderId(id);
    setCurrentScreen("order_detail");
  };

  const calculateUnitPrice = (product: Product, quantity: number): number => {
    const matchingTier = [...product.priceTiers]
      .reverse()
      .find((t) => quantity >= t.minQuantity && (t.maxQuantity === null || quantity <= t.maxQuantity));
    return matchingTier ? matchingTier.unitPriceFcfa : product.basePrice;
  };

  const addToCart = (product: Product, quantity: number = product.minOrderQuantity) => {
    setCartItems((prev) => {
      const existing = prev.find((item) => item.product.id === product.id);
      if (existing) {
        const newQty = existing.quantity + quantity;
        const newPrice = calculateUnitPrice(product, newQty);
        return prev.map((item) =>
          item.product.id === product.id
            ? { ...item, quantity: newQty, unitPrice: newPrice, totalPrice: newQty * newPrice }
            : item
        );
      } else {
        const validQty = Math.max(quantity, product.minOrderQuantity);
        const price = calculateUnitPrice(product, validQty);
        return [...prev, { product, quantity: validQty, unitPrice: price, totalPrice: validQty * price }];
      }
    });
    showNotice(`${product.name} ajouté au panier (${quantity} carton(s))`);
  };

  const updateCartQty = (productId: string, qty: number) => {
    if (qty <= 0) {
      removeFromCart(productId);
      return;
    }
    setCartItems((prev) =>
      prev.map((item) => {
        if (item.product.id === productId) {
          const unitPrice = calculateUnitPrice(item.product, qty);
          return {
            ...item,
            quantity: qty,
            unitPrice,
            totalPrice: qty * unitPrice,
          };
        }
        return item;
      })
    );
  };

  const removeFromCart = (productId: string) => {
    setCartItems((prev) => prev.filter((item) => item.product.id !== productId));
  };

  const clearCart = () => setCartItems([]);

  const cartTotal = cartItems.reduce((acc, item) => acc + item.totalPrice, 0);

  const placeOrder = (
    deliveryType: "CLICK_AND_COLLECT" | "GARE_EXPEDITION",
    notes: string
  ): Order | null => {
    if (cartItems.length === 0) return null;

    const firstProduct = cartItems[0].product;
    const sellerShop = shops.find((s) => s.id === firstProduct.sellerId);

    const pin = Math.floor(1000 + Math.random() * 9000).toString();
    const newOrder: Order = {
      id: `CMD-${Math.floor(1000 + Math.random() * 9000)}`,
      buyerId: currentUser.id,
      buyerName: currentUser.fullName,
      buyerPhone: currentUser.phone,
      sellerId: firstProduct.sellerId,
      sellerName: sellerShop?.name || firstProduct.sellerName,
      sellerSector: sellerShop?.marketSector || firstProduct.sellerSector,
      items: cartItems.map((c) => ({
        productId: c.product.id,
        productName: c.product.name,
        packaging: c.product.packaging,
        quantity: c.quantity,
        unitPrice: c.unitPrice,
        subtotal: c.totalPrice,
      })),
      totalAmount: cartTotal,
      status: "PENDING",
      pickupPinCode: pin,
      deliveryType,
      notes,
      createdAt: Date.now(),
    };

    setOrders((prev) => [newOrder, ...prev]);
    clearCart();

    setNotifications((prev) => [
      {
        id: `notif_${Date.now()}`,
        title: "Commande Confirmée",
        message: `Votre commande ${newOrder.id} de ${newOrder.totalAmount.toLocaleString()} FCFA est transmise au grossiste. Code PIN : ${pin}`,
        type: "ORDER",
        timestamp: Date.now(),
        isRead: false,
      },
      ...prev,
    ]);

    setSelectedOrderId(newOrder.id);
    setCurrentScreen("order_confirmation");
    showNotice(`Commande ${newOrder.id} passée avec succès !`);
    return newOrder;
  };

  const updateOrderStatus = (orderId: string, status: OrderStatus) => {
    setOrders((prev) =>
      prev.map((o) => (o.id === orderId ? { ...o, status } : o))
    );
    showNotice(`Statut de la commande ${orderId} mis à jour : ${status}`);
  };

  const toggleFavoriteProduct = (id: string) => {
    setFavoriteProductIds((prev) =>
      prev.includes(id) ? prev.filter((i) => i !== id) : [...prev, id]
    );
  };

  const toggleFavoriteShop = (id: string) => {
    setFavoriteShopIds((prev) =>
      prev.includes(id) ? prev.filter((i) => i !== id) : [...prev, id]
    );
  };

  const saveSellerNote = (shopId: string, note: string) => {
    setSellerNotes((prev) => ({ ...prev, [shopId]: note }));
    showNotice("Note privée enregistrée pour ce grossiste !");
  };

  const submitReport = (shopId: string, shopName: string, reason: string, description: string) => {
    const report: ShopReport = {
      id: `rep_${Math.floor(1000 + Math.random() * 9000)}`,
      shopId,
      shopName,
      reporterId: currentUser.id,
      reporterName: currentUser.fullName,
      reason,
      description,
      status: "PENDING",
      createdAt: Date.now(),
    };
    setReports((prev) => [report, ...prev]);
    showNotice("Signalement transmis à notre équipe de sécurité terrain.");
  };

  const markNotificationAsRead = (id: string) => {
    setNotifications((prev) =>
      prev.map((n) => (n.id === id ? { ...n, isRead: true } : n))
    );
  };

  const addProduct = (p: Omit<Product, "id">) => {
    const newProd: Product = {
      ...p,
      id: `prod_${Date.now()}`,
    };
    setProducts((prev) => [newProd, ...prev]);
    showNotice(`Article "${newProd.name}" publié au catalogue !`);
  };

  const deleteProduct = (id: string) => {
    setProducts((prev) => prev.filter((p) => p.id !== id));
    showNotice("Article retiré du catalogue.");
  };

  const toggleStock = (id: string) => {
    setProducts((prev) =>
      prev.map((p) => {
        if (p.id === id) {
          const next = p.stockStatus === "IN_STOCK" ? "OUT_OF_STOCK" : "IN_STOCK";
          return { ...p, stockStatus: next };
        }
        return p;
      })
    );
  };

  const toggleShopVerification = (shopId: string) => {
    setShops((prev) =>
      prev.map((s) => {
        if (s.id === shopId) {
          const isV = !s.isVerified;
          return {
            ...s,
            isVerified: isV,
            verificationBadge: isV ? "TERRAIN_VERIFIED" : "STANDARD",
          };
        }
        return s;
      })
    );
    showNotice("Statut de certification boutique mis à jour !");
  };

  return (
    <MarketContext.Provider
      value={{
        currentUser,
        switchRole,
        updateProfile,
        categories,
        products,
        shops,
        currentScreen,
        navigate: setCurrentScreen,
        selectedProductId,
        openProductDetail,
        selectedShopId,
        openShopDetail,
        selectedOrderId,
        openOrderDetail,
        searchQuery,
        setSearchQuery,
        selectedCategory,
        setSelectedCategory,
        selectedSector,
        setSelectedSector,
        verifiedOnly,
        setVerifiedOnly,
        cartItems,
        addToCart,
        updateCartQty,
        removeFromCart,
        clearCart,
        cartTotal,
        orders,
        placeOrder,
        updateOrderStatus,
        favoriteProductIds,
        favoriteShopIds,
        toggleFavoriteProduct,
        toggleFavoriteShop,
        sellerNotes,
        saveSellerNote,
        reports,
        submitReport,
        notifications,
        markNotificationAsRead,
        addProduct,
        deleteProduct,
        toggleStock,
        toggleShopVerification,
        bannerNotice,
        showNotice,
      }}
    >
      {children}
    </MarketContext.Provider>
  );
};

export const useMarket = () => {
  const context = useContext(MarketContext);
  if (!context) throw new Error("useMarket must be used within MarketProvider");
  return context;
};
