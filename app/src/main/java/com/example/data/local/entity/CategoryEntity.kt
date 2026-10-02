package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entité représentant une catégorie de produits d'Adjamé en cache local.
 * Permet un chargement instantané sans solliciter la bande passante réseau.
 */
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val slug: String,
    val iconKey: String,
    val description: String,
    val displayOrder: Int = 0,
    val productCount: Int = 0,
    val cachedAt: Long = System.currentTimeMillis()
)
