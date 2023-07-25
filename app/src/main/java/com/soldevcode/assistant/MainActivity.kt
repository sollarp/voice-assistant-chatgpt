package com.soldevcode.assistant


import android.Manifest.permission.RECORD_AUDIO
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.api.gax.rpc.ClientStream
import com.google.api.gax.rpc.ResponseObserver
import com.google.api.gax.rpc.StreamController
import com.google.cloud.speech.v1.*
import com.google.protobuf.ByteString
import com.soldevcode.assistant.data.RetrofitClient
import com.soldevcode.assistant.data.dto.MessagesRequest
import com.soldevcode.assistant.util.AudioPermission
import com.soldevcode.assistant.util.Constants
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel


class MainActivity : AppCompatActivity() {

    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var resultTextView: TextView
    private lateinit var gptTextView: TextView

    val channel = Channel<String>()

    private var isRecording = false

    var clientStream: ClientStream<StreamingRecognizeRequest>? = null
    var responseObserver: ResponseObserver<StreamingRecognizeResponse?>? = null
    private lateinit var audioRecord: AudioRecord
    private var request: StreamingRecognizeRequest? = null
    val transcriptions = mutableListOf<String>()
    private lateinit var retrofitClient: RetrofitClient
    private var prompt: ArrayList<MessagesRequest>? = arrayListOf()
    private lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resultTextView = findViewById(R.id.txt_from_speech)
        gptTextView = findViewById(R.id.txt_from_gpt)
        startButton = findViewById(R.id.btn_start)
        stopButton = findViewById(R.id.btn_stop)
        val scrollView: ScrollView = findViewById(R.id.txt_from_gpt_scroll)

        //AudioPermission.checkAudioPermission(this)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mainViewModel.getWords().observe(this) { currentMessage ->
            scrollView.post {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN)
            }
            gptTextView.text = currentMessage
            scrollView.post {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN)
            }
            scrollView.postDelayed({
                scrollView.fullScroll(ScrollView.FOCUS_DOWN)
            }, 100)
        }

        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this,
                RECORD_AUDIO
            ) -> {
                // You can use the API that requires the permission.

            }
            else -> {
                // You can directly ask for the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(RECORD_AUDIO),
                    Constants.RECORD_AUDIO_REQUEST_CODE

                )
            }
        }
        startButton.setOnClickListener {
            isRecording = true
            setRecording()
            startRecording()
        }
        stopButton.setOnClickListener {
            isRecording = false
            stopRecording()

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constants.RECORD_AUDIO_REQUEST_CODE
            -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    println("permission ok")
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the feature requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun setRecording() {
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

    @SuppressLint("MissingPermission")
    private fun startRecording() {
        val credentialsProvider = SpeechCredentialsProvider().createSpeechClient(this)
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
                                //val allTranscriptions = transcriptions.joinToString(" ")
                                runOnUiThread {
                                    resultTextView.text = newTranscriptions[0].toString()
                                    //prompt = listOf( MessagesRequest("user", allTranscriptions))
                                    prompt?.add(MessagesRequest("user", newTranscriptions.toString()))
                                    isRecording = false
                                    stopRecording()
                                }
                                mainViewModel.fetchApiResponse(newTranscriptions[0].toString())
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
        responseObserver?.onComplete()
    }
}