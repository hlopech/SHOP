package com.example.shop.repositories

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.shop.data.DeletedUser
import com.example.shop.data.User
import com.example.shop.viewModels.AuthState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AuthRepository {
    val auth = Firebase.auth
    private val fs = Firebase.firestore


    fun loadUser(currentUser: MutableLiveData<User?>) {

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

    fun logIn(
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


    fun singUp(
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

    fun logOut(authState: MutableLiveData<AuthState>) {
        auth.signOut()
        authState.value = AuthState.Unauthenticated
    }
}
