package com.example.shop.viewModels

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.shop.data.ProfileOption
import com.example.shop.data.User

class ProfileViewModel(navController: NavController, currentUser: State<User?>) : ViewModel() {

    val userAccountOptions =
        listOf(
            ProfileOption(
                Icons.Default.AccountCircle,
                "Личные данные",
                ""
            ) { navController.navigate("personalUserData") },
            ProfileOption(
                Icons.Default.Lock,
                "Пароль", ""
            ) { navController.navigate("changePassword") },
            ProfileOption(
                Icons.Default.Favorite,
                "Избранное", ""
            ) { navController.navigate("favorites") },
            ProfileOption(
                Icons.Default.List,
                "История покупок", ""
            ) { navController.navigate("orderHistory") },
            ProfileOption(
                Icons.Default.CheckCircle,
                "Статус аккаунта", currentUser.value?.role.toString()
            ) { navController.navigate("changeRole") }
        )


}