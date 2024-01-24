package com.soldevcode.composechat.util

object Constants {

    const val baseUrlOpenAI = "https://api.openai.com/"
    const val RECORD_AUDIO_REQUEST_CODE = 101
    const val endPointCompletion = "v1/completions"
    const val turboEndPointChat = "v1/chat/completions"
    const val END_OF_STREAM = "[DONE]"
    const val STOP = "stop"
    const val DATA = "data: "
    const val API_KEY_ERROR = "GPT API key is missing, incorrect or invalid"
    const val UNKNOWN_ERROR = "Unrecognized error API request"
    const val CONNECTION_ERROR = "Internet connection error can’t connect to the server"
    const val USER_PREFERENCES_NAME = "user_preferences"
    const val DATA_STORE_FILE_NAME = "user_prefs.pb"

}