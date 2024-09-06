package com.example.shop.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shop.R
import com.example.shop.ui.theme.LightBlue
import com.example.shop.viewModels.AdminViewModel
import com.example.shop.viewModels.MainViewModel
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(navController: NavController, viewModel: MainViewModel) {

    val adminViewModel = AdminViewModel()

    val categories = adminViewModel.categories.observeAsState()
//
    val isActive = remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    SearchBar(
                        query = "",
                        onQueryChange = {
                            navController.navigate("searchScreen")

                        },
                        onSearch = {
                        },
                        active = isActive.value,
                        onActiveChange = {
                            navController.navigate("searchScreen")

                        },
                        placeholder = { Text(text = "Поиск", color = Color.Gray) },
                        leadingIcon = {
                            IconButton(onClick = {
                                navController.navigate("searchScreen")

                            }) {

                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "", tint = LightBlue,
                                    modifier = Modifier.size(30.dp)

                                )
                            }
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {


                                }) {

                                Icon(
                                    painter = painterResource(id = R.drawable.voice_icon),
                                    contentDescription = "",
                                    tint = LightBlue,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = SearchBarDefaults.colors(Color.White),


                        ) {

                    }
                },
                navigationIcon = {
                },
                actions = {


                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(Color.Transparent)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LightBlue),
            horizontalAlignment = Alignment.CenterHorizontally,

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

                        ElevatedButton(
                            onClick = {
                                viewModel.selectedCategory.value = category
                                navController.navigate("searchResult")
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.padding(2.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(Color.White)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column {
                                    Text(
                                        text = category?.title.toString(),
                                        style = TextStyle(
                                            fontSize = 18.sp,
                                            color = LightBlue,
                                            fontWeight = FontWeight(500),
                                        ),
                                        modifier = Modifier.padding(
                                            top = 5.dp,
                                            start = 5.dp
                                        )
                                    )
                                    AsyncImage(
                                        model = category?.image,
                                        contentDescription = "",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .aspectRatio(1f),
                                        contentScale = ContentScale.FillWidth
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