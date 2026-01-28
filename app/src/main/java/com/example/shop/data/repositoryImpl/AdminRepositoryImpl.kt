package com.example.shop.data.repositoryImpl

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.shop.domain.model.Category
import com.example.shop.domain.model.RequestToChangeRole
import com.example.shop.domain.model.RequestToCreateProduct
import com.example.shop.domain.model.User
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.storage.ktx.storage
import com.example.shop.domain.repository.AdminRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminRepositoryImpl @Inject constructor(
    private val fs: FirebaseFirestore,
    private val storage : FirebaseStorage

) : AdminRepository {

    private var requestToChangeRoleListener: ListenerRegistration? = null
    private var requestToPublishProductListener: ListenerRegistration? = null

    override fun loadUserDataByUid(uid: String, user: MutableState<User?>) {
        fs.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                user.value = doc.toObject(User::class.java)
                Log.d("AdminRepo", "Loaded user: ${user.value}")
            }
            .addOnFailureListener {
                user.value = null
            }
    }

    override fun getAllUsers(users: MutableLiveData<List<User?>>) {
        fs.collection("users").get()
            .addOnSuccessListener { docs ->
                users.value = docs.map { it.toObject(User::class.java) }
            }
            .addOnFailureListener {
                users.value = null
            }
    }

    override fun updateRequestToChangeRole(
        response: RequestToChangeRole,
        navController: NavController,
        context: Context
    ) {
        fs.collection("requestsToChangeRole")
            .document(response.userUid)
            .set(response)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navController.navigate("profile")
                    Toast.makeText(context, "Данные успешно обновлены", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        context,
                        "Произошла ошибка, данные не обновлены", Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun getRequestsToPublishProduct(
        requests: MutableLiveData<List<RequestToCreateProduct?>>
    ) {
        requestToPublishProductListener = fs
            .collection("requestsToCreateProduct")
            .addSnapshotListener { snaps, e ->
                if (e != null) {
                    requests.value = null
                    return@addSnapshotListener
                }
                requests.value = snaps
                    ?.documents
                    ?.map { it.toObject(RequestToCreateProduct::class.java) }
                    ?.filter { it?.response == "waiting" }
            }
    }

    override fun getRequestsToChangeRole(
        requests: MutableLiveData<List<RequestToChangeRole?>>
    ) {
        requestToChangeRoleListener = fs
            .collection("requestsToChangeRole")
            .addSnapshotListener { snaps, e ->
                if (e != null) {
                    requests.value = null
                    return@addSnapshotListener
                }
                requests.value = snaps
                    ?.documents
                    ?.map { it.toObject(RequestToChangeRole::class.java) }
                    ?.filter { it?.response == "waiting" }
            }
    }

    override fun getAllCategories(categories: MutableLiveData<ArrayList<Category?>>) {
        fs.collection("categories").get()
            .addOnSuccessListener { docs ->
                val list = docs.map { it.toObject(Category::class.java) }
                categories.value = ArrayList(list)
            }
            .addOnFailureListener {
                // можно логировать
            }
    }

    override fun createCategory(context: Context, category: Category) {
        fs.collection("categories").document(category.uid)
            .set(category)
            .addOnSuccessListener {
                Toast.makeText(
                    context,
                    "Новая категория успешно добавлена", Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    context,
                    "Ошибка добавления", Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun deleteCategory(
        category: Category,
        categories: MutableLiveData<ArrayList<Category?>>
    ) {
        val imageRef = storage.getReferenceFromUrl(category.image)
        imageRef.delete().addOnCompleteListener {
            fs.collection("categories").document(category.uid).delete()
            getAllCategories(categories)
        }
    }

    override fun updateCategory(category: Category, context: Context) {
        fs.collection("categories").document(category.uid)
            .set(category)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Данные успешно обновлены", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Произошла ошибка, данные не обновлены", Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun removeRequestListener() {
        requestToChangeRoleListener?.remove()
        requestToPublishProductListener?.remove()
    }
}
