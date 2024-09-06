package com.example.shop.data

data class User(
    val uid: String = "",
    val email: String = "",
    val role: String = "",
    val userName: String = "",
    val address: String = "",
    val phone: String = "",
    val imageUrl: String = "",
    val shoppingCart: ArrayList<String> = arrayListOf(),
    val favorites: ArrayList<String> = arrayListOf(),
    var searchHistory: ArrayList<String> = arrayListOf(),
    var orderHistory: ArrayList<String> = arrayListOf(),


    )