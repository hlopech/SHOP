package com.example.shop.views.UI

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.example.shop.data.User
import com.example.shop.ui.theme.LightBlue
import com.example.shop.viewModels.MainViewModel
import com.example.shop.viewModels.ProductViewModel

@Composable
fun ProductCard(
    product: Product,
    navController: NavController,
    viewModel: MainViewModel,
) {
    val currentUser = viewModel.currentUser.observeAsState()
    val productViewModel = ProductViewModel()
    val inUserShoppingCart = remember {
        mutableStateOf(currentUser.value?.shoppingCart?.contains(product.id))
    }
    val inUserFavorites = remember {
        mutableStateOf(currentUser.value?.favorites?.contains(product.id))
    }
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
        ),
        modifier = Modifier
            .padding(5.dp)
            .clickable {
                viewModel.viewedProduct.value = product
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
                        model = product?.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp),
                        contentScale = ContentScale.Crop,
                    )
                }
                IconButton(onClick = {

                    if (inUserFavorites.value == false) {
                        Toast.makeText(context, "Добавлено в избранное", Toast.LENGTH_SHORT).show()
                        currentUser.value?.favorites?.add(product.id)
                        inUserFavorites.value = !inUserFavorites.value!!
                        productViewModel.addProductToFavorites(
                            currentUser.value?.uid!!,
                            currentUser.value!!,
                            navController,
                            context
                        )
                    } else {
                        Toast.makeText(context, "Удалено из избранных", Toast.LENGTH_SHORT).show()
                        currentUser.value?.favorites?.remove(product.id)
                        inUserFavorites.value = !inUserFavorites.value!!

                        productViewModel.addProductToFavorites(
                            currentUser.value?.uid!!,
                            currentUser.value!!,
                            navController,
                            context
                        )
                    }

                }) {

                    Icon(
                        imageVector = when (inUserFavorites.value) {
                            true -> Icons.Default.Favorite
                            else -> Icons.Default.FavoriteBorder
                        },
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(30.dp)

                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Column(modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(
                    text = "${product?.price} р.",
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product?.name!!,
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

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "",
                        tint = Color(0xFFFFA500)
                    )
                    Text(text = product.rating.toString(), style = TextStyle(fontSize = 17.sp))
                }
            }
        }
        ElevatedButton(
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .padding(5.dp)
                .align(Alignment.CenterHorizontally),
            onClick = {
                if (inUserShoppingCart.value == false) {
                    Toast.makeText(context, "Добавлено в корзину", Toast.LENGTH_SHORT).show()
                    currentUser.value?.shoppingCart?.add(product.id)
                    inUserShoppingCart.value = !inUserShoppingCart.value!!
                    productViewModel.addProductToShoppingCart(
                        currentUser.value?.uid!!,
                        currentUser.value!!,
                        navController,
                        context
                    )
                } else {
                    Toast.makeText(context, "Удалено из корзины", Toast.LENGTH_SHORT).show()
                    currentUser.value?.shoppingCart?.remove(product.id)
                    inUserShoppingCart.value = !inUserShoppingCart.value!!

                    productViewModel.addProductToShoppingCart(
                        currentUser.value?.uid!!,
                        currentUser.value!!,
                        navController,
                        context
                    )
                }
            },
            elevation = ButtonDefaults.elevatedButtonElevation(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = when (inUserShoppingCart.value) {
                    true -> Color.Gray
                    else -> MaterialTheme.colorScheme.primary
                },
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = ""
                )



                Text(
                    text = when (inUserShoppingCart.value) {
                        true -> "В корзине"
                        else -> "В корзину"
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
