package com.example.shop.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.shop.data.Category
import com.example.shop.data.RequestToChangeRole
import com.example.shop.data.User
import com.example.shop.repositories.AdminRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Locale

class AdminViewModel() : ViewModel() {

    private val adminRepository = AdminRepository()
    private val _users: MutableLiveData<List<User?>> = MutableLiveData()
    val users: LiveData<List<User?>> = _users

    private val _categories: MutableLiveData<ArrayList<Category?>> = MutableLiveData()
    val categories: LiveData<ArrayList<Category?>> = _categories


    init {
        getAllUsers()
        getAllCategories()
    }

    private fun getAllUsers() {
        adminRepository.getAllUsers(_users)
    }

    fun getUserDataById(uid: String, user: MutableState<User?>) {
        adminRepository.loadUserDataByUid(uid, user)
    }


    private fun getAllCategories() {
        adminRepository.getAllCategories(_categories)
    }

    fun createCategory(context: Context, category: Category) {
        adminRepository.createCategory(context, category)
        getAllCategories()
    }

    fun deleteCategory(category: Category) {
        adminRepository.deleteCategory(category, _categories)
    }

    fun updateCategory(
        category: Category,
        context: Context
    ) {
        adminRepository.updateCategory(category, context)
        getAllCategories()


    }

    fun sendAnswerToRequest(
        response: RequestToChangeRole,
        navController: NavController,
        context: Context
    ) {
        adminRepository.updateRequestToChangeRole(response, navController, context)
        getAllCategories()

    }


}
