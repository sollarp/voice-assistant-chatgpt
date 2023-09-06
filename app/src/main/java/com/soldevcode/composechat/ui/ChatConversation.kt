package com.soldevcode.composechat.ui


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.soldevcode.composechat.MainViewModel
import com.soldevcode.composechat.ui.components.MessageBox

@Composable
fun ConversationList(viewModel: MainViewModel = viewModel()) {

    val listState = rememberLazyListState()
    val conversations = viewModel.conversationsLiveData.observeAsState().value

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
                Column {
                    MessageBox(msg = conversations[conversation])
                }
            }
        }
    }
}
