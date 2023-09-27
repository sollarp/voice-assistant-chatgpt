package com.soldevcode.composechat.data.dto.gptRequestBody

import com.google.gson.annotations.SerializedName

data class GptRequestStream(
    @SerializedName("messages")
    val messages: List<Message>,
    @SerializedName("model")
    val model: String,
    @SerializedName("stream")
    val stream: Boolean = true
)