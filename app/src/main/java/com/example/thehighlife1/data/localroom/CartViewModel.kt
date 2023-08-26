package com.example.thehighlife1.data.localroom

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Entity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Entity
class CartViewModel(application: Application) : AndroidViewModel(application){

    private val repository: CartRepo
    val allproducts: LiveData<List<ProductEntity>>

    init {
        val productDao = AppDatabase.getInstance(application).productDao()
        repository = CartRepo(productDao)
        allproducts = repository.allCartProducts.asLiveData()
    }

    fun insert(product: ProductEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(product)
    }

    fun deleteCart(product: ProductEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(product)
    }

    fun updateCart(product: ProductEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(product)
    }
}

private fun <T> LiveData<T>.asLiveData(): LiveData<T> {
    return this.asLiveData()
}
