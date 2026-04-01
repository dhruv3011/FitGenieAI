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

    private fun buildPrompt(userInput: String): String {
        return """
        You are a professional fitness coach and nutrition expert.

        Your job:
        - Give clear, structured, and practical fitness advice
        - Keep answers short and easy to follow
        - Use bullet points where helpful
        - Focus on diet + workout + lifestyle

        User details:
        - Vegetarian, eats eggs
        - Goal: fat loss + muscle gain

        Respond in this format:
        1. Diet Plan
        2. Workout Plan
        3. Tips

        User question:
        $userInput
        """.trimIndent()
    }
}