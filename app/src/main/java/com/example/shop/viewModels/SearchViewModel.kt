package com.example.shop.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.shop.data.Category
import com.example.shop.data.Product
import com.example.shop.data.User
import com.example.shop.repositories.ProductRepository
import com.example.shop.repositories.UserRepository

class SearchViewModel : ViewModel() {

    private val productRepository = ProductRepository()
    private val userRepository = UserRepository()

    private val _products: MutableLiveData<List<Product?>> = MutableLiveData()
    val products: LiveData<List<Product?>> = _products

    fun getProductsByCategory(
        category: Category?
    ) {
        productRepository.getProductsByCategory(_products, category)
    }

    fun getProductsByQuery(
        query: String
    ) {
        productRepository.getProductsByQuery(_products, query)
    }


    fun addSearchToHistory(
        uid: String,
        newUserData: User,
        navController: NavController?,
        context: Context
    ) {
        userRepository.updateUserData(uid, newUserData, null, context)
    }

    fun deleteSearchFromHistory(
        uid: String,
        newUserData: User,
        navController: NavController?,
        context: Context
    ) {
        userRepository.updateUserData(uid, newUserData, null, context)
    }

    fun refreshData() {

        _products.value = listOf()

    }

}