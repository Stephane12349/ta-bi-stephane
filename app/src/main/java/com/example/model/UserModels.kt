package com.example.model

enum class UserRole {
    BUYER,       // Revendeur, Vendeur WhatsApp/TikTok, Détaillant
    WHOLESALER,  // Grossiste, Importateur au Forum/Dallas/Roxy
    ADMIN        // Administrateur plateforme & Superviseur terrain
}

data class UserProfile(
    val id: String,
    val phone: String,
    val fullName: String,
    val email: String = "",
    val role: UserRole = UserRole.BUYER,
    val businessName: String = "",
    val commune: String = "Adjamé",
    val isVerified: Boolean = false,
    val dataSaverEnabled: Boolean = false,
    val avatarUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
