package com.example.shop.data

import java.util.Calendar
import java.util.Date


data class Product(
    val sellerId: String = "",
    val sellerName: String = "",
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val price: Int = 0,
    var count: Int = 0,
    val category: String = "",
    var rating: Double = 0.0,
    var sales: Int = 0,
    var reviews: Int = 0,
    val timeCreated: Date = Calendar.getInstance().time,
    val properties: ArrayList<ProductProperty> = arrayListOf()
)

data class ProductProperty(
    val prop: String = "",
    val value: String = "",

    )
