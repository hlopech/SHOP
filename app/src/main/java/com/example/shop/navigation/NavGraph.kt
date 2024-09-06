package com.example.shop.navigation

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.shop.viewModels.AuthState
import com.example.shop.views.HomeScreen
import com.example.shop.viewModels.MainViewModel
import com.example.shop.views.AdminChangeUserRole
import com.example.shop.views.AllProducts
import com.example.shop.views.AllUsersScreen
import com.example.shop.views.CategoriesScreen
import com.example.shop.views.CategorySettings
import com.example.shop.views.ChangePasswordScreen
import com.example.shop.views.ChangeUserRole
import com.example.shop.views.CreateProduct
import com.example.shop.views.DeleteAccountScreen
import com.example.shop.views.FavoritesScreen
import com.example.shop.views.RegisterScreen
import com.example.shop.views.LoginScreen
import com.example.shop.views.OrderHistoryScreen
import com.example.shop.views.PersonalUserData
import com.example.shop.views.PlacingOrderScreen
import com.example.shop.views.ProductScreen
import com.example.shop.views.ProfileScreen
import com.example.shop.views.RequestToPublicProductScreen
import com.example.shop.views.SearchResultScreen
import com.example.shop.views.SearchScreen
import com.example.shop.views.SellerProducts
import com.example.shop.views.SellerScreen
import com.example.shop.views.SettingScreen
import com.example.shop.views.ShoppingCartScreen
import com.example.shop.views.UserScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavGraph(
    navHostController: NavHostController,
    navController: NavController,
    viewModel: MainViewModel
) {
    val authState by viewModel.authState.observeAsState()
    val startScreenPath = if (authState == AuthState.Authenticated) "home" else "login"

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val showBottomBar = when (currentBackStackEntry?.destination?.route) {
        "login", "register", "product" -> false
        else -> true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController = navHostController)
            }
        }
    ) {
        NavHost(navController = navHostController, startDestination = startScreenPath) {
            composable("login") {
                LoginScreen(navController, viewModel)
            }
            composable("register") {
                RegisterScreen(navController, viewModel)
            }
            composable("home") {
                HomeScreen(navController, viewModel)
            }
            composable("profile") {
                ProfileScreen(navController, viewModel)
            }
            composable("personalUserData") {
                PersonalUserData(navController, viewModel)
            }
            composable("changePassword") {
                ChangePasswordScreen(navController, viewModel)
            }
            composable("deleteAccount") {
                DeleteAccountScreen(navController, viewModel)
            }
            composable("changeRole") {
                ChangeUserRole(navController, viewModel)
            }
            composable("adminChangeUserRole") {
                AdminChangeUserRole(navController, viewModel)
            }
            composable("setting") {
                SettingScreen(navController, viewModel)
            }
            composable("allUsers") {
                AllUsersScreen(navController, viewModel)
            }
            composable("sellerProducts") {
                SellerProducts(navController, viewModel)
            }

            composable("allProducts") {
                AllProducts(navController, viewModel)
            }
            composable("createProduct") {
                CreateProduct(navController, viewModel)
            }
            composable("product") {
                ProductScreen(navController, viewModel)
            }
            composable("sellerScreen") {
                SellerScreen(navController, viewModel)
            }

            composable("userScreen") {
                UserScreen(navController, viewModel)
            }
            composable("shoppingCart") {
                ShoppingCartScreen(navController, viewModel)
            }
            composable("favorites") {
                FavoritesScreen(navController, viewModel)
            }
            composable("requestsToPublishProduct") {
                RequestToPublicProductScreen(navController, viewModel)
            }
            composable("categorySettings") {
                CategorySettings(navController, viewModel)
            }
            composable("categoriesScreen") {
                CategoriesScreen(navController, viewModel)
            }
            composable("searchResult") {
                SearchResultScreen(navController, viewModel)
            }
            composable("searchScreen") {
                SearchScreen(navController, viewModel)
            }
            composable("placingOrder") {
                PlacingOrderScreen(navController, viewModel)
            }
            composable("orderHistory") {
                OrderHistoryScreen(navController, viewModel)
            }

        }
    }
}