package com.example.ui.screens.buyer

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
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserRole
import com.example.ui.MarketplaceViewModel
import com.example.ui.components.ProductCard
import com.example.ui.components.WholesalerShopCard
import com.example.ui.theme.AdjameGreenPrimary

@Composable
fun BuyerFavoritesScreen(
    viewModel: MarketplaceViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val shops by viewModel.shops.collectAsState()
    val products by viewModel.products.collectAsState()
    val favoriteShopIds by viewModel.favoriteShopIds.collectAsState()
    val favoriteProductIds by viewModel.favoriteProductIds.collectAsState()

    val favShops = shops.filter { favoriteShopIds.contains(it.id) }
    val favProducts = products.filter { favoriteProductIds.contains(it.id) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("screen_buyer_favorites")
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = AdjameGreenPrimary
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Grossistes Enregistrés (${favShops.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Articles Favoris (${favProducts.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
        }

        if (selectedTab == 0) {
            if (favShops.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Aucun grossiste dans vos favoris", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 90.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(favShops) { shop ->
                        WholesalerShopCard(
                            shop = shop,
                            isFavorite = true,
                            onShopClick = { viewModel.openShopDetail(shop.id) },
                            onFavoriteToggle = { viewModel.toggleFavoriteShop(shop.id) }
                        )
                    }
                }
            }
        } else {
            if (favProducts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Aucun article dans vos favoris", color = Color.Gray)
                }
            } else {
                val chunks = favProducts.chunked(2)
                LazyColumn(
                    contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 90.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(chunks) { chunk ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            for (p in chunk) {
                                ProductCard(
                                    product = p,
                                    isFavorite = true,
                                    onProductClick = { viewModel.openProductDetail(p.id) },
                                    onFavoriteToggle = { viewModel.toggleFavoriteProduct(p.id) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (chunk.size == 1) Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BuyerReportScreen(
    viewModel: MarketplaceViewModel,
    modifier: Modifier = Modifier
) {
    var selectedReason by remember { mutableStateOf("BOUTIQUE_INTROUVABLE") }
    var description by remember { mutableStateOf("") }
    var shopName by remember { mutableStateOf("Boutique Forum B-14") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("screen_buyer_report")
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Flag, contentDescription = null, tint = Color.Red)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Signaler un litige ou une anomalie", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Text(
            text = "Adjamé Market s'engage à traiter tout signalement sous 2 heures.",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("Motif du signalement :", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))

                val reasons = listOf(
                    "BOUTIQUE_INTROUVABLE" to "Boutique introuvable à l'adresse indiquée",
                    "PRODUIT_NON_CONFORME" to "Marchandise reçue non conforme / abîmée",
                    "PRIX_TROMPEUR" to "Prix en magasin différent de l'application",
                    "ARNAQUE_ACOMPTE" to "Tentative d'arnaque sur acompte"
                )

                reasons.forEach { (key, label) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedReason = key }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedReason == key,
                            onClick = { selectedReason = key },
                            colors = RadioButtonDefaults.colors(selectedColor = AdjameGreenPrimary)
                        )
                        Text(label, fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Explications détaillées...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.submitReport("shop_report_target", shopName, selectedReason, description)
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B)),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Envoyer le signalement à l'équipe de sécurité", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
