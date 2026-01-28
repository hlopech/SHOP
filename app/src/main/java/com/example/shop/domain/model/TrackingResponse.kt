package com.example.shop.domain.model

import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class ApiResponse<T>(
    val code: Int,
    val payload: T
)

@Serializable
data class PackagePayload(
    val status: Int
)
