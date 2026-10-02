package com.example.ui.screens.buyer

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OrderStatus
import com.example.ui.MarketplaceViewModel
import com.example.ui.components.callPhone
import com.example.ui.components.openWhatsApp
import com.example.ui.theme.AdjameGreenContainer
import com.example.ui.theme.AdjameGreenPrimary
import com.example.ui.theme.AdjameOrangeSecondary
import com.example.ui.theme.AdjameSuccess
import com.example.ui.theme.AdjameWhatsApp

@Composable
fun BuyerOrderDetailScreen(
    viewModel: MarketplaceViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val selectedId by viewModel.selectedOrderId.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val order = orders.find { it.id == selectedId }

    if (order == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Commande introuvable")
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("screen_order_detail")
    ) {
        Text(
            text = "Détail de la Commande",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            text = "Numéro : ${order.id}",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Code PIN Grand Format si pas encore retirée
        if (order.status != OrderStatus.COMPLETED && order.status != OrderStatus.CANCELLED) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AdjameGreenContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "CODE PIN DE RETRAIT MAGASIN",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AdjameGreenPrimary
                    )
                    Text(
                        text = order.pickupPinCode,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AdjameGreenPrimary,
                        letterSpacing = 6.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = "Donnez ce code au grossiste pour valider la délivrance des cartons.",
                        fontSize = 11.sp,
                        color = Color(0xFF2C3E50),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Stepper de Statut
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Statut : ${order.status.label}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = when (order.status) {
                        OrderStatus.COMPLETED, OrderStatus.READY -> AdjameSuccess
                        OrderStatus.PENDING, OrderStatus.CONFIRMED, OrderStatus.PREPARING -> AdjameGreenPrimary
                        else -> Color.Red
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                val steps = listOf("En attente", "Confirmée", "En préparation", "Prête au retrait", "Terminée")
                val currentStepIndex = when (order.status) {
                    OrderStatus.PENDING -> 0
                    OrderStatus.CONFIRMED -> 1
                    OrderStatus.PREPARING -> 2
                    OrderStatus.READY -> 3
                    OrderStatus.COMPLETED -> 4
                    else -> 0
                }

                steps.forEachIndexed { index, step ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (index <= currentStepIndex) AdjameGreenPrimary else Color(0xFFEAEDED),
                            modifier = Modifier.size(20.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${index + 1}",
                                    fontSize = 10.sp,
                                    color = if (index <= currentStepIndex) Color.White else Color.Gray,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = step,
                            fontSize = 13.sp,
                            fontWeight = if (index == currentStepIndex) FontWeight.Bold else FontWeight.Normal,
                            color = if (index <= currentStepIndex) Color.Black else Color.Gray
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Fiche Grossiste & Raccourcis
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("Fournisseur : ${order.sellerName}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(
                    text = "Secteur : ${order.sellerSector}",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 2.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { callPhone(context, "+2250708091011") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Appeler", fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            val msg = "Bonjour ${order.sellerName}, je vous contacte concernant ma commande ${order.id} sur Adjamé Market."
                            openWhatsApp(context, "+2250708091011", msg)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AdjameWhatsApp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("WhatsApp", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Liste des Articles Commandés
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("Articles commandés", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))

                order.items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = item.productName, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text(text = "${item.quantity} × ${item.packaging}", fontSize = 11.sp, color = Color.Gray)
                        }
                        Text(
                            text = "${item.subtotal.toLong()} FCFA",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = AdjameOrangeSecondary
                        )
                    }
                    Divider(color = Color(0xFFF2F4F4), thickness = 1.dp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("TOTAL RÉSERVATION :", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                    Text(
                        text = "${order.totalAmount.toLong()} FCFA",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = AdjameOrangeSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Bouton de signalement
        OutlinedButton(
            onClick = {
                viewModel.submitReport(
                    shopId = order.sellerId,
                    shopName = order.sellerName,
                    reason = "LITIGE_COMMANDE",
                    description = "Litige signalé sur la commande ${order.id}"
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Flag, contentDescription = null, tint = Color.Red, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Signaler un problème sur cette commande", color = Color.Red, fontSize = 12.sp)
        }
    }
}
