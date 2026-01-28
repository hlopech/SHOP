package com.example.shop.domain.model

data class OrderedProduct(
    val productId: String = "",
    val userId: String = "",
    val sellerId: String = "",
    val status: String = "",
    val rated: Int = 0,
    val trackingCode: String = "",
)