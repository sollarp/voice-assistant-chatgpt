package com.soldevcode.composechat.data

import android.content.Context
import java.io.InputStream


interface GoogleCredentialRepository {
    fun getInputStream (): Context

}

class GoogleCredentialRepositoryImpl(context: Context) : GoogleCredentialRepository {
    private val setContext = context
    override fun getInputStream(): Context = setContext
}



/*

interface GoogleCredentialRepository {
    fun getInputStream (): InputStream
            
}

class GoogleCredentialRepositoryImpl(inputStream: InputStream) : GoogleCredentialRepository {
    private val setInputStream = inputStream
    override fun getInputStream(): InputStream = setInputStream
}
*/
