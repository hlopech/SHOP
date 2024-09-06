package com.example.shop.views

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shop.data.User
import com.example.shop.ui.theme.LightBlue
import com.example.shop.viewModels.MainViewModel
import com.example.shop.viewModels.UserScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(navController: NavController, viewModel: MainViewModel) {
    val currentUser = viewModel.currentUser.observeAsState()
    val context = LocalContext.current

    var showDropDownMenu by remember {
        mutableStateOf(false)
    }
    val userScreenViewModel = UserScreenViewModel()

    val user = remember {
        mutableStateOf<User?>(null)
    }

    LaunchedEffect(Unit) {
        userScreenViewModel.loadUserDataByUid(viewModel.viewedUserId.value.toString(), user)
    }



    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(user.value?.userName ?: "") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            modifier = Modifier
                                .size(30.dp), contentDescription = "213",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }

                },
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
                        DropdownMenuItem(text = { Text(text = "Обновить",color=MaterialTheme.colorScheme.secondary) }, onClick = {
                            user.value = null
                            userScreenViewModel.loadUserDataByUid(
                                viewModel.viewedUserId.value.toString(),
                                user
                            )

                        })
                        if (currentUser.value?.role == "admin")
                            DropdownMenuItem(
                                text = { Text(text = "Удалить пользователя",color=MaterialTheme.colorScheme.secondary) },
                                onClick = {
                                    userScreenViewModel.deleteUserData(
                                        context,
                                        user.value?.uid.toString(),
                                        user.value
                                    )
                                    navController.popBackStack()
                                })

                    }

                },
                colors =
                TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)

            )
        },
    )
    {
        if (currentUser.value == null || user.value == null) {
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

                LazyColumn(

                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally


                ) {
                    item {
                        Spacer(modifier = Modifier.height(80.dp))


                        OutlinedCard(
                            elevation = CardDefaults.cardElevation(20.dp),
                            shape = RoundedCornerShape(280.dp),
                            modifier = Modifier.size(280.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = user.value?.imageUrl,
                                    contentDescription = "",
                                    modifier = Modifier.size(350.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }


                    item {
                        Divider(modifier = Modifier.padding(20.dp))



                        Divider(modifier = Modifier.padding(20.dp))

                    }

                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp)
                        ) {
                            Text(
                                text = "Роль",
                                modifier = Modifier.padding(bottom = 10.dp),
                                color = MaterialTheme.colorScheme.secondary,
                                style = TextStyle(fontWeight = FontWeight(900), fontSize = 20.sp)
                            )
                            Text(
                                text = user.value?.role!!,
                                color = MaterialTheme.colorScheme.secondary,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight(400)
                                )
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                            Text(
                                text = "Псевдоним",
                                modifier = Modifier.padding(bottom = 10.dp),
                                color=MaterialTheme.colorScheme.secondary,
                                style = TextStyle(fontWeight = FontWeight(900), fontSize = 20.sp)
                            )
                            Text(
                                text = user.value?.userName!!,
                                color = MaterialTheme.colorScheme.secondary,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight(400)
                                )
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                            Text(
                                text = "Почта",
                                color=MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(bottom = 10.dp),
                                style = TextStyle(fontWeight = FontWeight(900), fontSize = 20.sp)
                            )
                            Text(
                                text = user.value?.email!!,
                                color = MaterialTheme.colorScheme.secondary,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight(400)
                                )
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                            Text(
                                text = "Адресс",
                                color=MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(bottom = 10.dp),
                                style = TextStyle(fontWeight = FontWeight(900), fontSize = 20.sp)
                            )
                            Text(
                                text = user.value?.address!!,
                                color = MaterialTheme.colorScheme.secondary,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight(400)
                                )
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                            Text(
                                text = "Телефон",
                                color=MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(bottom = 10.dp),
                                style = TextStyle(fontWeight = FontWeight(900), fontSize = 20.sp)
                            )
                            Text(
                                text = user.value?.phone!!,
                                color = MaterialTheme.colorScheme.secondary,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight(400)
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(80.dp))
                    }

                }
            }


        }
    }
}