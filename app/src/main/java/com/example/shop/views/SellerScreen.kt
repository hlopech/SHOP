package com.example.shop.views

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.example.shop.viewModels.MainViewModel
import com.example.shop.viewModels.SellerScreenViewModel
import com.example.shop.views.UI.ProductCard
import kotlin.math.roundToInt

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SellerScreen(navController: NavController, viewModel: MainViewModel) {

    val context = LocalContext.current
    val currentUser = viewModel.currentUser.observeAsState()
    val sellerScreenViewModel = SellerScreenViewModel()
    val sellerProducts = sellerScreenViewModel.products.observeAsState()

    val sortsSheetState = rememberModalBottomSheetState()
    var sortsShowBottomSheet by remember { mutableStateOf(false) }

    val filtersSheetState = rememberModalBottomSheetState()
    var filtersShowBottomSheet by remember { mutableStateOf(false) }

    val sorts = listOf("Выше рейтинг", "Дороже", "Дешевле", "Новинки")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(sorts[0]) }
    val seller = remember {
        mutableStateOf<User?>(null)
    }

    var ratingSwitch by remember { mutableStateOf(false) }


    val minPrice = remember { mutableStateOf(0f) }
    val maxPrice = remember { mutableStateOf(100000f) }


    sellerScreenViewModel.loadSellerData(
        viewModel.viewedUserId.value.toString(),
        seller
    )
    sellerScreenViewModel.getProductsBySellerId(viewModel.viewedUserId.value.toString())

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

    var showDropDownMenu by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(applyChanges.value) {
//
        sortedProducts = when (selectedOption) {
            "Выше рейтинг" -> sellerProducts.value?.sortedByDescending { it?.rating }
            "Дороже" -> sellerProducts.value?.sortedByDescending { it?.price }
            "Дешевле" -> sellerProducts.value?.sortedBy { it?.price }
            "Новинки" -> sellerProducts.value?.sortedByDescending { it?.timeCreated }
            else -> sellerProducts.value
        } ?: emptyList()

        sortedProducts = sortedProducts.filter {
            it?.price?.toFloat()!! >= sliderPrice.start && it?.price?.toFloat()!! <= sliderPrice.endInclusive
        }
        if (ratingSwitch) sortedProducts = sortedProducts.filter { it?.rating!! >= 4.5 }

    }

    LaunchedEffect(sellerProducts.value) {
        if (sellerProducts.value != null && sellerProducts.value != emptyList<Product>()) {
            allSales.value = sellerProducts.value?.sumOf { it?.sales ?: 0 } ?: 0
            allReviews.value = sellerProducts.value?.sumOf { it?.reviews ?: 0 } ?: 0

            val productsWithSales = sellerProducts.value?.filter { it?.sales != 0 } ?: emptyList()
            val newRating = if (productsWithSales.isNotEmpty()) {
                productsWithSales.sumOf { it?.rating ?: 0.0 } / productsWithSales.size
            } else {
                0.0
            }
            averageRating.value = (newRating * 10).roundToInt() / 10.0

            minPrice.value = sellerProducts?.value?.minOf { it?.price ?: 0 }?.toFloat() ?: 0f
            maxPrice.value = sellerProducts?.value?.maxOf { it?.price ?: 0 }?.toFloat() ?: 100000f

            sliderPrice = minPrice.value..maxPrice.value

        }
        applyChanges.value = !applyChanges.value
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        seller.value?.userName ?: "",
                        color = MaterialTheme.colorScheme.secondary
                    )
                },
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

                    IconButton(onClick = { showDropDownMenu = !showDropDownMenu }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "", tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = showDropDownMenu,
                        onDismissRequest = { showDropDownMenu = false }) {
                        DropdownMenuItem(text = {
                            Text(
                                text = "Обновить",
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }, onClick = {
                            seller.value = null
                            sellerScreenViewModel.loadSellerData(
                                viewModel.viewedUserId.value.toString(),
                                seller
                            )
                        })
                        if (currentUser.value?.role == "admin")
                            DropdownMenuItem(
                                text = { Text(text = "Удалить пользователя", color = MaterialTheme.colorScheme.secondary) },
                                onClick = {
                                    sellerScreenViewModel.deleteUserData(
                                        context,
                                        seller.value?.uid.toString(),
                                        seller.value
                                    )
                                    navController.popBackStack()
                                })

                    }

                }
            )
        }
    )
    {
        if (currentUser.value == null || seller.value == null || sellerProducts.value == null) {
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

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item { Spacer(modifier = Modifier.height(80.dp)) }
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(0.9f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,

                            ) {

                            OutlinedCard(
                                elevation = CardDefaults.cardElevation(20.dp),
                                shape = RoundedCornerShape(80.dp),
                                modifier = Modifier.size(80.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AsyncImage(
                                        model = seller.value?.imageUrl!!,
                                        contentDescription = "",
                                        modifier = Modifier.size(150.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }

                            Row {

                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "",
                                    tint = Color(0xFFFFA500)
                                )

                                Text(
                                    text = averageRating.value.toString(),
                                    style = TextStyle(fontSize = 20.sp)
                                )
                            }
                        }

                        Column() {
                            Text(text = "Продажи ${allSales.value}",color = MaterialTheme.colorScheme.secondary)
                            Row {
                                Text(text = "Отзывы ${allReviews.value}",color = MaterialTheme.colorScheme.secondary)

                            }

                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))

                    Divider()

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
                item {
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Товаров ${sortedProducts?.size}",
                        color = Color.Gray,
                        fontSize = 20.sp,
                        fontWeight = FontWeight(700)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    FlowRow(
                        maxItemsInEachRow = 2
                    ) {
                        sortedProducts
                            ?.forEach { product ->
                                Box(modifier = Modifier.fillMaxSize(0.5f)) {
                                    ProductCard(product!!, navController, viewModel)
                                }
                            }
                    }
                    Spacer(modifier = Modifier.height(100.dp))

                }
            }

        }
    }
}