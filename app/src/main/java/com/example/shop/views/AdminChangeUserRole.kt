package com.example.shop.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shop.data.RequestToChangeRole
import com.example.shop.data.User
import com.example.shop.ui.theme.LightBlue
import com.example.shop.ui.theme.ProfileIcons
import com.example.shop.viewModels.AdminViewModel
import com.example.shop.viewModels.MainViewModel
import com.example.shop.viewModels.ProfileViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminChangeUserRole(navController: NavController, viewModel: MainViewModel) {

    val currentUser = viewModel.currentUser.observeAsState()
    val adminViewModel = AdminViewModel()
    val context = LocalContext.current
    val requests = viewModel.requestsToChangeRole.observeAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Заявки на изменение статуса", color=MaterialTheme.colorScheme.secondary) },
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
        if (currentUser.value == null && requests.value == null) {
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
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(100.dp))

                requests.value?.forEach { item ->
                    var user = remember {
                        mutableStateOf<User?>(null)
                    }

                    adminViewModel.getUserDataById(item?.userUid!!, user)
                    Card(
                        elevation = CardDefaults.cardElevation(10.dp),
                        modifier = Modifier.fillMaxWidth(0.9f),
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
                                        model = user.value?.imageUrl ?: "",
                                        contentDescription = "",
                                        modifier = Modifier.size(150.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .padding(start = 15.dp),
                            ) {
                                Text(
                                    text = user.value?.userName ?: "",
                                    color = MaterialTheme.colorScheme.secondary,
                                    style = TextStyle(
                                        fontSize = 23.sp,
                                        fontWeight = FontWeight(500)
                                    )
                                )
                                Text(
                                    text = user.value?.email ?: "",
                                    color = Color.Gray,
                                    style = TextStyle(
                                        fontSize = 19.sp,
                                        fontWeight = FontWeight(500)
                                    )
                                )
                                Text(
                                    text = user.value?.role ?: "", color = Color.Green,

                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight(500)
                                    )
                                )
                            }
                            IconButton(onClick = {
                                adminViewModel.sendAnswerToRequest(
                                    RequestToChangeRole(item?.userUid!!, "approved"),
                                    navController,
                                    context
                                )
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "",
                                    tint = Color.Green,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            IconButton(onClick = {
                                adminViewModel.sendAnswerToRequest(
                                    RequestToChangeRole(item?.userUid!!, "denied"),
                                    navController,
                                    context
                                )
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "",
                                    tint = Color.Red,
                                    modifier = Modifier.size(30.dp)

                                )
                            }
                        }
                    }
                }
            }
        }
    }
}