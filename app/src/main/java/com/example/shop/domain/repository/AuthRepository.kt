package com.example.shop.domain.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.shop.domain.model.User
import com.example.shop.presentation.common.viewmodel.AuthState

    interface AuthRepository {
        fun loadUser(currentUser: MutableLiveData<User?>): Unit
        fun logIn(
            email: String,
            password: String,
            authState: MutableLiveData<AuthState>,
            currentUser: MutableLiveData<User?>,
            context: Context
        ):  Unit

        fun singUp(
            username: String,
            email: String,
            password: String,
            role: String,
            authState: MutableLiveData<AuthState>,
            currentUser: MutableLiveData<User?>,
            context: Context
        ):  Unit

        fun logOut(authState: MutableLiveData<AuthState>)

        fun isUserAuthenticated(authState: MutableLiveData<AuthState>):Unit

    }