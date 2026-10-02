package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.model.PriceTier
import com.example.data.repository.MarketplaceRepository
import com.example.model.AppNotification
import com.example.model.CartItem
import com.example.model.Category
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.Product
import com.example.model.ShopReport
import com.example.model.UserProfile
import com.example.model.UserRole
import com.example.model.WholesalerShop
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel principal gérant l'état de l'application Adjamé Market,
 * les rôles utilisateurs (Acheteur, Grossiste, Admin) et la navigation.
 */
class MarketplaceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MarketplaceRepository.getInstance(application)

    // Navigation & Écran Actif
    private val _currentScreen = MutableStateFlow("buyer_home")
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    private val _selectedProductId = MutableStateFlow<String?>(null)
    val selectedProductId: StateFlow<String?> = _selectedProductId.asStateFlow()

    private val _selectedShopId = MutableStateFlow<String?>(null)
    val selectedShopId: StateFlow<String?> = _selectedShopId.asStateFlow()

    private val _selectedOrderId = MutableStateFlow<String?>(null)
    val selectedOrderId: StateFlow<String?> = _selectedOrderId.asStateFlow()

    // Filtres & Recherche
    val searchQuery = MutableStateFlow("")
    val selectedCategoryFilter = MutableStateFlow<String?>(null)
    val selectedSectorFilter = MutableStateFlow<String?>(null)
    val verifiedOnlyFilter = MutableStateFlow(false)

    // Messages éphémères (Snackbar)
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    // Données réactives exposées
    val currentUser: StateFlow<UserProfile> = repository.currentUser
    val categories: StateFlow<List<Category>> = repository.categories
    val products: StateFlow<List<Product>> = repository.products
    val shops: StateFlow<List<WholesalerShop>> = repository.shops
    val cartItems: StateFlow<List<CartItem>> = repository.cartItems
    val orders: StateFlow<List<Order>> = repository.orders
    val notifications: StateFlow<List<AppNotification>> = repository.notifications
    val favoriteProductIds: StateFlow<Set<String>> = repository.favoriteProductIds
    val favoriteShopIds: StateFlow<Set<String>> = repository.favoriteShopIds
    val sellerPrivateNotes: StateFlow<Map<String, String>> = repository.sellerPrivateNotes
    val reports: StateFlow<List<ShopReport>> = repository.reports

    // Produits filtrés en temps réel
    val filteredProducts: StateFlow<List<Product>> = combine(
        products,
        searchQuery,
        selectedCategoryFilter,
        selectedSectorFilter,
        verifiedOnlyFilter
    ) { allProds, query, cat, sector, verifiedOnly ->
        allProds.filter { p ->
            val matchQuery = query.isBlank() || 
                p.name.contains(query, ignoreCase = true) || 
                p.description.contains(query, ignoreCase = true) ||
                p.sellerName.contains(query, ignoreCase = true)
            val matchCategory = cat == null || p.categoryId == cat
            val matchSector = sector == null || p.sellerSector.contains(sector, ignoreCase = true)
            val matchVerified = !verifiedOnly || p.isVerifiedSeller
            matchQuery && matchCategory && matchSector && matchVerified
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- NAVIGATION ---

    fun navigateTo(screen: String) {
        _currentScreen.value = screen
    }

    fun openProductDetail(productId: String) {
        _selectedProductId.value = productId
        _currentScreen.value = "buyer_product_detail"
    }

    fun openShopDetail(shopId: String) {
        _selectedShopId.value = shopId
        _currentScreen.value = "buyer_shop_detail"
    }

    fun openOrderDetail(orderId: String) {
        _selectedOrderId.value = orderId
        _currentScreen.value = "buyer_order_detail"
    }

    fun getProductById(id: String): Product? = repository.getProductById(id)
    fun getShopById(id: String): WholesalerShop? = repository.getShopById(id)

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    // --- ACTIONS AUTHENTIFICATION & RÔLES ---

    fun switchRole(role: UserRole) {
        repository.switchRole(role)
        // Rediriger vers l'accueil approprié selon le rôle
        when (role) {
            UserRole.BUYER -> _currentScreen.value = "buyer_home"
            UserRole.WHOLESALER -> _currentScreen.value = "wholesaler_dashboard"
            UserRole.ADMIN -> _currentScreen.value = "admin_dashboard"
        }
        showSnackbar("Bascule en mode : ${role.name}")
    }

    fun login(phone: String): Boolean {
        val success = repository.login(phone)
        if (success) {
            showSnackbar("Connexion réussie avec $phone")
        }
        return success
    }

    fun register(name: String, phone: String, role: UserRole, commune: String, business: String) {
        repository.register(name, phone, role, commune, business)
        switchRole(role)
    }

    fun updateProfile(name: String, business: String, commune: String, dataSaver: Boolean) {
        repository.updateProfile(name, business, commune, dataSaver)
        showSnackbar("Profil mis à jour avec succès")
    }

    // --- ACTIONS PANIER & COMMANDES ---

    fun addToCart(product: Product, quantity: Int = 1) {
        repository.addToCart(product, quantity)
        showSnackbar("${product.name} ajouté au panier ($quantity cartons)")
    }

    fun updateCartQuantity(productId: String, quantity: Int) {
        repository.updateCartQuantity(productId, quantity)
    }

    fun removeFromCart(productId: String) {
        repository.removeFromCart(productId)
    }

    fun placeOrder(deliveryType: String, notes: String): Order? {
        val order = repository.placeOrder(deliveryType, notes)
        if (order != null) {
            _selectedOrderId.value = order.id
            _currentScreen.value = "buyer_order_confirmation"
            showSnackbar("Réservation enregistrée ! Code PIN : ${order.pickupPinCode}")
        }
        return order
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        val ok = repository.updateOrderStatus(orderId, newStatus)
        if (ok) {
            showSnackbar("Statut commande $orderId mis à jour : ${newStatus.label}")
        }
    }

    // --- ACTIONS GROSSISTE ---

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
    ) {
        repository.addProduct(
            name, description, categoryId, packaging, minOrderQuantity,
            basePrice, priceTiers, stockQuantity, imageUrl
        )
        showSnackbar("Article $name publié avec succès au catalogue")
        _currentScreen.value = "wholesaler_catalog"
    }

    fun toggleProductStock(productId: String) {
        repository.toggleProductStock(productId)
    }

    fun deleteProduct(productId: String) {
        repository.deleteProduct(productId)
        showSnackbar("Article retiré du catalogue")
    }

    // --- FAVORIS & SIGNALEMENT ---

    fun toggleFavoriteProduct(productId: String) {
        repository.toggleFavoriteProduct(productId)
    }

    fun toggleFavoriteShop(shopId: String) {
        repository.toggleFavoriteShop(shopId)
    }

    fun saveSellerNote(shopId: String, note: String) {
        repository.saveSellerNote(shopId, note)
        showSnackbar("Note enregistrée en local")
    }

    fun submitReport(shopId: String, shopName: String, reason: String, description: String) {
        repository.submitReport(shopId, shopName, reason, description)
        showSnackbar("Signalement transmis à notre équipe de sécurité terrain")
        _currentScreen.value = "buyer_home"
    }

    fun toggleShopVerification(shopId: String) {
        repository.toggleShopVerification(shopId)
        showSnackbar("Statut de certification boutique mis à jour")
    }
}
