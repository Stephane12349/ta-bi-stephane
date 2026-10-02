package com.example.data.repository

import android.content.Context
import com.example.data.local.AdjameMarketDatabase
import com.example.data.local.SeedData
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.SellerContactEntity
import com.example.data.local.model.PriceTier
import com.example.model.AppNotification
import com.example.model.CartItem
import com.example.model.Category
import com.example.model.ChatMessage
import com.example.model.Order
import com.example.model.OrderItem
import com.example.model.OrderStatus
import com.example.model.PriceQuoteRequest
import com.example.model.Product
import com.example.model.ShopReport
import com.example.model.UserProfile
import com.example.model.UserRole
import com.example.model.WholesalerShop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

/**
 * Référentiel centralisé pour l'application Adjamé Market.
 * Gère l'état réactif, la synchronisation avec le cache local Room (hors-ligne),
 * la séparation stricte des rôles (Acheteur, Grossiste, Admin) et la logique métier.
 */
class MarketplaceRepository private constructor(context: Context) {

    private val db = AdjameMarketDatabase.getInstance(context)
    private val scope = CoroutineScope(Dispatchers.IO)

    // Profil utilisateur actuel
    private val _currentUser = MutableStateFlow(
        UserProfile(
            id = "user_buyer_awa",
            phone = "+2250701020304",
            fullName = "Awa Traoré",
            email = "awa.revendeuse@gmail.com",
            role = UserRole.BUYER,
            businessName = "Awa Chic Boutique",
            commune = "Yopougon Siporex",
            isVerified = true,
            dataSaverEnabled = false
        )
    )
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    // Catégories
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    // Produits
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    // Boutiques
    private val _shops = MutableStateFlow<List<WholesalerShop>>(emptyList())
    val shops: StateFlow<List<WholesalerShop>> = _shops.asStateFlow()

    // Panier Acheteur
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // Commandes
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    // Demandes de cotation
    private val _quotes = MutableStateFlow<List<PriceQuoteRequest>>(emptyList())
    val quotes: StateFlow<List<PriceQuoteRequest>> = _quotes.asStateFlow()

    // Messages
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    // Notifications
    private val _notifications = MutableStateFlow<List<AppNotification>>(emptyList())
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    // Favoris
    private val _favoriteProductIds = MutableStateFlow<Set<String>>(setOf("prod_savon_kanza", "prod_pagne_woodin"))
    val favoriteProductIds: StateFlow<Set<String>> = _favoriteProductIds.asStateFlow()

    private val _favoriteShopIds = MutableStateFlow<Set<String>>(setOf("seller_elhadj_oumar", "seller_fanta_cosmetics"))
    val favoriteShopIds: StateFlow<Set<String>> = _favoriteShopIds.asStateFlow()

    // Notes privées acheteur sur les grossistes (ex: "Demander le commis Salif")
    private val _sellerPrivateNotes = MutableStateFlow<Map<String, String>>(
        mapOf("seller_elhadj_oumar" to "Demander le commis Salif pour 5% de remise au carton.")
    )
    val sellerPrivateNotes: StateFlow<Map<String, String>> = _sellerPrivateNotes.asStateFlow()

    // Signalements (Modération Admin)
    private val _reports = MutableStateFlow<List<ShopReport>>(emptyList())
    val reports: StateFlow<List<ShopReport>> = _reports.asStateFlow()

    init {
        initInitialData()
    }

    private fun initInitialData() {
        // Initialiser les catégories
        val catList = SeedData.categories.map { entity ->
            Category(
                id = entity.id,
                name = entity.name,
                slug = entity.slug,
                iconKey = entity.iconKey,
                description = entity.description,
                productCount = entity.productCount
            )
        }
        _categories.value = catList

        // Initialiser les boutiques grossistes
        val shopList = SeedData.sellers.map { seller ->
            WholesalerShop(
                id = seller.sellerId,
                name = seller.businessName,
                description = "Grossiste importateur direct. Vente exclusive au carton et au ballot. Arrivages hebdomadaires.",
                ownerName = seller.managerName,
                phone = seller.phone,
                whatsapp = seller.whatsappNumber,
                address = seller.marketSector,
                marketSector = if (seller.marketSector.contains("Forum")) "Forum d'Adjamé" else if (seller.marketSector.contains("Roxy")) "Adjamé Roxy" else if (seller.marketSector.contains("Black")) "Black Market" else "Marché Gouro",
                landmarks = seller.landmarks,
                hours = "7h30 - 18h00",
                categories = listOf("cat_cosmetics", "cat_textile", "cat_electronics"),
                isVerified = seller.verificationBadge == "TERRAIN_VERIFIED" || seller.verificationBadge == "CERTIFIED_IMPORTATEUR",
                verificationBadge = seller.verificationBadge,
                rating = seller.rating,
                reviewCount = seller.reviewCount,
                transactionCount = seller.transactionCount,
                responseTimeMinutes = seller.averageResponseMinutes,
                coverImageUrl = "https://images.unsplash.com/photo-1578575437130-527eed3abbec?w=600"
            )
        }
        _shops.value = shopList

        // Initialiser les produits
        val prodList = SeedData.products.map { p ->
            val shop = shopList.find { it.id == p.sellerId }
            Product(
                id = p.id,
                name = p.name,
                description = p.description,
                categoryId = p.categoryId,
                subCategory = "Gros Standard",
                sellerId = p.sellerId,
                sellerName = shop?.name ?: "Grossiste Adjamé",
                sellerSector = p.marketSector,
                images = listOf(p.imageUrl),
                packaging = p.packaging,
                minOrderQuantity = p.minOrderQuantity,
                basePrice = p.basePrice,
                priceTiers = p.priceTiers,
                stockStatus = p.stockStatus,
                stockQuantity = 45,
                variants = listOf("Standard", "Lot promotionnel"),
                isVerifiedSeller = p.isVerifiedSeller,
                isPromoted = p.isPromoted
            )
        }
        _products.value = prodList

        // Initialiser des commandes réalistes
        _orders.value = listOf(
            Order(
                id = "CMD-9481",
                buyerId = "user_buyer_awa",
                buyerName = "Awa Traoré",
                buyerPhone = "+2250701020304",
                sellerId = "seller_fanta_cosmetics",
                sellerName = "Fanta Beauté Distribution",
                sellerSector = "Adjamé Roxy",
                items = listOf(
                    OrderItem(
                        productId = "prod_savon_kanza",
                        productName = "Savon Kanza Éclaircissant Anti-Taches",
                        packaging = "Carton de 48 pcs",
                        quantity = 5,
                        unitPrice = 21600.0,
                        subtotal = 108000.0
                    )
                ),
                totalAmount = 108000.0,
                status = OrderStatus.CONFIRMED,
                pickupPinCode = "4829",
                deliveryType = "CLICK_AND_COLLECT",
                notes = "Retrait prévu demain matin par mon coursier."
            ),
            Order(
                id = "CMD-8210",
                buyerId = "user_buyer_awa",
                buyerName = "Awa Traoré",
                buyerPhone = "+2250701020304",
                sellerId = "seller_elhadj_oumar",
                sellerName = "Éts El Hadj Oumar & Frères",
                sellerSector = "Forum d'Adjamé",
                items = listOf(
                    OrderItem(
                        productId = "prod_pagne_woodin",
                        productName = "Pagne Imprimé Prestige 6 Yards",
                        packaging = "Ballot de 10 pcs",
                        quantity = 2,
                        unitPrice = 95000.0,
                        subtotal = 190000.0
                    )
                ),
                totalAmount = 190000.0,
                status = OrderStatus.COMPLETED,
                pickupPinCode = "7391",
                deliveryType = "GARE_EXPEDITION",
                notes = "Expédié à la gare UTB Yopougon."
            )
        )

        // Initialiser des notifications
        _notifications.value = listOf(
            AppNotification(
                id = "notif_1",
                title = "Arrivage Conteneur Forum",
                message = "El Hadj Oumar vient de recevoir 300 ballots de pagnes Woodin.",
                type = "ARRIVAGE"
            ),
            AppNotification(
                id = "notif_2",
                title = "Commande Confirmée",
                message = "Votre commande CMD-9481 a été confirmée par Fanta Beauté Distribution.",
                type = "ORDER"
            )
        )

        // Synchroniser vers Room Database en tâche de fond
        scope.launch {
            try {
                db.categoryDao().insertCategories(SeedData.categories)
                db.sellerContactDao().insertSellers(SeedData.sellers)
                db.productDao().insertProducts(SeedData.products)
            } catch (e: Exception) {
                // Ignore silent database sync errors
            }
        }
    }

    // --- AUTHENTIFICATION & RÔLES ---

    fun switchRole(role: UserRole) {
        _currentUser.update { current ->
            when (role) {
                UserRole.BUYER -> current.copy(
                    id = "user_buyer_awa",
                    fullName = "Awa Traoré (Revendeuse)",
                    role = UserRole.BUYER,
                    businessName = "Awa Chic Boutique",
                    commune = "Yopougon"
                )
                UserRole.WHOLESALER -> current.copy(
                    id = "seller_elhadj_oumar",
                    fullName = "El Hadj Oumar Traoré (Grossiste)",
                    role = UserRole.WHOLESALER,
                    businessName = "Éts El Hadj Oumar & Frères",
                    commune = "Adjamé Forum B-14"
                )
                UserRole.ADMIN -> current.copy(
                    id = "user_admin_jeanluc",
                    fullName = "Jean-Luc Kouadio (Superviseur Adjamé)",
                    role = UserRole.ADMIN,
                    businessName = "Adjamé Market Ops",
                    commune = "Adjamé Siège"
                )
            }
        }
    }

    fun login(phone: String): Boolean {
        if (phone.length >= 8) {
            _currentUser.update { it.copy(phone = phone) }
            return true
        }
        return false
    }

    fun register(name: String, phone: String, role: UserRole, commune: String, business: String): Boolean {
        val newProfile = UserProfile(
            id = "user_" + UUID.randomUUID().toString().take(8),
            phone = phone,
            fullName = name,
            role = role,
            businessName = business,
            commune = commune,
            isVerified = role == UserRole.BUYER
        )
        _currentUser.value = newProfile
        return true
    }

    fun updateProfile(name: String, business: String, commune: String, dataSaver: Boolean) {
        _currentUser.update {
            it.copy(
                fullName = name,
                businessName = business,
                commune = commune,
                dataSaverEnabled = dataSaver
            )
        }
    }

    // --- PRODUITS & RECHERCHE ---

    fun getProductById(id: String): Product? {
        return _products.value.find { it.id == id }
    }

    fun getShopById(id: String): WholesalerShop? {
        return _shops.value.find { it.id == id }
    }

    fun searchProducts(query: String, categoryId: String? = null, sector: String? = null, maxPrice: Double? = null): List<Product> {
        return _products.value.filter { product ->
            val matchQuery = query.isBlank() || 
                product.name.contains(query, ignoreCase = true) || 
                product.description.contains(query, ignoreCase = true) ||
                product.sellerName.contains(query, ignoreCase = true)
            val matchCategory = categoryId.isNullOrBlank() || product.categoryId == categoryId
            val matchSector = sector.isNullOrBlank() || product.sellerSector.contains(sector, ignoreCase = true)
            val matchPrice = maxPrice == null || product.basePrice <= maxPrice
            matchQuery && matchCategory && matchSector && matchPrice
        }
    }

    // --- GESTION DU CATALOGUE (GROSSISTE) ---

    fun addProduct(
        name: String,
        description: String,
        categoryId: String,
        packaging: String,
        minOrderQuantity: Int,
        basePrice: Double,
        priceTiers: List<PriceTier>,
        stockQuantity: Int,
        imageUrl: String
    ): Product {
        val seller = _currentUser.value
        val newProduct = Product(
            id = "prod_" + UUID.randomUUID().toString().take(8),
            name = name,
            description = description,
            categoryId = categoryId,
            sellerId = seller.id,
            sellerName = seller.businessName.ifBlank { seller.fullName },
            sellerSector = seller.commune,
            packaging = packaging,
            minOrderQuantity = minOrderQuantity,
            basePrice = basePrice,
            priceTiers = priceTiers,
            stockStatus = if (stockQuantity > 0) "IN_STOCK" else "OUT_OF_STOCK",
            stockQuantity = stockQuantity,
            images = listOf(imageUrl.ifBlank { "https://images.unsplash.com/photo-1584917865442-de89df76afd3?w=400" }),
            isVerifiedSeller = true,
            isPromoted = false
        )
        _products.update { listOf(newProduct) + it }
        
        // Persister dans Room
        scope.launch {
            try {
                db.productDao().insertProduct(
                    ProductEntity(
                        id = newProduct.id,
                        categoryId = newProduct.categoryId,
                        sellerId = newProduct.sellerId,
                        name = newProduct.name,
                        description = newProduct.description,
                        packaging = newProduct.packaging,
                        minOrderQuantity = newProduct.minOrderQuantity,
                        basePrice = newProduct.basePrice,
                        priceTiers = newProduct.priceTiers,
                        stockStatus = newProduct.stockStatus,
                        imageUrl = newProduct.images.firstOrNull() ?: "",
                        marketSector = newProduct.sellerSector
                    )
                )
            } catch (e: Exception) {
                // ignore
            }
        }
        return newProduct
    }

    fun updateProduct(product: Product) {
        _products.update { list ->
            list.map { if (it.id == product.id) product else it }
        }
    }

    fun toggleProductStock(productId: String) {
        _products.update { list ->
            list.map { p ->
                if (p.id == productId) {
                    val nextStatus = if (p.stockStatus == "IN_STOCK") "OUT_OF_STOCK" else "IN_STOCK"
                    p.copy(stockStatus = nextStatus)
                } else p
            }
        }
    }

    fun deleteProduct(productId: String) {
        _products.update { it.filter { p -> p.id != productId } }
        scope.launch {
            db.productDao().deleteProductById(productId)
        }
    }

    // --- PANIER & COMMANDES (ACHETEUR & GROSSISTE) ---

    fun addToCart(product: Product, quantity: Int = 1) {
        _cartItems.update { current ->
            val existing = current.find { it.product.id == product.id }
            if (existing != null) {
                current.map {
                    if (it.product.id == product.id) it.copy(quantity = it.quantity + quantity) else it
                }
            } else {
                current + CartItem(product = product, quantity = maxOf(quantity, product.minOrderQuantity))
            }
        }
    }

    fun updateCartQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(productId)
            return
        }
        _cartItems.update { current ->
            current.map {
                if (it.product.id == productId) it.copy(quantity = quantity) else it
            }
        }
    }

    fun removeFromCart(productId: String) {
        _cartItems.update { it.filter { item -> item.product.id != productId } }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun placeOrder(deliveryType: String, notes: String): Order? {
        val items = _cartItems.value
        if (items.isEmpty()) return null

        val user = _currentUser.value
        val firstSeller = items.first().product.sellerId
        val sellerShop = _shops.value.find { it.id == firstSeller }

        val orderItems = items.map { item ->
            OrderItem(
                productId = item.product.id,
                productName = item.product.name,
                packaging = item.product.packaging,
                quantity = item.quantity,
                unitPrice = item.unitPrice,
                subtotal = item.totalPrice
            )
        }

        val total = orderItems.sumOf { it.subtotal }
        val pin = (1000 + Random.nextInt(9000)).toString()

        val newOrder = Order(
            id = "CMD-" + (1000 + Random.nextInt(9000)),
            buyerId = user.id,
            buyerName = user.fullName,
            buyerPhone = user.phone,
            sellerId = firstSeller,
            sellerName = sellerShop?.name ?: items.first().product.sellerName,
            sellerSector = sellerShop?.marketSector ?: items.first().product.sellerSector,
            items = orderItems,
            totalAmount = total,
            status = OrderStatus.PENDING,
            pickupPinCode = pin,
            deliveryType = deliveryType,
            notes = notes
        )

        _orders.update { listOf(newOrder) + it }
        clearCart()

        // Ajouter une notification
        _notifications.update {
            listOf(
                AppNotification(
                    id = "notif_" + System.currentTimeMillis(),
                    title = "Commande Passée",
                    message = "Votre commande ${newOrder.id} de ${total.toLong()} FCFA a été envoyée au grossiste.",
                    type = "ORDER"
                )
            ) + it
        }

        return newOrder
    }

    /**
     * Cycle de transition contrôlé des commandes :
     * PENDING -> CONFIRMED -> PREPARING -> READY -> COMPLETED
     * Possibilité de REJECTED ou CANCELLED
     */
    fun updateOrderStatus(orderId: String, nextStatus: OrderStatus): Boolean {
        var updated = false
        _orders.update { list ->
            list.map { order ->
                if (order.id == orderId) {
                    updated = true
                    order.copy(status = nextStatus, updatedAt = System.currentTimeMillis())
                } else order
            }
        }
        return updated
    }

    // --- FAVORIS & CARNET GROSSISTES ---

    fun toggleFavoriteProduct(productId: String) {
        _favoriteProductIds.update { set ->
            if (set.contains(productId)) set - productId else set + productId
        }
    }

    fun toggleFavoriteShop(shopId: String) {
        _favoriteShopIds.update { set ->
            if (set.contains(shopId)) set - shopId else set + shopId
        }
    }

    fun saveSellerNote(sellerId: String, note: String) {
        _sellerPrivateNotes.update { it + (sellerId to note) }
    }

    // --- GESTION DES LITIGES & ADMIN ---

    fun submitReport(shopId: String, shopName: String, reason: String, description: String) {
        val user = _currentUser.value
        val report = ShopReport(
            id = "rep_" + UUID.randomUUID().toString().take(6),
            shopId = shopId,
            shopName = shopName,
            reporterId = user.id,
            reporterName = user.fullName,
            reason = reason,
            description = description
        )
        _reports.update { listOf(report) + it }
    }

    fun toggleShopVerification(shopId: String) {
        _shops.update { list ->
            list.map { shop ->
                if (shop.id == shopId) {
                    val newVerified = !shop.isVerified
                    shop.copy(
                        isVerified = newVerified,
                        verificationBadge = if (newVerified) "TERRAIN_VERIFIED" else "STANDARD"
                    )
                } else shop
            }
        }
    }

    fun markNotificationAsRead(id: String) {
        _notifications.update { list ->
            list.map { if (it.id == id) it.copy(isRead = true) else it }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: MarketplaceRepository? = null

        fun getInstance(context: Context): MarketplaceRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MarketplaceRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
