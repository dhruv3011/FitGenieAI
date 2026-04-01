package com.dhruv.fitgenieai.domain.repository

import com.dhruv.fitgenieai.data.repository.DataState
import com.dhruv.fitgenieai.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface FitGenieAIRepository {
    suspend fun getGeminiResponse(listMessages: List<ChatMessage>): Flow<DataState<ChatMessage>>
}