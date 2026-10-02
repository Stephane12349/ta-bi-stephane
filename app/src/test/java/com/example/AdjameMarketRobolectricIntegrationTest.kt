package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AdjameMarketDatabase
import com.example.data.local.model.PriceTier
import com.example.data.repository.MarketplaceRepository
import com.example.model.OrderStatus
import com.example.model.Product
import com.example.model.UserRole
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Tests d'intégration automatisés avec Robolectric (JVM locale)
 * Vérifie le comportement complet du Repository, du Cache Room,
 * du panier, du passage de commande et des bascules de rôles.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class AdjameMarketRobolectricIntegrationTest {

    private lateinit var context: Context
    private lateinit var repository: MarketplaceRepository

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        repository = MarketplaceRepository.getInstance(context)
    }

    @Test
    fun testRepositoryInitializationWithSeedData() = runBlocking {
        val categories = repository.categories.value
        val products = repository.products.value
        val shops = repository.shops.value

        assertTrue("Les catégories initiales doivent être chargées", categories.isNotEmpty())
        assertTrue("Les produits initiaux doivent être chargés", products.isNotEmpty())
        assertTrue("Les boutiques d'Adjamé doivent être chargées", shops.isNotEmpty())

        val forumSellers = shops.filter { it.marketSector.contains("Forum", ignoreCase = true) }
        assertTrue("Des grossistes du Forum d'Adjamé doivent être présents", forumSellers.isNotEmpty())
    }

    @Test
    fun testCartOperationsAndOrderPlacement() = runBlocking {
        repository.clearCart()
        assertEquals(0, repository.cartItems.value.size)

        val product = repository.products.value.first()
        repository.addToCart(product, quantity = 5)

        val cartItems = repository.cartItems.value
        assertEquals(1, cartItems.size)
        assertEquals(5, cartItems.first().quantity)

        val placedOrder = repository.placeOrder(
            deliveryType = "CLICK_AND_COLLECT",
            notes = "Test retrait magasin Adjamé"
        )

        assertNotNull("La commande doit être générée avec succès", placedOrder)
        assertEquals(OrderStatus.PENDING, placedOrder?.status)
        assertEquals(4, placedOrder?.pickupPinCode?.length)
        assertEquals(0, repository.cartItems.value.size) // Le panier doit être vidé

        // Vérifier la présence de la commande dans la liste réactive
        val orders = repository.orders.value
        assertTrue(orders.any { it.id == placedOrder?.id })
    }

    @Test
    fun testOrderStatusTransitionByWholesaler() {
        val orders = repository.orders.value
        assertTrue("Au moins une commande initiale existe", orders.isNotEmpty())

        val targetOrder = orders.first()
        val success = repository.updateOrderStatus(targetOrder.id, OrderStatus.CONFIRMED)
        assertTrue(success)

        val updatedOrder = repository.orders.value.find { it.id == targetOrder.id }
        assertEquals(OrderStatus.CONFIRMED, updatedOrder?.status)
    }

    @Test
    fun testWholesalerCatalogManagement() {
        val initialCount = repository.products.value.size

        val newProduct = repository.addProduct(
            name = "Savon Noir Liquide Spécial Adjamé",
            description = "Bidon de 5L fabrication locale de qualité supérieure.",
            categoryId = "cat_cosmetics",
            packaging = "Carton de 6 bidons",
            minOrderQuantity = 2,
            basePrice = 18000.0,
            priceTiers = listOf(
                PriceTier(minQuantity = 2, maxQuantity = 5, unitPriceFcfa = 17000.0, label = "2 - 5 bidons"),
                PriceTier(minQuantity = 6, maxQuantity = null, unitPriceFcfa = 15000.0, label = "6+ bidons")
            ),
            stockQuantity = 40,
            imageUrl = ""
        )

        assertNotNull(newProduct.id)
        assertEquals(initialCount + 1, repository.products.value.size)

        // Test toggle stock
        repository.toggleProductStock(newProduct.id)
        val toggled = repository.getProductById(newProduct.id)
        assertEquals("OUT_OF_STOCK", toggled?.stockStatus)

        // Test suppression
        repository.deleteProduct(newProduct.id)
        assertEquals(initialCount, repository.products.value.size)
    }

    @Test
    fun testFavoritesAndPrivateNotes() {
        val testShopId = "seller_elhadj_oumar"
        val noteContent = "Passer avant 11h sinon trop de monde au Forum."

        repository.saveSellerNote(testShopId, noteContent)
        val savedNote = repository.sellerPrivateNotes.value[testShopId]
        assertEquals(noteContent, savedNote)

        val initialFav = repository.favoriteShopIds.value.contains(testShopId)
        repository.toggleFavoriteShop(testShopId)
        val afterToggleFav = repository.favoriteShopIds.value.contains(testShopId)
        assertFalse(initialFav == afterToggleFav)
    }

    @Test
    fun testUserRoleSwitching() {
        repository.switchRole(UserRole.WHOLESALER)
        assertEquals(UserRole.WHOLESALER, repository.currentUser.value.role)

        repository.switchRole(UserRole.ADMIN)
        assertEquals(UserRole.ADMIN, repository.currentUser.value.role)

        repository.switchRole(UserRole.BUYER)
        assertEquals(UserRole.BUYER, repository.currentUser.value.role)
    }

    @Test
    fun testAdminShopVerificationToggle() {
        val shop = repository.shops.value.first()
        val originalStatus = shop.isVerified

        repository.toggleShopVerification(shop.id)
        val toggledShop = repository.getShopById(shop.id)
        assertEquals(!originalStatus, toggledShop?.isVerified)

        // Revenir à l'état initial
        repository.toggleShopVerification(shop.id)
        val restoredShop = repository.getShopById(shop.id)
        assertEquals(originalStatus, restoredShop?.isVerified)
    }
}
