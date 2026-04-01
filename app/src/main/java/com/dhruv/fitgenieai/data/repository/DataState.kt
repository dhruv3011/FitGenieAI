package com.dhruv.fitgenieai.data.repository

sealed class DataState<out T> {
    object Loading : DataState<Nothing>()
    data class Success<T>(val data: T) : DataState<T>()
    data class Error<T>(val error: String) : DataState<T>()
}