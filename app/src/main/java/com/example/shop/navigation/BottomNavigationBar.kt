package com.example.shop.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BottomNavigationBar(navController: NavController) {
    var config = listOf(
        NavBotItem(Icons.Default.Home, "home"),
        NavBotItem(Icons.Default.List, "categoriesScreen"),
        NavBotItem(Icons.Default.ShoppingCart, "shoppingCart"),
        NavBotItem(Icons.Default.Person, "profile"),
    )
    var (selectedOption, onOptionSelected) = remember { mutableStateOf(config[0]) }

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Divider()
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,

            ) {
            items(config) { it ->
                IconButton(onClick = {
                    onOptionSelected(it)
                    navController.navigate(it.route)
                }) {

                    Icon(
                        imageVector = it.icon,
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                            .padding(5.dp), tint = when (it == selectedOption) {
                            true -> MaterialTheme.colorScheme.primary
                            false -> Color.Gray
                        }
                    )
                }
            }
        }
    }

}

data class NavBotItem(val icon: ImageVector, val route: String)
