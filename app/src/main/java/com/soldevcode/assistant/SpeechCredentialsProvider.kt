package com.soldevcode.assistant

import android.content.Context
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.speech.v1.SpeechSettings
import java.io.InputStream

class SpeechCredentialsProvider() : GoogleCredentials() {

    fun createSpeechClient(context: Context): SpeechSettings? {
        // Load the service account key JSON file from the assets folder
        val credentialsStream: InputStream = context.resources.openRawResource(R.raw.google_key)

        // Create GoogleCredentials from the JSON key file
        val credentials = fromStream(credentialsStream)

        // Set up the SpeechSettings using the credentials

        // Create and return the SpeechClient
        return SpeechSettings.newBuilder()
            .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
            .build()
    }
}