package com.dhruv.fitgenieai.data.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeminiResponse(
    @SerialName("candidates")
    val candidates: List<Candidate>,
    @SerialName("modelVersion")
    val modelVersion: String,
    @SerialName("responseId")
    val responseId: String,
    @SerialName("usageMetadata")
    val usageMetadata: UsageMetadata
)