package com.soldevcode.assistant.data.dto

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class MessagesRequest(
    @SerializedName("role")
    val role: String = "user",
    @SerializedName("content")
    val content: String
)
fun MessagesRequest.toJson() : JsonObject {
    val json = JsonObject()
    json.addProperty("content", content)
    json.addProperty("role", role)

    return json
}
