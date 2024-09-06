package com.example.shop.views

import RatingDialog
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shop.data.Product
import com.example.shop.ui.theme.LightBlue
import com.example.shop.ui.theme.LightGray
import com.example.shop.ui.theme.Orange
import com.example.shop.utils.openGoogleMaps
import com.example.shop.viewModels.MainViewModel
import com.example.shop.viewModels.ShoppingCartViewModel
import com.example.shop.views.UI.ProductCard
import kotlin.math.roundToInt

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlacingOrderScreen(navController: NavController, viewModel: MainViewModel) {
    val currentUser = viewModel.currentUser.observeAsState()
    var order = viewModel.order.observeAsState()
    val shoppingCartViewModel = ShoppingCartViewModel()

    val context = LocalContext.current
    val totalPrice = remember {
        mutableStateOf(0f)
    }
    val showRatingDialog = remember { mutableStateOf(false) }


    LaunchedEffect(order.value) {

        totalPrice.value =
            (order.value?.filter { it.count > 0 }?.sumOf { it?.price ?: 0 } ?: 0).toFloat()
    }
    Scaffold(
    )
    {
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
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(0.dp, 0.dp, 20.dp, 20.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
                ) {

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            viewModel.clearOrder()
                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                modifier = Modifier.size(50.dp),
                                contentDescription = "213",
                                tint = Color.White
                            )
                        }
                        Column(
                            modifier = Modifier.padding(10.dp),
                        ) {
                            Text(
                                "Оформление заказа",
                                style = TextStyle(fontSize = 20.sp, color = Color.White)
                            )
                            Text(
                                "${order.value?.filter { it.count > 0 }?.size} товара , ${totalPrice.value} р.",
                                style = TextStyle(
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight(700),
                                    color = Color.LightGray
                                )
                            )


                        }
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))


                TextButton(
                    onClick = { openGoogleMaps(context, currentUser.value!!.address) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(LightBlue)
                ) {

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "Адрес доставки",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight(900),
                                color = Color.White
                            ),
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,

                            ) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = "",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(
                                currentUser?.value?.address ?: "",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight(900),
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))


                LazyRow(modifier = Modifier) {
                    if (order.value.isNullOrEmpty()) {

                    } else {
                        item {
                            order?.value?.filter { it.count > 0 }?.forEach { product ->
                                ElevatedButton(
                                    elevation = ButtonDefaults.elevatedButtonElevation(10.dp),
                                    onClick = {
                                        viewModel.viewedProduct.value = product
                                        navController.navigate("product")
                                    },
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
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
                                            shape = RoundedCornerShape(5.dp),
                                            modifier = Modifier.size(150.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                AsyncImage(
                                                    model = product.imageUrl,
                                                    contentDescription = "",
                                                    modifier = Modifier.size(150.dp),
                                                    contentScale = ContentScale.Crop
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(
                                            text = "${product?.price} р.",
                                            style = TextStyle(
                                                fontSize = 20.sp,
                                                color = MaterialTheme.colorScheme.secondary
                                            ),
                                            modifier = Modifier.align(Alignment.Start)
                                        )

                                        Text(
                                            text = product.name,
                                            color = MaterialTheme.colorScheme.secondary,
                                            style = TextStyle(
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight(400),
                                            ),
                                            modifier = Modifier
                                                .fillMaxWidth(0.9f)
                                                .widthIn(max = 160.dp),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                            }
                        }
                    }

                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(LightBlue)

                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)

                    ) {


                        Text(
                            "Срок доставки",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight(900),
                                color = Color.White
                            ),
                        )
                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            "В течении 3-5 дней",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight(900),
                                color = Color.White
                            ),
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            "Доставка осуществляется почтой по указанному вами адресу в профиле. ",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight(600),
                                color = Color.White
                            ),
                        )


                    }

                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Итого ",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight(600),
                                color = MaterialTheme.colorScheme.primary
                            ),
                        )
                        Text(
                            text = "${totalPrice.value + 5f} р.",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight(600),
                                color = MaterialTheme.colorScheme.primary
                            ),
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${order.value?.filter { it.count > 0 }?.size} товар на сумму ",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight(500),
                                color = Color.Gray
                            ),
                        )
                        Text(
                            text = "${totalPrice.value} р.",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight(500),
                                color = Color.Gray
                            ),
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Доставка",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight(500),
                                color = Color.Gray
                            ),
                        )
                        Text(
                            text = "5 р.",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight(500),
                                color = Color.Gray
                            ),
                        )
                    }
                }

                Spacer(modifier = Modifier.fillMaxHeight(0.05f))


                ElevatedButton(
                    onClick = { showRatingDialog.value = true },
                    colors = ButtonDefaults.buttonColors(Orange),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Заказать",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight(500),
                                color = Color.White
                            ),
                        )
                        Text(
                            text = "${totalPrice.value + 5f} р.",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight(500),
                                color = Color.White
                            ),
                        )
                    }
                }

                if (showRatingDialog.value) {
                    RatingDialog(
                        navController,
                        products = order.value!!.filter { it.count > 0 },
                        onDismiss = { showRatingDialog.value = false },
                        onRate = { product, rating ->

                            val newRating =
                                ((product.rating * product.sales) + rating) / (product.sales + 1)

                            product.sales += 1
                            product.reviews += 1
                            product.count -= 1
                            Log.d("ASD", newRating.toString())
                            product.rating = (newRating*10).roundToInt()/10.0
                            viewModel.updateProduct(product)
                            currentUser?.value?.shoppingCart?.remove(product.id)
                            currentUser?.value?.orderHistory?.add(product.id)
                            shoppingCartViewModel.deleteProductFromShoppingCart(
                                currentUser.value?.uid!!,
                                currentUser.value!!,
                                navController,
                                context
                            )

                        }
                    )
                }
            }
        }
    }
}