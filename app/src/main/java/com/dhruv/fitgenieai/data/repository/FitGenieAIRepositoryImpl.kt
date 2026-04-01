package com.dhruv.fitgenieai.data.repository

import com.dhruv.fitgenieai.BuildConfig
import com.dhruv.fitgenieai.data.mapper.toDomain
import com.dhruv.fitgenieai.data.mapper.toGeminiRequest
import com.dhruv.fitgenieai.data.remote.api.FitGenieAIAPI
import com.dhruv.fitgenieai.domain.model.ChatMessage
import com.dhruv.fitgenieai.domain.repository.FitGenieAIRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FitGenieAIRepositoryImpl @Inject constructor(
    private val fitGenieAIAPI: FitGenieAIAPI
) : FitGenieAIRepository {
    override suspend fun getGeminiResponse(listMessages: List<ChatMessage>): Flow<DataState<ChatMessage>> = flow {
        emit(DataState.Loading)
        try {
            val response = fitGenieAIAPI.generateContent("", listMessages.toGeminiRequest())
            val body = response.body()
            if (response.isSuccessful && body != null) {
                emit(DataState.Success(body.toDomain()))
            } else {
                emit(DataState.Error("No response from server"))
            }
        } catch (e: Exception) {
            emit(DataState.Error(e.message ?: ""))
        }
    }
}