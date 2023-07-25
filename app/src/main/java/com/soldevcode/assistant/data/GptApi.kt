package com.soldevcode.assistant.data

import com.google.gson.JsonObject
import com.soldevcode.assistant.data.dto.GptRequest
import com.soldevcode.assistant.data.dto.GptResponse
import com.soldevcode.assistant.data.dto.GptStream.GptApiStream
import com.soldevcode.assistant.util.Constants.endPointCompletion
import com.soldevcode.assistant.util.Constants.turboEndPointChat
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Streaming

interface GptApi {
    @Headers("Authorization: Bearer ")
    @POST(turboEndPointChat)
    @Streaming
    suspend fun getChatGptCompletion(@Body body: JsonObject): ResponseBody

    @Headers("Authorization: Bearer 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")
    @POST(turboEndPointChat)
    @Streaming
    suspend fun getChatGptStream(@Body request: GptRequest): GptApiStream
}
