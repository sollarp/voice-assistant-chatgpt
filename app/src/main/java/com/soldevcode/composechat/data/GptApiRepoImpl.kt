package com.soldevcode.composechat.data

import android.util.Log
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
import java.io.IOException

/**
 * Response starts with data: {"id":"chatcmpl-123",..."finish_reason":null...... }
 * Removing the "data: " prefix and parsing for response data content.
 * Emit stops when "finish_reason":"stop" received.
 * Closing stream when data: {DONE} received.
 */
interface GptApiRepo {
    fun callGptApi(requestStream: GptRequestStream): Flow<Resource<String>>
}

class GptRepositoryImpl : GptApiRepo {
    private val client = RetrofitHelper.getInstance().create(GptApi::class.java)

    override fun callGptApi(requestStream: GptRequestStream) =
        flow {
            try {
                val response = client.getChatGptCompletion(requestStream)
                val reader = response.charStream().buffered()
                reader.useLines { lines ->
                    lines.forEach { line ->
                        Log.i("log info", line)
                        emit(parseContentFromResponse(line))  // Invoking the callback
                    }
                }
            } catch (e: HttpException ) {
                emit(Resource.HttpError(e))
                //Log.i("log response", e.code().toString())
            }
            catch (e: IOException) {
                emit(Resource.IoError(e))
            }
        }.flowOn(Dispatchers.IO)
}

private fun parseContentFromResponse(line: String): Resource.Success<String> {
    var content = ""
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
    return Resource.Success(content)
}

fun removeDataPrefix(response: String): String {
    return response.substringAfter(DATA)
}