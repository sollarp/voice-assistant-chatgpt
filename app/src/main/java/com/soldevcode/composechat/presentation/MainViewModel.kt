package com.soldevcode.composechat.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.soldevcode.composechat.Languages
import com.soldevcode.composechat.TextToSpeechCode
import com.soldevcode.composechat.data.GptApiRepo
import com.soldevcode.composechat.data.UserPreferencesRepo
import com.soldevcode.composechat.data.dto.gptRequest.GptRequestStream
import com.soldevcode.composechat.models.Message
import com.soldevcode.composechat.models.Message.Answer
import com.soldevcode.composechat.models.Message.Question
import com.soldevcode.composechat.models.toApiMessage
import com.soldevcode.composechat.util.Constants.CONNECTION_ERROR
import com.soldevcode.composechat.util.LanguageItems
import com.soldevcode.composechat.util.Resource
import com.soldevcode.composechat.util.UiState
import com.soldevcode.composechat.util.handleApiExceptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application,
    private val gptApiRepo: GptApiRepo,
    userPreferencesRepo: UserPreferencesRepo
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    val context = application

    private val getUserPreferences = userPreferencesRepo
    /*init {
        viewModelScope.launch {
            getUserPreferences.readUserPreferences(context).collect { language ->
                language.selectedLanguage
                _uiState.value.languages?.languages = language.selectedLanguage.languages
            }
        }
    }*/

    fun updateCurrentLanguage(languages: LanguageItems) {
        _uiState.update { currentState ->
            currentState.copy(languages = languages)
        }
    }

    fun saveToPreference(language: LanguageItems?) {
        _uiState.value.languages?.languages = language?.languages.toString()

        viewModelScope.launch {
            getUserPreferences.readUserPreferences(context).collect { language ->
                println("ez kellene = ${language.selectedLanguage.languages}")
            }
        }


        viewModelScope.launch {
            val lang = language?.languages
            val langCode = language?.recordingLanCode
            val upper = language?.textLanCode?.upper
            val lower = language?.textLanCode?.lower
            getUserPreferences.updateUserPreferences(
                context,
                Languages.newBuilder()
                    .setLanguages(lang)
                    .setTextLanCode(
                        TextToSpeechCode
                            .newBuilder()
                            .setLower(lower)
                            .setUpper(upper)
                            .build())
                    .setRecordingLanCode(langCode)
                    .build()
            )
        }
    }

    fun clearErrorDialog() =
        _uiState.update { it.copy(isErrorDialog = false) }

    private fun handleApiStream(text: String, currentState: UiState): List<Message> {
        val listOfAllMessages = currentState.conversation.toMutableList()
        when (listOfAllMessages.last()) {
            is Answer -> {
                listOfAllMessages[listOfAllMessages.size - 1] = Answer(text)
            }

            else -> {
                updateMessageUiState(Answer(text))
            }
        }
        return listOfAllMessages
    }
    fun getSelectedLanguage() {
        viewModelScope.launch {
            getUserPreferences.readUserPreferences(context).collect { language ->
                _uiState.value.languages?.languages = language.selectedLanguage.languages
            }
        }
    }
    //= uiState.value.languages

    /*fun setSelectedLanguage(language: LanguageItems?) {
        saveToPreference(application, language = language)
    }*/

    fun updateMessageUiState(newState: Message) {
        _uiState.update { currentState ->
            val updatedConversation = currentState.conversation.toMutableList().apply {
                add(newState)
            }
            currentState.copy(conversation = updatedConversation)
        }
        println("talan ez nem =  ${getSelectedLanguage()}")
    }

    private fun fetchApiResponse(request: GptRequestStream) {
        viewModelScope.launch {
            val listOfWords = mutableListOf<String>()
            gptApiRepo.callGptApi(request).collect { resource ->
                _uiState.update { currentState ->
                    when (resource) {
                        is Resource.Success -> {
                            listOfWords.add(resource.data.toString())
                            currentState.copy(
                                isErrorDialog = false,
                                conversation = handleApiStream(
                                    listOfWords.joinToString(""),
                                    currentState
                                )
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

    fun jsonRequestBody(message: Question) {
        val messages = uiState.value.conversation + message
        val request = GptRequestStream(
            model = "gpt-3.5-turbo",
            messages = messages.map { it.toApiMessage() },
            stream = true
        )
        fetchApiResponse(request)
    }
}

