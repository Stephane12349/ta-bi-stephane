package com.example.ui.screens.wholesaler

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.model.PriceTier
import com.example.model.OrderStatus
import com.example.ui.MarketplaceViewModel
import com.example.ui.theme.AdjameGold
import com.example.ui.theme.AdjameGreenContainer
import com.example.ui.theme.AdjameGreenPrimary
import com.example.ui.theme.AdjameOrangeSecondary
import com.example.ui.theme.AdjameSuccess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WholesalerAddProductScreen(
    viewModel: MarketplaceViewModel,
    modifier: Modifier = Modifier
) {
    val categories by viewModel.categories.collectAsState()

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf(categories.firstOrNull()?.id ?: "cat_cosmetics") }
    var packaging by remember { mutableStateOf("Carton de 24 pièces") }
    var minOrderQty by remember { mutableStateOf("2") }
    var basePrice by remember { mutableStateOf("25000") }
    var stockQty by remember { mutableStateOf("50") }
    var tierPrice1 by remember { mutableStateOf("22500") } // Prix dès 5 cartons
    var tierQty1 by remember { mutableStateOf("5") }
    var imageUrl by remember { mutableStateOf("https://images.unsplash.com/photo-1584917865442-de89df76afd3?w=400") }

    var categoryDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("screen_wholesaler_add_product")
    ) {
        Text(
            text = "Publier un Nouvel Arrivage",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            text = "Ajout express en moins de 60 secondes pour les commerçants d'Adjamé.",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Nom du produit
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom de l'article / du carton *") },
                    placeholder = { Text("Ex: Savon Kanza Éclaircissant") },
                    modifier = Modifier.fillMaxWidth().testTag("input_product_name")
                )

                // Sélecteur Catégorie
                ExposedDropdownMenuBox(
                    expanded = categoryDropdownExpanded,
                    onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded }
                ) {
                    val currentCatName = categories.find { it.id == selectedCategoryId }?.name ?: "Choisir une catégorie"
                    OutlinedTextField(
                        value = currentCatName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Catégorie *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryDropdownExpanded,
                        onDismissRequest = { categoryDropdownExpanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.name) },
                                onClick = {
                                    selectedCategoryId = cat.id
                                    categoryDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Conditionnement
                OutlinedTextField(
                    value = packaging,
                    onValueChange = { packaging = it },
                    label = { Text("Conditionnement de gros *") },
                    placeholder = { Text("Ex: Carton de 24 pièces, Ballot de 10 pcs, Sac 50kg") },
                    modifier = Modifier.fillMaxWidth()
                )

                // MOQ & Stock
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = minOrderQty,
                        onValueChange = { minOrderQty = it },
                        label = { Text("Min. Commande (MOQ) *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = stockQty,
                        onValueChange = { stockQty = it },
                        label = { Text("Stock initial (cartons)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Prix de base
                OutlinedTextField(
                    value = basePrice,
                    onValueChange = { basePrice = it },
                    label = { Text("Prix de base (FCFA / carton) *") },
                    placeholder = { Text("Ex: 25000") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().testTag("input_product_price")
                )

                // Palier dégressif
                Text(
                    text = "Palier de remise sur gros volume :",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = AdjameGreenPrimary
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = tierQty1,
                        onValueChange = { tierQty1 = it },
                        label = { Text("Dès X cartons") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = tierPrice1,
                        onValueChange = { tierPrice1 = it },
                        label = { Text("Prix remisé (FCFA)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description courte (optionnelle)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Bouton de Soumission
                Button(
                    onClick = {
                        val parsedBase = basePrice.toDoubleOrNull() ?: 20000.0
                        val parsedMoq = minOrderQty.toIntOrNull() ?: 1
                        val parsedStock = stockQty.toIntOrNull() ?: 20
                        val parsedTierQty = tierQty1.toIntOrNull() ?: 5
                        val parsedTierPrice = tierPrice1.toDoubleOrNull() ?: (parsedBase * 0.9)

                        val tiers = listOf(
                            PriceTier(
                                minQuantity = parsedMoq,
                                maxQuantity = parsedTierQty - 1,
                                unitPriceFcfa = parsedBase,
                                label = "$parsedMoq à ${parsedTierQty - 1} cartons : ${parsedBase.toLong()} F/ctn"
                            ),
                            PriceTier(
                                minQuantity = parsedTierQty,
                                maxQuantity = null,
                                unitPriceFcfa = parsedTierPrice,
                                label = "$parsedTierQty cartons et + : ${parsedTierPrice.toLong()} F/ctn"
                            )
                        )

                        viewModel.addProduct(
                            name = name.ifBlank { "Nouvel Arrivage Adjamé" },
                            description = description.ifBlank { "Marchandise de premier choix disponible au magasin d'Adjamé." },
                            categoryId = selectedCategoryId,
                            packaging = packaging,
                            minOrderQuantity = parsedMoq,
                            basePrice = parsedBase,
                            priceTiers = tiers,
                            stockQuantity = parsedStock,
                            imageUrl = imageUrl
                        )
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AdjameGreenPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_submit_product")
                ) {
                    Text("Mettre en vente immédiatement", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun WholesalerOrdersScreen(
    viewModel: MarketplaceViewModel,
    modifier: Modifier = Modifier
) {
    val orders by viewModel.orders.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("screen_wholesaler_orders"),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 90.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Commandes Reçues (${orders.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = "Pilotez la préparation des lots et validez les retraits par Code PIN client.",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        items(orders) { order ->
            var pinInput by remember { mutableStateOf("") }
            var pinError by remember { mutableStateOf(false) }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = order.id,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = AdjameGreenPrimary
                        )
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = when (order.status) {
                                OrderStatus.COMPLETED -> Color(0xFFEAFAF1)
                                OrderStatus.READY -> Color(0xFFE8F8F5)
                                else -> Color(0xFFFEF9E7)
                            }
                        ) {
                            Text(
                                text = order.status.label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (order.status == OrderStatus.COMPLETED) AdjameSuccess else AdjameOrangeSecondary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Text(
                        text = "Acheteur : ${order.buyerName} (${order.buyerPhone})",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Text(
                        text = "Montant : ${order.totalAmount.toLong()} FCFA • Mode : ${if (order.deliveryType == "CLICK_AND_COLLECT") "Retrait magasin" else "Gare"}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Actions de Transition d'État
                    when (order.status) {
                        OrderStatus.PENDING -> {
                            Button(
                                onClick = { viewModel.updateOrderStatus(order.id, OrderStatus.CONFIRMED) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AdjameGreenPrimary),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Confirmer la commande", fontWeight = FontWeight.Bold)
                            }
                        }
                        OrderStatus.CONFIRMED -> {
                            Button(
                                onClick = { viewModel.updateOrderStatus(order.id, OrderStatus.PREPARING) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2980B9)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Mettre en préparation au dépôt", fontWeight = FontWeight.Bold)
                            }
                        }
                        OrderStatus.PREPARING -> {
                            Button(
                                onClick = { viewModel.updateOrderStatus(order.id, OrderStatus.READY) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AdjameOrangeSecondary),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Marquer prête pour retrait client", fontWeight = FontWeight.Bold)
                            }
                        }
                        OrderStatus.READY -> {
                            Column(modifier = Modifier.padding(top = 4.dp)) {
                                Text(
                                    text = "Saisir le Code PIN à 4 chiffres du client pour délivrer :",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AdjameGreenPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    OutlinedTextField(
                                        value = pinInput,
                                        onValueChange = {
                                            pinInput = it.take(4)
                                            pinError = false
                                        },
                                        placeholder = { Text("Code à 4 chiffres") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.weight(1f).height(50.dp),
                                        singleLine = true
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = {
                                            if (pinInput == order.pickupPinCode) {
                                                viewModel.updateOrderStatus(order.id, OrderStatus.COMPLETED)
                                                pinError = false
                                            } else {
                                                pinError = true
                                                viewModel.showSnackbar("Code PIN erroné. Veuillez redemander le code au client.")
                                            }
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = AdjameSuccess),
                                        modifier = Modifier.height(50.dp)
                                    ) {
                                        Text("Valider", fontWeight = FontWeight.Bold)
                                    }
                                }
                                if (pinError) {
                                    Text("Code erroné !", color = Color.Red, fontSize = 11.sp, modifier = Modifier.padding(top = 2.dp))
                                }
                            }
                        }
                        OrderStatus.COMPLETED -> {
                            Text(
                                text = "Marchandise remise au client ✓",
                                color = AdjameSuccess,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}

@Composable
fun WholesalerShopProfileScreen(
    viewModel: MarketplaceViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    var businessName by remember { mutableStateOf(currentUser.businessName) }
    var address by remember { mutableStateOf("Adjamé Forum, Niveau 1, Magasin B-14") }
    var landmarks by remember { mutableStateOf("Face pharmacie Mirador, couloir B, porte 3") }
    var whatsapp by remember { mutableStateOf("+2250708091011") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("screen_wholesaler_profile")
    ) {
        Text(
            text = "Gestion de Ma Boutique Physique",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            text = "Informations visibles par tous les commerçants de Côte d'Ivoire.",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Badge Statut de Vérification
        Card(
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = AdjameGreenContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Verified, contentDescription = null, tint = AdjameGold, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Statut : BOUTIQUE VÉRIFIÉE TERRAIN", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AdjameGreenPrimary)
                    Text("Audit physique réalisé au Forum d'Adjamé.", fontSize = 11.sp, color = Color(0xFF2C3E50))
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = businessName,
                    onValueChange = { businessName = it },
                    label = { Text("Nom commercial de la boutique") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Secteur et numéro de magasin") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = landmarks,
                    onValueChange = { landmarks = it },
                    label = { Text("Repères visuels pour orienter les acheteurs") },
                    placeholder = { Text("Ex: Face pharmacie Mirador, couloir B") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = whatsapp,
                    onValueChange = { whatsapp = it },
                    label = { Text("Numéro WhatsApp de négociation") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        viewModel.updateProfile(currentUser.fullName, businessName, address, false)
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AdjameGreenPrimary),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Enregistrer les modifications", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
