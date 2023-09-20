package com.soldevcode.composechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.soldevcode.composechat.data.GoogleCredentialRepositoryImpl
import com.soldevcode.composechat.presentation.MainViewModel
import com.soldevcode.composechat.presentation.ViewModelFactoryHelper
import com.soldevcode.composechat.ui.ConversationScreen
import com.soldevcode.composechat.util.SpeechCredentialsProvider

class MainActivity : ComponentActivity() {

    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inputStream = SpeechCredentialsProvider().setGoogleCredentialFromStream(this)

        val repository = GoogleCredentialRepositoryImpl(inputStream) // Create your dependency
        val viewModelFactory = ViewModelFactoryHelper(repository)

        setContent {
            mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

            ConversationScreen()
        }
    }
}
