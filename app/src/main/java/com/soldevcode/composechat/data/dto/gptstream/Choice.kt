package com.soldevcode.composechat.data.dto.gptstream

import kotlinx.serialization.Serializable

@Serializable
data class ChoiceStream(
    val delta: Delta,
    val finish_reason: Any,
    val index: Int
)