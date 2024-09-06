package com.example.shop.viewModels

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shop.data.Category
import com.example.shop.data.Product
import com.example.shop.data.RequestToChangeRole
import com.example.shop.data.RequestToCreateProduct
import com.example.shop.data.User
import com.example.shop.repositories.AdminRepository
import com.example.shop.repositories.AuthRepository
import com.example.shop.repositories.ProductRepository

class MainViewModel : ViewModel() {
    private val adminRepository = AdminRepository()
    private val authRepository = AuthRepository()
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState
    private val productRepository = ProductRepository()

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


    init {
        getRequest()

        checkAuthStatus()
        loadUser()
    }


    private fun getRequest() {
        adminRepository.getRequestsToChangeRole(_requestsToChangeRole)
        adminRepository.getRequestsToPublishProduct(_requestsToPublishProduct)


    }


    fun updateProduct(product: Product) {
        productRepository.updateProduct(product)
    }

    fun clearOrder() {
        _order.value = arrayListOf()
    }

    fun addProductToOrder(product: ArrayList<Product>) {
        _order.value = product
    }

    fun addProductsToOrderByUid(uids: List<String>) {

        productRepository.getProductsByUidList(uids, _order)
    }


    fun loadUser() {
        authRepository.loadUser(_currentUser)
    }

    private fun checkAuthStatus() {
        if (authRepository.auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    fun logIn(email: String, password: String, context: Context) {
        authRepository.logIn(email, password, _authState, _currentUser, context)
    }

    fun singUp(
        username: String, email: String, password: String, role: String,
        context: Context
    ) {
        authRepository.singUp(username, email, password, role, _authState, _currentUser, context)
    }

    fun logOut() {
        authRepository.logOut(_authState)
    }

    override fun onCleared() {
        super.onCleared()
        adminRepository.removeRequestListener()
    }
}


sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}
