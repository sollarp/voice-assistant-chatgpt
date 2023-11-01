package com.soldevcode.composechat.presentation

import android.speech.tts.TextToSpeech
import androidx.compose.runtime.mutableStateOf
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
import com.soldevcode.composechat.util.handleApiExceptions
import kotlinx.coroutines.launch
import java.util.Locale


class MainViewModel(
    private val applicationContext: ApplicationContextRepo,
    private val gptApiRepo: GptApiRepo
) : ViewModel() {

    private val listOfWords = mutableListOf<String>()
    private var messages: ArrayList<Message> = arrayListOf()
    val speechToTextValue = mutableStateOf(String())
    private var textToSpeech: TextToSpeech? = null
    val errorMessageHolder = mutableStateOf(String())
    var isErrorDialog = mutableStateOf(false)

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

    init {
        textToSpeech = TextToSpeech(
            applicationContext.getContext()
        ) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val locale = Locale("hu", "HU")
                textToSpeech!!.language = locale
            } else {
                // Handle error
            }
        }
    }

    private fun setErrorDialog(errorMessage: String) {
        errorMessageHolder.value = errorMessage
    }

    fun speak(text: String) {
        textToSpeech!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onCleared() {
        super.onCleared()
        textToSpeech?.shutdown()
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
                when (resource) {
                    is Resource.Success -> {
                        listOfWords.add(resource.data.toString())
                        addAnswer(answer = listOfWords.joinToString(""))
                    }
                    is Resource.HttpError -> {
                        val errorMessageForUser = handleApiExceptions(resource.httpException.code())
                        setMessageForDialog(errorMessageForUser)
                    }
                    is Resource.IoError -> {
                        setMessageForDialog(CONNECTION_ERROR)
                    }
                }
            }
        }
    }

    private fun setMessageForDialog(errorMessageForUser: String) {
        addAnswer(answer = "ERROR")
        setErrorDialog(errorMessageForUser)
        isErrorDialog.value = true
    }
}
