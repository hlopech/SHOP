package com.example.shop.domain.model

data class ChatRequest(
    val model: String,
    val messages: List<Message>,
    val max_tokens: Int = 1000
)

data class Message(
    val role: String,
    val content: List<Content>
)

data class Content(
    val type: String,
    val text: String? = null,
    val image_url: ImageUrl? = null
)

data class ImageUrl(
    val url: String
)

@kotlinx.serialization.Serializable
data class ItemMeta(
    val описание: String,
    val характеристики: Map<String, String>
)
data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: MessageResponse
)

data class MessageResponse(
    val content: String
)
