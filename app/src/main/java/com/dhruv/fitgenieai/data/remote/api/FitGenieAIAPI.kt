package com.dhruv.fitgenieai.data.remote.api

import com.dhruv.fitgenieai.data.remote.model.GeminiRequest
import com.dhruv.fitgenieai.data.remote.model.GeminiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface FitGenieAIAPI {
    @POST("models/gemini-2.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body geminiRequest: GeminiRequest
    ): Response<GeminiResponse>
}