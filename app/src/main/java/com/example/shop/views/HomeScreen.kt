package com.example.shop.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shop.R
import com.example.shop.data.Product
import com.example.shop.data.User
import com.example.shop.ui.theme.LightBlue
import com.example.shop.ui.theme.LightGray
import com.example.shop.ui.theme.Orange
import com.example.shop.viewModels.HomeScreenViewModel
import com.example.shop.viewModels.MainViewModel
import com.example.shop.viewModels.ProductViewModel
import com.example.shop.views.UI.ProductCard

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: MainViewModel) {
    val currentUser = viewModel.currentUser.observeAsState()
    val homeScreenViewModel = HomeScreenViewModel()
    val users = homeScreenViewModel.users.observeAsState()
    val popularProducts = homeScreenViewModel.popularProduct.observeAsState()
    val recommendedProducts = homeScreenViewModel.recommendedProducts.observeAsState()
    val favoritesProduct = homeScreenViewModel.favoritesProduct.observeAsState()
    val shoppingCart = homeScreenViewModel.shoppingCart.observeAsState()




    if (currentUser.value != null) {
        homeScreenViewModel.getFavoriteProductsByUid(currentUser?.value!!.favorites)
        homeScreenViewModel.getShoppingCartByUid(currentUser?.value!!.shoppingCart)
    }


    LaunchedEffect(favoritesProduct.value, shoppingCart.value) {
        if (favoritesProduct.value != null && shoppingCart.value != null) {
            homeScreenViewModel.getRecommendedProducts(
                favoritesProduct.value!!,
                shoppingCart.value!!
            )

        }
    }


    Scaffold(
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Добро пожаловать , ",
                                color = Color.Gray,
                                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight(400)),
                                modifier = Modifier.padding(bottom = 10.dp, top = 10.dp)
                            )
                            Text(
                                text = currentUser.value?.userName ?: "",
                                color = MaterialTheme.colorScheme.primary,
                                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight(700)),
                                modifier = Modifier.padding(bottom = 10.dp, top = 10.dp)
                            )
                            Text(
                                text = " !",
                                color = Color.Gray,
                                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight(400)),
                                modifier = Modifier.padding(bottom = 10.dp, top = 10.dp)
                            )
                        }
                    }

                    SearchBar(
                        query = "",
                        onQueryChange = {},
                        onSearch = {

                        },
                        active = false,
                        onActiveChange = { navController.navigate("searchScreen") },
                        placeholder = { Text(text = "Поиск", color = Color.Gray) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(30.dp)
                            )
                        },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.voice_icon),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(30.dp)
                            )
                        },
                        colors = SearchBarDefaults.colors(MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {

                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Лучшие продавцы",
                    color = MaterialTheme.colorScheme.primary,
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight(700)),
                    modifier = Modifier.padding(10.dp)
                )
                Divider()

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    if (users.value.isNullOrEmpty()) {
                        items(10) {
                            ElevatedButton(
                                onClick = { },
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
                                border = null
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth(),
                                ) {
                                    Card(
                                        elevation = CardDefaults.cardElevation(5.dp),
                                        shape = RoundedCornerShape(80.dp),
                                        modifier = Modifier.size(80.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            AsyncImage(
                                                model = "user?.imageUrl!!",
                                                contentDescription = "",
                                                modifier = Modifier.size(150.dp),
                                                contentScale = ContentScale.Crop
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(
                                        modifier = Modifier
                                            .align(Alignment.Start)
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "",
                                            color = MaterialTheme.colorScheme.secondary,
                                            style = TextStyle(
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight(400)
                                            ),
                                            modifier = Modifier
                                        )
                                    }

                                    Text(
                                        text = "Loading...",
                                        color = MaterialTheme.colorScheme.secondary,
                                        style = TextStyle(
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight(400),
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth(0.9f)
                                            .widthIn(max = 80.dp),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }

                    } else {
                        item {
                            users?.value?.forEach { user ->

                                ElevatedButton(
                                    onClick = { viewModel.viewedUserId.value = user?.uid
                                              navController.navigate("sellerScreen")},
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
                                    border = null
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .fillMaxWidth(),
                                    ) {
                                        Card(
                                            elevation = CardDefaults.cardElevation(5.dp),
                                            shape = RoundedCornerShape(80.dp),
                                            modifier = Modifier.size(80.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                AsyncImage(
                                                    model = user?.imageUrl,
                                                    contentDescription = "",
                                                    modifier = Modifier.size(150.dp),
                                                    contentScale = ContentScale.Crop
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))



                                        Text(
                                            text = user?.userName ?: "",
                                            color = MaterialTheme.colorScheme.secondary,
                                            style = TextStyle(
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight(400),
                                            ),
                                            modifier = Modifier
                                                .fillMaxWidth(0.9f)
                                                .widthIn(max = 80.dp),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Divider()
                Text(
                    text = "Популярные товары", color = MaterialTheme.colorScheme.primary,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight(700)
                    ),
                    modifier = Modifier
                        .padding(
                            10.dp
                        )
                )
                LazyRow(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                    if (popularProducts.value.isNullOrEmpty()) {
                        items(6) {
                            ProductCard(
                                product = Product(),
                                navController = navController,
                                viewModel = viewModel
                            )

                        }
                    } else {
                        item {
                            popularProducts?.value?.forEach { product ->
                                ProductCard(
                                    product = product!!,
                                    navController = navController,
                                    viewModel = viewModel
                                )

                            }
                        }
                    }

                }
                Divider()

                Text(
                    text = "На основе ваших последних действий",
                    color = MaterialTheme.colorScheme.primary,
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight(700)),
                    modifier = Modifier.padding(10.dp)
                )
                Divider()


                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.heightIn(max = 20000.dp),
                    userScrollEnabled = false,

                ) {
                    if (recommendedProducts?.value.isNullOrEmpty()) {
                        items(6) {
                            ProductCard(
                                product = Product(),
                                navController = navController,
                                viewModel = viewModel
                            )
                        }
                    } else {
                        items(recommendedProducts?.value!!) { product ->
                            ProductCard(
                                product = product!!,
                                navController = navController,
                                viewModel = viewModel
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(100.dp))

            }


        }
    )


}