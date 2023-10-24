package com.soldevcode.composechat.presentation

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.api.gax.rpc.ClientStream
import com.google.api.gax.rpc.ResponseObserver
import com.google.api.gax.rpc.StreamController
import com.google.cloud.speech.v1.RecognitionConfig
import com.google.cloud.speech.v1.SpeechClient
import com.google.cloud.speech.v1.SpeechRecognitionAlternative
import com.google.cloud.speech.v1.StreamingRecognitionConfig
import com.google.cloud.speech.v1.StreamingRecognitionResult
import com.google.cloud.speech.v1.StreamingRecognizeRequest
import com.google.cloud.speech.v1.StreamingRecognizeResponse
import com.google.protobuf.ByteString
import com.soldevcode.composechat.models.ConversationModel
import com.soldevcode.composechat.util.SpeechCredentialsProvider
import kotlinx.coroutines.launch
import android.speech.tts.TextToSpeech
import com.soldevcode.composechat.data.ApplicationContextRepo
import com.soldevcode.composechat.data.GptApiRepo
import com.soldevcode.composechat.data.dto.gptRequest.GptRequestStream
import com.soldevcode.composechat.data.dto.gptRequest.Message
import com.soldevcode.composechat.util.Resource
import okhttp3.ResponseBody
import retrofit2.Response
import java.util.Locale


class MainViewModel(
    private val applicationContext: ApplicationContextRepo,
    private val gptApiRepo: GptApiRepo
) : ViewModel() {

    private val listOfWords = mutableListOf<String>()
    private var responseObserver: ResponseObserver<StreamingRecognizeResponse?>? = null
    val transcriptions = mutableListOf<String>()
    private lateinit var audioRecord: AudioRecord
    private var messages: ArrayList<Message> = arrayListOf()
    private var isRecording = false
    private var request: StreamingRecognizeRequest? = null
    val speechToTextValue = mutableStateOf(String())
    private var textToSpeech: TextToSpeech? = null

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

    fun speak(text: String) {
        textToSpeech!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onCleared() {
        super.onCleared()
        textToSpeech?.shutdown()
    }

    fun setRecording() {
        isRecording = true
        //API service set up for Hungarian language
        val recognitionConfig: RecognitionConfig = RecognitionConfig.newBuilder()
            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
            .setLanguageCode("hu-HU")
            .setSampleRateHertz(16000)
            .build()
        val streamingRecognitionConfig: StreamingRecognitionConfig =
            StreamingRecognitionConfig.newBuilder().setConfig(recognitionConfig).build()
        request = StreamingRecognizeRequest.newBuilder()
            .setStreamingConfig(streamingRecognitionConfig)
            .build()
    }

    /** Referring to Google documentation
    https://cloud.google.com/speech-to-text
    /docs/transcribe-streaming-audio#perform_streaming_speech_recognition_on_an_audio_stream
     */
    @SuppressLint("MissingPermission")
    fun startRecording() {
        val credentialsProvider = SpeechCredentialsProvider().getSpeechClient(
            applicationContext.getContext().assets.open("google_key.json")
        )
        viewModelScope.launch {
            try {
                SpeechClient.create(credentialsProvider).use { client ->
                    responseObserver = object : ResponseObserver<StreamingRecognizeResponse?> {
                        val responses = mutableListOf<StreamingRecognizeResponse>()

                        override fun onStart(controller: StreamController) {
                        }

                        override fun onResponse(response: StreamingRecognizeResponse?) {
                            if (response != null) {
                                responses.add(response)
                                val newTranscriptions = response.resultsList
                                    .flatMap { it.alternativesList }
                                    .map { it.transcript }
                                transcriptions.addAll(newTranscriptions)
                                isRecording = false
                                stopRecording()
                                speechToTextValue.value = newTranscriptions[0].toString()
                            }
                        }

                        override fun onComplete() {
                            for (response in responses) {
                                val result: StreamingRecognitionResult = response.resultsList[0]
                                val alternative: SpeechRecognitionAlternative =
                                    result.alternativesList[0]
                                System.out.printf(
                                    "Transcript : %s\n",
                                    alternative.transcript
                                )
                            }
                        }

                        override fun onError(t: Throwable) {
                            println(t)
                        }
                    }
                    val clientStream: ClientStream<StreamingRecognizeRequest> =
                        client.streamingRecognizeCallable().splitCall(responseObserver)
                    // The first request in a streaming call has to be a config
                    clientStream.send(request)
                    // Audio set up
                    audioRecord = AudioRecord.Builder()
                        .setAudioSource(MediaRecorder.AudioSource.MIC)
                        .setAudioFormat(
                            AudioFormat.Builder()
                                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                                .setSampleRate(16000)
                                .setChannelMask(AudioFormat.CHANNEL_IN_MONO)
                                .build()
                        )
                        .setBufferSizeInBytes(3200)
                        .build()
                    // Start recording
                    audioRecord.startRecording()

                    while (isRecording) {
                        val audioBuffer = ByteArray(6400)
                        val bytesRead = audioRecord.read(audioBuffer, 0, audioBuffer.size)
                        if (bytesRead > 0) {
                            request = StreamingRecognizeRequest.newBuilder()
                                .setAudioContent(ByteString.copyFrom(audioBuffer, 0, bytesRead))
                                .build()
                            request = StreamingRecognizeRequest.newBuilder()
                                .setAudioContent(ByteString.copyFrom(audioBuffer))
                                .build()
                            clientStream.send(request)
                        }
                    }
                }
            } catch (e: Exception) {
                println("try catch error in main activity: $e")
            }
            responseObserver?.onComplete()
        }
    }

    private fun stopRecording() {
        audioRecord.stop()  // Stop recording
        // responseObserver?.onComplete()
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
                    is Resource.Error -> {
                        addAnswer(answer = resource.message.toString())
                    }
                }
            }
        }
    }
}
