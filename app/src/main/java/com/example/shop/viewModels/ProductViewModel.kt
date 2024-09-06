package com.example.shop.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.shop.data.Product
import com.example.shop.data.User
import com.example.shop.repositories.ProductRepository
import com.example.shop.repositories.UserRepository

class ProductViewModel : ViewModel() {
    private val productRepository = ProductRepository()
    private val userRepository = UserRepository()


    private val _products: MutableLiveData<List<Product?>> = MutableLiveData()
    val products: LiveData<List<Product?>> = _products


    fun getAllProducts() {
        productRepository.getAllProducts(_products)
    }

    fun getProductsBySellerId(sellerId: String?) {
        productRepository.getProductsBySellerId(_products, sellerId)
    }

    fun deleteProduct(context: Context, product: Product?) {
        productRepository.deleteProduct(context, product)
    }


    fun addProductToShoppingCart(
        uid: String,
        newUserData: User,
        navController: NavController?,
        context: Context
    ) {
        userRepository.updateUserData(uid, newUserData, null, context)
    }

    fun addProductToFavorites(
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
