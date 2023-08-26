package com.soldevcode.composechat.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.soldevcode.composechat.MainViewModel
import com.soldevcode.composechat.ui.components.TextInput

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")


fun ConversationScreen(viewModel: MainViewModel = viewModel()) {
    val listOfWords = viewModel.listOfWords.observeAsState().value
    val chatOwner = viewModel.chatOwner.observeAsState().value
    val chatId = viewModel.chatId.observeAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chat with GPT") },
                modifier = Modifier
                    .padding(start = 2.dp, end = 2.dp)
                    .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(4.dp)
                ),
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween // Ensure items are spaced out, with `TextInput` at the bottom
            ) {
                if (chatOwner != null && listOfWords != null && chatId != null) {
                    Box(
                        modifier = Modifier
                            .weight(1f), // Occupy all available space, pushing the TextInput to the bottom
                        contentAlignment = Alignment.TopCenter // Center the ConversationList within the Box
                    ) {
                        ConversationList(chatOwner, listOfWords, chatId)
                    }
                }
                Spacer(modifier = Modifier.weight(.01f)) // Pushes TextInput to the bottom
                TextInput() // This will be at the bottom because of the weight modifier applied to the Box above
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewConversationScreen() {
    ConversationScreen()
}