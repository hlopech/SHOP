package com.example.shop.repositories

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.shop.data.DeletedUser
import com.example.shop.data.RequestToChangeRole
import com.example.shop.data.User
import com.example.shop.viewModels.AuthState
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class UserRepository {
    private val auth = Firebase.auth
    private val fs = Firebase.firestore
    private val storage = Firebase.storage
    private var requestListener: ListenerRegistration? = null

    fun deleteRequest(uid: String) {
        fs.collection("requestsToChangeRole").document(uid).delete()
    }

    fun sendRequestToChangeRole(
        context: Context,
        requestToChangeRole: MutableLiveData<RequestToChangeRole?>
    ) {
        val request = RequestToChangeRole(auth.uid!!, "waiting")
        fs.collection("requestsToChangeRole").document(auth.uid!!)
            .set(request)
            .addOnSuccessListener {
                requestToChangeRole.value = request
                Toast.makeText(context, "Запрос на изменение роли отправлен", Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Ошибка отправки запроса: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    fun getRequestToChangeRole(
        requestToChangeRole: MutableLiveData<RequestToChangeRole?>
    ) {
        val uid = auth.uid ?: return

        requestListener = fs.collection("requestsToChangeRole").document(uid)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    requestToChangeRole.value = null
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val request = snapshot.toObject(RequestToChangeRole::class.java)
                    requestToChangeRole.value = request
                } else {
                    requestToChangeRole.value = null
                }
            }
    }

    fun removeRequestListener() {
        requestListener?.remove()
    }

    fun deleteUserAccount(
        context: Context,
        password: String,
        onAccountDeleted: () -> Unit
    ) {
        val user = auth.currentUser
        if (user != null) {
            val credential = EmailAuthProvider.getCredential(user.email!!, password)
            user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    val userDocRef = fs.collection("users").document(user.uid)
                    userDocRef.get().addOnSuccessListener { document ->
                        if (document.exists()) {
                            val imageUrl = document.getString("imageUrl")
                            if (!imageUrl.isNullOrEmpty()) {
                                val imageRef = storage.getReferenceFromUrl(imageUrl)
                                imageRef.delete().addOnCompleteListener { deleteImageTask ->
                                    if (deleteImageTask.isSuccessful) {
                                        userDocRef.delete()
                                            .addOnCompleteListener { deleteUserDocTask ->
                                                if (deleteUserDocTask.isSuccessful) {
                                                    user.delete()
                                                        .addOnCompleteListener { deleteUserTask ->
                                                            if (deleteUserTask.isSuccessful) {
                                                                Toast.makeText(
                                                                    context,
                                                                    "Аккаунт успешно удален",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                                onAccountDeleted()
                                                            } else {
                                                                Toast.makeText(
                                                                    context,
                                                                    "Ошибка при удалении аккаунта",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                        }
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Ошибка при удалении данных пользователя",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Ошибка при удалении аватарки",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else {
                                userDocRef.delete().addOnCompleteListener { deleteUserDocTask ->
                                    if (deleteUserDocTask.isSuccessful) {
                                        user.delete().addOnCompleteListener { deleteUserTask ->
                                            if (deleteUserTask.isSuccessful) {
                                                Toast.makeText(
                                                    context,
                                                    "Аккаунт успешно удален",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                onAccountDeleted()
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Ошибка при удалении аккаунта",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Ошибка при удалении данных пользователя",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Пользовательские данные не найдены",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Ошибка аутентификации, неверный пароль",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun updateUserPassword(
        context: Context,
        currentPassword: String,
        newPassword: String,
        onPasswordChanged: () -> Unit
    ) {
        val user = auth.currentUser
        if (user != null) {
            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
            user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    user.updatePassword(newPassword).addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            Toast.makeText(context, "Пароль успешно изменен", Toast.LENGTH_SHORT)
                                .show()
                            onPasswordChanged()
                        } else {
                            Toast.makeText(
                                context,
                                "Ошибка при изменении пароля",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Ошибка аутентификации, неверный текущий пароль",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun updateUserData(
        uid: String,
        newUserData: User,
        navController: NavController?,
        context: Context
    ) {
        fs.collection("users").document(uid).set(newUserData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (navController != null) {
                    navController?.navigate("profile")
                    Toast.makeText(context, "Данные успешно обновленны", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Успешно добавленно в корзину", Toast.LENGTH_SHORT)
                        .show()

                }
            } else {
                Toast.makeText(
                    context,
                    "Произошла ошибка ,данные не обновленны",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }

    }

    fun deleteUserDataByUid(
        context: Context,
        uid: String,
        user: User?
    ) {

        val userDocRef = fs.collection("users").document(uid)
        userDocRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val imageUrl = document.getString("imageUrl")
                if (!imageUrl.isNullOrEmpty()) {
                    val imageRef = storage.getReferenceFromUrl(imageUrl)
                    imageRef.delete().addOnCompleteListener { deleteImageTask ->
                        if (deleteImageTask.isSuccessful) {
                            userDocRef.delete().addOnCompleteListener { deleteUserDocTask ->
                                fs.collection("deletedUsers").document(user?.email!!)
                                    .set(DeletedUser(uid, user?.email!!))

                                if (deleteUserDocTask.isSuccessful) {
                                    Toast.makeText(
                                        context,
                                        "Данные пользователя успешно удалены",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                } else {
                                    Toast.makeText(
                                        context,
                                        "Ошибка при удалении данных пользователя",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Ошибка при удалении аватарки",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    userDocRef.delete().addOnCompleteListener { deleteUserDocTask ->
                        if (deleteUserDocTask.isSuccessful) {
                            fs.collection("deletedUsers").document(user?.email!!)
                                .set(DeletedUser(uid, user?.email!!))
                            Toast.makeText(
                                context,
                                "Данные пользователя успешно удалены",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Ошибка при удалении данных пользователя",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                Toast.makeText(
                    context,
                    "Пользовательские данные не найдены",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


}


