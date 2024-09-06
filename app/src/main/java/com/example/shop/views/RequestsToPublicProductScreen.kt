package com.example.shop.views

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shop.ui.theme.LightBlue
import com.example.shop.viewModels.CreateProductViewModel
import com.example.shop.viewModels.MainViewModel
import com.example.shop.viewModels.ProductViewModel
import com.example.shop.views.UI.ProductCard

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestToPublicProductScreen(navController: NavController, viewModel: MainViewModel) {
    val requests = viewModel.requestsToPublishProduct.observeAsState()
    val context = LocalContext.current
    val currentUser = viewModel.currentUser.observeAsState()
    val createProductViewModel = CreateProductViewModel()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Заявки на рассмотрение", color = MaterialTheme.colorScheme.secondary) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            modifier = Modifier.size(50.dp),
                            contentDescription = "213",
                            tint = Color.Gray
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(MaterialTheme.colorScheme.background)
            )
        }
    ) {

        if (currentUser.value == null && requests.value == null) {
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
                    .padding(bottom = 80.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(80.dp))
                requests.value?.forEach { request ->
                    TextButton(
                        onClick = {
                            viewModel.viewedProduct.value = request?.product
                            navController.navigate("product")
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RectangleShape,

                        ) {

                        Column() {

                            Row(

                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Card(
                                    shape = RectangleShape,
                                    modifier = Modifier
                                ) {
                                    AsyncImage(
                                        model = request?.product?.imageUrl,

                                        contentDescription = "",
                                        modifier = Modifier.background(Color.White)
                                            .padding(
                                                top = 5.dp,
                                                bottom = 5.dp
                                            )
                                            .width(130.dp)
                                            .height(200.dp)
                                    )
                                }
                                Column(
                                    modifier = Modifier.padding(
                                        start = 15.dp,
                                        top = 5.dp,
                                        bottom = 15.dp,
                                        end = 10.dp
                                    )

                                ) {

                                    Text(
                                        text = request?.product?.name.toString(),
                                        style = TextStyle(
                                            fontSize = 19.sp,
                                            color = MaterialTheme.colorScheme.secondary
                                        ),
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = request?.product?.sellerName.toString(),
                                        style = TextStyle(
                                            fontWeight = FontWeight(700),
                                            fontSize = 17.sp,
                                            color = MaterialTheme.colorScheme.secondary
                                        ),
                                        modifier = Modifier
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "Категория : " + request?.product?.category.toString(),
                                        style = TextStyle(
                                            fontSize = 17.sp,
                                            color = MaterialTheme.colorScheme.secondary
                                        ),
                                        modifier = Modifier
                                    )

                                    Spacer(modifier = Modifier.height(45.dp))

                                    Text(
                                        text = "Опубликован : " + request?.product?.timeCreated.toString(),
                                        style = TextStyle(
                                            fontSize = 17.sp,
                                            color = MaterialTheme.colorScheme.secondary
                                        ),
                                        modifier = Modifier
                                    )
                                }

                            }
                            Divider()
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "${request?.product?.price} р.",
                                    style = TextStyle(
                                        fontSize = 25.sp,
                                        color = MaterialTheme.colorScheme.secondary
                                    ),
                                    modifier = Modifier.padding(start = 15.dp)
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    TextButton(onClick = {
                                        createProductViewModel.createProduct(
                                            context,
                                            request?.product!!
                                        )

                                    }) {

                                        Text(
                                            text = "Одобрить",
                                            fontSize = 18.sp,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight(800)

                                        )
                                    }
                                    TextButton(onClick = {
                                        createProductViewModel.dismissRequest(request?.product!!)
                                    }) {

                                        Text(
                                            text = "Отклонить",
                                            fontSize = 18.sp,
                                            color = Color.Red,
                                            fontWeight = FontWeight(800)

                                        )
                                    }
                                }
                            }
                            Divider()

                        }
                    }
                }
            }
        }

    }
}