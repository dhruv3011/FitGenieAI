package com.dhruv.fitgenieai.data.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContentX(
    @SerialName("role")
    val role: String,
    @SerialName("parts")
    val parts: List<PartX>
)