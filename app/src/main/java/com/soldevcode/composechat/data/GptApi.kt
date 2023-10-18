package com.soldevcode.composechat.data

import com.google.gson.JsonObject
import com.soldevcode.composechat.BuildConfig
import com.soldevcode.composechat.data.dto.gptRequest.GptRequestStream
import com.soldevcode.composechat.util.Constants.turboEndPointChat
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Streaming

interface GptApi {
    @Headers("Authorization: Bearer ${BuildConfig.GPT_KEY}")
    @POST(turboEndPointChat)
    @Streaming
    suspend fun getChatGptCompletion(@Body requestStream: GptRequestStream): ResponseBody
}
