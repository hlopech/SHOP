package com.example.shop.viewModels

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.shop.data.Product
import com.example.shop.data.User
import com.example.shop.repositories.ProductRepository
import com.example.shop.repositories.UserRepository

class ShoppingCartViewModel : ViewModel() {

    private val productRepository = ProductRepository()
    private val userRepository = UserRepository()


    fun getProductByUid(uid: String, product: MutableState<Product?>) {
        productRepository.getProductByUid(uid, product)
    }

    fun deleteProductFromShoppingCart(
        uid: String,
        newUserData: User,
        navController: NavController?,
        context: Context
    ) {

        userRepository.updateUserData(uid, newUserData, null, context)
    }
}