package com.example.ui.screens.buyer

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MarketplaceViewModel
import com.example.ui.components.ProductCard
import com.example.ui.components.callPhone
import com.example.ui.components.openWhatsApp
import com.example.ui.theme.AdjameGold
import com.example.ui.theme.AdjameGreenContainer
import com.example.ui.theme.AdjameGreenPrimary
import com.example.model.Product
import com.example.model.WholesalerShop
import com.example.ui.theme.AdjameOrangeSecondary
import com.example.ui.theme.AdjameWhatsApp

@Composable
fun BuyerShopDetailScreen(
    viewModel: MarketplaceViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val shopId by viewModel.selectedShopId.collectAsState()
    val shop = viewModel.getShopById(shopId ?: "")
    val products by viewModel.products.collectAsState()
    val shopProducts = products.filter { it.sellerId == shopId }
    val favorites by viewModel.favoriteProductIds.collectAsState()
    val shopFavorites by viewModel.favoriteShopIds.collectAsState()
    val sellerNotes by viewModel.sellerPrivateNotes.collectAsState()

    var noteText by remember(shopId) {
        mutableStateOf(sellerNotes[shopId] ?: "")
    }

    if (shop == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Boutique introuvable")
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("screen_buyer_shop_detail"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // En-tête Vitrine Grossiste
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AdjameGreenPrimary)
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AdjameGold
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Verified, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("VÉRIFIÉ TERRAIN", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }

                        IconButton(onClick = { viewModel.toggleFavoriteShop(shop.id) }) {
                            Icon(
                                imageVector = if (shopFavorites.contains(shop.id)) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favori",
                                tint = if (shopFavorites.contains(shop.id)) Color.Red else Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = shop.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "Gérant : ${shop.ownerName}",
                        fontSize = 13.sp,
                        color = Color(0xFFD4EFDF)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = AdjameOrangeSecondary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${shop.address} (${shop.marketSector})",
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }

                    Text(
                        text = "Repère : ${shop.landmarks}",
                        fontSize = 12.sp,
                        color = Color(0xFFE8F8F5),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Boutons Appel direct & WhatsApp
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { callPhone(context, shop.phone) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            modifier = Modifier.weight(1f).height(42.dp)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null, tint = AdjameGreenPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Appeler", color = AdjameGreenPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        Button(
                            onClick = {
                                val msg = "Bonjour ${shop.name}, je vous contacte depuis Adjamé Market pour négocier une commande de gros."
                                openWhatsApp(context, shop.whatsapp, msg)
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AdjameWhatsApp),
                            modifier = Modifier.weight(1f).height(42.dp)
                        ) {
                            Text("WhatsApp", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Section Notes Privées Acheteur (ex: "Demander le commis Salif")
        item {
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Note, contentDescription = null, tint = AdjameGreenPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Ma Note Personnelle Privée sur ce Grossiste :",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = AdjameGreenPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = noteText,
                        onValueChange = { noteText = it },
                        placeholder = { Text("Ex: Demander le commis Salif pour 5% de remise...", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = { viewModel.saveSellerNote(shop.id, noteText) },
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AdjameGreenPrimary),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Enregistrer ma note", fontSize = 11.sp)
                    }
                }
            }
        }

        // Catalogue du Grossiste
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Catalogue de la Boutique (${shopProducts.size} articles)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        val chunks = shopProducts.chunked(2)
        items(chunks) { chunk ->
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
