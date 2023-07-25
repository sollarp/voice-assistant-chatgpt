package com.soldevcode.assistant.data

import com.soldevcode.assistant.util.Constants.baseUrlOpenAI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    fun gptClientApi (): GptApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrlOpenAI)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(GptApi::class.java)
    }

}