package com.example.shop.views

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.twotone.Delete
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shop.R
import com.example.shop.data.Product
import com.example.shop.data.User
import com.example.shop.ui.theme.LightBlue
import com.example.shop.viewModels.FavoritesScreenViewModel
import com.example.shop.viewModels.MainViewModel
import com.example.shop.viewModels.ProductViewModel
import com.example.shop.viewModels.SellerScreenViewModel
import com.example.shop.views.UI.ProductCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val currentUser = viewModel.currentUser.observeAsState()
    val favoritesScreenViewModel = FavoritesScreenViewModel()
    val productViewModel = ProductViewModel()

    val favorites = remember { mutableStateOf(ArrayList<Product>()) }


    val sortsSheetState = rememberModalBottomSheetState()
    var sortsShowBottomSheet by remember { mutableStateOf(false) }

    val filtersSheetState = rememberModalBottomSheetState()
    var filtersShowBottomSheet by remember { mutableStateOf(false) }

    val sorts = listOf("Выше рейтинг", "Дороже", "Дешевле", "Новинки")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(sorts[0]) }


    var ratingSwitch by remember { mutableStateOf(false) }


    val minPrice = remember { mutableStateOf(0f) }
    val maxPrice = remember { mutableStateOf(100000f) }


    var sliderPrice by remember {
        mutableStateOf(0f..100000f)
    }

    var allSales = remember {
        mutableIntStateOf(0)
    }
    var allReviews = remember {
        mutableIntStateOf(0)
    }
    var averageRating = remember {
        mutableStateOf(0.0)
    }
    var sortedProducts by remember { mutableStateOf(emptyList<Product?>()) }


    val applyChanges = remember {
        mutableStateOf(true)
    }



    LaunchedEffect(currentUser.value?.favorites) {
        if (currentUser.value?.favorites != null) {
            favorites.value.clear()

            withContext(Dispatchers.IO) {
                val ids = currentUser.value?.favorites ?: listOf()
                val products = ids.mapNotNull { id ->
                    try {
                        favoritesScreenViewModel.getProductByUid(id)
                    } catch (e: Exception) {
                        Log.e("FavoritesScreen", "Error fetching product with UID: $id", e)
                        null
                    }
                }
                withContext(Dispatchers.Main) {
                    favorites.value = products as ArrayList<Product>
                }
            }
        }
    }

    LaunchedEffect(applyChanges.value) {
        sortedProducts = when (selectedOption) {
            "Выше рейтинг" -> favorites.value?.sortedByDescending { it?.rating }
            "Дороже" -> favorites.value?.sortedByDescending { it?.price }
            "Дешевле" -> favorites.value?.sortedBy { it?.price }
            "Новинки" -> favorites.value?.sortedByDescending { it?.timeCreated }
            else -> favorites.value
        } ?: emptyList()

        sortedProducts = sortedProducts.filter {
            it?.price?.toFloat()!! >= sliderPrice.start && it?.price?.toFloat()!! <= sliderPrice.endInclusive
        }
        if (ratingSwitch) sortedProducts = sortedProducts.filter { it?.rating!! >= 4.5 }

    }

    LaunchedEffect(favorites.value) {
        if (favorites.value != null && favorites.value != emptyList<Product>()) {
            allSales.value = favorites.value?.sumOf { it?.sales ?: 0 } ?: 0
            allReviews.value = favorites.value?.sumOf { it?.reviews ?: 0 } ?: 0

            averageRating.value =
                (favorites.value?.sumOf {
                    it?.rating!!
                }!!).toDouble() / favorites.value?.size!!
            minPrice.value = favorites?.value?.minOf { it?.price ?: 0 }?.toFloat() ?: 0f
            maxPrice.value = favorites?.value?.maxOf { it?.price ?: 0 }?.toFloat() ?: 100000f

            sliderPrice = minPrice.value..maxPrice.value

        }
        applyChanges.value = !applyChanges.value
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Избранное", color = MaterialTheme.colorScheme.secondary) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            modifier = Modifier.size(50.dp),
                            contentDescription = "213",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(MaterialTheme.colorScheme.background),
                actions = {
                }
            )
        }
    )
    {

        if (sortsShowBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { sortsShowBottomSheet = false },
                sheetState = sortsSheetState,
                containerColor = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 15.dp, end = 15.dp)
                    ) {
                        Text(
                            text = "Сортировка",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight(700),
                            fontSize = 23.sp
                        )
                        IconButton(onClick = { sortsShowBottomSheet = false }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 15.dp, end = 15.dp),
                        text = "Какие товары показывать сначала",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight(700),
                        fontSize = 20.sp
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .selectableGroup()
                            .padding(start = 15.dp, end = 15.dp)
                    ) {
                        sorts.forEach { sort ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = sort,
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight(300),
                                    fontSize = 18.sp
                                )
                                RadioButton(
                                    selected = (sort == selectedOption),
                                    onClick = { onOptionSelected(sort) },
                                    colors = RadioButtonDefaults.colors(MaterialTheme.colorScheme.primary)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Card(
                        elevation = CardDefaults.cardElevation(10.dp),
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(30.dp)
                    ) {

                        TextButton(onClick = {
                            applyChanges.value = !applyChanges.value
                            sortsShowBottomSheet = false
                        }) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = "Применить",
                                color = Color.White,
                                fontWeight = FontWeight(900),
                                fontSize = 25.sp,
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .padding(10.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(50.dp))

                }
            }
        }
        if (filtersShowBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { filtersShowBottomSheet = false },
                sheetState = filtersSheetState,
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.background

            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 15.dp, end = 15.dp)
                    ) {
                        Text(
                            text = "Фильтры",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight(700),
                            fontSize = 23.sp
                        )
                        IconButton(onClick = { filtersShowBottomSheet = false }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Text(
                                text = "С рейтингом от 4.5",
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight(300),
                                fontSize = 18.sp
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "",
                                tint = Color(0xFFFFA500)
                            )
                        }

                        Switch(
                            checked = ratingSwitch,
                            onCheckedChange = {
                                ratingSwitch = it
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                            )
                        )

                    }

                    Text(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        text = "Диапазон цены",
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight(300),
                        fontSize = 18.sp
                    )




                    RangeSlider(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        value = sliderPrice,
                        onValueChange = { range -> sliderPrice = range },
                        valueRange = minPrice.value..maxPrice.value,
                        onValueChangeFinished = {
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary,

                            )
                    )

                    Text(
                        text = "От ${
                            String.format(
                                "%.1f",
                                sliderPrice.start
                            )
                        }р.                           до ${
                            String.format(
                                "%.1f",
                                sliderPrice.endInclusive
                            )
                        }р.",
                        fontSize = 18.sp,
                        fontWeight = FontWeight(300),
                        color = MaterialTheme.colorScheme.secondary

                        )

                    Spacer(modifier = Modifier.height(20.dp))
                    Card(
                        elevation = CardDefaults.cardElevation(10.dp),
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(30.dp)
                    ) {

                        TextButton(onClick = {
                            applyChanges.value = !applyChanges.value
                            filtersShowBottomSheet = false
                        }) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = "Применить",
                                color = Color.White,
                                fontWeight = FontWeight(900),
                                fontSize = 25.sp,
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .padding(10.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(50.dp))

                }
            }
        }
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                Spacer(modifier = Modifier.height(80.dp))




                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {


                    Row {
                        TextButton(onClick = { sortsShowBottomSheet = true }) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.sort_icon),
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(30.dp)
                                )
                                Text(
                                    text = selectedOption,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight(700)
                                )
                            }
                        }
                    }

                    Row {
                        TextButton(onClick = { filtersShowBottomSheet = true }) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.filter_icon),
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(30.dp)
                                )
                                Text(
                                    text = "Фильтры",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight(700)
                                )
                            }
                        }
                    }
                }


            }
            if (currentUser.value == null || favorites.value == null) {
                item {
                    Box(modifier = Modifier.fillMaxSize()) {

                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(100.dp)
                                .align(Alignment.Center),
                            color = LightBlue,
                            strokeWidth = 4.dp
                        )
                    }
                }
            } else {
                item {
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Товаров ${sortedProducts?.size}",
                        color = Color.Gray,
                        fontSize = 20.sp,
                        fontWeight = FontWeight(700)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    sortedProducts
                        ?.forEach { product ->

                            val inUserShoppingCart = remember {
                                mutableStateOf(currentUser.value?.shoppingCart?.contains(product?.id))
                            }
                            TextButton(
                                onClick = {
                                    viewModel.viewedProduct.value = product
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
                                            modifier = Modifier,
                                            colors = CardDefaults.cardColors(Color.White)
                                        ) {
                                            AsyncImage(
                                                model = product?.imageUrl,

                                                contentDescription = "",
                                                modifier = Modifier
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
                                                text = product?.name.toString(),
                                                style = TextStyle(
                                                    fontSize = 19.sp,
                                                    color = MaterialTheme.colorScheme.secondary
                                                ),
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = product?.sellerName.toString(),
                                                style = TextStyle(
                                                    fontWeight = FontWeight(700),
                                                    fontSize = 17.sp,
                                                    color = MaterialTheme.colorScheme.secondary
                                                ),
                                                modifier = Modifier
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = "Категория : " + product?.category.toString(),
                                                style = TextStyle(
                                                    fontSize = 17.sp,
                                                    color = MaterialTheme.colorScheme.secondary
                                                ),
                                                modifier = Modifier
                                            )

                                            Spacer(modifier = Modifier.height(45.dp))

                                        }

                                    }
                                    Divider()
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "${product?.price} р.",
                                            style = TextStyle(
                                                fontSize = 25.sp,
                                                color = MaterialTheme.colorScheme.secondary
                                            ),
                                            modifier = Modifier.padding(start = 15.dp)
                                        )
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            IconButton(onClick = {
                                                favorites.value?.remove(product)
                                                currentUser.value?.favorites?.remove(
                                                    product?.id
                                                )
                                                applyChanges.value = !applyChanges.value

                                                favoritesScreenViewModel.deleteProductFromFavorites(
                                                    currentUser.value?.uid!!,
                                                    currentUser.value!!,
                                                    navController,
                                                    context
                                                )


                                            }) {

                                                Icon(
                                                    imageVector = Icons.TwoTone.Delete,
                                                    contentDescription = "",
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(30.dp)

                                                )
                                            }
                                            IconButton(onClick = {

                                                if (inUserShoppingCart.value == false) {
                                                    Toast.makeText(
                                                        context,
                                                        "Добавлено в корзину",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    currentUser.value?.shoppingCart?.add(product?.id!!)
                                                    inUserShoppingCart.value =
                                                        !inUserShoppingCart.value!!
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
                                                    currentUser.value?.shoppingCart?.remove(product?.id)
                                                    inUserShoppingCart.value =
                                                        !inUserShoppingCart.value!!

                                                    productViewModel.addProductToShoppingCart(
                                                        currentUser.value?.uid!!,
                                                        currentUser.value!!,
                                                        navController,
                                                        context
                                                    )
                                                }


                                            }) {

                                                Icon(
                                                    imageVector = Icons.Outlined.ShoppingCart,
                                                    contentDescription = "",
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(30.dp)
                                                )
                                            }
                                        }
                                    }
                                    Divider()

                                }
                            }
                        }
                    Spacer(modifier = Modifier.height(100.dp))

                }
            }
        }
    }
}


