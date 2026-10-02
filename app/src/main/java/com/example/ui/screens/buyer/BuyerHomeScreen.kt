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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Category
import com.example.ui.MarketplaceViewModel
import com.example.ui.components.ProductCard
import com.example.ui.components.WholesalerShopCard
import com.example.ui.theme.AdjameGold
import com.example.ui.theme.AdjameGreenContainer
import com.example.ui.theme.AdjameGreenPrimary
import com.example.ui.theme.AdjameOrangeSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyerHomeScreen(
    viewModel: MarketplaceViewModel,
    modifier: Modifier = Modifier
) {
    val categories by viewModel.categories.collectAsState()
    val products by viewModel.products.collectAsState()
    val shops by viewModel.shops.collectAsState()
    val favorites by viewModel.favoriteProductIds.collectAsState()
    val shopFavorites by viewModel.favoriteShopIds.collectAsState()

    val sectors = listOf("Tous les secteurs", "Forum", "Black Market", "Marché Gouro", "Roxy", "Dallas")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("screen_buyer_home"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Barre de Recherche Rapide
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AdjameGreenPrimary)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.navigateTo("buyer_search") }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Rechercher",
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Rechercher un produit, un carton, un grossiste...",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Bannière "Arrivages Conteneurs Adjamé"
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AdjameGreenContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = null,
                        tint = AdjameGreenPrimary,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Arrivages Conteneurs du Jour",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = AdjameGreenPrimary
                        )
                        Text(
                            text = "Déstockage direct au Forum & Black Market sans intermédiaire.",
                            fontSize = 12.sp,
                            color = Color(0xFF2C3E50)
                        )
                    }
                }
            }
        }

        // Catégories Marchandes
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Filières Marchandes",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    FilterChip(
                        selected = viewModel.selectedCategoryFilter.value == cat.id,
                        onClick = {
                            if (viewModel.selectedCategoryFilter.value == cat.id) {
                                viewModel.selectedCategoryFilter.value = null
                            } else {
                                viewModel.selectedCategoryFilter.value = cat.id
                                viewModel.navigateTo("buyer_search")
                            }
                        },
                        label = { Text(cat.name, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AdjameGreenPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Grossistes Recommandés ("Vérifiés sur le Terrain")
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Grossistes Certifiés",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Vérifié",
                        tint = AdjameGold,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(
                    text = "Voir tout",
                    color = AdjameGreenPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { viewModel.navigateTo("buyer_search") }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(shops) { shop ->
                    WholesalerShopCard(
                        shop = shop,
                        isFavorite = shopFavorites.contains(shop.id),
                        onShopClick = { viewModel.openShopDetail(shop.id) },
                        onFavoriteToggle = { viewModel.toggleFavoriteShop(shop.id) },
                        modifier = Modifier.width(280.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Produits en Vedette (Vente au Carton)
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Articles en Vedette (Prix au Carton)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Tarifs dégressifs applicables selon les quantités commandées.",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        // Grille de Produits (2 par 2 en affichage par lignes)
        val productChunks = products.chunked(2)
        items(productChunks) { chunk ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for (prod in chunk) {
                    ProductCard(
                        product = prod,
                        isFavorite = favorites.contains(prod.id),
                        onProductClick = { viewModel.openProductDetail(prod.id) },
                        onFavoriteToggle = { viewModel.toggleFavoriteProduct(prod.id) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (chunk.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
