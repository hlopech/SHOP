package com.example.shop.domain.useCases

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.shop.domain.model.Category
import com.example.shop.domain.model.RequestToChangeRole
import com.example.shop.domain.model.RequestToCreateProduct
import com.example.shop.domain.model.User
import com.example.shop.domain.repository.AdminRepository
import javax.inject.Inject

class LoadUserDataUseCase @Inject constructor(
    private val repo: AdminRepository
) {
    operator fun invoke(uid: String, user: MutableState<User?>) {
        repo.loadUserDataByUid(uid, user)
    }
}
class GetAllUsersUseCase @Inject constructor(
    private val repo: AdminRepository
) {
    operator fun invoke(users: MutableLiveData<List<User?>>) {
        repo.getAllUsers(users)
    }
}
class UpdateRequestToChangeRoleUseCase @Inject constructor(
    private val repo: AdminRepository
) {
    operator fun invoke(
        response: RequestToChangeRole,
        navController: NavController,
        context: Context
    ) {
        repo.updateRequestToChangeRole(response, navController, context)
    }
}

class GetRequestsToPublishProductUseCase @Inject constructor(
    private val repo: AdminRepository
) {
    operator fun invoke(requests: MutableLiveData<List<RequestToCreateProduct?>>) {
        repo.getRequestsToPublishProduct(requests)
    }
}

class GetRequestsToChangeRoleUseCase @Inject constructor(
    private val repo: AdminRepository
) {
    operator fun invoke(requests: MutableLiveData<List<RequestToChangeRole?>>) {
        repo.getRequestsToChangeRole(requests)
    }
}
class GetAllCategoriesUseCase @Inject constructor(
    private val repo: AdminRepository
) {
    operator fun invoke(categories: MutableLiveData<ArrayList<Category?>>) {
        repo.getAllCategories(categories)
    }
}

class CreateCategoryUseCase @Inject constructor(
    private val repo: AdminRepository
) {
    operator fun invoke(context: Context, category: Category) {
        repo.createCategory(context, category)
    }
}

class DeleteCategoryUseCase @Inject constructor(
    private val repo: AdminRepository
) {
    operator fun invoke(
        category: Category,
        categories: MutableLiveData<ArrayList<Category?>>
    ) {
        repo.deleteCategory(category, categories)
    }
}

class UpdateCategoryUseCase @Inject constructor(
    private val repo: AdminRepository
) {
    operator fun invoke(category: Category, context: Context) {
        repo.updateCategory(category, context)
    }
}

class RemoveRequestListenerUseCase @Inject constructor(
    private val repo: AdminRepository
) {
    operator fun invoke() = repo.removeRequestListener()
}