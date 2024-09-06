package com.example.shop.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shop.data.Product
import com.example.shop.repositories.ProductRepository

class OrderHistoryViewModel : ViewModel() {
    private val productRepository = ProductRepository()


    private val _orderHistory = MutableLiveData<List<Product>>(arrayListOf())
    var orderHistory: LiveData<List<Product>> = _orderHistory

    fun getHistory(uids: List<String>) {
        productRepository.getProductsByUidList(uids, _orderHistory)

    }

}