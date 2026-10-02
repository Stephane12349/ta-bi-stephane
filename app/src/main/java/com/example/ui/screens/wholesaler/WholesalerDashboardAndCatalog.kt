package com.example.ui.screens.wholesaler

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OrderStatus
import com.example.ui.MarketplaceViewModel
import com.example.ui.components.OrderCard
import com.example.ui.theme.AdjameGreenPrimary
import com.example.ui.theme.AdjameOrangeSecondary
import com.example.ui.theme.AdjameSuccess

@Composable
fun WholesalerDashboardScreen(
    viewModel: MarketplaceViewModel,
    modifier: Modifier = Modifier
) {
    val orders by viewModel.orders.collectAsState()
    val products by viewModel.products.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    val pendingOrders = orders.filter { it.status == OrderStatus.PENDING || it.status == OrderStatus.CONFIRMED }
    val totalRevenue = orders.filter { it.status == OrderStatus.COMPLETED }.sumOf { it.totalAmount }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("screen_wholesaler_dashboard"),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // En-tête Grossiste
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AdjameOrangeSecondary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Tableau de Bord Vendeur",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = currentUser.businessName.ifBlank { "Éts El Hadj Oumar & Frères" },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Localisation : Adjamé Forum Niveau 1, Magasin B-14",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        // Grille des 4 Métriques Clés
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricCard(
                    title = "CA Réalisé",
                    value = "${totalRevenue.toLong()} F",
                    icon = Icons.Default.Payments,
                    color = AdjameGreenPrimary,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Commandes en attente",
                    value = "${pendingOrders.size}",
                    icon = Icons.Default.ReceiptLong,
                    color = AdjameOrangeSecondary,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricCard(
                    title = "Cartons au catalogue",
                    value = "${products.size}",
                    icon = Icons.Default.Inventory,
                    color = Color(0xFF2980B9),
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Vues revendeurs",
                    value = "1 240",
                    icon = Icons.Default.Visibility,
                    color = Color(0xFF8E44AD),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Action Rapide : Publier un nouvel arrivage
        item {
            Button(
                onClick = { viewModel.navigateTo("wholesaler_add_product") },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AdjameGreenPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_quick_add_product")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Publier un Arrivage Conteneur (< 60s)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }

        // Commandes Récentes Urgentes
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Commandes à Traiter (${pendingOrders.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Voir toutes",
                    color = AdjameOrangeSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { viewModel.navigateTo("wholesaler_orders") }
                )
            }
        }

        items(pendingOrders.take(3)) { order ->
            OrderCard(
                order = order,
                onOrderClick = { viewModel.navigateTo("wholesaler_orders") }
            )
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            Text(text = title, fontSize = 11.sp, color = Color.Gray)
        }
    }
}

@Composable
fun WholesalerCatalogScreen(
    viewModel: MarketplaceViewModel,
    modifier: Modifier = Modifier
) {
    val products by viewModel.products.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("screen_wholesaler_catalog")
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 90.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = "Mon Catalogue de Gros (${products.size} articles)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "Basculez instantanément l'état du stock ou modifiez vos prix de gros.",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            items(products) { product ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = product.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "${product.packaging} • Min: ${product.minOrderQuantity} carton(s)",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "${product.basePrice.toLong()} FCFA / ctn",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                color = AdjameOrangeSecondary
                            )
                        }

                        // Switch Stock (En Stock / Épuisé)
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val inStock = product.stockStatus == "IN_STOCK"
                            Switch(
                                checked = inStock,
                                onCheckedChange = { viewModel.toggleProductStock(product.id) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = AdjameSuccess
                                )
                            )
                            Text(
                                text = if (inStock) "En Stock" else "Épuisé",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (inStock) AdjameSuccess else Color.Red
                            )
                        }

                        IconButton(onClick = { viewModel.deleteProduct(product.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = Color.Gray)
                        }
                    }
                }
            }
        }

        // Bouton Flottant d'Ajout
        FloatingActionButton(
            onClick = { viewModel.navigateTo("wholesaler_add_product") },
            containerColor = AdjameGreenPrimary,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp, 16.dp, 16.dp, 90.dp)
                .testTag("fab_add_product")
        ) {
            Icon(Icons.Default.Add, contentDescription = "Ajouter un produit")
        }
    }
}
