/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.soldevcode.composechat.data

import android.content.Context


// This is an AppModule

/**
 * Dependency Injection container at the application level.
 */

interface AppContainer {
    val googleCredentialRepository: GoogleCredentialRepository
    val getInputStream: Context

}
class DefaultAppContainer(appContext: Context) : AppContainer {
    override val googleCredentialRepository: GoogleCredentialRepository by lazy {
        GoogleCredentialRepositoryImpl(getInputStream)
    }

    override val getInputStream: Context by lazy {
        //appContext.assets.open("google_key.json")
        appContext
    }
}




/*interface AppContainer {
    val googleCredentialRepository: GoogleCredentialRepository

}

class DefaultAppContainer(appContext: Context) : AppContainer {
    override val googleCredentialRepository: GoogleCredentialRepository by lazy {
        val getInputStream = appContext.assets.open("google_key.json")
        GoogleCredentialRepositoryImpl(getInputStream)
    }

    *//**
     * DI implementation for Mars photos repository
     *//*

}*/
