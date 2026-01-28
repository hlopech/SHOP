package com.example.shop.domain.model

data class RequestToCreateProduct(
    val userUid: String = "",
    val response: String = "",
    val product: Product? = null
)
