package com.soldevcode.assistant.data.dto.GptStream

import kotlinx.serialization.Serializable

@Serializable
data class ChoiceStream(
    val delta: Delta,
    val finish_reason: Any,
    val index: Int
)