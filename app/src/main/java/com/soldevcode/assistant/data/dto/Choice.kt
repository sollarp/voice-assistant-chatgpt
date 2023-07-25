package com.soldevcode.assistant.data.dto

import com.google.gson.annotations.SerializedName

data class Choice(
    @SerializedName("message")
    val message: MessageResponse
)

/*
data class Choice(
    @SerializedName("text")
    val text: String
)*/
