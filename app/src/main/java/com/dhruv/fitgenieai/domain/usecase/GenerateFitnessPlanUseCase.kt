package com.dhruv.fitgenieai.domain.usecase

import com.dhruv.fitgenieai.data.repository.DataState
import com.dhruv.fitgenieai.domain.model.ChatMessage
import com.dhruv.fitgenieai.domain.repository.FitGenieAIRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GenerateFitnessPlanUseCase @Inject constructor(
    private val fitGenieAIRepository: FitGenieAIRepository
){
     suspend operator fun invoke(listMessages: List<ChatMessage>) : Flow<DataState<ChatMessage>> {
        return fitGenieAIRepository.getGeminiResponse(listMessages)
    }
}