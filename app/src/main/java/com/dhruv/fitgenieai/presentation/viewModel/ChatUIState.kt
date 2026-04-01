package com.dhruv.fitgenieai.presentation.viewModel

sealed class ChatUIState<out T> {
    object Idle : ChatUIState<Nothing>()
    object Loading : ChatUIState<Nothing>()
    data class Success<T> (val data: T) :  ChatUIState<T>()
    data class Error (val error: String) :  ChatUIState<Nothing>()
}