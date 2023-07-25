package com.soldevcode.assistant

import android.content.Context
import com.google.auth.oauth2.GoogleCredentials
import java.io.IOException

object GoogleCredentialProvider {

    @Throws(IOException::class)
    fun getCredentialsFromRawResource(context: Context): GoogleCredentials {
        val inputStream = context.resources.openRawResource(R.raw.google_key)
        return GoogleCredentials.fromStream(inputStream)
    }
}