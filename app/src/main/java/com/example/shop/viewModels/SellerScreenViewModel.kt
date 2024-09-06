package com.example.shop.viewModels

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shop.data.Product
import com.example.shop.data.User
import com.example.shop.repositories.AdminRepository
import com.example.shop.repositories.ProductRepository
import com.example.shop.repositories.UserRepository

class SellerScreenViewModel : ViewModel() {

    private val productRepository = ProductRepository()
    private val adminRepository = AdminRepository()
    private val userRepository = UserRepository()


    private val _products: MutableLiveData<List<Product?>> = MutableLiveData()
    val products: LiveData<List<Product?>> = _products


    fun getProductsBySellerId(sellerId: String?) {
        productRepository.getProductsBySellerId(_products, sellerId)
    }

    fun loadSellerData(uid: String, seller: MutableState<User?>) {
        adminRepository.loadUserDataByUid(uid, seller)
    }

    fun deleteUserData(context: Context, uid: String, user: User?) {
        userRepository.deleteUserDataByUid(context, uid, user)
    }

}