package com.example.shop.domain.useCases

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.shop.domain.model.RequestToChangeRole
import com.example.shop.domain.model.User
import com.example.shop.domain.repository.UserRepository
import javax.inject.Inject

class DeleteRequestUseCase @Inject constructor(
    private val repo: UserRepository
) {
    operator fun invoke(uid: String) = repo.deleteRequest(uid)
}

class SendRequestToChangeRoleUseCase @Inject constructor(
    private val repo: UserRepository
) {
    operator fun invoke(
        context: Context,
        requestLiveData: MutableLiveData<RequestToChangeRole?>
    ) = repo.sendRequestToChangeRole(context, requestLiveData)
}

class GetRequestToChangeRoleUseCase @Inject constructor(
    private val repo: UserRepository
) {
    operator fun invoke(
        requestLiveData: MutableLiveData<RequestToChangeRole?>
    ) = repo.getRequestToChangeRole(requestLiveData)
}

class RemoveUserRequestListenerUseCase @Inject constructor(
    private val repo: UserRepository
) {
    operator fun invoke() = repo.removeUserRequestListener()
}

class DeleteUserAccountUseCase @Inject constructor(
    private val repo: UserRepository
) {
    operator fun invoke(
        context: Context,
        password: String,
        onDeleted: () -> Unit
    ) = repo.deleteUserAccount(context, password, onDeleted)
}

class UpdateUserPasswordUseCase @Inject constructor(
    private val repo: UserRepository
) {
    operator fun invoke(
        context: Context,
        currentPassword: String,
        newPassword: String,
        onChanged: () -> Unit
    ) = repo.updateUserPassword(context, currentPassword, newPassword, onChanged)
}

class UpdateUserDataUseCase @Inject constructor(
    private val repo: UserRepository
) {
    operator fun invoke(
        uid: String,
        newUserData: User,
        navController: NavController?,
        context: Context
    ) = repo.updateUserData(uid, newUserData, navController, context)
}

class DeleteUserDataByUidUseCase @Inject constructor(
    private val repo: UserRepository
) {
    operator fun invoke(
        context: Context,
        uid: String,
        user: User?
    ) = repo.deleteUserDataByUid(context, uid, user)
}
