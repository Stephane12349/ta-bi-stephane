package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.local.entity.ProductEntity
import com.example.data.local.model.ProductWithSeller
import kotlinx.coroutines.flow.Flow

/**
 * DAO pour la persistance locale et la recherche hors-ligne des articles de gros.
 */
@Dao
interface ProductDao {

    @Query("SELECT * FROM products ORDER BY isPromoted DESC, cachedAt DESC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE categoryId = :categoryId ORDER BY isPromoted DESC, basePrice ASC")
    fun getProductsByCategory(categoryId: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE sellerId = :sellerId ORDER BY cachedAt DESC")
    fun getProductsBySeller(sellerId: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE marketSector = :marketSector ORDER BY isPromoted DESC")
    fun getProductsBySector(marketSector: String): Flow<List<ProductEntity>>

    @Query("""
        SELECT * FROM products 
        WHERE name LIKE '%' || :query || '%' 
           OR description LIKE '%' || :query || '%' 
           OR packaging LIKE '%' || :query || '%'
        ORDER BY isPromoted DESC
    """)
    fun searchProducts(query: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE isPromoted = 1 ORDER BY cachedAt DESC")
    fun getPromotedProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    fun getProductById(productId: String): Flow<ProductEntity?>

    @Transaction
    @Query("SELECT * FROM products ORDER BY isPromoted DESC, cachedAt DESC")
    fun getProductsWithSeller(): Flow<List<ProductWithSeller>>

    @Transaction
    @Query("SELECT * FROM products WHERE categoryId = :categoryId ORDER BY isPromoted DESC, basePrice ASC")
    fun getProductsWithSellerByCategory(categoryId: String): Flow<List<ProductWithSeller>>

    @Transaction
    @Query("""
        SELECT * FROM products 
        WHERE name LIKE '%' || :query || '%' 
           OR description LIKE '%' || :query || '%' 
           OR packaging LIKE '%' || :query || '%'
        ORDER BY isPromoted DESC
    """)
    fun searchProductsWithSeller(query: String): Flow<List<ProductWithSeller>>

    @Transaction
    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    fun getProductWithSellerById(productId: String): Flow<ProductWithSeller?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Query("DELETE FROM products WHERE id = :productId")
    suspend fun deleteProductById(productId: String)

    @Query("DELETE FROM products")
    suspend fun clearAllProducts()

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int
}
