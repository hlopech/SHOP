package com.example.shop.domain.useCases

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.shop.domain.model.User
import com.example.shop.domain.repository.AuthRepository
import com.example.shop.presentation.common.viewmodel.AuthState
import javax.inject.Inject

class LoadUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(currentUser: MutableLiveData<User?>) {
        repository.loadUser(currentUser)
    }
}

class LogInUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(
        email: String,
        password: String,
        authState: MutableLiveData<AuthState>,
        currentUser: MutableLiveData<User?>,
        context: Context
    ) {
        repository.logIn(email, password, authState, currentUser, context)
    }
}

class SignUpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(
        username: String,
        email: String,
        password: String,
        role: String,
        authState: MutableLiveData<AuthState>,
        currentUser: MutableLiveData<User?>,
        context: Context
    ) {
        repository.singUp(username, email, password, role, authState, currentUser, context)
    }
}

class IsUserAuthenticatedUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(authState: MutableLiveData<AuthState>) {
        repository.isUserAuthenticated(authState)
    }
}

class LogOutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(authState: MutableLiveData<AuthState>) {
        repository.logOut(authState)
    }
}
