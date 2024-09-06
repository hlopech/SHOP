package com.example.shop.viewModels

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.example.shop.data.User
import com.example.shop.repositories.AdminRepository
import com.example.shop.repositories.UserRepository

class UserScreenViewModel : ViewModel() {

    private val adminRepository = AdminRepository()
    private val userRepository = UserRepository()




    fun loadUserDataByUid(uid: String, user: MutableState<User?>) {
        adminRepository.loadUserDataByUid(uid, user)
    }

    fun deleteUserData(context: Context, uid: String, user: User?) {
        userRepository.deleteUserDataByUid(context, uid,user)
    }


}