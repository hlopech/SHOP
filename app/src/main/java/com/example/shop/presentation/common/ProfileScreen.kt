package com.example.shop.presentation.common

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shop.domain.model.ProfileOption
import com.example.shop.ui.theme.LightBlue
import com.example.shop.ui.theme.ProfileIcons
import com.example.shop.presentation.common.viewmodel.MainViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: MainViewModel) {
    val currentUser = viewModel.currentUser.observeAsState()
    val requestsToChangeRole = viewModel.requestsToChangeRole.observeAsState()
    val requestsToPublishProduct = viewModel.requestsToPublishProduct.observeAsState()

    val activeOrders = viewModel.orders.collectAsState()
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

    val adminPanelOptions =
        listOf(
            ProfileOption(
                Icons.Default.Email,
                "Заявки на изменение статуса",
                when (requestsToChangeRole.value) {
                    null -> "0"
                    else -> requestsToChangeRole.value?.size.toString()
                }
            ) { navController.navigate("adminChangeUserRole") },
            ProfileOption(
                Icons.Default.ShoppingCart,
                "Заявки на создания товара",
                when (requestsToPublishProduct.value) {
                    null -> "0"
                    else -> requestsToPublishProduct.value?.size.toString()
                }
            ) { navController.navigate("requestsToPublishProduct") },
            ProfileOption(
                Icons.Default.Menu,
                "Все пользователи", ""
            ) { navController.navigate("allUsers") },
            ProfileOption(
                Icons.Default.List,
                "Все товары",
                ""
            ) { navController.navigate("allProducts") },
            ProfileOption(
                Icons.Default.Settings,
                "Настройка категорий",
                ""
            ) { navController.navigate("categorySettings") }
        )

    val sellerPanelOptions =
        listOf(
            ProfileOption(
                Icons.Default.List,
                "Мои товары",
                ""
            ) { navController.navigate("sellerProducts") },
            ProfileOption(
                Icons.Default.Place,
                "Отправка заказов",
                when (requestsToChangeRole.value) {
                    null -> "0"
                    else -> activeOrders.value?.filter { it.status != "closed"}?.size.toString()
                }
            ) { navController.navigate("manageOrderedProducts") },
            ProfileOption(
                Icons.Default.Add,
                "Создать товар",
                ""
            ) { navController.navigate("createProduct") },
        )
    LaunchedEffect(currentUser.value) {
        if (currentUser.value?.role == "seller" || currentUser.value?.role == "admin") {
            viewModel.getSellerActiveOrders()
        }

    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "") },
//                navigationIcon = {
//                    IconButton(
//                        onClick = { navController.navigate("setting") },
//                        modifier = Modifier.padding(10.dp),
//                        content = {
//                            Icon(
//                                imageVector = Icons.Default.Settings,
//                                modifier = Modifier.size(50.dp),
//                                contentDescription = "213",
//                                tint = Color.Gray
//                            )
//                        }
//                    )
//                },
                actions = {
                    IconButton(
                        onClick = { viewModel.logOut() },
                        modifier = Modifier.padding(10.dp),
                        content = {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                modifier = Modifier.size(50.dp),
                                contentDescription = "213",
                                tint = Color.Red
                            )
                        }
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.Transparent
                )
            )
        }
    ) {
        if (currentUser.value == null) {
            Box(modifier = Modifier.fillMaxSize()) {

                CircularProgressIndicator(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center),
                    color = LightBlue,
                    strokeWidth = 4.dp
                )
            }

        } else {
            Column(modifier = Modifier.fillMaxSize()) {

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Spacer(modifier = Modifier.height(70.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            OutlinedCard(
                                elevation = CardDefaults.cardElevation(20.dp),
                                shape = RoundedCornerShape(150.dp),
                                modifier = Modifier.size(150.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AsyncImage(
                                        model = currentUser.value?.imageUrl,
                                        contentDescription = "",
                                        modifier = Modifier.size(150.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(30.dp))
                            Text(
                                text = currentUser.value?.userName.toString(),
                                style = TextStyle(
                                    fontSize = 26.sp, fontWeight = FontWeight(700),
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            )
                        }


                        Spacer(modifier = Modifier.height(30.dp))


                        Text(
                            text = "Аккаунт",
                            modifier = Modifier.padding(20.dp),
                            style = TextStyle(
                                fontWeight = FontWeight(900), fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                    items(userAccountOptions) { item ->
                        Button(
                            onClick = { item.onClick() },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),
                            shape = RectangleShape,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 5.dp, end = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    tint = ProfileIcons,
                                    modifier = Modifier.size(30.dp)
                                )
                                Spacer(modifier = Modifier.width(15.dp))
                                Text(
                                    text = item.title,
                                    color = MaterialTheme.colorScheme.secondary,
                                    style = TextStyle(
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight(400)
                                    )
                                )
                                Spacer(modifier = Modifier.width(120.dp))
                                Text(
                                    text = item.desc,
                                    color = MaterialTheme.colorScheme.secondary,
                                    style = TextStyle(
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight(400)
                                    ),
                                )

                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    if (currentUser.value?.role!! == "admin") {
                        item {

                            Spacer(modifier = Modifier.height(30.dp))

                            Text(
                                text = "Админ панель",
                                modifier = Modifier.padding(20.dp),
                                style = TextStyle(
                                    fontWeight = FontWeight(900), fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                        items(adminPanelOptions) { item ->
                            Button(
                                onClick = { item.onClick() },
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),
                                shape = RectangleShape,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 5.dp, end = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.title,
                                        tint = ProfileIcons,
                                        modifier = Modifier.size(30.dp)
                                    )
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Text(
                                        text = item.title,
                                        color = MaterialTheme.colorScheme.secondary,
                                        style = TextStyle(
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight(400)
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(35.dp))
                                    Text(
                                        text = item.desc,
                                        color = MaterialTheme.colorScheme.primary,
                                        style = TextStyle(
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight(900)
                                        ),
                                    )

                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                    if (currentUser.value?.role!! == "seller" || currentUser.value?.role!! == "admin") {
                        item {

                            Spacer(modifier = Modifier.height(30.dp))

                            Text(
                                text = "Панель продавца",
                                modifier = Modifier.padding(20.dp),
                                style = TextStyle(
                                    fontWeight = FontWeight(900), fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.primary


                                )
                            )
                        }
                        items(sellerPanelOptions) { item ->
                            Button(
                                onClick = { item.onClick() },
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),
                                shape = RectangleShape,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 5.dp, end = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.title,
                                        tint = ProfileIcons,
                                        modifier = Modifier.size(30.dp)
                                    )
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Text(
                                        text = item.title,
                                        color = MaterialTheme.colorScheme.secondary,
                                        style = TextStyle(
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight(400)
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(35.dp))
                                    Text(
                                        text = item.desc,
                                        color = MaterialTheme.colorScheme.primary,
                                        style = TextStyle(
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight(900)
                                        ),
                                    )

                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        item {
                            Spacer(modifier = Modifier.height(100.dp))
                            Button(
                                onClick = {
                                    navController.navigate("deleteAccount")
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),
                            ) {
                                Text(
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    text = "Удалить аккаунт",
                                    color = Color.Red,
                                    fontSize = 20.sp,
                                )
                            }
                            Spacer(modifier = Modifier.height(100.dp))
                        }

                    }
                }
            }
        }
    }
}