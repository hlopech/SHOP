package com.example.shop.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shop.data.Product
import com.example.shop.ui.theme.LightBlue
import com.example.shop.viewModels.MainViewModel
import com.example.shop.viewModels.ProductViewModel
import com.example.shop.views.UI.ProductCard

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun AllProducts(navController: NavController, viewModel: MainViewModel) {
    val currentUser = viewModel.currentUser.observeAsState()
    val productViewModel = ProductViewModel()
    var products = productViewModel.products.observeAsState()
    productViewModel.getAllProducts()

    var showDropDownMenu by remember {
        mutableStateOf(false)
    }
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

    LaunchedEffect(applyChanges.value) {

        sortedProducts = when (selectedOption) {
            "Выше рейтинг" -> products.value?.sortedByDescending { it?.rating }
            "Дороже" -> products.value?.sortedByDescending { it?.price }
            "Дешевле" -> products.value?.sortedBy { it?.price }
            "Новинки" -> products.value?.sortedByDescending { it?.timeCreated }
            else -> products.value
        } ?: emptyList()

        sortedProducts = sortedProducts.filter {
            it?.price?.toFloat()!! >= sliderPrice.start && it?.price?.toFloat()!! <= sliderPrice.endInclusive
        }
        if (ratingSwitch) sortedProducts = sortedProducts.filter { it?.rating!! >= 4.5 }

    }

    LaunchedEffect(products.value) {
        if (products.value != null && products.value?.size != 0) {
            allSales.value = products.value?.sumOf { it?.sales ?: 0 } ?: 0
            allReviews.value = products.value?.sumOf { it?.reviews ?: 0 } ?: 0

            averageRating.value =
                (products.value?.sumOf {
                    it?.rating!!
                }!!).toDouble() / products.value?.size!!
            minPrice.value = products.value?.minOf { it?.price!! }?.toFloat() ?: 0f
            maxPrice.value = products.value?.maxOf { it?.price!! }?.toFloat() ?: 100000f

            sliderPrice = minPrice.value..maxPrice.value

        }
        applyChanges.value = !applyChanges.value
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Все товары",color=MaterialTheme.colorScheme.secondary) },
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(MaterialTheme.colorScheme.background)
                ,
                actions = {
                    IconButton(onClick = { showDropDownMenu = !showDropDownMenu }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            modifier = Modifier.size(50.dp),
                            contentDescription = "options",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    DropdownMenu(
                        expanded = showDropDownMenu,
                        onDismissRequest = { showDropDownMenu = false }) {


                        DropdownMenuItem(
                            text = { Text(text = "Обновить",color=MaterialTheme.colorScheme.secondary) },
                            onClick = {
                                filtersShowBottomSheet = false
                                sortsShowBottomSheet = false
                                productViewModel.refreshData()
                            })
                        DropdownMenuItem(
                            text = { Text(text = "Сортировки",color=MaterialTheme.colorScheme.secondary) },
                            onClick = {
                                filtersShowBottomSheet = false
                                sortsShowBottomSheet = true
                            })

                        DropdownMenuItem(
                            text = { Text(text = "Фильтры",color=MaterialTheme.colorScheme.secondary) },
                            onClick = {
                                sortsShowBottomSheet = false
                                filtersShowBottomSheet = true
                            })


                    }
                }
            )
        }
    )
    {
        if (currentUser.value == null || products.value?.size == 0) {
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
                                        colors = RadioButtonDefaults.colors(LightBlue)
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
                                 filtersShowBottomSheet = false
                                sortsShowBottomSheet = false
                                productViewModel.refreshData()
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


            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(80.dp))

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(150.dp),
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(bottom = 80.dp)
                ) {
                    items(sortedProducts,
                    ) { product ->
                        product?.let {
                            ProductCard(
                                product = it,
                                navController = navController,
                                viewModel = viewModel
                            )
                        }

                    }
                }
            }
        }
    }
}