package com.soldevcode.composechat.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soldevcode.composechat.data.ApplicationContextRepo
import com.soldevcode.composechat.data.GptApiRepo
import com.soldevcode.composechat.data.dto.gptRequest.GptRequestStream
import com.soldevcode.composechat.data.dto.gptRequest.Message
import com.soldevcode.composechat.models.ConversationModel
import com.soldevcode.composechat.util.Constants.CONNECTION_ERROR
import com.soldevcode.composechat.util.Resource
import com.soldevcode.composechat.util.UiState
import com.soldevcode.composechat.util.handleApiExceptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MainViewModel(
    gptApiRepo1: ApplicationContextRepo,
    private val gptApiRepo: GptApiRepo
) : ViewModel() {

    private val listOfWords = mutableListOf<String>()
    private var messages: ArrayList<Message> = arrayListOf()
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _conversationsLiveData = MutableLiveData<MutableList<ConversationModel>>()
    val conversationsLiveData: MutableLiveData<MutableList<ConversationModel>>
        get() = _conversationsLiveData

    private fun getConversations(): MutableList<ConversationModel> =
        conversationsLiveData.value ?: mutableListOf()

    fun addQuestionToLiveData(chatOwner: String, question: String) {
        listOfWords.clear()
        val items = getConversations()
        _conversationsLiveData.value = items.toMutableList().apply {
            add(ConversationModel(chatOwner = chatOwner, question = question))
        }
    }
    fun clearErrorDialog() {
        _uiState.update { it.copy(isErrorDialog = false) }
    }
    /**
     * It starts with a list of chat items (items) retrieved from getConversations().
     * It checks if the last chat item in the list is owned by "system."
     * If the last item is owned by "system," it updates the answer for that item.
     * If not or if there are no items, it adds a new chat item owned by
    "system" with the provided answer.
     * Finally, it updates the live data containing the chat items.
     */
    private fun addAnswer(answer: String) {
        val items = getConversations().toMutableList()
        items.lastOrNull()?.takeIf { it.chatOwner == "system" }?.apply {
            items[items.size - 1] = copy(answer = answer)
        } ?: run {
            items.add(ConversationModel(chatOwner = "system", answer = answer))
        }
        _conversationsLiveData.value = items
    }

    fun jsonRequestBody() {
        val lastConversation = getConversations().lastOrNull()
        val chatOwner = lastConversation?.chatOwner ?: ""
        val content =
            if (chatOwner == "user") lastConversation?.question ?: ""
            else lastConversation?.answer ?: ""

        messages.add(Message(role = chatOwner, content = content))

        val request = GptRequestStream(
            model = "gpt-3.5-turbo",
            messages = messages,
            stream = true
        )
        fetchApiResponse(request)
    }

    private fun fetchApiResponse(request: GptRequestStream) {
        viewModelScope.launch {
            gptApiRepo.callGptApi(request).collect { resource ->
                _uiState.update { currentState ->
                    when (resource) {
                        is Resource.Success -> {
                            listOfWords.add(resource.data.toString())
                            addAnswer(answer = listOfWords.joinToString(""))
                            currentState.copy(
                                isErrorDialog = false,
                            )
                        }
                        is Resource.HttpError -> {
                            val errorMessageForUser =
                                handleApiExceptions(resource.httpException.code())
                            currentState.copy(
                                isErrorDialog = true,
                                errorMessage = errorMessageForUser
                            )
                        }
                        is Resource.IoError -> {
                            currentState.copy(
                                isErrorDialog = true,
                                errorMessage = CONNECTION_ERROR
                            )
                        }
                    }
                }
            }
        }
    }
}

