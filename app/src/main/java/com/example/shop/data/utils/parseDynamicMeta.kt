package com.example.shop.data.utils

import android.util.Log
import com.example.shop.domain.model.ItemMeta
import kotlinx.serialization.json.Json

fun parseDynamicMeta(json: String): ItemMeta {
    val fixedJson = json
        .replace("```json", "")
        .replace("```", "")
        .replace("“", "\"")
        .replace("”", "\"")
        .trim()

    return try {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }.decodeFromString(ItemMeta.serializer(), fixedJson)
    } catch(e: Exception) {
        Log.e("MetaParser", "Error parsing JSON: ${e.message}\nJSON: $fixedJson")
        ItemMeta("неизвестно", emptyMap())
    }
}