package com.soldevcode.composechat.util

import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.speech.v1.SpeechSettings
import java.io.InputStream

class SpeechCredentialsProvider() : GoogleCredentials() {

    fun getSpeechClient(inputStream: InputStream): SpeechSettings? {
        val credentials = fromStream(inputStream)
        return SpeechSettings.newBuilder()
            .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
            .build()
    }
}
