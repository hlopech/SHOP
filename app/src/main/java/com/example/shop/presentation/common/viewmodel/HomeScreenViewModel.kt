package com.example.shop.presentation.common.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shop.domain.model.Product
import com.example.shop.domain.model.User
import com.example.shop.domain.useCases.GetAllUsersUseCase
import com.example.shop.domain.useCases.GetPopularProductsUseCase
import com.example.shop.domain.useCases.GetProductsByCategoriesUseCase
import com.example.shop.domain.useCases.GetProductsByCategoryUseCase
import com.example.shop.domain.useCases.GetProductsByUidListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getPopularProductsUseCase: GetPopularProductsUseCase,
    private val getProductsByUidListUseCase: GetProductsByUidListUseCase,
    private val getProductsByCategoriesUseCase: GetProductsByCategoriesUseCase
) : ViewModel() {


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
        getAllUsersUseCase.invoke(_users)
    }

    private fun getPopularProducts() {
        getPopularProductsUseCase.invoke(_popularProduct)
    }

    fun getShoppingCartByUid(uids: List<String>) {
        getProductsByUidListUseCase.invoke(uids, _shoppingCart)
    }

    fun getFavoriteProductsByUid(uids: List<String>) {
        getProductsByUidListUseCase.invoke(uids, _favoritesProduct)
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

        getProductsByCategoriesUseCase.invoke(
            _recommendedProducts,
            _categories?.value?.toList()!!
        )

    }


}