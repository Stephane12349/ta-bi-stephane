package com.example.data.local

import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.SellerContactEntity
import com.example.data.local.model.PriceTier

/**
 * Données d'initialisation et de cache hors-ligne pour Adjamé Market.
 * Permet à l'application d'être instantanément fonctionnelle même avec
 * un débit internet très faible ou nul dès le premier lancement.
 */
object SeedData {

    val categories = listOf(
        CategoryEntity(
            id = "cat_cosmetics",
            name = "Beauté & Cosmétiques",
            slug = "beaute-cosmetiques",
            iconKey = "sparkles",
            description = "Savons éclaircissants, lotions corporelles, pommades, parfums et mèches",
            displayOrder = 1,
            productCount = 28
        ),
        CategoryEntity(
            id = "cat_electronics",
            name = "Électronique & High-Tech",
            slug = "electronique-high-tech",
            iconKey = "phone_android",
            description = "Écouteurs, chargeurs rapides, câbles, haut-parleurs Bluetooth et accessoires",
            displayOrder = 2,
            productCount = 42
        ),
        CategoryEntity(
            id = "cat_textile",
            name = "Pagnes & Textiles",
            slug = "pagnes-textiles",
            iconKey = "checkroom",
            description = "Pagnes Wax, Woodin, basin riche, dentelles et prêt-à-porter en gros",
            displayOrder = 3,
            productCount = 35
        ),
        CategoryEntity(
            id = "cat_hardware",
            name = "Quincaillerie & Bazar",
            slug = "quincaillerie-bazar",
            iconKey = "home_repair_service",
            description = "Ustensiles de cuisine, bassines, matériel électrique, cadenas et outillage",
            displayOrder = 4,
            productCount = 50
        ),
        CategoryEntity(
            id = "cat_food",
            name = "Alimentaire & Épices de Gros",
            slug = "alimentaire-epices",
            iconKey = "restaurant",
            description = "Huile de table, riz, bouillon d'assaisonnement, pâte de tomate et conserves",
            displayOrder = 5,
            productCount = 19
        )
    )

    val sellers = listOf(
        SellerContactEntity(
            sellerId = "seller_elhadj_oumar",
            businessName = "Éts El Hadj Oumar & Frères",
            managerName = "El Hadj Oumar Traoré",
            phone = "+2250708091011",
            whatsappNumber = "+2250708091011",
            marketSector = "Forum d'Adjamé, Niveau 1, Magasin B-14",
            landmarks = "Entrée principale face pharmacie Mirador, couloir B",
            verificationBadge = "TERRAIN_VERIFIED",
            rating = 4.9f,
            reviewCount = 142,
            transactionCount = 520,
            averageResponseMinutes = 10,
            isFavorite = true,
            userPrivateNotes = "Fournisseur direct conteneurs de Chine. Demander son commis Salif pour les gros volumes.",
            lastContactedAt = System.currentTimeMillis() - 86400000L
        ),
        SellerContactEntity(
            sellerId = "seller_fanta_cosmetics",
            businessName = "Fanta Beauté Distribution",
            managerName = "Mme Fanta Koné",
            phone = "+2250505123456",
            whatsappNumber = "+2250505123456",
            marketSector = "Adjamé Roxy, Allée Centrale",
            landmarks = "Près de l'ancien cinéma Roxy, porte verte",
            verificationBadge = "TERRAIN_VERIFIED",
            rating = 4.8f,
            reviewCount = 89,
            transactionCount = 310,
            averageResponseMinutes = 15,
            isFavorite = true,
            userPrivateNotes = "Spécialiste savons et crèmes. Prix imbattables à partir de 5 cartons.",
            lastContactedAt = System.currentTimeMillis() - (86400000L * 3)
        ),
        SellerContactEntity(
            sellerId = "seller_diallo_tech",
            businessName = "Diallo High-Tech Adjamé",
            managerName = "M. Amadou Diallo",
            phone = "+2250102030405",
            whatsappNumber = "+2250102030405",
            marketSector = "Black Market Adjamé, Galerie Nord",
            landmarks = "Derrière la mairie d'Adjamé, stand N° 45",
            verificationBadge = "CERTIFIED_IMPORTATEUR",
            rating = 4.7f,
            reviewCount = 67,
            transactionCount = 240,
            averageResponseMinutes = 8,
            isFavorite = false,
            userPrivateNotes = "Très réactif sur WhatsApp. Fait tester les écouteurs avant expédition gare.",
            lastContactedAt = null
        ),
        SellerContactEntity(
            sellerId = "seller_gouro_alimentation",
            businessName = "Comptoir Vivrier & Épicerie du Gouro",
            managerName = "M. Ibrahima Coulibaly",
            phone = "+2250748963214",
            whatsappNumber = "+2250748963214",
            marketSector = "Marché Gouro d'Adjamé",
            landmarks = "Secteur des camions vivriers, Entrepôt C-2",
            verificationBadge = "TERRAIN_VERIFIED",
            rating = 4.6f,
            reviewCount = 53,
            transactionCount = 180,
            averageResponseMinutes = 20,
            isFavorite = false,
            userPrivateNotes = "Prix au sac très avantageux pour les boutiques de quartier.",
            lastContactedAt = null
        )
    )

    val products = listOf(
        ProductEntity(
            id = "prod_savon_kanza",
            categoryId = "cat_cosmetics",
            sellerId = "seller_fanta_cosmetics",
            name = "Savon Kanza Éclaircissant Anti-Taches (Original)",
            description = "Savon de beauté clarifiant aux extraits végétaux. Produit original certifié, emballage scellé avec hologramme.",
            packaging = "Carton de 48 pièces (150g)",
            minOrderQuantity = 1,
            basePrice = 24000.0,
            priceTiers = listOf(
                PriceTier(minQuantity = 1, maxQuantity = 4, unitPriceFcfa = 24000.0, label = "1 à 4 cartons : 24 000 F/ctn (500 F/pièce)"),
                PriceTier(minQuantity = 5, maxQuantity = 19, unitPriceFcfa = 21600.0, label = "5 à 19 cartons : 21 600 F/ctn (450 F/pièce)"),
                PriceTier(minQuantity = 20, maxQuantity = null, unitPriceFcfa = 19200.0, label = "20 cartons et + : 19 200 F/ctn (400 F/pièce)")
            ),
            stockStatus = "IN_STOCK",
            imageUrl = "https://images.unsplash.com/photo-1608248597359-bb5835cf4649?w=400",
            marketSector = "Adjamé Roxy",
            isVerifiedSeller = true,
            isPromoted = true
        ),
        ProductEntity(
            id = "prod_ecouteurs_airpro",
            categoryId = "cat_electronics",
            sellerId = "seller_diallo_tech",
            name = "Écouteurs Sans-Fil TWS Pro Bluetooth 5.3",
            description = "Écouteurs avec boîtier de recharge, réduction de bruit passive et autonomie 18h. Livrés avec câbles de charge.",
            packaging = "Carton de 20 pièces",
            minOrderQuantity = 1,
            basePrice = 38000.0,
            priceTiers = listOf(
                PriceTier(minQuantity = 1, maxQuantity = 4, unitPriceFcfa = 38000.0, label = "1 à 4 cartons : 38 000 F/ctn (1 900 F/u)"),
                PriceTier(minQuantity = 5, maxQuantity = 14, unitPriceFcfa = 34000.0, label = "5 à 14 cartons : 34 000 F/ctn (1 700 F/u)"),
                PriceTier(minQuantity = 15, maxQuantity = null, unitPriceFcfa = 30000.0, label = "15 cartons et + : 30 000 F/ctn (1 500 F/u)")
            ),
            stockStatus = "IN_STOCK",
            imageUrl = "https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=400",
            marketSector = "Black Market Adjamé",
            isVerifiedSeller = true,
            isPromoted = true
        ),
        ProductEntity(
            id = "prod_chargeur_rapide_30w",
            categoryId = "cat_electronics",
            sellerId = "seller_diallo_tech",
            name = "Tête de Chargeur Ultra-Rapide 30W Dual USB-C",
            description = "Adaptateur secteur avec port Type-C et USB classique. Conforme aux normes avec protection contre les surtensions.",
            packaging = "Boîte de 25 unités",
            minOrderQuantity = 2,
            basePrice = 27500.0,
            priceTiers = listOf(
                PriceTier(minQuantity = 2, maxQuantity = 9, unitPriceFcfa = 27500.0, label = "2 à 9 boîtes : 27 500 F/boîte (1 100 F/u)"),
                PriceTier(minQuantity = 10, maxQuantity = null, unitPriceFcfa = 23750.0, label = "10 boîtes et + : 23 750 F/boîte (950 F/u)")
            ),
            stockStatus = "IN_STOCK",
            imageUrl = "https://images.unsplash.com/photo-1583863788434-e58a36330cf0?w=400",
            marketSector = "Black Market Adjamé",
            isVerifiedSeller = true,
            isPromoted = false
        ),
        ProductEntity(
            id = "prod_pagne_woodin",
            categoryId = "cat_textile",
            sellerId = "seller_elhadj_oumar",
            name = "Pagne Imprimé Prestige 6 Yards (Motifs Nouveautés)",
            description = "Tissu 100% coton, motifs éclatants haute tenue au lavage. Arrivage spécial revendeurs et confectionneurs.",
            packaging = "Ballot de 10 pièces (6 yards/pièce)",
            minOrderQuantity = 1,
            basePrice = 95000.0,
            priceTiers = listOf(
                PriceTier(minQuantity = 1, maxQuantity = 4, unitPriceFcfa = 95000.0, label = "1 à 4 ballots : 95 000 F (9 500 F/pièce)"),
                PriceTier(minQuantity = 5, maxQuantity = null, unitPriceFcfa = 88000.0, label = "5 ballots et + : 88 000 F (8 800 F/pièce)")
            ),
            stockStatus = "ARRIVAGE",
            imageUrl = "https://images.unsplash.com/photo-1584917865442-de89df76afd3?w=400",
            marketSector = "Forum d'Adjamé",
            isVerifiedSeller = true,
            isPromoted = true
        ),
        ProductEntity(
            id = "prod_huile_dinor",
            categoryId = "cat_food",
            sellerId = "seller_gouro_alimentation",
            name = "Huile Végétale Raffinée Dinor Bidon 5L",
            description = "Huile de palme raffinée enrichie en Vitamine A. Idéale pour maquis, restaurants et revente au détail.",
            packaging = "Carton de 4 bidons de 5 Litres",
            minOrderQuantity = 3,
            basePrice = 24500.0,
            priceTiers = listOf(
                PriceTier(minQuantity = 3, maxQuantity = 9, unitPriceFcfa = 24500.0, label = "3 à 9 cartons : 24 500 F/ctn"),
                PriceTier(minQuantity = 10, maxQuantity = null, unitPriceFcfa = 23800.0, label = "10 cartons et + : 23 800 F/ctn")
            ),
            stockStatus = "IN_STOCK",
            imageUrl = "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=400",
            marketSector = "Marché Gouro d'Adjamé",
            isVerifiedSeller = true,
            isPromoted = false
        )
    )
}
