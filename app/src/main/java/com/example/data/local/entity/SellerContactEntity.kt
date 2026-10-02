package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entité représentant un grossiste / contact fournisseur enregistré en local.
 * Essentiel pour permettre aux commerçants et revendeurs de consulter leur carnet
 * d'adresses d'Adjamé, retrouver les coordonnées et lancer un appel ou WhatsApp
 * même sans connexion internet active.
 */
@Entity(
    tableName = "seller_contacts",
    indices = [
        Index(value = ["isFavorite"]),
        Index(value = ["marketSector"]),
        Index(value = ["phone"])
    ]
)
data class SellerContactEntity(
    @PrimaryKey
    val sellerId: String,
    val businessName: String, // ex: "Éts El Hadj Oumar & Fils"
    val managerName: String, // ex: "El Hadj Oumar"
    val phone: String, // ex: "+225 07 01 02 03 04"
    val whatsappNumber: String, // Numéro WhatsApp professionnel avec indicatif
    val marketSector: String, // ex: "Forum Adjamé, 1er étage, Magasin B-14"
    val landmarks: String, // Repère visuel ivoirien : "Face pharmacie Mirador, couloir B"
    val verificationBadge: String = "TERRAIN_VERIFIED", // TERRAIN_VERIFIED, CERTIFIED_IMPORTATEUR, STANDARD
    val rating: Float = 4.8f,
    val reviewCount: Int = 0,
    val transactionCount: Int = 0,
    val averageResponseMinutes: Int = 15,
    val isFavorite: Boolean = false, // Vendeur sauvegardé dans les favoris de l'acheteur
    val userPrivateNotes: String = "", // Notes privées de l'acheteur (ex: "Remise de 5% dès 10 cartons")
    val lastContactedAt: Long? = null, // Timestamp du dernier appel ou message
    val cachedAt: Long = System.currentTimeMillis()
)
