package com.example.thehighlife1.data.localroom

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao @Entity
interface ProductDao {

    @Query("SELECT * FROM cart_items order by id desc")
    fun getAll(): Flow<List<ProductEntity>> // Use Flow instead of LiveData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg product: ProductEntity)

    @Delete
    suspend fun delete(product: ProductEntity)

    @Update
    suspend fun update(vararg product: ProductEntity)
}
