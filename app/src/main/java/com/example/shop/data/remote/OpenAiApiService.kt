package com.example.shop.data.remote

import com.example.shop.domain.model.ChatRequest
import com.example.shop.domain.model.ChatResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAiApiService {
    @POST("v1/chat/completions")
    suspend fun chat(
        @Header("Authorization") authHeader: String,
        @Body body: ChatRequest
    ): ChatResponse
}