package com.soldevcode.composechat.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class GptResponse(
    val id: String,
    val `object`: String,
    val created: Long,
    val model: String,
    val choices: List<ChoiceResponse>
)

@Serializable
data class ChoiceResponse(
    val delta: Delta,
    val index: Int,
    val finish_reason: String?
)
@Serializable
data class Delta(
    val role: String,
    val content: String
)