package com.soldevcode.composechat.data

import android.content.Context

interface ApplicationContextRepo {
    fun getContext (): Context

}
class ApplicationContextRepoImpl(context: Context) : ApplicationContextRepo {
    private val setContext = context
    override fun getContext(): Context = setContext
}