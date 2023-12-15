package com.soldevcode.composechat.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel

class ViewModelContainer {
    private val viewModels = mutableMapOf<Class<out ViewModel>, ViewModel>()

    fun <T : ViewModel> getViewModel(viewModelClass: Class<T>): T {
        val viewModel = viewModels[viewModelClass]
        if (viewModel == null) {
            val newViewModel = viewModelClass.newInstance()
            viewModels[viewModelClass] = newViewModel
            return newViewModel as T
        }
        return viewModel as T
    }
}

@Composable
fun provideViewModelContainer() = remember { ViewModelContainer() }