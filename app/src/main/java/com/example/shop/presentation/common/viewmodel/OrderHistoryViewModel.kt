package com.example.shop.presentation.common.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.shop.domain.model.Product
import com.example.shop.data.repositoryImpl.ProductRepositoryImpl
import com.example.shop.domain.model.OrderedProduct
import com.example.shop.domain.model.User
import com.example.shop.domain.useCases.GetOrderedProductsUIDSUseCase
import com.example.shop.domain.useCases.GetProductByUidUseCase
import com.example.shop.domain.useCases.LoadUserDataUseCase
import com.example.shop.domain.useCases.UpdateOrderedProductUseCase
import com.example.shop.domain.useCases.UpdateProductUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val auth:FirebaseAuth,
    private val getProductByUidUseCase: GetProductByUidUseCase,
    private val getOrderedProductsUIDSUseCase: GetOrderedProductsUIDSUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val updateOrderedProductUseCase: UpdateOrderedProductUseCase,
    private val loadUserDataUseCase: LoadUserDataUseCase,
) : ViewModel() {

    val currentUser = auth.currentUser

    private val _orderHistory = MutableStateFlow<List<OrderedProduct>>(arrayListOf())
    var orderHistory: StateFlow<List<OrderedProduct>> = _orderHistory


    fun getProductByUID(productUID: String, product: MutableState<Product?>) {
        getProductByUidUseCase.invoke(productUID, product)
    }

    init {
        getOrderedProductsUIDS()
    }

    private fun getOrderedProductsUIDS() {
        getOrderedProductsUIDSUseCase.invoke(_orderHistory, currentUser?.uid)
    }

    fun rateProduct(product: Product, orderedProduct: OrderedProduct, star: Int) {
        updateProductUseCase.invoke(product)
        updateOrderedProductUseCase.invoke(orderedProduct.copy(rated = star))
    }

    suspend fun getSellerNumber(sellerId: String): String? = withContext(Dispatchers.IO) {
        var user = mutableStateOf<User?>(null)
        loadUserDataUseCase.invoke(sellerId!!, user)
        Log.d("AZX", user.value.toString())
        val timeoutMs = 5_000L        // например, 5 секунд
        val pollInterval = 50L        // опрашиваем каждые 50 мс
        var waited = 0L
        while (user.value == null && waited < timeoutMs) {
            delay(pollInterval)
            waited += pollInterval
        }
        user.value?.phone
    }

}