package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.data.local.model.PriceTier

/**
 * Entité représentant un produit de gros à Adjamé mis en cache local.
 * Comporte les spécifications B2B (conditionnement, MOQ, paliers dégressifs).
 */
@Entity(
    tableName = "products",
    indices = [
        Index(value = ["categoryId"]),
        Index(value = ["sellerId"]),
        Index(value = ["marketSector"])
    ]
)
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val categoryId: String,
    val sellerId: String,
    val name: String,
    val description: String,
    val packaging: String, // ex: "Carton de 24 pcs", "Douzaine", "Sac de 50 kg"
    val minOrderQuantity: Int = 1, // Quantité minimale de commande (MOQ)
    val basePrice: Double, // Prix indicatif unitaire en FCFA (par carton ou paquet)
    val priceTiers: List<PriceTier> = emptyList(), // Paliers dégressifs de prix
    val stockStatus: String = "IN_STOCK", // IN_STOCK, LOW_STOCK, ARRIVAGE, OUT_OF_STOCK
    val imageUrl: String = "",
    val thumbnailBase64: String? = null, // Prévisualisation basse résolution ultra-légère hors-ligne
    val marketSector: String, // "Forum Adjamé", "Black Market", "Marché Gouro", "Dallas", "Roxy"
    val isVerifiedSeller: Boolean = true,
    val isPromoted: Boolean = false,
    val cachedAt: Long = System.currentTimeMillis()
)
