package com.example.shop.views

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shop.ui.theme.Gray
import com.example.shop.ui.theme.LightBlue
import com.example.shop.viewModels.MainViewModel
import com.example.shop.viewModels.ProductViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(navController: NavController, viewModel: MainViewModel) {
    val currentUser = viewModel.currentUser.observeAsState()
    val product = viewModel.viewedProduct
    val productViewModel = ProductViewModel()
    val context = LocalContext.current
    var showDropDownMenu by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }
    val inUserShoppingCart = remember {
        mutableStateOf(currentUser.value?.shoppingCart?.contains(product.value?.id))
    }
    val inUserFavorites = remember {
        mutableStateOf(currentUser.value?.favorites?.contains(product.value?.id))
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("") },
                navigationIcon = {
                    FloatingActionButton(
                        onClick = { navController.popBackStack() },
                        containerColor = MaterialTheme.colorScheme.primary, modifier = Modifier
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            modifier = Modifier
                                .size(30.dp), contentDescription = "213",
                            tint = Color.White
                        )
                    }

                },
                actions = {
                    if (navController.previousBackStackEntry?.destination?.route != "requestsToPublishProduct") {
                        Row {
                            IconButton(onClick = {

                                if (inUserFavorites.value == false) {
                                    Toast.makeText(
                                        context,
                                        "Добавлено в избранное",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    currentUser.value?.favorites?.add(product.value?.id!!)
                                    inUserFavorites.value = !inUserFavorites.value!!
                                    productViewModel.addProductToFavorites(
                                        currentUser.value?.uid!!,
                                        currentUser.value!!,
                                        navController,
                                        context
                                    )
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Удалено из избранных",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    currentUser.value?.favorites?.remove(product.value?.id!!)
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
                                    }, contentDescription = "", tint = Color.Red,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            if (currentUser.value?.role == "admin") {
                                IconButton(onClick = { showDropDownMenu = true }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "",
                                        tint = Color.Black,
                                        modifier = Modifier.size(30.dp)
                                    )


                                }
                                DropdownMenu(
                                    expanded = showDropDownMenu,
                                    onDismissRequest = { showDropDownMenu = false }) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = "Удалить товар",
                                                color = MaterialTheme.colorScheme.secondary
                                            )
                                        },
                                        onClick = {
                                            showDropDownMenu = false
                                            productViewModel.deleteProduct(
                                                context,
                                                product?.value ?: null
                                            )
                                            navController.popBackStack()
                                        })
                                }

                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)

            )
        }
    )
    {
        if (currentUser.value == null || product.value == null) {
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
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showBottomSheet = false
                        },
                        modifier = Modifier.fillMaxHeight(),
                        sheetState = sheetState,
                        containerColor = MaterialTheme.colorScheme.background
                    ) {
                        Column(modifier = Modifier.fillMaxHeight()) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "О товаре",
                                    style = TextStyle(
                                        fontWeight = FontWeight(900),
                                        fontSize = 25.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    ),
                                    modifier = Modifier
                                        .padding(10.dp),
                                )

                                IconButton(onClick = { showBottomSheet = false }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(30.dp),
                                    )
                                }
                            }

                            Row {
                                Text(
                                    text = "Изменено:",
                                    style = TextStyle(
                                        fontSize = 19.sp,
                                        color = MaterialTheme.colorScheme.secondary
                                    ),
                                    modifier = Modifier
                                        .padding(10.dp),

                                    )

                                Text(
                                    text = product.value?.timeCreated!!.toString(),
                                    style = TextStyle(
                                        fontSize = 19.sp,
                                        color = MaterialTheme.colorScheme.secondary
                                    ),
                                    modifier = Modifier
                                        .padding(10.dp),

                                    )
                            }
                            Text(
                                text = "Описание",

                                style = TextStyle(
                                    fontWeight = FontWeight(900),
                                    fontSize = 23.sp,
                                    color = MaterialTheme.colorScheme.secondary
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),

                                )

                            Text(
                                text = product.value?.description!!,
                                style = TextStyle(
                                    fontSize = 19.sp,
                                    color = MaterialTheme.colorScheme.secondary
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),

                                )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = "Категория ",

                                    style = TextStyle(
                                        fontWeight = FontWeight(900),
                                        fontSize = 20.sp,
                                        color = MaterialTheme.colorScheme.secondary
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .padding(10.dp),

                                    )
                                Text(
                                    text = product.value?.category!!,

                                    style = TextStyle(
                                        fontSize = 17.sp,
                                        color = MaterialTheme.colorScheme.secondary
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),

                                    )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = "Кол-во на складе ",

                                    style = TextStyle(
                                        fontWeight = FontWeight(900),
                                        fontSize = 20.sp,
                                        color = MaterialTheme.colorScheme.secondary
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .padding(10.dp),

                                    )
                                Text(
                                    text = product.value?.count.toString(),

                                    style = TextStyle(
                                        fontSize = 17.sp,
                                        color = MaterialTheme.colorScheme.secondary
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),

                                    )
                            }

                            product.value?.properties?.forEach { prop ->

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = prop.prop,

                                        style = TextStyle(
                                            fontWeight = FontWeight(900),
                                            fontSize = 20.sp,
                                            color = MaterialTheme.colorScheme.secondary
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth(0.5f)
                                            .padding(10.dp),

                                        )
                                    Text(
                                        text = prop.value,

                                        style = TextStyle(
                                            fontSize = 17.sp,
                                            color = MaterialTheme.colorScheme.secondary
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),

                                        )
                                }

                            }


                        }
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {

                    item {

                        AsyncImage(
                            model = viewModel.viewedProduct.value?.imageUrl!!,
                            contentDescription = "",
                            modifier = Modifier
                                .size(450.dp)
                                .background(Color.White)
                        )

                    }
                    item {

                        Text(
                            text = product.value?.price.toString() + "р.",
                            modifier = Modifier.padding(20.dp),
                            style = TextStyle(
                                fontWeight = FontWeight(900),
                                color = MaterialTheme.colorScheme.secondary,
                                fontSize = 20.sp
                            )
                        )
                        Divider()
                    }

                    item {
                        Text(
                            text = product.value?.name!!,
                            modifier = Modifier.padding(20.dp),
                            style = TextStyle(
                                fontWeight = FontWeight(900),
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        )
                        Divider()
                    }
                    item {
                        Row {

                            Card(
                                elevation = CardDefaults.cardElevation(10.dp),
                                modifier = Modifier.padding(
                                    start = 10.dp,
                                    end = 5.dp,
                                    top = 10.dp,
                                    bottom = 10.dp
                                ),
                                shape = RoundedCornerShape(20.dp)

                            ) {

                                Row(
                                    modifier = Modifier
                                        .padding(20.dp)
                                        .height(30.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "",
                                        tint = Color(0xFFFFA500)
                                    )
                                    Text(
                                        text = product.value?.rating!!.toString(),
                                        style = TextStyle(fontSize = 20.sp),

                                        )
                                    Divider(
                                        color = Color.Gray,
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .padding(start = 4.dp, end = 4.dp)
                                            .width(2.dp)
                                    )
                                    Text(
                                        text = "${product.value?.reviews!!} отзывов ",
                                        style = TextStyle(fontSize = 16.sp)
                                    )

                                }
                            }
                            Card(
                                elevation = CardDefaults.cardElevation(10.dp),
                                modifier = Modifier.padding(
                                    start = 5.dp,
                                    end = 10.dp,
                                    top = 10.dp,
                                    bottom = 10.dp
                                ),
                                shape = RoundedCornerShape(20.dp)

                            ) {

                                Row(
                                    modifier = Modifier
                                        .padding(
                                            start = 20.dp,
                                            end = 20.dp,
                                            top = 20.dp,
                                            bottom = 20.dp
                                        )
                                        .height(30.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = "${product.value?.sales!!} покупок ",
                                        style = TextStyle(
                                            fontSize = 17.sp,
                                            textDecoration = TextDecoration.Underline
                                        )
                                    )

                                }
                            }
                        }

                    }
                    item {
                        Card(
                            elevation = CardDefaults.cardElevation(10.dp),
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(30.dp),
                        ) {
                            TextButton(
                                onClick = {
                                    viewModel.viewedUserId.value = product.value?.sellerId
                                    navController.navigate("sellerScreen")
                                },
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = product.value?.sellerName!!, style = TextStyle(
                                            fontSize = 20.sp,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight(900)
                                        )
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Продавец", style = TextStyle(
                                                fontSize = 20.sp,
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight(900)
                                            )
                                        )
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowRight,
                                            contentDescription = "",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }

                                }
                            }
                        }
                    }

                    item {
                        Card(
                            elevation = CardDefaults.cardElevation(10.dp),
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(30.dp),
                        ) {
                            TextButton(
                                onClick = {
                                    showBottomSheet = true
                                },
                            ) {

                                Text(
                                    text = "Характеристики",
                                    modifier = Modifier
                                        .padding(
                                            12.dp
                                        )
                                        .fillMaxWidth(),
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight(600),
                                        color = MaterialTheme.colorScheme.primary

                                    )
                                )
                            }
                        }
                    }

                    item {
                        if (navController.previousBackStackEntry?.destination?.route != "requestsToPublishProduct") {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                ElevatedButton(
                                    enabled = product?.value?.count!! > 0,
                                    modifier = Modifier
                                        .padding(top = 8.dp, bottom = 8.dp),
                                    onClick = {
                                        viewModel.addProductToOrder(product = arrayListOf(product.value!!))
                                        navController.navigate("placingOrder")
                                    },
                                    elevation = ButtonDefaults.elevatedButtonElevation(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(
                                            start = 5.dp,
                                            end = 5.dp,
                                            top = 5.dp,
                                            bottom = 5.dp
                                        ),
                                    ) {


                                        Text(
                                            text = when (product?.value?.count!! > 0) {
                                                true -> "Купить"
                                                false -> "Нет на складе"
                                            },
                                            style = TextStyle(
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight(900),
                                                color = Color.White
                                            ),
                                            modifier = Modifier.padding(
                                                top = 5.dp,
                                                bottom = 5.dp
                                            ),
                                        )
                                    }
                                }
                                ElevatedButton(
                                    modifier = Modifier
                                        .padding(top = 8.dp, bottom = 8.dp),
                                    onClick = {
                                        if (inUserShoppingCart.value == false) {
                                            Toast.makeText(
                                                context,
                                                "Добавлено в корзину",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            currentUser.value?.shoppingCart?.add(product.value?.id!!)
                                            inUserShoppingCart.value = !inUserShoppingCart.value!!
                                            productViewModel.addProductToShoppingCart(
                                                currentUser.value?.uid!!,
                                                currentUser.value!!,
                                                navController,
                                                context
                                            )
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Удалено из корзины",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            currentUser.value?.shoppingCart?.remove(product.value?.id)
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
                                            else -> LightBlue
                                        },
                                    )
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(
                                            top = 5.dp,
                                            bottom = 5.dp
                                        ),
                                    ) {
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
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight(900),
                                                color = Color.White
                                            ),
                                            modifier = Modifier.padding(
                                                top = 5.dp,
                                                bottom = 5.dp
                                            ),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}