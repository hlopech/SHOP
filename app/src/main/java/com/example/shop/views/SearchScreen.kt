package com.example.shop.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shop.MainActivity
import com.example.shop.R
import com.example.shop.ui.theme.LightBlue
import com.example.shop.viewModels.MainViewModel
import com.example.shop.viewModels.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(navController: NavController, viewModel: MainViewModel) {

    val searchViewModel = SearchViewModel()
    val currentUser = viewModel.currentUser.observeAsState()
    val context = LocalContext.current
    val contextMainActivity = LocalContext.current as MainActivity

    val query = remember {
        mutableStateOf(viewModel.currentSearch.value)
    }

    val history = remember {
        mutableStateListOf(*currentUser.value?.searchHistory?.toTypedArray() ?: arrayOf())
    }

    LaunchedEffect(contextMainActivity.recognizedText.value) {
        query.value = contextMainActivity.recognizedText.value
        if (query.value.isNotEmpty()) {
            viewModel.currentSearch.value = query.value
            contextMainActivity.recognizedText.value = ""
            history.add(query.value)
            currentUser.value?.searchHistory?.add(query.value)
            navController.navigate("searchResult")
            searchViewModel.addSearchToHistory(
                currentUser.value?.uid!!,
                currentUser.value!!, navController, context
            )

        }
    }

    Scaffold() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.965f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchBar(
                    query = query.value,
                    onQueryChange = { query.value = it },
                    onSearch = {
                        if (query.value.isNotEmpty()) {
                            history.add(query.value)
                            currentUser.value?.searchHistory?.add(query.value)
                            navController.navigate("searchResult")
                            viewModel.currentSearch.value = query.value
                            searchViewModel.addSearchToHistory(
                                currentUser.value?.uid!!,
                                currentUser.value!!, navController, context
                            )
                        }
                    },
                    active = false,
                    onActiveChange = {},
                    placeholder = { Text(text = "Поиск", color = MaterialTheme.colorScheme.secondary) },
                    leadingIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "", tint = LightBlue,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            contextMainActivity.startSpeechRecognition()
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.voice_icon),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.7f),
                    colors = SearchBarDefaults.colors()

                ) { }
                TextButton(
                    shape = RoundedCornerShape(20.dp),
                    onClick = {
                        viewModel.currentSearch.value = ""
                        navController.navigate("categoriesScreen")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Отменить",
                        fontSize = 18.sp,
                        color = Color.White,
                        fontWeight = FontWeight(800)
                    )
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.01f))


            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(0.dp),
            ) {
                items(history) { item ->
                    TextButton(
                        onClick = {
                            navController.navigate("searchResult")
                            viewModel.currentSearch.value = item
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.background),
                        shape = RectangleShape,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.history_icon),
                                    contentDescription = "", tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = item,
                                    Modifier.fillMaxWidth(0.8f),
                                    color = MaterialTheme.colorScheme.secondary,
                                    textAlign = TextAlign.Start
                                )
                                IconButton(onClick = {
                                    history.remove(item)
                                    currentUser.value?.searchHistory?.remove(item)
                                    searchViewModel.deleteSearchFromHistory(
                                        currentUser.value?.uid!!,
                                        currentUser.value!!, navController, context
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "",
                                        tint = Color.Red
                                    )
                                }
                            }
                            Divider()
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}
