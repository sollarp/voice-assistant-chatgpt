package com.soldevcode.composechat

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
import com.soldevcode.composechat.models.ConversationModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val listOfWords = mutableListOf<String>()

    private val _conversationsLiveData = MutableLiveData<MutableList<ConversationModel>>()
    val conversationsLiveData: MutableLiveData<MutableList<ConversationModel>>
        get() = _conversationsLiveData

    fun addQuestion(chatOwner: String, question: String) {
        val items = getConversations()
        _conversationsLiveData.value = items.toMutableList().apply {
            add(ConversationModel(chatOwner = chatOwner, question = question))
        }
    }

    private fun addAnswer(answer: String, chatOwner: String) {
        val items = getConversations()
        if (chatOwner == "bot" && items.lastOrNull()?.chatOwner == "bot") {
            val updatedItem = items.last().copy(answer = answer)
            _conversationsLiveData.value = (items.dropLast(1) + updatedItem).toMutableList()
        } else {
            _conversationsLiveData.value = (items +
                    ConversationModel(chatOwner = chatOwner, answer = answer)).toMutableList()
        }
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
                                listOfWords.add(newWord)
                                addAnswer(answer = listOfWords.joinToString(""), chatOwner = "bot")
                                delay(50) // Applied to resolve streaming conflict
                            }
                            else{
                                listOfWords.clear()
                            }
                        }
                    } catch (e: JsonSyntaxException) {
                        println("JSON syntax error occurred: ${e.message}")
                    }
                }
            }
        }
    }
    private fun getConversations(): MutableList<ConversationModel> =
        conversationsLiveData.value ?: mutableListOf()
}
