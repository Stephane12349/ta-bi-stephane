package com.example.model

enum class OrderStatus(val label: String) {
    PENDING("En attente de confirmation"),
    CONFIRMED("Confirmée par le grossiste"),
    PREPARING("En cours de préparation"),
    READY("Prête pour retrait / expédition"),
    COMPLETED("Terminée & Réceptionnée"),
    CANCELLED("Annulée par l'acheteur"),
    REJECTED("Refusée par le grossiste")
}

data class OrderItem(
    val productId: String,
    val productName: String,
    val packaging: String,
    val quantity: Int, // En cartons ou unités
    val unitPrice: Double, // Prix par carton appliqué
    val subtotal: Double = quantity * unitPrice
)

data class Order(
    val id: String,
    val buyerId: String,
    val buyerName: String,
    val buyerPhone: String,
    val sellerId: String,
    val sellerName: String,
    val sellerSector: String,
    val items: List<OrderItem>,
    val totalAmount: Double,
    val status: OrderStatus = OrderStatus.PENDING,
    val pickupPinCode: String, // Code à 4 chiffres (ex: "7482")
    val deliveryType: String = "CLICK_AND_COLLECT", // CLICK_AND_COLLECT (Retrait magasin) ou GARE_EXPEDITION (Expédition gare)
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class CartItem(
    val product: Product,
    val quantity: Int = 1,
    val selectedVariant: String = ""
) {
    val unitPrice: Double
        get() {
            // Calculer le prix selon le palier atteint
            val matchingTier = product.priceTiers.lastOrNull { tier ->
                quantity >= tier.minQuantity && (tier.maxQuantity == null || quantity <= tier.maxQuantity)
            }
            return matchingTier?.unitPriceFcfa ?: product.basePrice
        }

    val totalPrice: Double
        get() = unitPrice * quantity
}

data class PriceQuoteRequest(
    val id: String,
    val buyerId: String,
    val buyerName: String,
    val buyerPhone: String,
    val sellerId: String,
    val sellerName: String,
    val productId: String,
    val productName: String,
    val requestedQuantity: Int,
    val proposedPricePerUnit: Double,
    val status: String = "PENDING", // PENDING, ACCEPTED, COUNTER_PROPOSED, REJECTED
    val counterPrice: Double? = null,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

data class ChatMessage(
    val id: String,
    val conversationId: String,
    val senderId: String,
    val senderName: String,
    val senderRole: UserRole,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val type: String, // "ORDER", "QUOTE", "ARRIVAGE", "ALERT"
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val targetRoute: String = ""
)

data class ShopReport(
    val id: String,
    val shopId: String,
    val shopName: String,
    val reporterId: String,
    val reporterName: String,
    val reason: String, // "BOUTIQUE_INTROUVABLE", "PRODUIT_NON_CONFORME", "ARNAQUE_ACOMPTE", "PRIX_TROMPEUR"
    val description: String,
    val status: String = "PENDING", // PENDING, RESOLVED, DISMISSED
    val createdAt: Long = System.currentTimeMillis()
)
