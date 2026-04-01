package com.dhruv.fitgenieai.domain.model

data class ChatMessage(
    val message: String = "",
    val sections: List<Section>? = null,
    val isFromUser: Boolean,
    val isSystem: Boolean = false
)
