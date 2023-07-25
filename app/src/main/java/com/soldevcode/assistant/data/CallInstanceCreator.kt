package com.soldevcode.assistant.data

import com.google.gson.InstanceCreator
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CallInstanceCreator : InstanceCreator<Call<ResponseBody>> {
    override fun createInstance(type: java.lang.reflect.Type): Call<ResponseBody> {
        return object : Call<ResponseBody> {
            override fun clone(): Call<ResponseBody> {
                TODO("Not yet implemented")
            }

            override fun execute(): Response<ResponseBody> {
                TODO("Not yet implemented")

            }

            override fun isExecuted(): Boolean {
                TODO("Not yet implemented")
            }

            override fun cancel() {
                TODO("Not yet implemented")
            }

            override fun isCanceled(): Boolean {
                TODO("Not yet implemented")
            }

            override fun enqueue(callback: Callback<ResponseBody>) {
                TODO("Not yet implemented")
            }

            override fun request(): Request {
                throw UnsupportedOperationException("request() is not supported")
            }

            override fun timeout(): Timeout {
                TODO("Not yet implemented")
            }
        }
    }
}