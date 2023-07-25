package com.soldevcode.assistant.data.dto

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName


data class GptRequest(
    @SerializedName("model")
    val model: String,
    @SerializedName("messages")
    val messages: List<MessagesRequest>,
    @SerializedName("stream")
    val stream: Boolean,
)

fun GptRequest.toJson(): JsonObject {
    val json = JsonObject()
    json.addProperty("model", model)

    val jsonArray = JsonArray()
    jsonArray.add(messages[0].toJson())

    json.add("messages", jsonArray)
    json.addProperty("stream", stream)

    return json

}

/*
data class GptRequest(
    @SerializedName("model")
    val model: String,
    @SerializedName("prompt")
    val prompt: String,
    @SerializedName("max_tokens")
    val maxTokens: Int,
    @SerializedName("temperature")
    val temperature: Double,
    @SerializedName("top_p")
    val topP: Double
)*/
