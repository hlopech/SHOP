package com.example.shop.presentation.common.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shop.domain.model.RequestToChangeRole
import com.example.shop.domain.useCases.DeleteRequestUseCase
import com.example.shop.domain.useCases.GetRequestToChangeRoleUseCase
import com.example.shop.domain.useCases.RemoveUserRequestListenerUseCase
import com.example.shop.domain.useCases.SendRequestToChangeRoleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeRoleViewModel @Inject constructor(
    private val deleteRequestUseCase: DeleteRequestUseCase,
    private val sendRequestToChangeRoleUseCase: SendRequestToChangeRoleUseCase,
    private val getRequestToChangeRoleUseCase: GetRequestToChangeRoleUseCase,
    private val removeUserRequestListenerUseCase: RemoveUserRequestListenerUseCase
) : ViewModel() {
    private val _requestToChangeRole = MutableLiveData<RequestToChangeRole?>()
    var requestToChangeRole: LiveData<RequestToChangeRole?> = _requestToChangeRole

    fun deleteRequest(uid: String) {
        deleteRequestUseCase.invoke(uid)
    }

    fun sendRequest(context: Context) {
        sendRequestToChangeRoleUseCase.invoke(context, _requestToChangeRole)
    }

    init {
        getRequest()
    }

    fun getRequest() {
        getRequestToChangeRoleUseCase.invoke(_requestToChangeRole)
    }

    override fun onCleared() {
        super.onCleared()
        removeUserRequestListenerUseCase.invoke()
    }
}