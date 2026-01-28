package com.example.shop.presentation.common.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shop.domain.model.Product
import com.example.shop.domain.model.User
import com.example.shop.domain.useCases.DeleteUserDataByUidUseCase
import com.example.shop.domain.useCases.GetProductsBySellerIdUseCase
import com.example.shop.domain.useCases.LoadUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SellerScreenViewModel @Inject constructor(
    private val getProductsBySellerIdUseCase: GetProductsBySellerIdUseCase,
    private val loadUserDataUseCase: LoadUserDataUseCase,
    private val deleteUserDataByUidUseCase: DeleteUserDataByUidUseCase
) : ViewModel() {



    private val _products: MutableLiveData<List<Product?>> = MutableLiveData()
    val products: LiveData<List<Product?>> = _products


    fun getProductsBySellerId(sellerId: String?) {
        getProductsBySellerIdUseCase.invoke(_products, sellerId)
    }

    fun loadSellerData(uid: String, seller: MutableState<User?>) {
        loadUserDataUseCase.invoke(uid, seller)
    }

    fun deleteUserData(context: Context, uid: String, user: User?) {
        deleteUserDataByUidUseCase.invoke(context, uid, user)
    }

}