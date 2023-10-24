package com.soldevcode.composechat.util

import retrofit2.HttpException

sealed class Resource<T>(val data: T? = null, val message: HttpException? = null) {
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: HttpException, data: T? = null): Resource<T>(data, message)
}