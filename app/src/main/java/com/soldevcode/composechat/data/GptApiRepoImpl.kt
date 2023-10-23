package com.soldevcode.composechat.data

import com.google.gson.Gson
import com.soldevcode.composechat.data.dto.GptResponse
import com.soldevcode.composechat.data.dto.gptRequest.GptRequestStream
import com.soldevcode.composechat.util.Constants.DATA
import com.soldevcode.composechat.util.Constants.END_OF_STREAM
import com.soldevcode.composechat.util.Constants.STOP
import com.soldevcode.composechat.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

/**
 * Response starts with data: {"id":"chatcmpl-123",..."finish_reason":null...... }
 * Removing the "data: " prefix and parsing for response data content.
 * Emit stops when "finish_reason":"stop" received.
 * Closing stream when data: {DONE} received.
 */
interface GptApiRepo {
    suspend fun callGptApi(requestStream: GptRequestStream): Flow<Resource<String>>
}

class GptRepositoryImpl : GptApiRepo {
    private val client = RetrofitHelper.getInstance().create(GptApi::class.java)

    override suspend fun callGptApi(requestStream: GptRequestStream) = flow {
        val response = client.getChatGptCompletion(requestStream)
        val responseText = response.body()?.string()
        println("hozza addva request stream = $responseText ")
        //val response = safeApiCall(response2)
        val result: Resource<String> =
            if (response.isSuccessful) {
                Resource.Success(
                    parseContentFromResponse(responseText)
                    //"hogy vagy"
                )
            } else {
                Resource.Error(HttpException(response))
            }

        emit(result)
        delay(20)
    }.flowOn(Dispatchers.IO)
}

    fun parseContentFromResponse(response: String?): String {
    var content = ""
    response.let { text ->
        text!!.lines().forEach { line ->
            val responseWithoutPrefix = removeDataPrefix(line)
            if (responseWithoutPrefix != END_OF_STREAM) {
                val chatCompletionData = Gson().fromJson(
                    responseWithoutPrefix, GptResponse::class.java
                )
                chatCompletionData?.choices?.firstOrNull()?.let { choice ->
                    if (choice.finish_reason != STOP) {
                        content = choice.delta.content
                    }
                }
            }
        }
    }
    return content
}


fun removeDataPrefix(response: String): String {
    return response.substringAfter(DATA)
}