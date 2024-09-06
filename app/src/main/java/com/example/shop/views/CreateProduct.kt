package com.example.shop.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shop.data.Product
import com.example.shop.data.ProductProperty
import com.example.shop.ui.theme.Gray
import com.example.shop.ui.theme.LightBlue
import com.example.shop.utils.setProductImage
import com.example.shop.viewModels.AdminViewModel
import com.example.shop.viewModels.CreateProductViewModel
import com.example.shop.viewModels.MainViewModel
import java.util.Calendar
import java.util.UUID

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProduct(navController: NavController, viewModel: MainViewModel) {
    val currentUser = viewModel.currentUser.observeAsState()
    val context = LocalContext.current
    val adminViewModel = AdminViewModel()
    val categories = adminViewModel.categories.observeAsState()
    val createProductViewModel = CreateProductViewModel()
    val name = remember {
        mutableStateOf("")
    }
    val description = remember {
        mutableStateOf("")
    }
    val price = remember {
        mutableIntStateOf(0)
    }

    val count = remember {
        mutableIntStateOf(0)
    }
    val selectedCategory = remember {
        mutableStateOf("")
    }
    val rating = remember {
        mutableDoubleStateOf(0.0)
    }


    var imageUrl = remember {
        mutableStateOf<String?>(null)
    }

    var properties = remember {
        mutableStateOf(
            ArrayList<ProductProperty>(
                arrayListOf(
                )
            )
        )
    }

    val property = remember {
        mutableStateOf("")
    }
    val value = remember {
        mutableStateOf("")
    }


    val newProduct = Product(
        currentUser.value?.uid!!,
        currentUser.value?.userName!!,
        UUID.randomUUID().toString(),
        name.value,
        description.value,
        imageUrl.value ?: "",
        price.value,
        count.value,
        selectedCategory.value,
        rating.value,
        0,
        0,
        Calendar.getInstance().time,
        properties.value
    )

    val launcher = setProductImage(context, imageUrl, newProduct)

    val modalBottomSheetCategory = remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Создание товара", color = MaterialTheme.colorScheme.secondary) },
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
            )
        }
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
            if (modalBottomSheetCategory.value) {
                ModalBottomSheet(
                    onDismissRequest = { modalBottomSheetCategory.value = false },
                    containerColor = MaterialTheme.colorScheme.background
                ) {

                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = ("Выбор категории"),
                            style = TextStyle(
                                fontSize = 25.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                            ),
                        )

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.padding(bottom = 82.dp, top = 80.dp)
                        ) {
                            if (categories.value == null) {
                                item {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(100.dp),
                                        color = MaterialTheme.colorScheme.primary,
                                        strokeWidth = 4.dp
                                    )
                                }

                            } else {
                                items(categories?.value!!) { category ->
                                    ElevatedButton(
                                        onClick = {
                                            selectedCategory.value = category?.title!!
                                            modalBottomSheetCategory.value = false
                                        },
                                        colors = ButtonDefaults.buttonColors(Color.White),
                                        shape = RectangleShape,
                                        modifier = Modifier.padding(2.dp),
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            AsyncImage(
                                                model = category?.image,
                                                contentDescription = "",
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .aspectRatio(1f),
                                                contentScale = ContentScale.FillWidth
                                            )
                                            Text(
                                                text = category?.title.toString(),
                                                style = TextStyle(
                                                    fontSize = 18.sp,
                                                    color = MaterialTheme.colorScheme.primary,
                                                    fontWeight = FontWeight.Bold,
                                                ), modifier = Modifier.align(Alignment.TopStart)
                                            )


                                        }
                                    }
                                }
                            }
                        }


                    }

                }
            }
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(70.dp))
                LazyColumn(Modifier.fillMaxSize()) {

                    item {
                        Text(
                            text = "Вы можете добавить до трех фотографий для товара.",
                            color = Color.Black,
                            style = TextStyle(
                                fontSize = 17.sp,
                                fontWeight = FontWeight(400)
                            ), modifier = Modifier.padding(15.dp)
                        )
                    }
                    item {
                        Card(
                            elevation = CardDefaults.cardElevation(20.dp),
                            modifier = Modifier
                                .size(130.dp)
                                .padding(15.dp)
                                .clickable {
                                    launcher.launch(
                                        PickVisualMediaRequest(
                                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                },
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {

                                AsyncImage(
                                    model = when (imageUrl.value) {
                                        null -> "https://cdn-icons-png.flaticon.com/512/4211/4211763.png"
                                        else -> imageUrl.value
                                    },
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(130.dp)
                                        .padding(5.dp),
                                    contentScale = ContentScale.Crop
                                )


                            }
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Название товара",
                                color = Gray,
                                fontSize = 20.sp,
                                fontWeight = FontWeight(700),
                                modifier = Modifier
                                    .padding(15.dp)
                                    .align(Alignment.Start)
                            )

                            BasicTextField(
                                value = name.value,
                                onValueChange = { name.value = it },
                                modifier = Modifier.fillMaxWidth(0.9f),
                                textStyle = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.secondary
                                ),
                                singleLine = true,

                                decorationBox = { innerTextField ->
                                    Column {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 4.dp)
                                        ) {
                                            innerTextField()
                                        }
                                        Divider(color = Color.LightGray, thickness = 1.dp)
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(30.dp))

                            Text(
                                text = "Описание продукта",
                                color = Gray,
                                fontSize = 20.sp,
                                fontWeight = FontWeight(700),
                                modifier = Modifier
                                    .padding(15.dp)
                                    .align(Alignment.Start)
                            )

                            BasicTextField(
                                value = description.value,
                                onValueChange = { description.value = it },
                                modifier = Modifier.fillMaxWidth(0.9f),
                                textStyle = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.secondary
                                ),
                                singleLine = true,

                                decorationBox = { innerTextField ->
                                    Column {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 4.dp)
                                        ) {
                                            innerTextField()
                                        }
                                        Divider(color = Color.LightGray, thickness = 1.dp)
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(30.dp))

                            Text(
                                text = "Цена",
                                color = Gray,
                                fontSize = 20.sp,
                                fontWeight = FontWeight(700),
                                modifier = Modifier
                                    .padding(15.dp)
                                    .align(Alignment.Start)
                            )

                            BasicTextField(
                                value = price.value.toString(),
                                onValueChange = { newText ->
                                    if (newText.all { it.isDigit() }) {
                                        price.value = newText.toInt()
                                    }
                                },
                                singleLine = true,

                                modifier = Modifier.fillMaxWidth(0.9f),
                                textStyle = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.secondary
                                ),
                                keyboardOptions =
                                KeyboardOptions(keyboardType = KeyboardType.Number),

                                decorationBox = { innerTextField ->
                                    Column {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 4.dp)
                                        ) {
                                            innerTextField()
                                        }
                                        Divider(color = Color.LightGray, thickness = 1.dp)
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(30.dp))

                            Text(
                                text = "Количество",
                                color = Gray,
                                fontSize = 20.sp,
                                fontWeight = FontWeight(700),
                                modifier = Modifier
                                    .padding(15.dp)
                                    .align(Alignment.Start)
                            )

                            BasicTextField(
                                value = count.value.toString(),
                                onValueChange = { newText ->
                                    if (newText.all { it.isDigit() }) {
                                        count.value = newText.toInt()
                                    }
                                }, modifier = Modifier.fillMaxWidth(0.9f),
                                keyboardOptions =
                                KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,


                                textStyle = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.secondary
                                ),
                                decorationBox = { innerTextField ->
                                    Column {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 4.dp)
                                        ) {
                                            innerTextField()
                                        }
                                        Divider(color = Color.LightGray, thickness = 1.dp)
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(30.dp))

                            TextButton(
                                onClick = { modalBottomSheetCategory.value = true },
                                shape = RectangleShape,
                                modifier = Modifier
                                    .align(Alignment.Start)

                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Text(
                                        text = "Категория",
                                        color = Gray,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight(700),
                                        modifier = Modifier

                                    )
                                    if (selectedCategory.value != "") {
                                        Text(
                                            text = selectedCategory.value,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight(700),
                                            modifier = Modifier
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(35.dp)
                                        )
                                    }

                                }
                            }

                            Text(
                                text = "Характеристики",
                                color = Gray,
                                fontSize = 20.sp,
                                fontWeight = FontWeight(700),
                                modifier = Modifier
                                    .padding(15.dp)
                                    .align(Alignment.Start)
                            )


                            Row(verticalAlignment = Alignment.CenterVertically) {


                                Column {

                                    Row {
                                        Text(
                                            text = "Свойство",
                                            color = Gray,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight(700),
                                            modifier = Modifier
                                                .fillMaxWidth(0.4f)
                                                .padding(start = 15.dp, end = 15.dp)
                                        )

                                        Text(
                                            text = "Значение",
                                            color = Gray,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight(700),
                                            modifier = Modifier
                                                .fillMaxWidth(0.8f)
                                                .padding(start = 15.dp, end = 15.dp)
                                        )

                                    }
                                    Row(
                                        modifier = Modifier.padding(start = 15.dp, end = 15.dp)
                                    ) {
                                        BasicTextField(
                                            value = property.value,
                                            onValueChange = { property.value = it },
                                            modifier = Modifier.fillMaxWidth(0.4f),
                                            textStyle = TextStyle(
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Normal,
                                                color = MaterialTheme.colorScheme.secondary
                                            ),
                                            singleLine = true,

                                            decorationBox = { innerTextField ->
                                                Column {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(bottom = 4.dp)
                                                    ) {
                                                        innerTextField()
                                                    }
                                                    Divider(
                                                        color = Color.LightGray,
                                                        thickness = 1.dp
                                                    )
                                                }
                                            }
                                        )

                                        Spacer(modifier = Modifier.width(20.dp))
                                        BasicTextField(
                                            value = value.value,
                                            onValueChange = { value.value = it },
                                            modifier = Modifier.fillMaxWidth(0.8f),
                                            textStyle = TextStyle(
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Normal,
                                                color = MaterialTheme.colorScheme.secondary
                                            ),
                                            singleLine = true,

                                            decorationBox = { innerTextField ->
                                                Column {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(bottom = 4.dp)
                                                    ) {
                                                        innerTextField()
                                                    }
                                                    Divider(
                                                        color = Color.LightGray,
                                                        thickness = 1.dp
                                                    )
                                                }
                                            }
                                        )
                                    }
                                }
                                IconButton(onClick = {

                                    properties.value.add(
                                        ProductProperty(
                                            property.value,
                                            value.value
                                        )
                                    )
                                    property.value = ""
                                    value.value = ""
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(35.dp)
                                    )
                                }

                            }

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .height(200.dp)
                            ) {
                                items(properties.value) { item ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 10.dp, end = 10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth(0.9f),
                                            horizontalArrangement = Arrangement.SpaceBetween

                                        ) {
                                            Text(
                                                text = item.prop,
                                                modifier = Modifier.fillMaxWidth(0.5f),
                                                color = MaterialTheme.colorScheme.secondary,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight(700),
                                            )

                                            Text(
                                                text = item.value,
                                                modifier = Modifier.fillMaxWidth(),
                                                color = MaterialTheme.colorScheme.secondary,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight(700),

                                                )
                                        }

                                        IconButton(onClick = {
                                            val newProperties = ArrayList(properties.value)
                                            newProperties.remove(item)
                                            properties.value = newProperties
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "",
                                                tint = Color.Red
                                            )
                                        }
                                    }

                                }
                            }

                            Spacer(modifier = Modifier.height(40.dp))
                            ElevatedButton(
                                modifier = Modifier
                                    .fillMaxWidth(0.5f),
                                onClick = {
                                    createProductViewModel.sendRequestToCreateProduct(
                                        context,
                                        newProduct
                                    )

                                    navController.popBackStack()
                                },
                                elevation = ButtonDefaults.elevatedButtonElevation(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                enabled = true
                            ) {
                                Text(
                                    text = "Создать",
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight(900),
                                        color = Color.White
                                    ),
                                    modifier = Modifier.padding(5.dp)
                                )
                            }

                        }
                        Spacer(modifier = Modifier.height(200.dp))
                    }


                }
            }

        }
    }
}