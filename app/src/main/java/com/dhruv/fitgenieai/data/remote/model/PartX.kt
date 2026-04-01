package com.dhruv.fitgenieai.data.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PartX(
    @SerialName("text")
    val text: String
)