package com.soldevcode.composechat.di

import com.soldevcode.composechat.data.GptApiRepo
import com.soldevcode.composechat.data.GptRepositoryImpl
import com.soldevcode.composechat.presentation.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val appModule = module {
    single<GptApiRepo> { GptRepositoryImpl() }
    viewModelOf(::MainViewModel)
}
