package com.soldevcode.assistant.data

import com.google.gson.GsonBuilder
import com.soldevcode.assistant.util.Constants.baseUrlOpenAI
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {

    fun getInstance(): Retrofit {
        val timeoutDuration = 30L // Timeout duration in seconds
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(timeoutDuration, TimeUnit.SECONDS)
            .readTimeout(timeoutDuration, TimeUnit.SECONDS)
            .writeTimeout(timeoutDuration, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(baseUrlOpenAI)
            .client(httpClient)
            //.addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}