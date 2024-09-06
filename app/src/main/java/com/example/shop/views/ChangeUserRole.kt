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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shop.ui.theme.LightBlue
import com.example.shop.ui.theme.ProfileIcons
import com.example.shop.viewModels.MainViewModel
import com.example.shop.viewModels.ChangeRoleViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeUserRole(navController: NavController, viewModel: MainViewModel) {
    val currentUser = viewModel.currentUser.observeAsState()
    val userViewModel = ChangeRoleViewModel()
    val requestStatus = userViewModel.requestToChangeRole.observeAsState()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { mutableStateOf(SnackbarHostState()) }
    val context = LocalContext.current

    var isButtonEnabled by remember { mutableStateOf(true) }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Изменение статуса", color = MaterialTheme.colorScheme.secondary) },
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
    ) {
        if (currentUser.value == null) {
            Box(modifier = Modifier.fillMaxSize()) {

                CircularProgressIndicator(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp
                )
            }
        } else {
            Column(Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(100.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(15.dp)
                ) {
                    Text(
                        text = "Ваш статус - ",
                        color = MaterialTheme.colorScheme.secondary,
                        style = TextStyle(
                            fontSize = 17.sp,
                            fontWeight = FontWeight(400)
                        )
                    )
                    Text(
                        text = currentUser.value?.role!!,
                        color = when (currentUser.value?.role!!) {
                            "user" -> Color.Green
                            "seller" -> Color.Yellow
                            else -> Color.Red
                        },
                        style = TextStyle(
                            fontSize = 17.sp,
                            fontWeight = FontWeight(700),
                        )
                    )
                    IconButton(onClick = {
                        scope.launch {
                            snackbarHostState.value.showSnackbar(
                                when (currentUser.value?.role!!) {
                                    "user" -> "Базовый статус аккаунта. Вы имеете возможность просматривать товары. Осуществлять покупку."
                                    "seller" -> "Статус продавец дает вам все базовые возможности, и так же позволяет вам создавать собственные товары и выкладывать их."
                                    else -> "Статус админ дает вам полный функционал. Позволяет управлять аккаунтами пользователей и товарами продавцов."
                                }
                            )
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            tint = Color.Gray,
                            contentDescription = ""
                        )
                    }
                }
                SnackbarHost(snackbarHostState.value)

                if (currentUser.value?.role!! == "user") {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Если вы хотите добавлять свои товары, то вы можете получить статус продавец. Для этого нажмите на кнопку. Позже администратор вышлет вам письмо на почту с ответом.",
                            color = MaterialTheme.colorScheme.secondary,
                            style = TextStyle(
                                fontSize = 17.sp,
                                fontWeight = FontWeight(400)
                            ), modifier = Modifier.padding(15.dp)
                        )

                        Button(
                            onClick = {
                                userViewModel.sendRequest(context)
                                isButtonEnabled = false
                            },

                            colors = ButtonDefaults.buttonColors(ProfileIcons),
                            shape = RectangleShape,
                            enabled = requestStatus.value == null && isButtonEnabled

                        ) {
                            Text(
                                text = "Отправить заявку",
                                color = MaterialTheme.colorScheme.secondary,
                                fontSize = 25.sp,
                                modifier = Modifier.padding(5.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        if (requestStatus.value != null) {
                            Text(
                                text = "Статус заявки: ${requestStatus.value?.response}",
                                fontSize = 18.sp,
                                color = Color.Blue,
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        if (requestStatus.value?.response == "approved") {
                            Button(
                                onClick = {
                                    userViewModel.userRepository.updateUserData(
                                        currentUser.value?.uid!!,
                                        currentUser.value?.copy(role = "seller")!!,
                                        navController, context
                                    )
                                    viewModel.loadUser()
                                },
                                colors = ButtonDefaults.buttonColors(ProfileIcons),
                                shape = RectangleShape,
                            ) {
                                Text(
                                    text = "Стать продавцом",
                                    fontSize = 25.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                        } else if (requestStatus.value?.response == "denied") {
                            Text(
                                text = "К сожелению вам отказанно",
                                fontSize = 25.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(5.dp)
                            )
                            Button(
                                onClick = {
                                    isButtonEnabled = true
                                    userViewModel.deleteRequest(currentUser.value?.uid!!)
                                },
                                colors = ButtonDefaults.buttonColors(ProfileIcons),
                                shape = RectangleShape,
                            ) {
                                Text(
                                    text = "Понятно",
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontSize = 25.sp,
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
