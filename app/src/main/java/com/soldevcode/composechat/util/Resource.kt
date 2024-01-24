package com.soldevcode.composechat.util

import com.soldevcode.composechat.models.Message
import retrofit2.HttpException
import java.io.IOException

sealed class Resource<T> {
    data class Success<T>(val data: T?) : Resource<T>()
    class HttpError<T>(val httpException: HttpException) : Resource<T>()
    class IoError<T>(val ioException: IOException) : Resource<T>()
}

data class UiState(
    var isErrorDialog: Boolean = false,
    val errorMessage: String = "",
    val postsFeed: String = "",
    val conversation: List<Message> = emptyList(),
    var languages: Languages = Languages(
        "United Kingdom",
        TextToSpeechCode("en", "GB"),
        "en")
)