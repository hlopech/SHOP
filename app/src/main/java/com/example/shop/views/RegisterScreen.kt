package com.example.shop.views

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shop.ui.theme.Gray
import com.example.shop.ui.theme.LightBlue
import com.example.shop.ui.theme.LightGray
import com.example.shop.viewModels.AuthState
import com.example.shop.viewModels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, viewModel: MainViewModel) {
    val username = remember {
        mutableStateOf("")
    }
    val emailState = remember {
        mutableStateOf("")
    }
    val passwordState = remember {
        mutableStateOf("")
    }

    val context = LocalContext.current
    val authState = viewModel.authState.observeAsState()
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBlue),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "РЕГИСТРАЦИЯ",
            style = TextStyle(color = Color.White, fontSize = 40.sp, fontWeight = FontWeight(900))
        )
        Spacer(modifier = Modifier.height(50.dp))

        Card(
            elevation = CardDefaults.cardElevation(15.dp),
            colors = CardDefaults.cardColors(
                Color.White
            ),
            shape = RoundedCornerShape(40.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.7f)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(50.dp))

                Card(
                    elevation = CardDefaults.cardElevation(15.dp),
                    shape = RoundedCornerShape(40.dp),

                    ) {

                    OutlinedTextField(
                        value = username.value,
                        onValueChange = { username.value = it },
                        placeholder = { Text(text = "Username", color = Gray, fontSize = 20.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = null,
                                tint = Gray
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(LightGray)
                            .padding(start = 10.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Card(
                    elevation = CardDefaults.cardElevation(15.dp),
                    shape = RoundedCornerShape(40.dp),

                    ) {

                    OutlinedTextField(
                        value = emailState.value,
                        onValueChange = { emailState.value = it },
                        placeholder = { Text(text = "Email", color = Gray, fontSize = 20.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Email,
                                contentDescription = null,
                                tint = Gray
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(LightGray)
                            .padding(start = 10.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Card(
                    elevation = CardDefaults.cardElevation(15.dp),
                    shape = RoundedCornerShape(40.dp),

                    ) {

                    OutlinedTextField(
                        value = passwordState.value,
                        onValueChange = { passwordState.value = it },
                        placeholder = { Text(text = "Пароль", color = Gray, fontSize = 20.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = null,
                                tint = Gray
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(LightGray)
                            .padding(start = 10.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }


                Spacer(modifier = Modifier.height(30.dp))

                ElevatedButton(
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    onClick = {
                        viewModel.singUp(
                            username.value.trim(),
                            emailState.value.trim(),
                            passwordState.value.trim(),
                            "user",
                            context
                        )
                    },
                    enabled = authState.value != AuthState.Loading,
                    elevation = ButtonDefaults.elevatedButtonElevation(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LightBlue)
                ) {
                    Text(
                        text = "Зарегистрироваться",
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight(900)),
                        modifier = Modifier.padding(5.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(text = "Уже есть аккаунт?")
                    TextButton(
                        onClick = {
                            navController.navigate("login")

                        },
                    ) {
                        Text(text = "Войти ")
                    }

                }
            }
        }

    }
}