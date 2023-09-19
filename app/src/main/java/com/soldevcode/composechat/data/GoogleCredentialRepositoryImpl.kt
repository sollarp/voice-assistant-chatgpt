package com.soldevcode.composechat.data

import android.content.Context
import com.soldevcode.composechat.R
import com.soldevcode.composechat.models.MarsPhoto
import java.io.InputStream


/**
 * Repository that fetch mars photos list from marsApi.
 */
interface GoogleCredentialRepository {
    /** Fetches list of MarsPhoto from marsApi */
    suspend fun getMarsPhotos(): List<MarsPhoto>
    fun getInputStream (context: Context): InputStream
            
}

/**
 * Network Implementation of Repository that fetch mars photos list from marsApi.
 */
class GoogleCredentialRepositoryImpl : GoogleCredentialRepository {
    /** Fetches list of MarsPhoto from marsApi*/
    override suspend fun getMarsPhotos(): List<MarsPhoto> = listOf(MarsPhoto("neve", "vlalami"))
    override fun getInputStream(context: Context): InputStream {
        return context.resources.openRawResource(R.raw.google_key)
    }
}
