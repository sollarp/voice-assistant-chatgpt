package com.soldevcode.composechat.data

import com.google.auth.oauth2.GoogleCredentials
import com.soldevcode.composechat.R
import com.soldevcode.composechat.models.MarsPhoto
import java.io.InputStream


/**
 * Repository that fetch mars photos list from marsApi.
 */
interface GoogleCredentialRepository {
    /** Fetches list of MarsPhoto from marsApi */
    suspend fun getMarsPhotos(): List<MarsPhoto>
    fun getInputStream (): InputStream
            
}

/**
 * Network Implementation of Repository that fetch mars photos list from marsApi.
 */
class GoogleCredentialRepositoryImpl(inputStream: InputStream) : GoogleCredentialRepository {
    /** Fetches list of MarsPhoto from marsApi*/
    private val gte = inputStream
    override suspend fun getMarsPhotos(): List<MarsPhoto> = listOf(MarsPhoto("neve", "vlalami"))
    override fun getInputStream(): InputStream = gte
}
