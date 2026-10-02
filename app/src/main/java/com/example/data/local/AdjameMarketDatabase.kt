package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.CategoryDao
import com.example.data.local.dao.ProductDao
import com.example.data.local.dao.SellerContactDao
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.SellerContactEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Base de données Room principale pour Adjamé Market.
 * Assure la persistance locale et le cache résilient pour les environnements
 * à faible bande passante (low-bandwidth) ou coupures réseau fréquentes.
 */
@Database(
    entities = [
        CategoryEntity::class,
        ProductEntity::class,
        SellerContactEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AdjameMarketDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun sellerContactDao(): SellerContactDao

    companion object {
        @Volatile
        private var INSTANCE: AdjameMarketDatabase? = null

        fun getInstance(context: Context): AdjameMarketDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AdjameMarketDatabase::class.java,
                    "adjame_market_database"
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Pré-remplir la base avec le catalogue de base pour fonctionnement immédiat hors-ligne
                        CoroutineScope(Dispatchers.IO).launch {
                            val database = getInstance(context)
                            database.categoryDao().insertCategories(SeedData.categories)
                            database.sellerContactDao().insertSellers(SeedData.sellers)
                            database.productDao().insertProducts(SeedData.products)
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
            }
        }
    }
}
