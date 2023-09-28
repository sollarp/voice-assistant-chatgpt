package com.soldevcode.composechat.data.dto.gptRequest

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val content: String,
    val role: String
)

