package com.example.shop.data

data class RequestToCreateProduct(
    val userUid: String = "",
    val response: String = "",
    val product: Product? = null
)
