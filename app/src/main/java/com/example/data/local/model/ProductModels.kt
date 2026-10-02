package com.example.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.SellerContactEntity

/**
 * Représente un palier de prix dégressif pour l'achat en gros (B2B).
 * Exemple : 1 à 5 cartons = 18 000 FCFA / carton ; 6 à 20 cartons = 16 500 FCFA / carton.
 */
data class PriceTier(
    val minQuantity: Int,
    val maxQuantity: Int? = null,
    val unitPriceFcfa: Double,
    val label: String = ""
)

/**
 * Relation Room combinant un produit et la boutique / grossiste qui le propose.
 * Permet un affichage complet en mode hors-ligne sans requêtes supplémentaires.
 */
data class ProductWithSeller(
    @Embedded
    val product: ProductEntity,

    @Relation(
        parentColumn = "sellerId",
        entityColumn = "sellerId"
    )
    val seller: SellerContactEntity?
)
