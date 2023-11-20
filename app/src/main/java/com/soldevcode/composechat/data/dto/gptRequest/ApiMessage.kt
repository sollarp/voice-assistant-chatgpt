package com.soldevcode.composechat.data.dto.gptRequest

import kotlinx.serialization.Serializable

@Serializable
data class ApiMessage(
    val content: String,
    val role: String
)