package com.example.shop.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shop.data.Category
import com.example.shop.ui.theme.Gray
import com.example.shop.ui.theme.LightBlue
import com.example.shop.utils.setCategoryImage
import com.example.shop.utils.setProductImage
import com.example.shop.viewModels.AdminViewModel
import com.example.shop.viewModels.MainViewModel
import java.util.UUID

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySettings(navController: NavController, viewModel: MainViewModel) {
    val adminViewModel = AdminViewModel()

    val context = LocalContext.current
    val title = remember {
        mutableStateOf("")
    }


    val categories = adminViewModel.categories.observeAsState()

    var imageUrl = remember {
        mutableStateOf<String?>(null)
    }
    val modalBottomSheet = remember {
        mutableStateOf(false)
    }
    val modalBottomSheetUpdateCategory = remember {
        mutableStateOf(false)
    }
    val launcher = setCategoryImage(context, title.value, imageUrl)

    val updatedCategory = remember {
        mutableStateOf<Category?>(null)
    }
    val newCategory =
        Category("", title.value, imageUrl.value ?: "")
    val isButtonEnabled = title.value.isNotEmpty() && imageUrl.value != null

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Настройки категорий",
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
                }
                ,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(MaterialTheme.colorScheme.background)
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (categories.value == null) {
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
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.padding(bottom = 82.dp, top = 80.dp)
                    ) {

                        items(categories?.value!!) { category ->
                            val dropdownMenu = remember {
                                mutableStateOf(false)
                            }
                            ElevatedButton(
                                onClick = {},
                                shape = RectangleShape,
                                modifier = Modifier.padding(2.dp),
                                contentPadding = PaddingValues(0.dp) ,
                                colors = ButtonDefaults.buttonColors(Color.White)
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
                                    Row(
                                        modifier = Modifier.align(Alignment.TopEnd),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = category?.title.toString(),
                                            style = TextStyle(
                                                fontSize = 18.sp,
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight.Bold,
                                            ),
                                        )
                                        IconButton(onClick = {
                                            dropdownMenu.value = !dropdownMenu.value
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.MoreVert,
                                                contentDescription = "",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                    DropdownMenu(
                                        expanded = dropdownMenu.value,
                                        onDismissRequest = { dropdownMenu.value = false }) {
                                        DropdownMenuItem(
                                            text = { Text(text = "Удалить категорию",color=MaterialTheme.colorScheme.secondary) },
                                            onClick = {
                                                adminViewModel.deleteCategory(category!!)
                                                dropdownMenu.value = false
                                            })
                                        DropdownMenuItem(
                                            text = { Text(text = "Изменить категорию",color =MaterialTheme.colorScheme.secondary) },
                                            onClick = {
                                                modalBottomSheetUpdateCategory.value = true
                                                updatedCategory.value = category
                                                dropdownMenu.value = false
                                            })
                                    }
                                }
                            }


                        }

                    }
                }
            }
            if (modalBottomSheet.value) {
                ModalBottomSheet(onDismissRequest = { modalBottomSheet.value = false },
                    containerColor = MaterialTheme.colorScheme.background) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = ("Создание новой категории"),
                            style = TextStyle(
                                fontSize = 25.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                            ),
                        )
                        Spacer(modifier = Modifier.height(40.dp))

                        Text(
                            text = ("Название"),
                            style = TextStyle(
                                fontSize = 19.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold,
                            ),
                        )

                        BasicTextField(
                            value = title.value,
                            onValueChange = { title.value = it },
                            modifier = Modifier.fillMaxWidth(0.5f),
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
                        Spacer(modifier = Modifier.height(20.dp))


                        ElevatedButton(
                            onClick = {
                                launcher.launch(
                                    PickVisualMediaRequest(
                                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            },
                            enabled = title.value != "",
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .size(200.dp)
                                .padding(15.dp),
                            contentPadding = PaddingValues(0.dp)  ,

                        ) {
                            AsyncImage(
                                model = when (imageUrl.value) {
                                    null -> "https://cdn-icons-png.flaticon.com/512/4211/4211763.png"
                                    else -> imageUrl.value
                                },
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }


                        ElevatedButton(
                            onClick = {
                                adminViewModel.createCategory(
                                    context,
                                    Category(
                                        UUID.randomUUID().toString(),
                                        newCategory.title, newCategory.image
                                    )
                                )
                                title.value = ""
                                imageUrl.value = ""
                                modalBottomSheet.value = false
                            },
                            enabled = isButtonEnabled,
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),

                            modifier = Modifier.padding(
                                bottom = 80.dp
                            )
                        ) {
                            Text(
                                text = "Добавить",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                ),
                                modifier = Modifier.padding(
                                    start = 10.dp,
                                    end = 10.dp,
                                    top = 5.dp,
                                    bottom = 5.dp
                                )
                            )

                        }


                    }
                }
            }
            if (modalBottomSheetUpdateCategory.value) {
                ModalBottomSheet(onDismissRequest = {
                    modalBottomSheetUpdateCategory.value = false
                }) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = ("Изменение категории"),
                            style = TextStyle(
                                fontSize = 25.sp,
                                color = LightBlue,
                                fontWeight = FontWeight.Bold,
                            ),
                        )
                        Spacer(modifier = Modifier.height(40.dp))

                        Text(
                            text = ("Название"),
                            style = TextStyle(
                                fontSize = 19.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold,
                            ),
                        )

                        BasicTextField(
                            value = title.value,
                            onValueChange = { title.value = it },
                            modifier = Modifier.fillMaxWidth(0.5f),
                            textStyle = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black
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
                        Spacer(modifier = Modifier.height(20.dp))


                        ElevatedButton(
                            onClick = {
                                launcher.launch(
                                    PickVisualMediaRequest(
                                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            },
                            enabled = title.value != "",
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .size(200.dp)
                                .padding(15.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            AsyncImage(
                                model = updatedCategory.value?.image,
                                contentDescription = "",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }


                        ElevatedButton(
                            onClick = {

                                adminViewModel.updateCategory(
                                    Category(
                                        updatedCategory.value?.uid!!,
                                        title.value,
                                        when (imageUrl.value == null) {
                                            true -> updatedCategory.value?.image!!
                                            false -> imageUrl?.value!!
                                        }
                                    ), context
                                )
                                modalBottomSheetUpdateCategory.value = false
                                updatedCategory.value = null
                                title.value = ""
                                imageUrl.value = null

                            },
                            colors = ButtonDefaults.buttonColors(LightBlue),
                            modifier = Modifier.padding(
                                bottom = 80.dp
                            )
                        ) {
                            Text(
                                text = "Обновить",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                ),
                                modifier = Modifier.padding(
                                    start = 10.dp,
                                    end = 10.dp,
                                    top = 5.dp,
                                    bottom = 5.dp
                                )
                            )

                        }


                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    modalBottomSheet.value = true
                    title.value = ""
                    imageUrl.value = null
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 100.dp, end = 20.dp),
                containerColor = LightBlue
            ) {
                Icon(imageVector = Icons.Default.Add, tint = Color.White, contentDescription = "")
            }

        }
    }
}