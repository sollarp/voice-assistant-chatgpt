package com.soldevcode.composechat.data.dto.gptRequestBody

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("content")
    val content: String,
    @SerializedName("role")
    val role: String
)

