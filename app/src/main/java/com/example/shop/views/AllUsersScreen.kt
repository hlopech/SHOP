package com.example.shop.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shop.ui.theme.DropMenuItemBackground
import com.example.shop.ui.theme.LightBlue
import com.example.shop.viewModels.AdminViewModel
import com.example.shop.viewModels.MainViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllUsersScreen(navController: NavController, viewModel: MainViewModel) {
    val adminViewModel = AdminViewModel()
    val users = adminViewModel.users.observeAsState()

    val selectedOption = remember {
        mutableStateOf("All")
    }

    val roleList = remember {
        mutableStateOf(listOf("user", "seller", "admin"))
    }

    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Все пользователи",color=MaterialTheme.colorScheme.secondary) },
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
        if (users.value == null) {
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
            Column {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item { Spacer(modifier = Modifier.height(100.dp)) }
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Выберите статус : ",
                                color = MaterialTheme.colorScheme.secondary,
                                style = TextStyle(
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight(400)
                                )
                            )
                            TextButton(onClick = { expanded = true }) {
                                Text(
                                    text = selectedOption.value,
                                    color = when (selectedOption.value) {
                                        "User" -> Color.Green
                                        "Seller" -> Color.Yellow
                                        "Admin" -> Color.Red
                                        else -> Color.Blue

                                    },
                                    style = TextStyle(
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight(900)
                                    )
                                )
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                offset = DpOffset(x = 90.dp, y = 0.dp),
                                modifier = Modifier.background(DropMenuItemBackground)
                            ) {
                                DropdownMenuItem(
                                    onClick = {
                                        selectedOption.value = "User"
                                        expanded = false
                                        roleList.value = listOf("user")
                                    },
                                    text = {
                                        Text(
                                            "User", color = Color.Green, style = TextStyle(
                                                fontSize = 17.sp,
                                                fontWeight = FontWeight(900)
                                            )
                                        )
                                    }, colors = MenuDefaults.itemColors(textColor = Color.Black)
                                )
                                Divider()

                                DropdownMenuItem(
                                    onClick = {
                                        selectedOption.value = "Seller"
                                        expanded = false
                                        roleList.value = listOf("seller")

                                    },
                                    text = {
                                        Text(
                                            "Seller", color = Color.Yellow, style = TextStyle(
                                                fontSize = 17.sp,
                                                fontWeight = FontWeight(900)
                                            )
                                        )
                                    }
                                )
                                Divider()
                                DropdownMenuItem(
                                    onClick = {
                                        selectedOption.value = "Admin"
                                        expanded = false
                                        roleList.value = listOf("admin")

                                    },
                                    text = {
                                        Text(
                                            "Admin", color = Color.Red, style = TextStyle(
                                                fontSize = 17.sp,
                                                fontWeight = FontWeight(900)
                                            )
                                        )
                                    }
                                )
                                Divider()

                                DropdownMenuItem(
                                    onClick = {
                                        selectedOption.value = "All"
                                        roleList.value = listOf("user", "seller", "admin")
                                        expanded = false
                                    },
                                    text = {
                                        Text(
                                            "All", color = Color.Blue, style = TextStyle(
                                                fontSize = 17.sp,
                                                fontWeight = FontWeight(900)
                                            )
                                        )
                                    }
                                )


                            }

                        }
                    }



                    items(users?.value!!.filter { roleList.value.contains(it?.role) }) { user ->
                        Card(
                            elevation = CardDefaults.cardElevation(10.dp),
                            modifier = Modifier
                                .fillMaxWidth(0.95f)
                                .padding(5.dp)
                                .clickable {
                                    viewModel.viewedUserId.value = user?.uid

                                    when (user?.role) {
                                        "user" -> {
                                            navController.navigate("userScreen")
                                        }

                                        "seller" -> {
                                            navController.navigate("sellerScreen")
                                        }

                                        else -> {}
                                    }
                                },
                            colors = CardDefaults.cardColors(
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(10.dp)
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
                                            model = user?.imageUrl!!,
                                            contentDescription = "",
                                            modifier = Modifier.size(150.dp),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .padding(start = 15.dp),
                                ) {
                                    Text(
                                        text = user?.userName!!,
                                        color = MaterialTheme.colorScheme.secondary,
                                        style = TextStyle(
                                            fontSize = 23.sp,
                                            fontWeight = FontWeight(500)
                                        )
                                    )

                                    Text(
                                        text = user?.email!!,
                                        color = Color.Gray,
                                        style = TextStyle(
                                            fontSize = 19.sp,
                                            fontWeight = FontWeight(500)
                                        )
                                    )
                                    Text(
                                        text = user?.role!!,
                                        color = when (user?.role!!) {
                                            "user" -> Color.Green
                                            "seller" -> Color.Yellow
                                            "admin" -> Color.Red
                                            else -> Color.Blue

                                        },

                                        style = TextStyle(
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight(500)
                                        )
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(150.dp))

                    }
                }

            }

        }
    }
}