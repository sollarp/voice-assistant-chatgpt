package com.soldevcode.composechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.soldevcode.composechat.presentation.MainViewModel
import com.soldevcode.composechat.presentation.viewModelFactory
import com.soldevcode.composechat.ui.ConversationScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            viewModel<MainViewModel>(
                factory = viewModelFactory {
                    MainViewModel(MyApp.container.applicationContextRepository)
                }
            )
            ConversationScreen()
        }
    }

}
