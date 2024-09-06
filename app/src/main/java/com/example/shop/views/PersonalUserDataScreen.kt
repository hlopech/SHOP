package com.example.shop.views

import android.annotation.SuppressLint
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shop.data.User
import com.example.shop.repositories.UserRepository
import com.example.shop.ui.theme.LightBlue
import com.example.shop.utils.setUserAvatarImage
import com.example.shop.viewModels.MainViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalUserData(navController: NavController, viewModel: MainViewModel) {
    val currentUser = viewModel.currentUser.observeAsState()
    var username = remember {
        mutableStateOf(currentUser.value?.userName)
    }
    var email = remember {
        mutableStateOf(currentUser.value?.email)
    }
    var phone = remember {
        mutableStateOf(currentUser.value?.phone)
    }
    var address = remember {
        mutableStateOf(currentUser.value?.address)
    }
    var imageUrl = remember {
        mutableStateOf(currentUser.value?.imageUrl)
    }
    val context = LocalContext.current

    val userRepositoty = UserRepository()

    val launcher = setUserAvatarImage(context = context, imageUrl = imageUrl)

    Scaffold(
        topBar =
        {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Личные данные",
                        color = MaterialTheme.colorScheme.secondary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(10.dp),
                        content = {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                modifier = Modifier.size(50.dp),
                                contentDescription = "213",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(MaterialTheme.colorScheme.background)
            )
        }) {
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
            Column(modifier = Modifier.fillMaxSize()) {

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item { Spacer(modifier = Modifier.height(100.dp)) }

                    item {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedCard(
                                elevation = CardDefaults.cardElevation(20.dp),
                                shape = RoundedCornerShape(150.dp),
                                modifier = Modifier.size(150.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AsyncImage(
                                        model = imageUrl.value,
                                        contentDescription = "",
                                        modifier = Modifier.size(150.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                            Button(
                                onClick = {
                                    launcher.launch(
                                        PickVisualMediaRequest(
                                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                },
                                modifier = Modifier.padding(start = 30.dp),
                                colors = ButtonDefaults.buttonColors(Color.Transparent),
                                shape = RectangleShape,
                            ) {
                                Text(
                                    text = "Изменить",
                                    color = Color.Blue,
                                    textDecoration = TextDecoration.Underline,
                                    fontSize = 20.sp
                                )
                            }
                        }
                    }
                    item {
                        Text(
                            text = "Пседвоним",
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(20.dp),
                            style = TextStyle(fontWeight = FontWeight(900), fontSize = 20.sp)
                        )

                        OutlinedTextField(
                            value = username.value!!,
                            onValueChange = { username.value = it },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = null,
                                    tint = LightBlue
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.7f),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = LightBlue,
                                unfocusedBorderColor = LightBlue
                            ),
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        )

                        Text(
                            text = "Почта",
                            modifier = Modifier.padding(20.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            style = TextStyle(fontWeight = FontWeight(900), fontSize = 20.sp)
                        )


                        OutlinedTextField(
                            value = email.value!!,
                            onValueChange = { email.value = it },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = null,
                                    tint = LightBlue
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.7f),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = LightBlue,
                                unfocusedBorderColor = LightBlue
                            ),
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        )
                        Text(
                            text = "Адресс",
                            modifier = Modifier.padding(20.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            style = TextStyle(fontWeight = FontWeight(900), fontSize = 20.sp)
                        )
                        OutlinedTextField(
                            value = address.value!!,
                            onValueChange = { address.value = it },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = null,
                                    tint = LightBlue
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.7f),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = LightBlue,
                                unfocusedBorderColor = LightBlue
                            ),
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        )

                        Text(
                            text = "Телефон",
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(20.dp),
                            style = TextStyle(fontWeight = FontWeight(900), fontSize = 20.sp)
                        )



                        OutlinedTextField(
                            value = phone.value!!,
                            onValueChange = { phone.value = it },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = null,
                                    tint = LightBlue
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.7f),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = LightBlue,
                                unfocusedBorderColor = LightBlue
                            ),
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        )

                    }

                    if (username.value != currentUser.value!!.userName ||
                        email.value != currentUser.value!!.email ||
                        phone.value != currentUser.value!!.phone ||
                        imageUrl.value != currentUser.value!!.imageUrl ||
                        address.value != currentUser.value!!.address ||
                        imageUrl.value != currentUser.value!!.imageUrl
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(50.dp))
                            ElevatedButton(
                                modifier = Modifier
                                    .fillMaxWidth(0.6f),
                                onClick = {
                                    val shoppingCart = currentUser.value!!.shoppingCart!!
                                    val favorites = currentUser.value!!.favorites!!
                                    val searchHistory = currentUser.value!!.searchHistory!!


                                    userRepositoty.updateUserData(
                                        currentUser.value?.uid!!,
                                        User(
                                            currentUser.value?.uid!!,
                                            email.value!!,
                                            currentUser.value?.role!!,
                                            username.value!!,
                                            address.value!!,
                                            phone.value!!,
                                            imageUrl.value!!,
                                            shoppingCart,
                                            favorites,
                                            searchHistory
                                        ),
                                        navController, context
                                    )
                                    viewModel.loadUser()
                                },
                                elevation = ButtonDefaults.elevatedButtonElevation(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text(
                                    text = "Сохранить",
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight(900)
                                    ),
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(200.dp))

                    }
                }

            }
        }
    }

}