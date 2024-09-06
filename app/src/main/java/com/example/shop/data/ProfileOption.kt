package com.example.shop.data

import androidx.compose.ui.graphics.vector.ImageVector

data class ProfileOption(val icon: ImageVector, val title: String,val desc: String, val onClick: () -> Unit)
