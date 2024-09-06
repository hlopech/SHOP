package com.example.shop.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shop.R
import com.example.shop.data.Product
import com.example.shop.ui.theme.LightBlue
import com.example.shop.utils.openGoogleMaps
import com.example.shop.viewModels.MainViewModel
import com.example.shop.viewModels.ShoppingCartViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShoppingCartScreen(navController: NavController, viewModel: MainViewModel) {
    val currentUser = viewModel.currentUser.observeAsState()
    val context = LocalContext.current
    val shoppingCartViewModel = ShoppingCartViewModel()
    val shoppingCart = remember { mutableStateListOf<String>() }

    LaunchedEffect(currentUser.value?.shoppingCart) {
        shoppingCart.clear()
        currentUser.value?.shoppingCart?.let { shoppingCart.addAll(it) }
    }

    Column {

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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(
                        top = 10.dp,
                        bottom = 10.dp
                    )
                ) {

                    Text(
                        text = "Корзина ",
                        color = MaterialTheme.colorScheme.primary,
                        style = TextStyle(
                            fontSize = 25.sp,
                            fontWeight = FontWeight(900)
                        )
                    )
                    Text(
                        text = "${shoppingCart.size} товара ",
                        color = Color.Gray,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight(500)
                        )
                    )
                }

                Divider()

                Text(
                    text = currentUser.value?.address!!, color = Color.Gray,

                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight(400)
                    ),
                    modifier = Modifier
                        .padding(
                            bottom = 10.dp,
                            top = 10.dp
                        )
                        .clickable { openGoogleMaps(context, currentUser.value!!.address) }
                )
                Divider()

            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    shoppingCart.forEach { productUID ->
                        val product = remember { mutableStateOf<Product?>(null) }
                        shoppingCartViewModel.getProductByUid(productUID, product)

                        if (product.value == null) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(30.dp),
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 8.dp
                            )
                        } else {
                            val showDropdownMenu = remember { mutableStateOf(false) }
                            val quantityOfProduct = remember { mutableIntStateOf(1) }

                            Card(
                                shape = RoundedCornerShape(25.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                colors = CardDefaults.cardColors(),
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.viewedProduct.value = product.value
                                        navController.navigate("product")
                                    }
                            ) {
                                Row {
                                    Card(
                                        shape = RoundedCornerShape(30.dp),
                                        modifier = Modifier.padding(15.dp)
                                    ) {
                                        AsyncImage(
                                            model = product.value?.imageUrl,
                                            contentDescription = "",
                                            modifier = Modifier
                                                .width(110.dp)
                                                .height(180.dp)
                                                .background(Color.White)
                                        )
                                    }
                                    Column(
                                        modifier = Modifier.padding(
                                            top = 5.dp,
                                            bottom = 15.dp,
                                            end = 10.dp
                                        )
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            Text(
                                                text = "${product.value?.price!! * quantityOfProduct.value} р.",
                                                style = TextStyle(
                                                    fontSize = 25.sp,
                                                    color = MaterialTheme.colorScheme.secondary
                                                ),
                                                modifier = Modifier
                                            )
                                            IconButton(onClick = {
                                                showDropdownMenu.value = true
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Default.MoreVert,
                                                    contentDescription = ""
                                                )
                                                DropdownMenu(
                                                    expanded = showDropdownMenu.value,
                                                    onDismissRequest = {
                                                        showDropdownMenu.value = false
                                                    }) {
                                                    DropdownMenuItem(
                                                        text = {
                                                            Text(
                                                                text = "Удалить из корзины",
                                                                color = MaterialTheme.colorScheme.secondary
                                                            )
                                                        },
                                                        onClick = {
                                                            shoppingCart.remove(product.value?.id)
                                                            currentUser.value?.shoppingCart?.remove(
                                                                product.value?.id
                                                            )

                                                            shoppingCartViewModel.deleteProductFromShoppingCart(
                                                                currentUser.value?.uid!!,
                                                                currentUser.value!!,
                                                                navController,
                                                                context
                                                            )
                                                            showDropdownMenu.value = false
                                                        })
                                                }
                                            }
                                        }

                                        Text(
                                            text = product.value?.name!!,
                                            style = TextStyle(
                                                fontSize = 17.sp,
                                                color = MaterialTheme.colorScheme.secondary
                                            ),
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = product.value?.sellerName!!,
                                            style = TextStyle(
                                                fontSize = 17.sp,
                                                color = MaterialTheme.colorScheme.secondary
                                            ),
                                            modifier = Modifier
                                        )

                                        Spacer(modifier = Modifier.height(45.dp))
                                        ElevatedButton(
                                            enabled = product?.value?.count!! > 0,
                                            onClick = {

                                                viewModel.addProductToOrder(arrayListOf(product?.value!!))
                                                navController.navigate("placingOrder")
                                            },
                                            colors = ButtonDefaults.buttonColors(Color.White)
                                        ) {
                                            Text(
                                                text = when (product?.value?.count!! > 0) {
                                                    true -> "Купить"
                                                    false -> "Нет на складе"
                                                },
                                                color = MaterialTheme.colorScheme.primary,
                                                fontSize = 16.sp,
                                                modifier = Modifier.padding(
                                                    top = 5.dp,
                                                    bottom = 5.dp
                                                )
                                            )
                                        }
//                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(80.dp))
                }

                FloatingActionButton(
                    onClick = {
                        viewModel.addProductsToOrderByUid(shoppingCart)
                        navController.navigate("placingOrder")
                    },
                    contentColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 100.dp),
                ) {
                    Text(
                        text = "К оформлению",
                        modifier = Modifier.padding(18.dp)
                    )
                }
            }
        }
    }
}
