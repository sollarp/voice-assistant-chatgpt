package com.soldevcode.composechat.models.platform.audio

import android.annotation.SuppressLint
import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

@SuppressLint("MissingPermission")
@Composable
fun rememberTextToSpeechManager(

) : TextToSpeechManager {
    val context = LocalContext.current
    val textToSpeech: TextToSpeech? = null


    return remember {
        TextToSpeechManager(
            context,textToSpeech
        )
    }
}

class TextToSpeechManager(
    context: Context,
    private var textToSpeech: TextToSpeech?,
) {

    init {
        textToSpeech = TextToSpeech(
            context.applicationContext
        ) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val locale = Locale("hu", "HU")
                textToSpeech!!.language = locale
            } else {
                // Handle error
            }
        }
    }

    fun speak(text: String) {
        textToSpeech!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun onCleared() {
        textToSpeech?.stop()
    }

}