package com.example.model

import com.example.data.local.model.PriceTier

data class Category(
    val id: String,
    val name: String,
    val slug: String,
    val iconKey: String,
    val description: String,
    val productCount: Int = 0
)

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val categoryId: String,
    val subCategory: String = "",
    val sellerId: String,
    val sellerName: String,
    val sellerSector: String,
    val images: List<String> = emptyList(),
    val packaging: String, // ex: "Carton de 24 flacons", "Douzaine", "Sac 50kg"
    val minOrderQuantity: Int = 1,
    val basePrice: Double, // en FCFA
    val priceTiers: List<PriceTier> = emptyList(),
    val stockStatus: String = "IN_STOCK", // IN_STOCK, LOW_STOCK, ARRIVAGE, OUT_OF_STOCK
    val stockQuantity: Int = 100, // En cartons/unités
    val variants: List<String> = emptyList(), // ex: ["150g", "250g"] ou ["Noir", "Blanc"]
    val isVerifiedSeller: Boolean = true,
    val isPromoted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class WholesalerShop(
    val id: String,
    val name: String,
    val description: String,
    val ownerName: String,
    val phone: String,
    val whatsapp: String,
    val address: String,
    val marketSector: String, // Forum, Black Market, Marché Gouro, Roxy, Dallas
    val landmarks: String, // Repère visuel ivoirien : "Face pharmacie Mirador, couloir B"
    val hours: String = "7h30 - 18h00",
    val categories: List<String> = emptyList(),
    val isVerified: Boolean = true,
    val verificationBadge: String = "TERRAIN_VERIFIED", // TERRAIN_VERIFIED, CERTIFIED_IMPORTATEUR, STANDARD
    val rating: Float = 4.8f,
    val reviewCount: Int = 84,
    val transactionCount: Int = 240,
    val responseTimeMinutes: Int = 15,
    val latitude: Double = 5.3544,
    val longitude: Double = -4.0253,
    val isActive: Boolean = true,
    val coverImageUrl: String = "",
    val notesCount: Int = 0
)
