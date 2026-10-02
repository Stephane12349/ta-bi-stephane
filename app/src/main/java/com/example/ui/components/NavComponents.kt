package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserRole
import com.example.ui.theme.AdjameGold
import com.example.ui.theme.AdjameGreenPrimary
import com.example.ui.theme.AdjameOrangeSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdjameTopBar(
    currentRole: UserRole,
    cartItemCount: Int,
    notificationCount: Int,
    canNavigateBack: Boolean,
    onNavigateBack: () -> Unit,
    onRoleChange: (UserRole) -> Unit,
    onOpenCart: () -> Unit,
    onOpenNotifications: () -> Unit
) {
    var showRoleMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Adjamé Market",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Tag de Rôle interactif
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = when (currentRole) {
                        UserRole.BUYER -> Color(0xFF16A085)
                        UserRole.WHOLESALER -> AdjameOrangeSecondary
                        UserRole.ADMIN -> AdjameGold
                    },
                    modifier = Modifier
                        .clickable { showRoleMenu = true }
                        .testTag("role_switcher_pill")
                ) {
                    Text(
                        text = when (currentRole) {
                            UserRole.BUYER -> "ACHETEUR"
                            UserRole.WHOLESALER -> "GROSSISTE"
                            UserRole.ADMIN -> "ADMIN"
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

                // Menu déroulant de changement de rôle
                DropdownMenu(
                    expanded = showRoleMenu,
                    onDismissRequest = { showRoleMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Espace Acheteur (Revendeur)") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        onClick = {
                            showRoleMenu = false
                            onRoleChange(UserRole.BUYER)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Espace Grossiste (Boutique)") },
                        leadingIcon = { Icon(Icons.Default.AddBusiness, contentDescription = null) },
                        onClick = {
                            showRoleMenu = false
                            onRoleChange(UserRole.WHOLESALER)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Espace Administrateur (Ops)") },
                        leadingIcon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = null) },
                        onClick = {
                            showRoleMenu = false
                            onRoleChange(UserRole.ADMIN)
                        }
                    )
                }
            }
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("nav_back_button")) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Retour",
                        tint = Color.White
                    )
                }
            }
        },
        actions = {
            // Notifications
            IconButton(onClick = onOpenNotifications, modifier = Modifier.testTag("btn_notifications")) {
                BadgedBox(
                    badge = {
                        if (notificationCount > 0) {
                            Badge { Text(notificationCount.toString()) }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White
                    )
                }
            }

            // Panier (pour acheteur)
            if (currentRole == UserRole.BUYER) {
                IconButton(onClick = onOpenCart, modifier = Modifier.testTag("btn_cart")) {
                    BadgedBox(
                        badge = {
                            if (cartItemCount > 0) {
                                Badge { Text(cartItemCount.toString()) }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Panier",
                            tint = Color.White
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AdjameGreenPrimary
        )
    )
}

@Composable
fun AdjameBottomNav(
    currentRole: UserRole,
    currentScreen: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        modifier = Modifier.testTag("bottom_nav_bar")
    ) {
        when (currentRole) {
            UserRole.BUYER -> {
                NavigationBarItem(
                    selected = currentScreen == "buyer_home",
                    onClick = { onNavigate("buyer_home") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Accueil") },
                    label = { Text("Accueil") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = AdjameGreenPrimary)
                )
                NavigationBarItem(
                    selected = currentScreen == "buyer_search",
                    onClick = { onNavigate("buyer_search") },
                    icon = { Icon(Icons.Default.Search, contentDescription = "Recherche") },
                    label = { Text("Recherche") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = AdjameGreenPrimary)
                )
                NavigationBarItem(
                    selected = currentScreen == "buyer_cart",
                    onClick = { onNavigate("buyer_cart") },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Panier") },
                    label = { Text("Panier") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = AdjameGreenPrimary)
                )
                NavigationBarItem(
                    selected = currentScreen == "buyer_orders",
                    onClick = { onNavigate("buyer_orders") },
                    icon = { Icon(Icons.Default.ReceiptLong, contentDescription = "Commandes") },
                    label = { Text("Commandes") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = AdjameGreenPrimary)
                )
                NavigationBarItem(
                    selected = currentScreen == "profile",
                    onClick = { onNavigate("profile") },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
                    label = { Text("Profil") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = AdjameGreenPrimary)
                )
            }
            UserRole.WHOLESALER -> {
                NavigationBarItem(
                    selected = currentScreen == "wholesaler_dashboard",
                    onClick = { onNavigate("wholesaler_dashboard") },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Dashboard") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = AdjameOrangeSecondary)
                )
                NavigationBarItem(
                    selected = currentScreen == "wholesaler_catalog",
                    onClick = { onNavigate("wholesaler_catalog") },
                    icon = { Icon(Icons.Default.Inventory, contentDescription = "Catalogue") },
                    label = { Text("Catalogue") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = AdjameOrangeSecondary)
                )
                NavigationBarItem(
                    selected = currentScreen == "wholesaler_orders",
                    onClick = { onNavigate("wholesaler_orders") },
                    icon = { Icon(Icons.Default.ReceiptLong, contentDescription = "Commandes") },
                    label = { Text("Commandes") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = AdjameOrangeSecondary)
                )
                NavigationBarItem(
                    selected = currentScreen == "wholesaler_profile",
                    onClick = { onNavigate("wholesaler_profile") },
                    icon = { Icon(Icons.Default.AddBusiness, contentDescription = "Ma Boutique") },
                    label = { Text("Boutique") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = AdjameOrangeSecondary)
                )
                NavigationBarItem(
                    selected = currentScreen == "profile",
                    onClick = { onNavigate("profile") },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
                    label = { Text("Profil") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = AdjameOrangeSecondary)
                )
            }
            UserRole.ADMIN -> {
                NavigationBarItem(
                    selected = currentScreen == "admin_dashboard",
                    onClick = { onNavigate("admin_dashboard") },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Supervision") },
                    label = { Text("Supervision") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = AdjameGold)
                )
                NavigationBarItem(
                    selected = currentScreen == "admin_kyc",
                    onClick = { onNavigate("admin_kyc") },
                    icon = { Icon(Icons.Default.VerifiedUser, contentDescription = "Validation") },
                    label = { Text("Vérifications") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = AdjameGold)
                )
                NavigationBarItem(
                    selected = currentScreen == "admin_disputes",
                    onClick = { onNavigate("admin_disputes") },
                    icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Litiges") },
                    label = { Text("Litiges") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = AdjameGold)
                )
                NavigationBarItem(
                    selected = currentScreen == "profile",
                    onClick = { onNavigate("profile") },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
                    label = { Text("Profil") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = AdjameGold)
                )
            }
        }
    }
}
