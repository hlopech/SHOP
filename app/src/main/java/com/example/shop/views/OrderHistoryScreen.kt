package com.example.shop.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shop.ui.theme.LightBlue
import com.example.shop.viewModels.HomeScreenViewModel
import com.example.shop.viewModels.MainViewModel
import com.example.shop.viewModels.OrderHistoryViewModel
import com.example.shop.views.UI.ProductCard

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrderHistoryScreen(navController: NavController, viewModel: MainViewModel) {
    val currentUser = viewModel.currentUser.observeAsState()

    val orderHistoryViewModel = OrderHistoryViewModel()
    val history = orderHistoryViewModel.orderHistory.observeAsState()

    if (currentUser.value != null) {
        orderHistoryViewModel.getHistory(currentUser?.value!!.orderHistory)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("История покупок", color = MaterialTheme.colorScheme.secondary) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("profile") }) {
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
        if (currentUser.value == null || history.value?.size == 0) {
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
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(top = 80.dp)
            ) {

                items(history?.value!!) { product ->
                    ProductCard(
                        product = product!!,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }


    }

}