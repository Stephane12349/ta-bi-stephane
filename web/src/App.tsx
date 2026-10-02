import React from "react";
import { MarketProvider, useMarket } from "./context/MarketContext";
import { Header } from "./components/Header";
import { BottomNav } from "./components/BottomNav";
import { BuyerHome } from "./screens/BuyerHome";
import { BuyerSearch } from "./screens/BuyerSearch";
import { CartScreen } from "./screens/CartScreen";
import { OrdersScreen } from "./screens/OrdersScreen";
import { FavoritesScreen } from "./screens/FavoritesScreen";
import { NotificationsScreen } from "./screens/NotificationsScreen";
import { UserProfileScreen } from "./screens/UserProfileScreen";
import { WholesalerDashboard } from "./screens/WholesalerDashboard";
import { AdminDashboard } from "./screens/AdminDashboard";
import { ProductDetailModal } from "./screens/ProductDetailModal";
import { ShopDetailModal } from "./screens/ShopDetailModal";
import { OrderDetailModal } from "./screens/OrderDetailModal";

const MainContent: React.FC = () => {
  const { currentScreen, bannerNotice } = useMarket();

  return (
    <div style={{ minHeight: "100vh", display: "flex", flexDirection: "column", position: "relative" }}>
      <Header />

      {/* Bannière de notification Toast */}
      {bannerNotice && (
        <div style={{
          position: "fixed",
          top: 60,
          left: "50%",
          transform: "translateX(-50%)",
          zIndex: 80,
          backgroundColor: "#1C2833",
          color: "#FFFFFF",
          fontSize: 12,
          fontWeight: 700,
          padding: "8px 16px",
          borderRadius: 20,
          boxShadow: "0 4px 12px rgba(0,0,0,0.25)",
          textAlign: "center",
          maxWidth: "90%"
        }}>
          {bannerNotice}
        </div>
      )}

      {/* Contenu principal */}
      <main style={{ flex: 1 }}>
        {currentScreen === "home" && <BuyerHome />}
        {currentScreen === "search" && <BuyerSearch />}
        {currentScreen === "cart" && <CartScreen />}
        {currentScreen === "orders" && <OrdersScreen />}
        {currentScreen === "favorites" && <FavoritesScreen />}
        {currentScreen === "notifications" && <NotificationsScreen />}
        {currentScreen === "profile" && <UserProfileScreen />}

        {/* Espace Grossiste */}
        {(currentScreen === "wholesaler_dashboard" || currentScreen === "wholesaler_catalog" || currentScreen === "wholesaler_orders") && (
          <WholesalerDashboard />
        )}

        {/* Espace Administrateur */}
        {(currentScreen === "admin_dashboard" || currentScreen === "admin_kyc") && (
          <AdminDashboard />
        )}

        {/* Modales contextuelles */}
        {currentScreen === "product_detail" && <ProductDetailModal />}
        {currentScreen === "shop_detail" && <ShopDetailModal />}
        {(currentScreen === "order_detail" || currentScreen === "order_confirmation") && <OrderDetailModal />}
      </main>

      <BottomNav />
    </div>
  );
};

export const App: React.FC = () => {
  return (
    <MarketProvider>
      <MainContent />
    </MarketProvider>
  );
};

export default App;
