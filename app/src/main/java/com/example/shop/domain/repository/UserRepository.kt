package com.example.shop.domain.repository

// domain/repository/UserRepository.kt
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.shop.domain.model.RequestToChangeRole
import com.example.shop.domain.model.User

interface UserRepository {
    fun deleteRequest(uid: String)
    fun sendRequestToChangeRole(
        context: Context,
        requestToChangeRole: MutableLiveData<RequestToChangeRole?>
    )
    fun getRequestToChangeRole(
        requestToChangeRole: MutableLiveData<RequestToChangeRole?>
    )
    fun removeUserRequestListener()
    fun deleteUserAccount(
        context: Context,
        password: String,
        onAccountDeleted: () -> Unit
    )
    fun updateUserPassword(
        context: Context,
        currentPassword: String,
        newPassword: String,
        onPasswordChanged: () -> Unit
    )
    fun updateUserData(
        uid: String,
        newUserData: User,
        navController: NavController?,
        context: Context
    )
    fun deleteUserDataByUid(
        context: Context,
        uid: String,
        user: User?
    )
}
