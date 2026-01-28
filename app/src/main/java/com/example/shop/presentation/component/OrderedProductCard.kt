package com.example.shop.presentation.component


import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.sharp.Build
import androidx.compose.material.icons.sharp.Done
import androidx.compose.material.icons.sharp.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.shop.domain.model.OrderedProduct
import com.example.shop.domain.model.Product
import com.example.shop.presentation.common.viewmodel.MainViewModel
import com.example.shop.presentation.common.viewmodel.OrderHistoryViewModel
import com.example.shop.ui.theme.DeliveredState
import com.example.shop.ui.theme.Gray
import com.example.shop.ui.theme.InTransitState
import com.example.shop.ui.theme.LightBlue
import com.example.shop.ui.theme.Orange
import com.example.shop.ui.theme.PlacedState
import kotlin.math.roundToInt


@Composable
fun OrderedProductCard(
    orderedProduct: OrderedProduct,
    navController: NavController,
    viewModel: MainViewModel,
    orderHistoryViewModel: OrderHistoryViewModel
) {
    val product = remember { mutableStateOf<Product?>(null) }
    val isRated = remember { mutableStateOf<Int>(orderedProduct.rated) }
    val context = LocalContext.current
    val showCallSellerDialog = remember { mutableStateOf(false) }
    val sellerNumber = remember { mutableStateOf("") }
    LaunchedEffect(orderedProduct) {
        orderHistoryViewModel.getProductByUID(orderedProduct.productId, product)
        sellerNumber.value =
            orderHistoryViewModel.getSellerNumber(orderedProduct.sellerId).toString()
    }

    Log.d("BBV", orderedProduct.toString())

    if (showCallSellerDialog.value) {
        AlertDialog(
            onDismissRequest = { showCallSellerDialog.value = false },
            title = { Text(text = "${sellerNumber.value}") },
            text = { Text(text = "Вы можете позвонить продавцу для уточнения деталей доставки.") },
            confirmButton = {
                Button(onClick = {
                    showCallSellerDialog.value = false
                }, colors = ButtonDefaults.buttonColors(LightBlue)) {
                    Text("Закрыть")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showCallSellerDialog.value = false
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${sellerNumber.value}")
                        }
                        if (intent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(intent)
                        } else {
                            Toast
                                .makeText(context, "Нет приложения для звонков", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LightBlue)
                ) {
                    Text("Позвонить")
                }
            }
        )
    }
    if (product.value == null) {
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
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
            ),
            modifier = Modifier
                .padding(5.dp)
                .clickable {
                    viewModel.viewedProduct.value = product.value
                    navController.navigate("product")
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .padding(6.dp)
                ) {
                    Card(shape = RoundedCornerShape(20.dp)) {
                        AsyncImage(
                            model = product?.value?.imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(200.dp),
                            contentScale = ContentScale.Crop,
                        )
                    }
                    if (orderedProduct.status != "delivered") {
                        IconButton(onClick = {
                            showCallSellerDialog.value = true
                        }) {

                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = null,
                                tint = LightBlue,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(30.dp)

                            )
                        }
                    }
                    if (orderedProduct.status == "delivered") {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .background(Color.White.copy(alpha = 0.1f)),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            (1..5).forEach { star ->
                                IconButton(
                                    modifier = Modifier.size(35.dp), enabled = isRated.value == 0,
                                    onClick = {
                                        val newRating =
                                            ((product.value!!.rating * product.value!!.sales) + star) / (product.value!!.sales + 1)
                                        product.value!!.sales += 1
                                        product.value!!.reviews += 1
                                        product.value!!.rating =
                                            (newRating * 10).roundToInt() / 10.0
                                        isRated.value = star
                                        orderHistoryViewModel.rateProduct(
                                            product.value!!,
                                            orderedProduct, star
                                        )

                                        Toast.makeText(
                                            context,
                                            "Спасибо за отзыв!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "",
                                        tint = if (isRated.value >= star) Orange else Gray

                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Column(modifier = Modifier.fillMaxWidth(0.8f)) {
//                Text(
//                    text = "${product?.price} р.",
//                    style = TextStyle(
//                        fontSize = 20.sp,
//                        color = MaterialTheme.colorScheme.secondary
//                    ),
//                    modifier = Modifier.align(Alignment.Start)
//                )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = product?.value?.name!!,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.secondary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 200.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(5.dp))

                }
            }
            ElevatedButton(
                modifier = Modifier
                    .fillMaxWidth(0.90f)
                    .padding(5.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = {

                },
                elevation = ButtonDefaults.elevatedButtonElevation(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = when (orderedProduct.status) {
                        "placed" -> PlacedState
                        "inTransit" -> InTransitState
                        else -> DeliveredState
                    },
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = when (orderedProduct.status) {
                            "placed" -> Icons.Sharp.Build
                            "inTransit" -> Icons.Sharp.Place
                            else -> Icons.Sharp.Done
                        },
                        contentDescription = ""
                    )

                    Text(
                        text = when (orderedProduct.status) {
                            "placed" -> "Оформлен"
                            "inTransit" -> "В пути"
                            else -> "Доставлен"
                        },

                        style = TextStyle(
                            fontSize = 17.sp,
                            fontWeight = FontWeight(900),
                            color = Color.White
                        ),
                        modifier = Modifier
                    )
                }
            }
        }
    }
}
