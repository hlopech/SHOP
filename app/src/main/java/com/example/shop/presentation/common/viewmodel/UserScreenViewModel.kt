package com.example.shop.presentation.common.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.example.shop.domain.model.User
import com.example.shop.domain.useCases.DeleteUserDataByUidUseCase
import com.example.shop.domain.useCases.LoadUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserScreenViewModel @Inject constructor(
    private val loadUserDataUseCase: LoadUserDataUseCase,
    private val deleteUserDataByUidUseCase: DeleteUserDataByUidUseCase


) : ViewModel() {



    fun loadUserDataByUid(uid: String, user: MutableState<User?>) {
        loadUserDataUseCase.invoke(uid, user)
    }

    fun deleteUserData(context: Context, uid: String, user: User?) {
        deleteUserDataByUidUseCase.invoke(context, uid,user)
    }


}