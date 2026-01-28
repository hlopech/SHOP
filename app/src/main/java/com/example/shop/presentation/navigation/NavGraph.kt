package com.example.shop.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.shop.presentation.common.viewmodel.AuthState
import com.example.shop.presentation.common.viewmodel.MainViewModel
import com.example.shop.presentation.admin.AdminChangeUserRole
import com.example.shop.presentation.admin.AllProducts
import com.example.shop.presentation.admin.AllUsersScreen
import com.example.shop.presentation.common.CategoriesScreen
import com.example.shop.presentation.admin.CategorySettings
import com.example.shop.presentation.common.ChangePasswordScreen
import com.example.shop.presentation.common.ChangeUserRole
import com.example.shop.presentation.seller.CreateProduct
import com.example.shop.presentation.common.DeleteAccountScreen
import com.example.shop.presentation.common.FavoritesScreen
import com.example.shop.presentation.common.HomeScreen
import com.example.shop.presentation.auth.LoginScreen
import com.example.shop.presentation.common.OrderHistoryScreen
import com.example.shop.presentation.common.PersonalUserData
import com.example.shop.presentation.common.PlacingOrderScreen
import com.example.shop.presentation.common.ProductScreen
import com.example.shop.presentation.common.ProfileScreen
import com.example.shop.presentation.auth.RegisterScreen
import com.example.shop.presentation.admin.RequestToPublicProductScreen
import com.example.shop.presentation.common.SearchResultScreen
import com.example.shop.presentation.common.SearchScreen
import com.example.shop.presentation.seller.SellerProducts
import com.example.shop.presentation.common.SellerScreen
import com.example.shop.presentation.common.SettingScreen
import com.example.shop.presentation.common.ShoppingCartScreen
import com.example.shop.presentation.common.UserScreen
import com.example.shop.presentation.seller.ManageOrderedProducts


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
            composable("manageOrderedProducts") {
                ManageOrderedProducts(navController, viewModel)
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
            composable(
                "placingOrder/{productId}", arguments = listOf(navArgument("productId") {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                val productId =
                    backStackEntry.arguments?.getString("productId") ?: ""
                PlacingOrderScreen(navController, viewModel, productId)
            }


            composable("orderHistory") {
                OrderHistoryScreen(navController, viewModel)
            }

        }
    }
}