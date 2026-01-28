package com.example.shop.domain.repository


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import com.example.shop.domain.model.*

interface AdminRepository {
    fun loadUserDataByUid(uid: String, user: MutableState<User?>)
    fun getAllUsers(users: MutableLiveData<List<User?>>)
    fun updateRequestToChangeRole(
        response: RequestToChangeRole,
        navController: NavController,
        context: Context
    )
    fun getRequestsToPublishProduct(requests: MutableLiveData<List<RequestToCreateProduct?>>)
    fun getRequestsToChangeRole(requests: MutableLiveData<List<RequestToChangeRole?>>)
    fun getAllCategories(categories: MutableLiveData<ArrayList<Category?>>)
    fun createCategory(context: Context, category: Category)
    fun deleteCategory(
        category: Category,
        categories: MutableLiveData<ArrayList<Category?>>
    )
    fun updateCategory(category: Category, context: Context)
    fun removeRequestListener()
}
