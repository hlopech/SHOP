package com.example.shop.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shop.data.RequestToChangeRole
import com.example.shop.repositories.UserRepository

class ChangeRoleViewModel : ViewModel() {
    val userRepository = UserRepository()
    private val _requestToChangeRole = MutableLiveData<RequestToChangeRole?>()
    var requestToChangeRole: LiveData<RequestToChangeRole?> = _requestToChangeRole

    fun deleteRequest(uid: String) {
        userRepository.deleteRequest(uid)
    }

    fun sendRequest(context: Context) {
        userRepository.sendRequestToChangeRole(context, _requestToChangeRole)
    }

    init {
        getRequest()
    }

    fun getRequest() {
        userRepository.getRequestToChangeRole(_requestToChangeRole)
    }

    override fun onCleared() {
        super.onCleared()
        userRepository.removeRequestListener()
    }
}