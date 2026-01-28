package com.example.shop.presentation.common.viewmodel


import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.shop.domain.model.Product
import com.example.shop.domain.model.User
import com.example.shop.domain.useCases.GetProductByUidUseCase
import com.example.shop.domain.useCases.LoadUserDataUseCase
import com.example.shop.domain.useCases.UpdateUserDataUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class FavoritesScreenViewModel @Inject constructor(
    private val loadUserDataUseCase: LoadUserDataUseCase,
    private val updateUserDataUseCase: UpdateUserDataUseCase,
    private val getProductByUidUseCase: GetProductByUidUseCase,
    private val fs: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {


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

    init {
        loadUserDataUseCase.invoke(auth.currentUser?.uid!!, u1)
    }

    fun deleteProductFromFavorites(
        uid: String,
        newUserData: User,
        navController: NavController?,
        context: Context
    ) {
        updateUserDataUseCase.invoke(uid, newUserData, null, context)
    }
}
