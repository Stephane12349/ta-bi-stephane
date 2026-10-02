package com.example.ui.screens.admin

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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MarketplaceViewModel
import com.example.ui.screens.wholesaler.MetricCard
import com.example.ui.theme.AdjameGold
import com.example.ui.theme.AdjameGreenPrimary
import com.example.ui.theme.AdjameOrangeSecondary
import com.example.ui.theme.AdjameSuccess

@Composable
fun AdminDashboardScreen(
    viewModel: MarketplaceViewModel,
    modifier: Modifier = Modifier
) {
    val shops by viewModel.shops.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val reports by viewModel.reports.collectAsState()
    val products by viewModel.products.collectAsState()

    val totalGMV = orders.sumOf { it.totalAmount }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("screen_admin_dashboard"),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // En-tête Superviseur
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B263B)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = AdjameGold, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Console Superviseur Adjamé",
                            fontSize = 14.sp,
                            color = AdjameGold,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Pilotage Marketplace B2B",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = "Zone de couverture : Forum, Black Market, Gouro, Roxy, Dallas",
                        fontSize = 12.sp,
                        color = Color(0xFFD5D8DC)
                    )
                }
            }
        }

        // Cartes de Métriques
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricCard(
                    title = "GMV Intermédié",
                    value = "${totalGMV.toLong()} F",
                    icon = Icons.Default.Payments,
                    color = AdjameGreenPrimary,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Boutiques Actives",
                    value = "${shops.size}",
                    icon = Icons.Default.Store,
                    color = AdjameOrangeSecondary,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricCard(
                    title = "Commandes Totales",
                    value = "${orders.size}",
                    icon = Icons.Default.Check,
                    color = AdjameSuccess,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Litiges Signalés",
                    value = "${reports.size}",
                    icon = Icons.Default.Flag,
                    color = Color.Red,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Raccourcis d'actions
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = { viewModel.navigateTo("admin_kyc") },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AdjameGreenPrimary),
                    modifier = Modifier.weight(1f).height(46.dp)
                ) {
                    Text("Valider Grossistes (KYC)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = { viewModel.navigateTo("admin_disputes") },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B)),
                    modifier = Modifier.weight(1f).height(46.dp)
                ) {
                    Text("Litiges (${reports.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun AdminKycScreen(
    viewModel: MarketplaceViewModel,
    modifier: Modifier = Modifier
) {
    val shops by viewModel.shops.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("screen_admin_kyc"),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 90.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Validation Terrain des Grossistes",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = "Attribuez ou révoquez le badge officiel 'Boutique Vérifiée Terrain'.",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        items(shops) { shop ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = shop.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (shop.isVerified) AdjameGold.copy(alpha = 0.2f) else Color(0xFFEAEDED)
                        ) {
                            Text(
                                text = if (shop.isVerified) "VÉRIFIÉ TERRAIN ✓" else "NON AUDITÉ",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (shop.isVerified) Color(0xFF7D6608) else Color.Gray,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Text("Gérant : ${shop.ownerName} • Contact : ${shop.phone}", fontSize = 12.sp, color = Color.Gray)
                    Text("Adresse : ${shop.address}", fontSize = 12.sp, color = Color(0xFF2C3E50), modifier = Modifier.padding(top = 2.dp))

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { viewModel.toggleShopVerification(shop.id) },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (shop.isVerified) Color(0xFFC0392B) else AdjameGreenPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (shop.isVerified) "Révoquer la certification" else "Approuver et certifier la boutique",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AdminDisputesScreen(
    viewModel: MarketplaceViewModel,
    modifier: Modifier = Modifier
) {
    val reports by viewModel.reports.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("screen_admin_disputes"),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 90.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Signalements & Litiges (${reports.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = "Traitement des réclamations déposées par les acheteurs et commerçants.",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        if (reports.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Zéro litige en attente. Toutes les boutiques sont conformes.", color = Color.Gray)
                }
            }
        } else {
            items(reports) { report ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Flag, contentDescription = null, tint = Color.Red, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Boutique : ${report.shopName}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Text("Motif : ${report.reason}", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Color(0xFFC0392B), modifier = Modifier.padding(top = 4.dp))
                        Text("Détail : ${report.description}", fontSize = 12.sp, color = Color(0xFF2C3E50), modifier = Modifier.padding(top = 2.dp))
                        Text("Déposé par : ${report.reporterName}", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedButton(
                            onClick = {
                                viewModel.showSnackbar("Litige instruit et clos par l'équipe de sécurité.")
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Clôturer le litige après médiation", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
