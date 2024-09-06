package com.example.shop.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shop.data.Product
import com.example.shop.data.User
import com.example.shop.repositories.AdminRepository
import com.example.shop.repositories.ProductRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeScreenViewModel : ViewModel() {

    private val adminRepository = AdminRepository()
    private val productRepository = ProductRepository()
    private val fs = Firebase.firestore


    private val _users: MutableLiveData<List<User?>> = MutableLiveData()
    val users: LiveData<List<User?>> = _users

    private val _popularProduct: MutableLiveData<List<Product?>> = MutableLiveData()
    val popularProduct: LiveData<List<Product?>> = _popularProduct

    private val _recommendedProducts: MutableLiveData<List<Product?>> = MutableLiveData()
    val recommendedProducts: LiveData<List<Product?>> = _recommendedProducts

    private val _favoritesProduct = MutableLiveData<List<Product>>()
    val favoritesProduct: LiveData<List<Product>> = _favoritesProduct

    private val _shoppingCart = MutableLiveData<List<Product>>()
    val shoppingCart: LiveData<List<Product>> = _shoppingCart


    private val _categories = MutableLiveData<Set<String>>()
    val categories: LiveData<Set<String>> = _categories

    val user = mutableStateOf<User?>(null)


    init {
        getAllUsers()
        getPopularProducts()
    }

    private fun getAllUsers() {
        adminRepository.getAllUsers(_users)
    }

    private fun getPopularProducts() {
        productRepository.getPopularProducts(_popularProduct)
    }

    fun getShoppingCartByUid(uids: List<String>) {
        productRepository.getProductsByUidList(uids, _shoppingCart)
    }

    fun getFavoriteProductsByUid(uids: List<String>) {
        productRepository.getProductsByUidList(uids, _favoritesProduct)
    }

    fun getRecommendedProducts(
        favorites: List<Product>,
        shoppingCart: List<Product>,
    ) {

        val arr = ArrayList<String>()
        favorites.forEach {
            arr.add(it.category)

        }
        shoppingCart.forEach {
            arr.add(it.category)

        }
        _categories.value = arr.toSet()

        productRepository.getProductsByCategories(
            _recommendedProducts,
            _categories?.value?.toList()!!
        )

    }


}