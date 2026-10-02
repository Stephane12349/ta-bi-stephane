package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.SellerContactEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO pour la gestion et la recherche hors-ligne des contacts grossistes sauvegardés.
 */
@Dao
interface SellerContactDao {

    @Query("SELECT * FROM seller_contacts ORDER BY isFavorite DESC, rating DESC, businessName ASC")
    fun getAllSellers(): Flow<List<SellerContactEntity>>

    @Query("SELECT * FROM seller_contacts WHERE isFavorite = 1 ORDER BY rating DESC, businessName ASC")
    fun getFavoriteSellers(): Flow<List<SellerContactEntity>>

    @Query("SELECT * FROM seller_contacts WHERE lastContactedAt IS NOT NULL ORDER BY lastContactedAt DESC")
    fun getRecentlyContactedSellers(): Flow<List<SellerContactEntity>>

    @Query("SELECT * FROM seller_contacts WHERE sellerId = :sellerId LIMIT 1")
    fun getSellerById(sellerId: String): Flow<SellerContactEntity?>

    @Query("SELECT * FROM seller_contacts WHERE marketSector LIKE '%' || :sector || '%' ORDER BY rating DESC")
    fun getSellersBySector(sector: String): Flow<List<SellerContactEntity>>

    @Query("""
        SELECT * FROM seller_contacts 
        WHERE businessName LIKE '%' || :query || '%' 
           OR managerName LIKE '%' || :query || '%' 
           OR marketSector LIKE '%' || :query || '%' 
           OR landmarks LIKE '%' || :query || '%'
        ORDER BY isFavorite DESC, rating DESC
    """)
    fun searchSellers(query: String): Flow<List<SellerContactEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSellers(sellers: List<SellerContactEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeller(seller: SellerContactEntity)

    @Query("UPDATE seller_contacts SET isFavorite = :isFavorite WHERE sellerId = :sellerId")
    suspend fun updateFavoriteStatus(sellerId: String, isFavorite: Boolean)

    @Query("UPDATE seller_contacts SET userPrivateNotes = :notes WHERE sellerId = :sellerId")
    suspend fun updateSellerNotes(sellerId: String, notes: String)

    @Query("UPDATE seller_contacts SET lastContactedAt = :timestamp WHERE sellerId = :sellerId")
    suspend fun updateLastContacted(sellerId: String, timestamp: Long)

    @Query("DELETE FROM seller_contacts WHERE sellerId = :sellerId")
    suspend fun deleteSellerById(sellerId: String)

    @Query("DELETE FROM seller_contacts")
    suspend fun clearAllSellers()

    @Query("SELECT COUNT(*) FROM seller_contacts")
    suspend fun getSellerCount(): Int
}
