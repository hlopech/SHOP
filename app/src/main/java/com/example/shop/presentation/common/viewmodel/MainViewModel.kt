package com.example.shop.presentation.common.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.shop.domain.model.Category
import com.example.shop.domain.model.Product
import com.example.shop.domain.model.RequestToChangeRole
import com.example.shop.domain.model.RequestToCreateProduct
import com.example.shop.domain.model.User
import com.example.shop.data.repositoryImpl.ProductRepositoryImpl
import com.example.shop.domain.model.OrderedProduct
import com.example.shop.domain.useCases.DeleteUserAccountUseCase
import com.example.shop.domain.useCases.GetProductByUidUseCase
import com.example.shop.domain.useCases.GetProductsByUidListUseCase
import com.example.shop.domain.useCases.GetRequestsToChangeRoleUseCase
import com.example.shop.domain.useCases.GetRequestsToPublishProductUseCase
import com.example.shop.domain.useCases.GetSellerOrderedProductsUIDSUseCase
import com.example.shop.domain.useCases.IsUserAuthenticatedUseCase
import com.example.shop.domain.useCases.LoadUserUseCase
import com.example.shop.domain.useCases.LogInUseCase
import com.example.shop.domain.useCases.LogOutUseCase
import com.example.shop.domain.useCases.RemoveRequestListenerUseCase
import com.example.shop.domain.useCases.SignUpUseCase
import com.example.shop.domain.useCases.UpdateProductUseCase
import com.example.shop.domain.useCases.UpdateUserDataUseCase
import com.example.shop.domain.useCases.UpdateUserPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase,
    private val loadUserUseCase: LoadUserUseCase,
    private val logInUseCase: LogInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val isUserAuthenticatedUseCase: IsUserAuthenticatedUseCase,
    private val getRequestsToChangeRoleUseCase: GetRequestsToChangeRoleUseCase,
    private val getRequestsToPublishProductUseCase: GetRequestsToPublishProductUseCase,
    private val removeRequestListenerUseCase: RemoveRequestListenerUseCase,
    private val getSellerOrderedProductsUIDSUseCase: GetSellerOrderedProductsUIDSUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val getProductByUidListUseCase: GetProductsByUidListUseCase,
    private val updateUserPasswordUseCase: UpdateUserPasswordUseCase,
    private val updateUserDataUseCase: UpdateUserDataUseCase,
    private val deleteUserAccountUseCase: DeleteUserAccountUseCase

) : ViewModel() {

    //    private val authRepository = AuthRepository()
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _currentUser: MutableLiveData<User?> = MutableLiveData()
    val currentUser: LiveData<User?> = _currentUser


    private val _requestsToChangeRole = MutableLiveData<List<RequestToChangeRole?>>()
    val requestsToChangeRole: LiveData<List<RequestToChangeRole?>> = _requestsToChangeRole

    private val _requestsToPublishProduct = MutableLiveData<List<RequestToCreateProduct?>>()
    val requestsToPublishProduct: LiveData<List<RequestToCreateProduct?>> =
        _requestsToPublishProduct


    private val _order = MutableLiveData<List<Product>>(arrayListOf())
    var order: LiveData<List<Product>> = _order


    var viewedProduct: MutableState<Product?> = mutableStateOf(null)

    var viewedUserId: MutableState<String?> = mutableStateOf(null)

    var selectedCategory: MutableState<Category?> = mutableStateOf(null)

    var currentSearch: MutableState<String> = mutableStateOf("")

    private val _orders = MutableStateFlow<List<OrderedProduct>>(arrayListOf())
    var orders: StateFlow<List<OrderedProduct>> = _orders

    init {
        getRequest()

        checkAuthStatus()
        loadUser()
    }


    private fun getRequest() {
        getRequestsToChangeRoleUseCase.invoke(_requestsToChangeRole)
        getRequestsToPublishProductUseCase.invoke(_requestsToPublishProduct)

    }

    fun updateUserPassword(
        context: Context,
        currentPassword: String,
        newPassword: String,
        onChanged: () -> Unit
    ) {
        updateUserPasswordUseCase.invoke(
            context,
            currentPassword,
            newPassword,
            onChanged
        )
    }

    fun deleteUserAccount(
        context: Context,
        password: String,
        onDeleted: () -> Unit
    ) {
        deleteUserAccountUseCase.invoke(context, password, onDeleted)
    }

    fun updateUserData(
        uid: String,
        newUserData: User,
        navController: NavController?,
        context: Context
    ) {
        updateUserDataUseCase.invoke(
            currentUser.value?.uid!!,
            newUserData,
            navController, context
        )
    }

    fun getSellerActiveOrders() {
        getSellerOrderedProductsUIDSUseCase.invoke(_orders, _currentUser.value?.uid)
    }

    fun updateProduct(product: Product) {
        updateProductUseCase.invoke(product)
    }

    fun clearOrder() {
        _order.value = arrayListOf()
    }

    fun addProductToOrder(product: ArrayList<Product>) {
        _order.value = product
    }

    fun addProductsToOrderByUid(uids: List<String>) {

        getProductByUidListUseCase.invoke(uids, _order)
    }


    fun loadUser() {
        loadUserUseCase.invoke(_currentUser)
    }

    private fun checkAuthStatus() {
        isUserAuthenticatedUseCase.invoke(_authState)
    }

    fun logIn(email: String, password: String, context: Context) {
        logInUseCase.invoke(email, password, _authState, _currentUser, context)
    }

    fun singUp(
        username: String, email: String, password: String, role: String,
        context: Context
    ) {
        signUpUseCase.invoke(username, email, password, role, _authState, _currentUser, context)
    }

    fun logOut() {
        logOutUseCase.invoke(_authState)
    }

    override fun onCleared() {
        super.onCleared()
        removeRequestListenerUseCase.invoke()
    }
}


sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}
