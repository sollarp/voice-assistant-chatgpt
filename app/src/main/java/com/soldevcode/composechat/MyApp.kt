package com.soldevcode.composechat

import android.app.Application
import com.soldevcode.composechat.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            // other configurations if needed
            androidLogger()
            androidContext(this@MyApp)
            modules(appModule)
        }
    }
}