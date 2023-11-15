package com.soldevcode.composechat.data.dto.gptRequest

import kotlinx.serialization.Serializable


@Serializable
data class GptRequestStream(
    val messages: List<ApiMessage>,
    val model: String,
    val stream: Boolean = true
)
