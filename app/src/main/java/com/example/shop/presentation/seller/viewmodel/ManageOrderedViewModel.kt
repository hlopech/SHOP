package com.example.shop.presentation.seller.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.domain.model.OrderedProduct
import com.example.shop.domain.model.Product
import com.example.shop.domain.model.User
import com.example.shop.domain.useCases.GetProductByUidUseCase
import com.example.shop.domain.useCases.GetSellerOrderedProductsUIDSUseCase
import com.example.shop.domain.useCases.LoadUserDataUseCase
import com.example.shop.domain.useCases.RegisterPackageUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ManageOrderedViewModel@Inject constructor(
    private val auth :FirebaseAuth,
    private val getSellerOrderedProductsUIDSUseCase: GetSellerOrderedProductsUIDSUseCase,
    private val getProductByUidUseCase: GetProductByUidUseCase,
    private val registerPackageUseCase: RegisterPackageUseCase,
    private val loadUserDataUseCase: LoadUserDataUseCase
) : ViewModel() {


    private val _orders = MutableStateFlow<List<OrderedProduct>>(arrayListOf())
    var orders: StateFlow<List<OrderedProduct>> = _orders

    init {
        getOrderedProductsUIDS()
    }


    private fun getOrderedProductsUIDS() {
        getSellerOrderedProductsUIDSUseCase.invoke(_orders, auth.currentUser?.uid)
    }

    fun getProductByUID(productUID: String, product: MutableState<Product?>) {
        getProductByUidUseCase.invoke(productUID, product)
    }

    fun confirmSending(
        orderId: String,
        trackNumber: String,
        recipientName: String,
        recipientPhone: String,
        destinationAddress: String
    ) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    registerPackageUseCase.invoke(
                        orderId,
                        trackNumber,
                        recipientName,
                        recipientPhone,
                        destinationAddress
                    )
                }
            } catch (e: Exception) {
                Log.e("ManageOrder", "Ошибка регистрации посылки", e)
            }
        }
    }


    suspend fun getBuyerNumber(userId: String): User? = withContext(Dispatchers.IO) {
        var user = mutableStateOf<User?>(null)
        loadUserDataUseCase.invoke(userId!!, user)
        Log.d("AZX", user.value.toString())
        val timeoutMs = 5_000L        // например, 5 секунд
        val pollInterval = 50L        // опрашиваем каждые 50 мс
        var waited = 0L
        while (user.value == null && waited < timeoutMs) {
            delay(pollInterval)
            waited += pollInterval
        }
        user.value
    }
}