package com.soldevcode.composechat.data

import com.google.gson.Gson
import com.soldevcode.composechat.data.dto.GptResponse
import com.soldevcode.composechat.data.dto.gptRequest.GptRequestStream
import com.soldevcode.composechat.util.Constants.DATA
import com.soldevcode.composechat.util.Constants.END_OF_STREAM
import com.soldevcode.composechat.util.Constants.STOP
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

    override fun callGptApi(requestStream: GptRequestStream) = flow {
        val response = client.getChatGptCompletion(requestStream)
        val responseText = response.body()?.string()
        responseText?.let { text ->
            text.lines().forEach { line ->
                val responseWithoutPrefix = removeDataPrefix(line)
                if (responseWithoutPrefix != END_OF_STREAM) {
                    val chatCompletionData = Gson().fromJson(
                        responseWithoutPrefix, GptResponse::class.java
                    )
                    chatCompletionData?.choices?.firstOrNull()?.let { choice ->
                        if (choice.finish_reason != STOP) {
                            delay(20)
                            emit(choice.delta.content)  // Emit the content
                        }
                    }
                }
            }
        }
    }.flowOn(Dispatchers.IO)
}

fun removeDataPrefix(response: String): String {
    return response.substringAfter(DATA)
}