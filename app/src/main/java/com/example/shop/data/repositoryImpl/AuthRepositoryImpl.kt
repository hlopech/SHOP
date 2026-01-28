package com.example.shop.data.repositoryImpl

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.shop.domain.model.DeletedUser
import com.example.shop.domain.model.User
import com.example.shop.domain.repository.AuthRepository
import com.example.shop.presentation.common.viewmodel.AuthState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val fs: FirebaseFirestore
) : AuthRepository {

    override fun loadUser(currentUser: MutableLiveData<User?>) {

        auth.currentUser?.uid?.let {
            fs.collection("users").document(it).get()
                .addOnSuccessListener { document ->
                    val user = document.toObject(User::class.java)
                    currentUser.value = user
                }.addOnFailureListener {
                    currentUser.value = null
                }
        }
    }


    override fun logIn(
        email: String,
        password: String,
        authState: MutableLiveData<AuthState>,
        currentUser: MutableLiveData<User?>,
        context: Context
    ) {
        if (email.isEmpty() || password.isEmpty()) {
            authState.value = AuthState.Error("Something wrong")
            return
        }
        authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                fs.collection("deletedUsers").document(
                    email
                ).get().addOnSuccessListener { documents ->
                    val deleted = documents.toObject(DeletedUser::class.java)
                    if (deleted == null) {
                        if (task.isSuccessful) {
                            authState.value = AuthState.Authenticated
                            loadUser(currentUser)
                        } else {
                            authState.value =
                                AuthState.Error(task.exception?.message ?: "Something wrong")
                        }
                    } else {
                        auth.signOut()
                        authState.value = AuthState.Error("Этот Email в черном списке")
                        Toast.makeText(context, "Этот Email в черном списке", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }


    override fun singUp(
        username: String,
        email: String,
        password: String,
        role: String,
        authState: MutableLiveData<AuthState>,
        currentUser: MutableLiveData<User?>,
        context: Context

    ) {
        if (email.isEmpty() || password.isEmpty() || role.isEmpty()) {
            authState.value = AuthState.Error("Something wrong")
            return
        }
        authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                fs.collection("deletedUsers").document(
                    email
                ).get().addOnSuccessListener { documents ->
                    val deleted = documents.toObject(DeletedUser::class.java)
                    if (task.isSuccessful && deleted == null) {
                        val firebaseUser = auth.currentUser
                        firebaseUser?.let {
                            val userData = User(
                                uid = it.uid,
                                email = it.email ?: "",
                                role = role,
                                searchHistory = arrayListOf<String>()
                            )
                            authState.value = AuthState.Authenticated
                            fs.collection("users").document(it.uid).set(userData)
                        }
                        loadUser(currentUser)


                    } else if (deleted != null) {
                        authState.value =
                            AuthState.Error("Этот Email в черном списке")
                        Toast.makeText(context, "Этот Email в черном списке", Toast.LENGTH_SHORT)
                    } else {
                        authState.value =
                            AuthState.Error(task.exception?.message ?: "Something wrong")
                    }
                }
            }
    }

    override fun logOut(authState: MutableLiveData<AuthState>) {
        auth.signOut()
        authState.value = AuthState.Unauthenticated
    }

    override fun isUserAuthenticated(authState: MutableLiveData<AuthState>) {
        if (auth.currentUser == null) {
            authState.value = AuthState.Unauthenticated
        } else {
            authState.value = AuthState.Authenticated
        }
    }


}
