package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.UserRole
import com.example.ui.MarketplaceViewModel
import com.example.ui.components.AdjameBottomNav
import com.example.ui.components.AdjameTopBar
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.admin.AdminDisputesScreen
import com.example.ui.screens.admin.AdminKycScreen
import com.example.ui.screens.buyer.BuyerCartScreen
import com.example.ui.screens.buyer.BuyerFavoritesScreen
import com.example.ui.screens.buyer.BuyerHomeScreen
import com.example.ui.screens.buyer.BuyerOrderConfirmationScreen
import com.example.ui.screens.buyer.BuyerOrderDetailScreen
import com.example.ui.screens.buyer.BuyerOrdersScreen
import com.example.ui.screens.buyer.BuyerProductDetailScreen
import com.example.ui.screens.buyer.BuyerReportScreen
import com.example.ui.screens.buyer.BuyerSearchScreen
import com.example.ui.screens.buyer.BuyerShopDetailScreen
import com.example.ui.screens.common.NotificationsScreen
import com.example.ui.screens.common.UserProfileScreen
import com.example.ui.screens.wholesaler.WholesalerAddProductScreen
import com.example.ui.screens.wholesaler.WholesalerCatalogScreen
import com.example.ui.screens.wholesaler.WholesalerDashboardScreen
import com.example.ui.screens.wholesaler.WholesalerOrdersScreen
import com.example.ui.screens.wholesaler.WholesalerShopProfileScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AdjameMarketApp()
            }
        }
    }
}

@Composable
fun AdjameMarketApp(viewModel: MarketplaceViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val snackbarMsg by viewModel.snackbarMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMsg) {
        snackbarMsg?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    // Gestion du bouton retour natif Android (BackHandler)
    val isRootScreen = when (currentUser.role) {
        UserRole.BUYER -> currentScreen == "buyer_home"
        UserRole.WHOLESALER -> currentScreen == "wholesaler_dashboard"
        UserRole.ADMIN -> currentScreen == "admin_dashboard"
    }

    BackHandler(enabled = !isRootScreen) {
        when {
            currentScreen == "buyer_product_detail" || currentScreen == "buyer_shop_detail" -> {
                viewModel.navigateTo("buyer_home")
            }
            currentScreen == "buyer_order_detail" || currentScreen == "buyer_order_confirmation" -> {
                viewModel.navigateTo("buyer_orders")
            }
            currentScreen == "wholesaler_add_product" -> {
                viewModel.navigateTo("wholesaler_catalog")
            }
            currentScreen == "admin_kyc" || currentScreen == "admin_disputes" -> {
                viewModel.navigateTo("admin_dashboard")
            }
            else -> {
                when (currentUser.role) {
                    UserRole.BUYER -> viewModel.navigateTo("buyer_home")
                    UserRole.WHOLESALER -> viewModel.navigateTo("wholesaler_dashboard")
                    UserRole.ADMIN -> viewModel.navigateTo("admin_dashboard")
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AdjameTopBar(
                currentRole = currentUser.role,
                cartItemCount = cartItems.size,
                notificationCount = notifications.count { !it.isRead },
                canNavigateBack = !isRootScreen,
                onNavigateBack = {
                    when (currentUser.role) {
                        UserRole.BUYER -> viewModel.navigateTo("buyer_home")
                        UserRole.WHOLESALER -> viewModel.navigateTo("wholesaler_dashboard")
                        UserRole.ADMIN -> viewModel.navigateTo("admin_dashboard")
                    }
                },
                onRoleChange = { role -> viewModel.switchRole(role) },
                onOpenCart = { viewModel.navigateTo("buyer_cart") },
                onOpenNotifications = { viewModel.navigateTo("notifications") }
            )
        },
        bottomBar = {
            AdjameBottomNav(
                currentRole = currentUser.role,
                currentScreen = currentScreen,
                onNavigate = { screen -> viewModel.navigateTo(screen) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        val screenModifier = Modifier.padding(innerPadding)

        when (currentScreen) {
            // Espace Acheteur
            "buyer_home" -> BuyerHomeScreen(viewModel, modifier = screenModifier)
            "buyer_search" -> BuyerSearchScreen(viewModel, modifier = screenModifier)
            "buyer_product_detail" -> BuyerProductDetailScreen(viewModel, modifier = screenModifier)
            "buyer_shop_detail" -> BuyerShopDetailScreen(viewModel, modifier = screenModifier)
            "buyer_cart" -> BuyerCartScreen(viewModel, modifier = screenModifier)
            "buyer_orders" -> BuyerOrdersScreen(viewModel, modifier = screenModifier)
            "buyer_order_detail" -> BuyerOrderDetailScreen(viewModel, modifier = screenModifier)
            "buyer_order_confirmation" -> BuyerOrderConfirmationScreen(viewModel, modifier = screenModifier)
            "buyer_favorites" -> BuyerFavoritesScreen(viewModel, modifier = screenModifier)
            "buyer_report" -> BuyerReportScreen(viewModel, modifier = screenModifier)

            // Espace Grossiste
            "wholesaler_dashboard" -> WholesalerDashboardScreen(viewModel, modifier = screenModifier)
            "wholesaler_catalog" -> WholesalerCatalogScreen(viewModel, modifier = screenModifier)
            "wholesaler_add_product" -> WholesalerAddProductScreen(viewModel, modifier = screenModifier)
            "wholesaler_orders" -> WholesalerOrdersScreen(viewModel, modifier = screenModifier)
            "wholesaler_profile" -> WholesalerShopProfileScreen(viewModel, modifier = screenModifier)

            // Espace Administrateur
            "admin_dashboard" -> AdminDashboardScreen(viewModel, modifier = screenModifier)
            "admin_kyc" -> AdminKycScreen(viewModel, modifier = screenModifier)
            "admin_disputes" -> AdminDisputesScreen(viewModel, modifier = screenModifier)

            // Écrans Communs
            "notifications" -> NotificationsScreen(viewModel, modifier = screenModifier)
            "profile" -> UserProfileScreen(viewModel, modifier = screenModifier)

            else -> BuyerHomeScreen(viewModel, modifier = screenModifier)
        }
    }
}
