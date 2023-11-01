package com.soldevcode.composechat.models.platform.audio

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.soldevcode.composechat.presentation.MainViewModel
import com.soldevcode.composechat.util.SpeechCredentialsProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** Referring to Google documentation
https://cloud.google.com/speech-to-text
/docs/transcribe-streaming-audio#perform_streaming_speech_recognition_on_an_audio_stream
 */

@SuppressLint("MissingPermission")
@Composable
fun rememberRecordingManager(viewModel: MainViewModel = viewModel()): RecordingManager {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val showRecording = rememberSaveable { mutableStateOf(false) }
    val responseObserver: ResponseObserver<StreamingRecognizeResponse?>? = null
    val transcriptions = mutableListOf<String>()
    val isRecording = rememberSaveable { mutableStateOf(false) }
    val request: StreamingRecognizeRequest? = null
    val audioRecord = AudioRecord(
        MediaRecorder.AudioSource.MIC,
        44100,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT,
        AudioRecord.getMinBufferSize(
            44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT
        )
    )

    return remember {
        RecordingManager(
            context, showRecording, scope,
            responseObserver, transcriptions,
            isRecording, request, audioRecord, viewModel
        )
    }
}

class RecordingManager(
    private val context: Context,
    val showRecording: MutableState<Boolean>,
    private val scope: CoroutineScope,
    private var responseObserver: ResponseObserver<StreamingRecognizeResponse?>?,
    var transcriptions: MutableList<String>,
    var isRecording: MutableState<Boolean>,
    private var request: StreamingRecognizeRequest?,
    private var audioRecord: AudioRecord,
    var viewModel: MainViewModel,
) {

    @SuppressLint("MissingPermission")
    fun startRecording() {
        val credentialsProvider = SpeechCredentialsProvider().getSpeechClient(
            context.assets.open("google_key.json")
        )

        scope.launch(Dispatchers.IO) {
            try {
                setRecording()
                SpeechClient.create(credentialsProvider).use { client ->
                    responseObserver = object : ResponseObserver<StreamingRecognizeResponse?> {
                        val responses = mutableListOf<StreamingRecognizeResponse>()

                        override fun onStart(controller: StreamController) {
                            showRecording.value = true
                        }

                        override fun onResponse(response: StreamingRecognizeResponse?) {
                            if (response != null) {
                                responses.add(response)
                                val newTranscriptions = response.resultsList
                                    .flatMap { it.alternativesList }
                                    .map { it.transcript }
                                transcriptions.addAll(newTranscriptions)
                                isRecording.value = false
                                stopRecording()
                                viewModel.speechToTextValue.value = newTranscriptions[0].toString()
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
                                showRecording.value = false
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
                    while (isRecording.value) {
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
                        showRecording.value = true
                    }
                }
            } catch (e: Exception) {
                println("try catch error in main activity: $e")
            }
            responseObserver?.onComplete()
        }
    }

    private fun setRecording() {
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
        // Delay require otherwise showRecording not been updated
        isRecording.value = true
    }

    fun stopRecording() {
        audioRecord.stop()
        isRecording.value = false
        showRecording.value = false
        //responseObserver?.onComplete()
    }

}