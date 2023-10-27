package com.soldevcode.composechat.util

import retrofit2.HttpException
import java.io.IOException

sealed class Resource<T> {
    data class Success<T>(val data: T?): Resource<T>()
    class HttpError<T>(val httpException: HttpException): Resource<T>()
    class IoError<T>(val ioException : IOException) : Resource<T>()
}