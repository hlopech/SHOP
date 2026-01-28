package com.example.shop.presentation.common.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.shop.domain.model.Category
import com.example.shop.domain.model.Product
import com.example.shop.domain.model.User
import com.example.shop.domain.useCases.GetProductsByCategoryUseCase
import com.example.shop.domain.useCases.GetProductsByQueryUseCase
import com.example.shop.domain.useCases.UpdateUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getProductsByQueryUseCase: GetProductsByQueryUseCase,
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase,
    private val updateUserDataUseCase: UpdateUserDataUseCase,

) : ViewModel() {


    private val _products: MutableLiveData<List<Product?>> = MutableLiveData()
    val products: LiveData<List<Product?>> = _products

    fun getProductsByCategory(
        category: Category?
    ) {
        getProductsByCategoryUseCase.invoke(_products, category)
    }

    fun getProductsByQuery(
        query: String
    ) {
        getProductsByQueryUseCase.invoke(_products, query)
    }


    fun addSearchToHistory(
        uid: String,
        newUserData: User,
        navController: NavController?,
        context: Context
    ) {
        updateUserDataUseCase.invoke(uid, newUserData, null, context)
    }

    fun deleteSearchFromHistory(
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