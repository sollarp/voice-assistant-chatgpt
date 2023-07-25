package com.soldevcode.assistant.data.dto.GptStream

import com.google.gson.annotations.SerializedName


data class GptApiStream(
@SerializedName("prompt")
val promptText: String = "",
@SerializedName("temperature")
val temperature: Double = 0.9,
@SerializedName("top_p")
val topP: Double = 1.0,
@SerializedName("n")
val n: Int = 1,
@SerializedName("stream")
var stream: Boolean = true,
@SerializedName("maxTokens")
val maxTokens: Int = 2048,
@SerializedName("model")
val model: String,
@SerializedName("messages")
val messagesTurbo: List<Delta> = emptyList()
)
/*

@Serializable
data class GptApiStream(
    val choices: List<ChoiceStream>,
    val created: Int,
    val id: String,
    val model: String,
    val `object`: String
)*/
