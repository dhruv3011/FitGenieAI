package com.dhruv.fitgenieai.data.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Content(
    @SerialName("parts")
    val parts: List<Part>,
    @SerialName("role")
    val role: String
)