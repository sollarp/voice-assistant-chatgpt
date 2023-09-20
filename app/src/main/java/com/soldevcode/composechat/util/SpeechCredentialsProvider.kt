package com.soldevcode.composechat.util

import android.content.Context
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.speech.v1.SpeechSettings
import java.io.InputStream

class SpeechCredentialsProvider() : GoogleCredentials() {

    fun getSpeechClient(jsonLiveData: InputStream): SpeechSettings? {
        val credentials = fromStream(jsonLiveData)
        return SpeechSettings.newBuilder()
            .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
            .build()
    }


    fun setGoogleCredentialFromStream(context: Context): InputStream {
        // Load the service account key JSON file from the assets folder


        // Create GoogleCredentials from the JSON key file
        return context.assets.open("google_key.json")

        //return context.resources.openRawResource(R.raw.google_key)
    }

  /*  fun createSpeechClient(): SpeechSettings? {

        // Set up the SpeechSettings using the credentials

        // Create and return the SpeechClient
        return SpeechSettings.newBuilder()
            .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
            .build()
    }*/
}
