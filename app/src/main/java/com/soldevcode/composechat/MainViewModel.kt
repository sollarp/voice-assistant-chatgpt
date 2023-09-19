package com.soldevcode.composechat

import android.media.AudioRecord
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.api.gax.rpc.ResponseObserver
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.speech.v1.RecognitionConfig
import com.google.cloud.speech.v1.StreamingRecognitionConfig
import com.google.cloud.speech.v1.StreamingRecognizeRequest
import com.google.cloud.speech.v1.StreamingRecognizeResponse
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.soldevcode.composechat.data.GptApi
import com.soldevcode.composechat.data.GoogleCredentialRepositoryImpl
import com.soldevcode.composechat.data.RetrofitHelper
import com.soldevcode.composechat.data.dto.GptResponse
import com.soldevcode.composechat.data.dto.MessagesRequest
import com.soldevcode.composechat.models.ConversationModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(private val networkMarsPhotosRepository: GoogleCredentialRepositoryImpl) : ViewModel() {

    private val listOfWords = mutableListOf<String>()
    private var responseObserver: ResponseObserver<StreamingRecognizeResponse?>? = null
    val transcriptions = mutableListOf<String>()
    private lateinit var audioRecord: AudioRecord
    private var prompt: ArrayList<MessagesRequest>? = arrayListOf()
    private var isRecording = false
    private var request: StreamingRecognizeRequest? = null



    private val _conversationsLiveData = MutableLiveData<MutableList<ConversationModel>>()
    val conversationsLiveData: MutableLiveData<MutableList<ConversationModel>>
        get() = _conversationsLiveData

    private fun getConversations(): MutableList<ConversationModel> =
        conversationsLiveData.value ?: mutableListOf()

    var jsonLiveData: GoogleCredentials? = null

    fun updateJsonValue(value: GoogleCredentials) {
        jsonLiveData = value
    }

    private fun getGoogleCredentials() = jsonLiveData

    suspend fun addQuestion(chatOwner: String, question: String) {
        println("ez most biztos jo = ${networkMarsPhotosRepository.getMarsPhotos().map { it }.toString()}")
        val items = getConversations()
        _conversationsLiveData.value = items.toMutableList().apply {
            add(ConversationModel(chatOwner = chatOwner, question = question))
        }
    }

    fun setRecording() {
        isRecording = true
        //API service set up
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

   /* @SuppressLint("MissingPermission")
    fun startRecording() {
        val credentialsProvider =
            jsonLiveData?.let { speechCredentialsProvider.getSpeechClient(it) }

        val credentialsProvider = SpeechCredentialsProvider().getSpeechClient(streamProvider)
        CoroutineScope(Dispatchers.IO).launch {
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
                                // resultTextView.text = newTranscriptions[0].toString()
                                prompt?.add(MessagesRequest("user", newTranscriptions.toString()))
                                isRecording = false
                                stopRecording()

                                fetchApiResponse(newTranscriptions[0].toString())
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
*/
    private fun stopRecording() {
        audioRecord.stop()  // Stop recording
        responseObserver?.onComplete()
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
                            val getFinishReason =
                                chatCompletionData.choices.map { it.finish_reason }[0].toString()
                            if (getFinishReason != "stop") {
                                val newWord = chatCompletionData.choices.map { it.delta.content }[0]
                                listOfWords.add(newWord)
                                addAnswer(answer = listOfWords.joinToString(""), chatOwner = "bot")
                                delay(50) // Applied to resolve streaming conflict
                            } else {
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
}
