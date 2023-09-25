package com.soldevcode.composechat.ui


import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.soldevcode.composechat.presentation.MainViewModel
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MessageBox(
                        msg = conversations[conversation],
                        onDeleteClicked = {
                            println("clicked = ${conversations[conversation].answer}")
                            viewModel.speak(conversations[conversation].answer)
                        }
                    )

                }
            }
        }
    }
}
