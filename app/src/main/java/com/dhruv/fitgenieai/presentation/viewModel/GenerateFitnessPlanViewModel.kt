package com.dhruv.fitgenieai.presentation.viewModel

import android.util.Log.e
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhruv.fitgenieai.data.repository.DataState
import com.dhruv.fitgenieai.domain.model.ChatMessage
import com.dhruv.fitgenieai.domain.model.Section
import com.dhruv.fitgenieai.domain.usecase.GenerateFitnessPlanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenerateFitnessPlanViewModel @Inject constructor(
    private val generateFitnessPlanUseCase: GenerateFitnessPlanUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<ChatUIState<ChatMessage>>(ChatUIState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages = _messages.asStateFlow()

    private val systemPrompt = ChatMessage(
        message = """
        You are a professional fitness coach.

Return response STRICTLY in this format:

1. Goal: <one line>
2. Workout Plan:
- bullet
- bullet
3. Diet Plan:
- bullet
4. Tips:
- bullet

Rules:
- No markdown
- No extra text
- Max 4 bullets per section
    """.trimIndent(),
        isFromUser = false,
        isSystem = true
    )


    fun sendMessage(userPrompt: String) {
        val enhancedPrompt = """
User goal: $userPrompt

Give a practical plan based on:
- Time availability (if mentioned)
- Diet preference (vegetarian if mentioned)
- Keep it simple and actionable
""".trimIndent()
        val userMessage = ChatMessage(userPrompt, isFromUser = true)
        _messages.update { listOf(userMessage) + it }

        val updatedMessages = _messages.value

        val apiMessages = listOf(systemPrompt) +
                updatedMessages
                    .take(6)
                    .reversed()
                    .map {
                        if (it.isFromUser && it.message == userPrompt) {
                            it.copy(message = enhancedPrompt)
                        } else {
                            it
                        }
                    }

        viewModelScope.launch {
            generateFitnessPlanUseCase(apiMessages)
                .catch { e ->
                    _uiState.value = ChatUIState.Error(e.message ?: "Unknown error")
                }
                .collectLatest { result ->
                    when(result) {
                        is DataState.Loading -> _uiState.value =  ChatUIState.Loading
                        is DataState.Success ->  {
                            val sections = result.data.message
                                .split(Regex("\\d\\."))
                                .filter { it.isNotEmpty() }
                                .map {
                                    val lines = it.split("\n", limit = 2)
                                    Section(
                                        title = lines.first(),
                                        content = lines.getOrNull(1) ?: ""
                                    )
                                }

                            //val formatted = sections.joinToString("\n\n")

                            val aiMessages = ChatMessage(sections =  sections, isFromUser = false)

                            _messages.update { listOf(aiMessages) + it }

                            _uiState.value = ChatUIState.Idle
                        }
                        is DataState.Error -> _uiState.value = ChatUIState.Error(result.error)
                    }
                }

        }
    }
}