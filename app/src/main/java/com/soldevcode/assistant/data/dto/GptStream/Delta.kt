package com.soldevcode.assistant.data.dto.GptStream

import com.google.gson.annotations.SerializedName

data class Delta(
    @SerializedName("content")
    val content: String
)