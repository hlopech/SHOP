package com.example.shop.viewModels


import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.shop.data.Product
import com.example.shop.data.User
import com.example.shop.repositories.AdminRepository
import com.example.shop.repositories.ProductRepository
import com.example.shop.repositories.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FavoritesScreenViewModel
    : ViewModel() {
    private val userRepository = UserRepository()
    private val adminRepository = AdminRepository()
    private val fs = Firebase.firestore
    private val auth = Firebase.auth


    suspend fun getProductByUid(uid: String): Product? {
        return try {
            val document = fs.collection("products").document(uid).get().await()
            document.toObject(Product::class.java)
        } catch (e: Exception) {
            Log.e("FavoritesScreen", "Error fetching product with UID: $uid", e)
            null
        }
    }

    private val _user: MutableLiveData<User?> = MutableLiveData()
    val user: LiveData<User?> = _user

    val u1 = mutableStateOf<User?>(null)

    //    fun addProducts
    init {
        adminRepository.loadUserDataByUid(auth.currentUser?.uid!!, u1)
    }

    fun deleteProductFromFavorites(
        uid: String,
        newUserData: User,
        navController: NavController?,
        context: Context
    ) {
        userRepository.updateUserData(uid, newUserData, null, context)
    }
}
