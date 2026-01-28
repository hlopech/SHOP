package com.example.shop.presentation.common.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.shop.domain.model.Product
import com.example.shop.domain.model.User
import com.example.shop.domain.useCases.CreateOrderedProductUseCase
import com.example.shop.domain.useCases.GetProductByUidUseCase
import com.example.shop.domain.useCases.UpdateUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel @Inject constructor(
    private val getProductByUidUseCase: GetProductByUidUseCase,
    private val createOrderedProductUseCase: CreateOrderedProductUseCase,
    private val updateUserDataUseCase: UpdateUserDataUseCase
) : ViewModel() {



    fun getProductByUid(uid: String, product: MutableState<Product?>) {
        getProductByUidUseCase.invoke(uid, product)
    }


    fun createOrderedProduct(productId: String,userId:String) {
        createOrderedProductUseCase.invoke(productId,userId)
    }

    fun deleteProductFromShoppingCart(
        uid: String,
        newUserData: User,
        navController: NavController?,
        context: Context
    ) {

        updateUserDataUseCase.invoke(uid, newUserData, null, context)
    }
}