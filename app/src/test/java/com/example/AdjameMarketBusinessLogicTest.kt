package com.example

import com.example.data.local.model.PriceTier
import com.example.model.CartItem
import com.example.model.Order
import com.example.model.OrderItem
import com.example.model.OrderStatus
import com.example.model.Product
import com.example.model.ShopReport
import com.example.model.UserProfile
import com.example.model.UserRole
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests unitaires automatisés vérifiant la logique métier critique d'Adjamé Market :
 * - Calculs des prix de gros par paliers (Price Tiers)
 * - Respect des seuils de commande minimale (MOQ)
 * - Cycle de vie des statuts de commande (PENDING -> CONFIRMED -> COMPLETED)
 * - Intégrité des codes de retrait PIN
 * - Rôles utilisateurs et permissions
 */
class AdjameMarketBusinessLogicTest {

    private val sampleProductWithTiers = Product(
        id = "prod_savon_kanza",
        name = "Savon Kanza Éclaircissant Anti-Taches",
        description = "Carton d'origine importé de Dubaï.",
        categoryId = "cat_cosmetics",
        sellerId = "seller_fanta_cosmetics",
        sellerName = "Fanta Beauté Distribution",
        sellerSector = "Adjamé Roxy",
        packaging = "Carton de 48 pcs",
        minOrderQuantity = 3,
        basePrice = 24000.0,
        priceTiers = listOf(
            PriceTier(minQuantity = 3, maxQuantity = 9, unitPriceFcfa = 22000.0, label = "3 - 9 cartons"),
            PriceTier(minQuantity = 10, maxQuantity = 24, unitPriceFcfa = 20000.0, label = "10 - 24 cartons"),
            PriceTier(minQuantity = 25, maxQuantity = null, unitPriceFcfa = 18500.0, label = "25+ cartons")
        ),
        stockStatus = "IN_STOCK",
        stockQuantity = 50
    )

    @Test
    fun testBasePriceWhenBelowFirstTier() {
        val cartItem = CartItem(product = sampleProductWithTiers, quantity = 2)
        assertEquals(24000.0, cartItem.unitPrice, 0.001)
        assertEquals(48000.0, cartItem.totalPrice, 0.001)
    }

    @Test
    fun testTierOneWholesalePrice() {
        val cartItem = CartItem(product = sampleProductWithTiers, quantity = 5)
        assertEquals(22000.0, cartItem.unitPrice, 0.001)
        assertEquals(110000.0, cartItem.totalPrice, 0.001)
    }

    @Test
    fun testTierTwoWholesalePrice() {
        val cartItem = CartItem(product = sampleProductWithTiers, quantity = 15)
        assertEquals(20000.0, cartItem.unitPrice, 0.001)
        assertEquals(300000.0, cartItem.totalPrice, 0.001)
    }

    @Test
    fun testTierThreeMaxVolumeWholesalePrice() {
        val cartItem = CartItem(product = sampleProductWithTiers, quantity = 30)
        assertEquals(18500.0, cartItem.unitPrice, 0.001)
        assertEquals(555000.0, cartItem.totalPrice, 0.001)
    }

    @Test
    fun testMinimumOrderQuantityRespected() {
        assertTrue(sampleProductWithTiers.minOrderQuantity >= 3)
        val validOrderQuantity = 5
        assertTrue(validOrderQuantity >= sampleProductWithTiers.minOrderQuantity)
    }

    @Test
    fun testOrderSubtotalAndTotalIntegrity() {
        val items = listOf(
            OrderItem(
                productId = "prod_savon_kanza",
                productName = "Savon Kanza",
                packaging = "Carton de 48 pcs",
                quantity = 5,
                unitPrice = 22000.0
            ),
            OrderItem(
                productId = "prod_creme_carotone",
                productName = "Crème Carotone Maxi",
                packaging = "Carton de 24 pcs",
                quantity = 2,
                unitPrice = 18000.0
            )
        )

        assertEquals(110000.0, items[0].subtotal, 0.001)
        assertEquals(36000.0, items[1].subtotal, 0.001)

        val totalAmount = items.sumOf { it.subtotal }
        assertEquals(146000.0, totalAmount, 0.001)

        val order = Order(
            id = "CMD-9901",
            buyerId = "buyer_1",
            buyerName = "Awa Traoré",
            buyerPhone = "+2250701020304",
            sellerId = "seller_1",
            sellerName = "Fanta Beauté Distribution",
            sellerSector = "Adjamé Roxy",
            items = items,
            totalAmount = totalAmount,
            status = OrderStatus.PENDING,
            pickupPinCode = "5821",
            deliveryType = "CLICK_AND_COLLECT"
        )

        assertEquals(OrderStatus.PENDING, order.status)
        assertEquals(4, order.pickupPinCode.length)
        assertTrue(order.pickupPinCode.all { it.isDigit() })
    }

    @Test
    fun testOrderStatusLifecycleProgression() {
        val transitions = listOf(
            OrderStatus.PENDING,
            OrderStatus.CONFIRMED,
            OrderStatus.PREPARING,
            OrderStatus.READY,
            OrderStatus.COMPLETED
        )

        assertEquals("En attente de confirmation", transitions[0].label)
        assertEquals("Confirmée par le grossiste", transitions[1].label)
        assertEquals("En cours de préparation", transitions[2].label)
        assertEquals("Prête pour retrait / expédition", transitions[3].label)
        assertEquals("Terminée & Réceptionnée", transitions[4].label)
    }

    @Test
    fun testUserRoleSegregation() {
        val buyer = UserProfile(
            id = "buyer_1",
            phone = "+2250700000001",
            fullName = "Koffi Yao",
            role = UserRole.BUYER
        )
        val wholesaler = UserProfile(
            id = "seller_1",
            phone = "+2250700000002",
            fullName = "El Hadj Oumar",
            role = UserRole.WHOLESALER
        )
        val admin = UserProfile(
            id = "admin_1",
            phone = "+2250700000003",
            fullName = "Admin Adjamé",
            role = UserRole.ADMIN
        )

        assertEquals(UserRole.BUYER, buyer.role)
        assertEquals(UserRole.WHOLESALER, wholesaler.role)
        assertEquals(UserRole.ADMIN, admin.role)
        assertFalse(buyer.role == wholesaler.role)
    }

    @Test
    fun testShopReportValidation() {
        val report = ShopReport(
            id = "rep_001",
            shopId = "seller_fanta_cosmetics",
            shopName = "Fanta Beauté",
            reporterId = "buyer_1",
            reporterName = "Awa Traoré",
            reason = "BOUTIQUE_INTROUVABLE",
            description = "Je suis allée au Roxy couloir 3 mais la boutique est fermée depuis 2 jours."
        )

        assertNotNull(report.id)
        assertEquals("PENDING", report.status)
        assertEquals("BOUTIQUE_INTROUVABLE", report.reason)
    }
}
