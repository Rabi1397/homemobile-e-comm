package com.example.thehighlife1.data.localroom

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Entity
import kotlinx.coroutines.flow.Flow
@Dao @Entity
class CartRepo(private val productDao: ProductDao) {

    val allCartProducts: Flow<List<ProductEntity>> = productDao.getAll()

    suspend fun insert(product: ProductEntity) {
        productDao.insert(product)
    }
    suspend fun delete(product: ProductEntity) {
        productDao.delete(product)
    }
    suspend fun update(product: ProductEntity) {
        productDao.update(product)
    }
}
