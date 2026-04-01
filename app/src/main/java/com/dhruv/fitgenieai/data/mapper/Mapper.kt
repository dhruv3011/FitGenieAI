package com.dhruv.fitgenieai.data.mapper

import com.dhruv.fitgenieai.data.remote.model.ContentX
import com.dhruv.fitgenieai.data.remote.model.GeminiRequest
import com.dhruv.fitgenieai.data.remote.model.GeminiResponse
import com.dhruv.fitgenieai.data.remote.model.PartX
import com.dhruv.fitgenieai.domain.model.ChatMessage
import kotlin.collections.map

fun GeminiResponse.toDomain(): ChatMessage {
    val text = candidates.firstOrNull()
        ?.content
        ?.parts
        ?.firstOrNull()
        ?.text
        ?: "No response"
    return ChatMessage(message = text, isFromUser = false)
}

fun List<ChatMessage>.toGeminiRequest(): GeminiRequest {
    return GeminiRequest(
        contents = this.map {
            ContentX(
                role = when {
                    it.isSystem -> "user"
                    it.isFromUser -> "user"
                    else -> "model"
                },
                parts = listOf(PartX(it.message))
            )
        }
    )
}
