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
package com.soldevcode.composechat.di

import android.content.Context
import com.soldevcode.composechat.data.ApplicationContextRepo
import com.soldevcode.composechat.data.ApplicationContextRepoImpl
import com.soldevcode.composechat.data.GptApiRepo
import com.soldevcode.composechat.data.GptRepositoryImpl

/**
 * Dependency Injection container at the application level.
 * Passing the Context from here to the ApplicationContextRepoImpl and inject it to ViewModel.
 * In production environment Google Json credential not stored in app it does not need context for
 * the inputStream to create.
 * This is usually happens using server side authentication so this could be implemented using
 * ApplicationContextRepo from remote resource. This is why the Context has been passed to repo.
 */

interface AppContainer {
    val applicationContextRepository: ApplicationContextRepo
    val getInputStream: Context
    val gptApiRepository: GptApiRepo

}
class DefaultAppContainer(appContext: Context ) : AppContainer {

    override val applicationContextRepository: ApplicationContextRepo by lazy {
        ApplicationContextRepoImpl(getInputStream)
    }

    override val getInputStream: Context by lazy {
        appContext
    }
    override val gptApiRepository: GptApiRepo by lazy {
        GptRepositoryImpl()
    }

}