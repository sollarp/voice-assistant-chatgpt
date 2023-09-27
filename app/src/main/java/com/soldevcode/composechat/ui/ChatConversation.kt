package com.soldevcode.composechat.ui


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.soldevcode.composechat.models.ConversationModel
import com.soldevcode.composechat.presentation.MainViewModel
import com.soldevcode.composechat.ui.components.MessageBox


@Composable
fun ConversationList(viewModel: MainViewModel = viewModel()) {
    ConversationList(
        conversations =
        viewModel.conversationsLiveData.observeAsState().value,
        onSpeak = viewModel::speak
    )

}

@Composable
private fun ConversationList(
    conversations: MutableList<ConversationModel>?,
    onSpeak: (String) -> Unit

) {

    val listState = rememberLazyListState()

    if (conversations != null) {
        LaunchedEffect(conversations) {
            listState.animateScrollToItem(conversations.size)
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
        )
        {
            items(conversations.size) { conversation ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MessageBox(
                        msg = conversations[conversation],
                        onSpeakerClicked = {
                            //println("clicked = ${observeConversations[conversation].answer}")
                            onSpeak(conversations[conversation].answer)
                        }
                    )

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConversationList(
) {
    ConversationList(
        conversations = mutableListOf(
            ConversationModel(
                chatOwner = "user",
                question = "user question in here"

            ),
            ConversationModel(
                chatOwner = "bot",
                answer = "bot response in here"
            )
        ),
        onSpeak = {}
    )
}