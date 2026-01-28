package com.example.shop.presentation.seller.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.data.remote.OpenAiApiService
import com.example.shop.data.utils.createDynamicPrompt
import com.example.shop.data.utils.parseDynamicMeta
import com.example.shop.data.utils.uriToBase64
import com.example.shop.domain.model.ChatRequest
import com.example.shop.domain.model.Content
import com.example.shop.domain.model.ImageUrl
import com.example.shop.domain.model.ItemMeta
import com.example.shop.domain.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AnalyzeViewModel(
   private val context: Context
) : ViewModel() {

    private var _meta = MutableStateFlow<ItemMeta?>(null)
    var meta: StateFlow<ItemMeta?> = _meta.asStateFlow()

    private var _loading = MutableStateFlow(false)
    var loading: StateFlow<Boolean> = _loading.asStateFlow()

    private var _error = MutableStateFlow<String?>(null)
    var error: StateFlow<String?> = _error.asStateFlow()

    fun analyze(uri: Uri) = viewModelScope.launch {
        _loading.value = true
        _error.value = null

        try {
            val result = callDynamicAnalysis(context, uri)
            _meta.value = result
            _error.value = null
            Log.d("GPTRESP", "Analysis result: $result")
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Неизвестная ошибка"
            _error.value = errorMessage
            Log.e("GPTRESP", "Analysis error: $errorMessage", e)
        } finally {
            _loading.value = false
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private suspend fun callDynamicAnalysis(context: Context, uri: Uri): ItemMeta {
        val base64Image = uriToBase64(context, uri)

        val messageContent = listOf(
            Content(
                type = "text",
                text = createDynamicPrompt()
            ),
            Content(
                type = "image_url",
                image_url = ImageUrl("data:image/jpeg;base64,$base64Image")
            )
        )
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val openAiApi = retrofit.create(OpenAiApiService::class.java)
        val response = openAiApi.chat(
            "API KEY",
            ChatRequest(
                model = "gpt-4.1-mini",
                messages = listOf(Message("user", messageContent))
            )
        )

        return parseDynamicMeta(response.choices.first().message.content)
    }
}