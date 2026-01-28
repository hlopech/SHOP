package com.example.shop.presentation.common.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.shop.domain.model.Product
import com.example.shop.domain.model.User
import com.example.shop.domain.useCases.DeleteProductUseCase
import com.example.shop.domain.useCases.GetAllProductsUseCase
import com.example.shop.domain.useCases.GetProductsBySellerIdUseCase
import com.example.shop.domain.useCases.UpdateUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val getProductsBySellerIdUseCase: GetProductsBySellerIdUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val updateUserDataUseCase: UpdateUserDataUseCase,
) : ViewModel() {



    private val _products: MutableLiveData<List<Product?>> = MutableLiveData()
    val products: LiveData<List<Product?>> = _products


    fun getAllProducts() {
        getAllProductsUseCase.invoke(_products)
    }

    fun getProductsBySellerId(sellerId: String?) {
        getProductsBySellerIdUseCase.invoke(_products, sellerId)
    }

    fun deleteProduct(context: Context, product: Product?) {
        deleteProductUseCase.invoke(context, product)
    }


    fun addProductToShoppingCart(
        uid: String,
        newUserData: User,
        navController: NavController?,
        context: Context
    ) {
        updateUserDataUseCase.invoke(uid, newUserData, null, context)
    }

    fun addProductToFavorites(
        uid: String,
        newUserData: User,
        navController: NavController?,
        context: Context
    ) {
        updateUserDataUseCase.invoke(uid, newUserData, null, context)
    }


    fun refreshData() {

        _products.value = listOf()

    }

}
