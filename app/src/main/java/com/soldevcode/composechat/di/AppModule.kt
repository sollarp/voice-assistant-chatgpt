package com.soldevcode.composechat.di

import com.soldevcode.composechat.data.GoogleCredentialRepository
import com.soldevcode.composechat.data.GoogleCredentialRepositoryImpl

interface AppContainer {
    val marsPhotosRepository: GoogleCredentialRepository
}

/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
class DefaultAppContainer : AppContainer {
    /**
     * DI implementation for Mars photos repository
     */
    override val marsPhotosRepository: GoogleCredentialRepository by lazy {
        GoogleCredentialRepositoryImpl()
    }
}

/*
interface AppModule {
    val inputStream: InputStream
}

class AppModuleImpl(
    private val appContext: Context
): AppModule {

    override val inputStream: InputStream
        get() = appContext.resources.openRawResource(R.raw.google_key)

}
*/
