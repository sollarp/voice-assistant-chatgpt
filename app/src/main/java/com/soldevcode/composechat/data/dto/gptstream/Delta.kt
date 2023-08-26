package com.soldevcode.composechat.data.dto.gptstream

import com.google.gson.annotations.SerializedName

data class Delta(
    @SerializedName("content")
    val content: String
)