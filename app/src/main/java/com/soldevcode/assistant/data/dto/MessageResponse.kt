package com.soldevcode.assistant.data.dto

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("role")
    val role: String,
    @SerializedName("content")
    val content: String
)
