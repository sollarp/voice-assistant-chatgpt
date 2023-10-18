package com.soldevcode.composechat.data

import com.google.gson.Gson
import com.soldevcode.composechat.data.dto.GptResponse
import com.soldevcode.composechat.data.dto.gptRequest.GptRequestStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface GptApiRepo {
    fun callGptApi(requestStream: GptRequestStream): Flow<String>
}

class GptRepositoryImpl : GptApiRepo {
    private val client = RetrofitHelper.getInstance().create(GptApi::class.java)

    companion object {
        private const val FINISHED_SENTENCE_GPT = "[DONE]"
    }
    override fun callGptApi(requestStream: GptRequestStream) =
        flow {
            val response = client.getChatGptCompletion(requestStream)
            val reader = response.charStream().buffered()

            reader.useLines { lines ->
                lines.forEach { line ->
                    val jsonString = line.substringAfter("data: ")

                    if(jsonString != FINISHED_SENTENCE_GPT) {
                        val chatCompletionData =
                            Gson().fromJson(jsonString, GptResponse::class.java)
                        chatCompletionData?.let {
                            val getFinishReason =
                                it.choices.map { choice -> choice.finish_reason }[0].toString()
                            if (getFinishReason != "stop") {
                                val newWord = it.choices.map { choice -> choice.delta.content }[0]
                                delay(20)
                                emit(newWord)  // Invoking the callback
                            }
                        }
                    }
                }
            }
        }.flowOn(Dispatchers.IO)
}