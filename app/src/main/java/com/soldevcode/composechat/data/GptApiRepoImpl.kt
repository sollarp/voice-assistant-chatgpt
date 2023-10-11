package com.soldevcode.composechat.data

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.soldevcode.composechat.data.dto.GptResponse
import kotlinx.coroutines.delay

interface GptApiRepo {
    suspend fun callGptApi(question: JsonObject, callback: (String) -> Unit)
}

class GptRepositoryImpl : GptApiRepo {
    override suspend fun callGptApi(question: JsonObject, callback: (String) -> Unit) {
        val getApiResponse = RetrofitHelper.getInstance().create(GptApi::class.java)
        val response = getApiResponse.getChatGptCompletion(question)
        val reader = response.charStream().buffered()

        reader.useLines { lines ->
            lines.forEach { line ->
                try {
                    val jsonString = line.substringAfter("data: ")
                    val chatCompletionData = Gson().fromJson(jsonString, GptResponse::class.java)
                    chatCompletionData?.let {
                        val getFinishReason = it.choices.map { choice -> choice.finish_reason }[0].toString()
                        if (getFinishReason != "stop") {
                            val newWord = it.choices.map { choice -> choice.delta.content }[0]
                            callback(newWord)  // Invoking the callback
                            delay(20) // Applied to resolve streaming conflict
                        }
                    }
                } catch (e: JsonSyntaxException) {
                    println("JSON syntax error occurred: ${e.message}")
                }
            }
        }
    }
}



/*
class GptApiRepoImpl : GptApiRepo {
    override suspend fun callGptApi(question: JsonObject): String {
        val getApiResponse = RetrofitHelper.getInstance().create(GptApi::class.java)
        val response = getApiResponse.getChatGptCompletion(question)
        val reader = response.charStream().buffered()
        reader.useLines { lines ->
            lines.forEach { line ->
                val jsonString = line.substringAfter("data: ")
                val chatCompletionData =
                    Gson().fromJson(jsonString, GptResponse::class.java)
                if (chatCompletionData != null) {
                    val getFinishReason =
                        chatCompletionData.choices.map { it.finish_reason }[0].toString()
                    if (getFinishReason != "stop") {
                        newWord = chatCompletionData.choices.map { it.delta.content }[0]
                        println("gyere most new word = $newWord ")

                    }
                }
            }
        }
        return newWord
    }
}
*/

/*
class GptApiRepoImpl : GptApiRepo {
    override suspend fun callGptApi(question: JsonObject): String {

        var newWord = ""

        withContext(Dispatchers.IO) {
            val getApiResponse = RetrofitHelper.getInstance().create(GptApi::class.java)
            val response = getApiResponse.getChatGptCompletion(question)
            val reader = response.charStream().buffered()
            reader.useLines { lines ->
                lines.forEach { line ->
                    try {
                        val jsonString = line.substringAfter("data: ")
                        val chatCompletionData =
                            Gson().fromJson(jsonString, GptResponse::class.java)
                        if (chatCompletionData != null) {
                            val getFinishReason =
                                chatCompletionData.choices.map { it.finish_reason }[0].toString()
                            if (getFinishReason != "stop") {
                                newWord = chatCompletionData.choices.map { it.delta.content }[0]
                            }
                        }
                    } catch (e: JsonSyntaxException) {
                        println("JSON syntax error occurred: ${e.message}")
                    }
                }
            }
        }
        return newWord
    }
}

*/
