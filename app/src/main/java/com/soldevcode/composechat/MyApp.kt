package com.soldevcode.composechat

import android.app.Application
import com.soldevcode.composechat.data.AppContainer
import com.soldevcode.composechat.data.DefaultAppContainer

class MyApp : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies
     * Manual Dependency Injection code source:
     * https://github.com/philipplackner/ManualDependencyInjection/tree/manual_di */
    companion object {
        lateinit var container: AppContainer
    }
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(applicationContext)
    }
}
