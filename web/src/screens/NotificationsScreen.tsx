import React from "react";
import { useMarket } from "../context/MarketContext";
import { Bell, CheckCircle2, PackageCheck } from "lucide-react";

export const NotificationsScreen: React.FC = () => {
  const { notifications, markNotificationAsRead } = useMarket();

  return (
    <div style={{ maxWidth: 700, margin: "0 auto", padding: "16px 16px 90px 16px" }}>
      <h2 style={{ fontSize: 18, fontWeight: 800, marginBottom: 14 }}>Notifications Commerciales</h2>

      {notifications.length === 0 ? (
        <div style={{ textAlign: "center", padding: "40px 16px", color: "var(--color-text-muted)" }}>
          <Bell size={32} style={{ margin: "0 auto 10px auto", display: "block" }} />
          <p style={{ fontWeight: 700 }}>Aucune notification pour le moment.</p>
        </div>
      ) : (
        <div style={{ display: "flex", flexDirection: "column", gap: 10 }}>
          {notifications.map((n) => (
            <div
              key={n.id}
              onClick={() => markNotificationAsRead(n.id)}
              style={{
                backgroundColor: n.isRead ? "#FFFFFF" : "#E8F8F5",
                borderRadius: 10,
                padding: 14,
                border: "1px solid var(--color-border)",
                cursor: "pointer",
                display: "flex",
                gap: 12,
                alignItems: "flex-start"
              }}
            >
              <div style={{
                width: 36,
                height: 36,
                borderRadius: "50%",
                backgroundColor: n.isRead ? "#F4F6F6" : "var(--color-primary)",
                color: n.isRead ? "#7F8C8D" : "#FFF",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                flexShrink: 0
              }}>
                <Bell size={16} />
              </div>

              <div style={{ flex: 1 }}>
                <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                  <h4 style={{ fontSize: 13, fontWeight: 800, color: "var(--color-text)" }}>{n.title}</h4>
                  <span style={{ fontSize: 10, color: "var(--color-text-muted)" }}>
                    {new Date(n.timestamp).toLocaleTimeString("fr-FR", { hour: "2-digit", minute: "2-digit" })}
                  </span>
                </div>
                <p style={{ fontSize: 12, color: "#566573", marginTop: 2 }}>{n.message}</p>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
