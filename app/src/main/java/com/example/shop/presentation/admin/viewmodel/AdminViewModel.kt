package com.example.shop.presentation.admin.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.shop.domain.model.Category
import com.example.shop.domain.model.RequestToChangeRole
import com.example.shop.domain.model.User
import com.example.shop.domain.useCases.CreateCategoryUseCase
import com.example.shop.domain.useCases.DeleteCategoryUseCase
import com.example.shop.domain.useCases.GetAllCategoriesUseCase
import com.example.shop.domain.useCases.GetAllUsersUseCase
import com.example.shop.domain.useCases.LoadUserDataUseCase
import com.example.shop.domain.useCases.UpdateCategoryUseCase
import com.example.shop.domain.useCases.UpdateRequestToChangeRoleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdminViewModel@Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val loadUserDataUseCase: LoadUserDataUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val updateRequestToChangeRoleUseCase: UpdateRequestToChangeRoleUseCase
): ViewModel() {

    private val _users: MutableLiveData<List<User?>> = MutableLiveData()
    val users: LiveData<List<User?>> = _users

    private val _categories: MutableLiveData<ArrayList<Category?>> = MutableLiveData()
    val categories: LiveData<ArrayList<Category?>> = _categories


    init {
        getAllUsers()
        getAllCategories()
    }

    private fun getAllUsers() {
        getAllUsersUseCase.invoke(_users)
    }

    fun getUserDataById(uid: String, user: MutableState<User?>) {
        loadUserDataUseCase.invoke(uid, user)
    }


    private fun getAllCategories() {
        getAllCategoriesUseCase.invoke(_categories)
    }

    fun createCategory(context: Context, category: Category) {
        createCategoryUseCase.invoke(context, category)
        getAllCategories()
    }

    fun deleteCategory(category: Category) {
        deleteCategoryUseCase.invoke(category, _categories)
    }

    fun updateCategory(
        category: Category,
        context: Context
    ) {
        updateCategoryUseCase.invoke(category, context)
        getAllCategories()


    }

    fun sendAnswerToRequest(
        response: RequestToChangeRole,
        navController: NavController,
        context: Context
    ) {
        updateRequestToChangeRoleUseCase(response, navController, context)
        getAllCategories()

    }


}
