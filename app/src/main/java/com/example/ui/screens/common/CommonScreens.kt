package com.example.ui.screens.common

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DataSaverOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserRole
import com.example.ui.MarketplaceViewModel
import com.example.ui.theme.AdjameGold
import com.example.ui.theme.AdjameGreenContainer
import com.example.ui.theme.AdjameGreenPrimary
import com.example.ui.theme.AdjameOrangeSecondary

@Composable
fun UserProfileScreen(
    viewModel: MarketplaceViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()

    var name by remember(currentUser) { mutableStateOf(currentUser.fullName) }
    var business by remember(currentUser) { mutableStateOf(currentUser.businessName) }
    var commune by remember(currentUser) { mutableStateOf(currentUser.commune) }
    var dataSaver by remember(currentUser) { mutableStateOf(currentUser.dataSaverEnabled) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("screen_user_profile")
    ) {
        // En-tête Profil
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = AdjameGreenPrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentUser.fullName.take(1).uppercase(),
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp,
                        color = AdjameGreenPrimary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = currentUser.fullName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color.White
                    )
                    Text(
                        text = currentUser.phone,
                        fontSize = 13.sp,
                        color = Color(0xFFD4EFDF)
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = AdjameGold,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "RÔLE : ${currentUser.role.name}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sélecteur de Rôles
        Text("Bascule Rapide d'Espace (Démonstration & Test)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { viewModel.switchRole(UserRole.BUYER) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentUser.role == UserRole.BUYER) AdjameGreenPrimary else Color(0xFFEAEDED)
                ),
                modifier = Modifier.weight(1f).height(44.dp)
            ) {
                Text(
                    "Acheteur",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (currentUser.role == UserRole.BUYER) Color.White else Color.Black
                )
            }

            Button(
                onClick = { viewModel.switchRole(UserRole.WHOLESALER) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentUser.role == UserRole.WHOLESALER) AdjameOrangeSecondary else Color(0xFFEAEDED)
                ),
                modifier = Modifier.weight(1f).height(44.dp)
            ) {
                Text(
                    "Grossiste",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (currentUser.role == UserRole.WHOLESALER) Color.White else Color.Black
                )
            }

            Button(
                onClick = { viewModel.switchRole(UserRole.ADMIN) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentUser.role == UserRole.ADMIN) AdjameGold else Color(0xFFEAEDED)
                ),
                modifier = Modifier.weight(1f).height(44.dp)
            ) {
                Text(
                    "Admin",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Édition du Profil
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Paramètres Commerciaux", fontWeight = FontWeight.Bold, fontSize = 14.sp)

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom complet") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = business,
                    onValueChange = { business = it },
                    label = { Text("Nom du commerce / boutique") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = commune,
                    onValueChange = { commune = it },
                    label = { Text("Commune / Ville principale") },
                    placeholder = { Text("Ex: Yopougon, Cocody, Bouaké...") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        viewModel.updateProfile(name, business, commune, dataSaver)
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AdjameGreenPrimary),
                    modifier = Modifier.fillMaxWidth().height(44.dp)
                ) {
                    Text("Sauvegarder", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mode Économiseur de Données (Low-Bandwidth ivoirien)
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
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Mode Économiseur de Données",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Compresse les images et privilégie le cache local pour économiser vos forfaits internet.",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                    Switch(
                        checked = dataSaver,
                        onCheckedChange = {
                            dataSaver = it
                            viewModel.updateProfile(name, business, commune, it)
                        },
                        colors = SwitchDefaults.colors(checkedTrackColor = AdjameGreenPrimary)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { viewModel.showSnackbar("Base de données Room synchronisée avec succès.") },
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2F4F4)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Storage, contentDescription = null, tint = AdjameGreenPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Synchroniser le cache hors-ligne", color = AdjameGreenPrimary, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun NotificationsScreen(
    viewModel: MarketplaceViewModel,
    modifier: Modifier = Modifier
) {
    val notifications by viewModel.notifications.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("screen_notifications"),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 90.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Centre de Notifications (${notifications.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = "Alertes d'arrivages, confirmations de commandes et cotations.",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        if (notifications.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                    Text("Aucune notification pour le moment.", color = Color.Gray)
                }
            }
        } else {
            items(notifications) { notif ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = AdjameGreenContainer,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Notifications, contentDescription = null, tint = AdjameGreenPrimary, modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = notif.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(text = notif.message, fontSize = 12.sp, color = Color(0xFF2C3E50), modifier = Modifier.padding(top = 2.dp))
                        }
                    }
                }
            }
        }
    }
}
