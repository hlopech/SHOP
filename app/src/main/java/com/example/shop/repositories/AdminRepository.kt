package com.example.shop.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.shop.data.Category
import com.example.shop.data.RequestToChangeRole
import com.example.shop.data.RequestToCreateProduct
import com.example.shop.data.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class AdminRepository {
    private val auth = Firebase.auth
    private val fs = Firebase.firestore
    private val storage = Firebase.storage
    private var requestToChangeRoleListener: ListenerRegistration? = null
    private var requestToPublishProdust: ListenerRegistration? = null


    fun loadUserDataByUid(uid: String, user: MutableState<User?>) {

        fs.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                var userData = document.toObject(User::class.java)
                user.value = userData
            }.addOnFailureListener {
                user.value = null
            }
    }

    fun getAllUsers(users: MutableLiveData<List<User?>>) {
        fs.collection("users").get()
            .addOnSuccessListener { documents ->
                val userList = mutableListOf<User?>()
                for (document in documents) {
                    val user = document.toObject(User::class.java)
                    userList.add(user)
                }
                users.value = userList
            }
            .addOnFailureListener {
                users.value = null
            }
    }


    fun updateRequestToChangeRole(
        response: RequestToChangeRole,
        navController: NavController,
        context: Context
    ) {
        fs.collection("requestsToChangeRole").document(response.userUid).set(response)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navController.navigate("profile")
                    Toast.makeText(context, "Данные успешно обновленны", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        context,
                        "Произошла ошибка ,данные не обновленны",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

    }

    fun getRequestsToPublishProduct(
        requestToCreateProducts: MutableLiveData<List<RequestToCreateProduct?>>
    ) {
        requestToPublishProdust = fs.collection("requestsToCreateProduct")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    requestToCreateProducts.value = null
                    return@addSnapshotListener
                }

                if (snapshots != null && !snapshots.isEmpty) {
                    val requests = snapshots.documents.map { document ->
                        document.toObject(RequestToCreateProduct::class.java)
                    }.filter { it?.response == "waiting" }
                    requestToCreateProducts.value = requests
                } else {
                    requestToCreateProducts.value = null
                }
            }
    }


    fun getRequestsToChangeRole(
        requestsToChangeRole: MutableLiveData<List<RequestToChangeRole?>>
    ) {
        requestToChangeRoleListener = fs.collection("requestsToChangeRole")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    requestsToChangeRole.value = null
                    return@addSnapshotListener
                }

                if (snapshots != null && !snapshots.isEmpty) {
                    val requests = snapshots.documents.map { document ->
                        document.toObject(RequestToChangeRole::class.java)
                    }.filter { it?.response == "waiting" }
                    requestsToChangeRole.value = requests
                } else {
                    requestsToChangeRole.value = null
                }
            }
    }


    @SuppressLint("SuspiciousIndentation")
    fun getAllCategories(categories: MutableLiveData<ArrayList<Category?>>) {
        fs.collection("categories").get().addOnSuccessListener { documents ->
            val categoriesList = mutableListOf<Category?>()
            for (document in documents) {
                val category = document.toObject(Category::class.java)
                categoriesList.add(category)
            }
            categories.value = categoriesList as ArrayList<Category?>
        }.addOnFailureListener {

        }
    }


    fun createCategory(context: Context, category: Category) {
        fs.collection("categories").document(category.uid).set(category).addOnSuccessListener {
            Toast.makeText(context, "Новая категория успешно добавлена", Toast.LENGTH_SHORT)
                .show()
        }.addOnFailureListener {
            Toast.makeText(context, "Ошибка добавления", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun deleteCategory(category: Category, categories: MutableLiveData<ArrayList<Category?>>) {
        val imageRef = storage.getReferenceFromUrl(category.image)
        imageRef.delete().addOnCompleteListener {
            fs.collection("categories").document(category.uid).delete()
            getAllCategories(categories)
        }.addOnFailureListener {

        }


    }

    fun updateCategory(
        category: Category,
        context: Context
    ) {
        fs.collection("categories").document(category.uid).set(category).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Данные успешно обновленны", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    context,
                    "Произошла ошибка ,данные не обновленны",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }

    fun removeRequestListener() {
        requestToChangeRoleListener?.remove()
        requestToPublishProdust?.remove()

    }


}