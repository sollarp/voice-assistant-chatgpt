package com.soldevcode.assistant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.soldevcode.assistant.data.GptApi
import com.soldevcode.assistant.data.RetrofitHelper
import com.soldevcode.assistant.data.dto.GptResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val currentMessageLive = MutableLiveData<String>()
    val currentMessageLiveData = MutableLiveData<String>()
    val getListOfWords = mutableListOf<String>()

    private val _listOfWords = MutableLiveData<String>()
    private val listOfWords : LiveData<String>
        get() = _listOfWords

    fun getWords(): LiveData<String> {
        return listOfWords
    }

    private fun updateListOfWords(newWords: String) {
        _listOfWords.value = newWords
    }

    fun fetchApiResponse(question: String) {
        viewModelScope.launch {
            val getApiResponse = RetrofitHelper.getInstance().create(GptApi::class.java)
            val requestBody = JsonObject().apply {
                addProperty("model", "gpt-3.5-turbo")
                add("messages", JsonArray().apply {
                    val message = JsonObject().apply {
                        addProperty("content", question)
                        addProperty("role", "user")
                    }
                    add(message)
                })
                addProperty("stream", true)
            }
            //val request = messages?.map { it }?.let { GptRequest("gpt-3.5-turbo", it, false) }
            println("json : $requestBody")


            val response = getApiResponse.getChatGptCompletion(requestBody)
            val reader = response.charStream().buffered()
            reader.useLines { lines ->
                lines.forEach { line ->
                    try {
                        val jsonString = line.substringAfter("data: ")
                        val chatCompletionData =
                            Gson().fromJson(jsonString, GptResponse::class.java)
                        println("response result: $chatCompletionData")
                        if (chatCompletionData != null) {
                            val newWord = chatCompletionData.choices.map { it.delta.content }[0]
                            getListOfWords.add(newWord)
                            println("response result list $getListOfWords")
                            updateListOfWords(getListOfWords.joinToString(""))
                            delay(50)
                        }
                    } catch (e: JsonSyntaxException) {
                        println("JSON syntax error occurred: ${e.message}")
                    }
                }
            }
        }
    }
}