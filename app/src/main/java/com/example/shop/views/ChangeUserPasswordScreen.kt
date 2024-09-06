package com.example.shop.views

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.shop.data.User
import com.example.shop.repositories.UserRepository
import com.example.shop.ui.theme.LightBlue
import com.example.shop.ui.theme.ProfileIcons
import com.example.shop.viewModels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChangePasswordScreen(navController: NavController, viewModel: MainViewModel) {
    val currentUser = viewModel.currentUser.observeAsState()
    val context = LocalContext.current
    val userRepository = UserRepository()
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Изменение пароля",color= MaterialTheme.colorScheme.secondary) },
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
                    color = LightBlue,
                    strokeWidth = 4.dp
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Введите текущий и новый пароль",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = MaterialTheme.colorScheme.secondary
                )

                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },

                    label = { Text(text = "Текущий пароль") },
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary
                    ),
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        color =MaterialTheme.colorScheme.secondary

                    ),
                )
                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },

                    label = { Text(text = "Новый пароль") },
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary
                    ),
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        color=MaterialTheme.colorScheme.secondary
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))


                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },

                    label = { Text(text = "Подтвердите новый пароль") },
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary
                    ),
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        color=MaterialTheme.colorScheme.secondary
                    )
                )

                Spacer(modifier = Modifier.height(40.dp))

                ElevatedButton(
                    modifier = Modifier
                        .fillMaxWidth(0.65f),
                    onClick = {
                        if (newPassword == confirmPassword) {
                            userRepository.updateUserPassword(
                                context,
                                currentPassword,
                                newPassword
                            ) {
                                navController.popBackStack()
                            }
                        } else {
                            Toast.makeText(context, "Пароли не совпадают", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    elevation = ButtonDefaults.elevatedButtonElevation(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    enabled = currentPassword.isNotEmpty()

                ) {
                    Text(
                        text = "Изменить пароль",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight(900),
                            color = MaterialTheme.colorScheme.secondary
                        ),
                        modifier = Modifier.padding(5.dp)
                    )
                }

            }
        }
    }
}
