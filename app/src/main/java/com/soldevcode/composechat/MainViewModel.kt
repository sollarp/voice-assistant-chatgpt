package com.soldevcode.composechat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.soldevcode.composechat.data.GptApi
import com.soldevcode.composechat.data.RetrofitHelper
import com.soldevcode.composechat.data.dto.GptResponse
import com.soldevcode.composechat.util.Constants.streamingStopped
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val currentMessageLive = MutableLiveData<String>()
    val currentMessageLiveData = MutableLiveData<String>()
    val getListOfWords = mutableListOf<String>()
    // Live data object to store API stream result.
    private val _listOfWords = MutableLiveData<String>()
    val listOfWords : LiveData<String>
        get() = _listOfWords

    private val _chatOwner = MutableLiveData<String>()
    val chatOwner : LiveData<String>
        get() = _chatOwner

    private val _chatId = MutableLiveData<String>()
    val chatId : LiveData<String>
        get() = _chatId

    fun getWords(): LiveData<String> {
        return listOfWords
    }

    fun updateListOfWords(user:String, newWords: String, chatId: String?) {
        _listOfWords.value = newWords
        _chatOwner.value = user
        _chatId.value = chatId
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

            val response = getApiResponse.getChatGptCompletion(requestBody)
            val reader = response.charStream().buffered()
            reader.useLines { lines ->
                lines.forEach { line ->
                    try {
                        val jsonString = line.substringAfter("data: ")
                        val chatCompletionData =
                            Gson().fromJson(jsonString, GptResponse::class.java)

                        if (chatCompletionData != null) {
                            val getFinishReason = chatCompletionData.choices.map { it.finish_reason }[0].toString()
                            if (getFinishReason != "stop") {
                                val newWord = chatCompletionData.choices.map { it.delta.content }[0]
                                val getChatId = chatCompletionData.id
                                getListOfWords.add(newWord)
                                val setChatOwner = "bot"
                                updateListOfWords( setChatOwner, getListOfWords.joinToString(""), getChatId)
                                delay(50) // Applied to resolve streaming conflict
                            }
                            else{
                                streamingStopped = true
                                getListOfWords.clear()
                            }
                        }
                    } catch (e: JsonSyntaxException) {
                        println("JSON syntax error occurred: ${e.message}")
                    }
                }
            }
        }
    }
}