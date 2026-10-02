package com.example.ui.screens.buyer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.MarketplaceViewModel
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
fun BuyerProductDetailScreen(
    viewModel: MarketplaceViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val selectedId by viewModel.selectedProductId.collectAsState()
    val product = viewModel.getProductById(selectedId ?: "")
    val shop = product?.let { viewModel.getShopById(it.sellerId) }

    if (product == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Produit introuvable")
        }
        return
    }

    var quantity by remember { mutableIntStateOf(product.minOrderQuantity) }

    // Calculer le prix unitaire selon le palier atteint
    val currentUnitPrice = remember(quantity, product) {
        val matchingTier = product.priceTiers.lastOrNull { tier ->
            quantity >= tier.minQuantity && (tier.maxQuantity == null || quantity <= tier.maxQuantity)
        }
        matchingTier?.unitPriceFcfa ?: product.basePrice
    }

    val totalAmount = currentUnitPrice * quantity

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("screen_buyer_product_detail")
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Image principale
            AsyncImage(
                model = product.images.firstOrNull(),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .background(Color.LightGray)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                // Secteur & Conditionnement
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = AdjameGreenContainer
                    ) {
                        Text(
                            text = product.sellerSector,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AdjameGreenPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF2F4F4)
                    ) {
                        Text(
                            text = product.packaging,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF2C3E50),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Titre
                Text(
                    text = product.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Prix unitaire actuel
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "${currentUnitPrice.toLong()} FCFA",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AdjameOrangeSecondary
                    )
                    Text(
                        text = " / carton",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }

                Text(
                    text = "Quantité minimale de commande : ${product.minOrderQuantity} carton(s)",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Grille des Paliers Dégressifs
                if (product.priceTiers.isNotEmpty()) {
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Grille des tarifs dégressifs de gros :",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = AdjameGreenPrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            for (tier in product.priceTiers) {
                                val isActiveTier = quantity >= tier.minQuantity && 
                                    (tier.maxQuantity == null || quantity <= tier.maxQuantity)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            if (isActiveTier) AdjameGreenContainer else Color.Transparent,
                                            RoundedCornerShape(6.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = tier.label,
                                        fontSize = 12.sp,
                                        fontWeight = if (isActiveTier) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isActiveTier) AdjameGreenPrimary else Color.Black
                                    )
                                    if (isActiveTier) {
                                        Text(
                                            text = "Appliqué ✓",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AdjameGreenPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Sélecteur de Quantité
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Quantité à commander :", fontSize = 12.sp, color = Color.Gray)
                            Text(
                                text = "Total : ${totalAmount.toLong()} FCFA",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = AdjameGreenPrimary
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { if (quantity > product.minOrderQuantity) quantity-- },
                                enabled = quantity > product.minOrderQuantity,
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color(0xFFEAEDED), RoundedCornerShape(8.dp))
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Moins")
                            }

                            Text(
                                text = "$quantity",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            IconButton(
                                onClick = { quantity++ },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color(0xFFEAEDED), RoundedCornerShape(8.dp))
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Plus")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Fiche Vendeur d'Adjamé
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { shop?.let { viewModel.openShopDetail(it.id) } }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = product.sellerName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    if (product.isVerifiedSeller) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            Icons.Default.Verified,
                                            contentDescription = "Boutique Vérifiée",
                                            tint = AdjameGold,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = shop?.address ?: product.sellerSector,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                            OutlinedButton(
                                onClick = { shop?.let { viewModel.openShopDetail(it.id) } },
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text("Boutique", fontSize = 11.sp)
                            }
                        }

                        if (shop != null) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = AdjameOrangeSecondary, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = shop.landmarks,
                                    fontSize = 11.sp,
                                    color = Color(0xFF34495E)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text("Description du produit", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.description,
                    fontSize = 13.sp,
                    color = Color(0xFF2C3E50),
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Signaler la boutique
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.submitReport(
                                shopId = product.sellerId,
                                shopName = product.sellerName,
                                reason = "PRIX_TROMPEUR",
                                description = "Signalement depuis la fiche du produit ${product.name}"
                            )
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Flag, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Signaler un problème sur ce produit ou ce grossiste", color = Color.Gray, fontSize = 12.sp)
                }
            }
        }

        // Barre d'Action Fixe en Bas
        Surface(
            shadowElevation = 8.dp,
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Bouton WhatsApp direct
                Button(
                    onClick = {
                        val msg = "Bonjour ${product.sellerName}, j'ai vu votre article ${product.name} sur Adjamé Market. Je souhaite commander $quantity carton(s) au prix de ${currentUnitPrice.toLong()} FCFA/ctn (Total: ${totalAmount.toLong()} FCFA). Est-ce disponible aujourd'hui à votre magasin ?"
                        openWhatsApp(context, shop?.whatsapp ?: "+2250708091011", msg)
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AdjameWhatsApp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("btn_whatsapp_negotiate")
                ) {
                    Text("Négocier sur WhatsApp", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                }

                // Bouton Ajouter au Panier
                Button(
                    onClick = {
                        viewModel.addToCart(product, quantity)
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AdjameGreenPrimary),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("btn_add_to_cart")
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ajouter au Panier", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                }
            }
        }
    }
}
