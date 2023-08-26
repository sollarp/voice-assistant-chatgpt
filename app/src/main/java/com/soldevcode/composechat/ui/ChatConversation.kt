package com.soldevcode.composechat.ui


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.soldevcode.composechat.models.ConversationObj
import com.soldevcode.composechat.ui.components.MessageBox
import com.soldevcode.composechat.util.Constants.streamingStopped


@Composable
fun ConversationList(chatOwner: String, listOfWords: String, chatId: String) {

    val messages = remember { mutableStateListOf<ConversationObj>() }

    val listState = rememberLazyListState()

    /*Tracing the listofword from openAI as string stream including the user input
    * So user input always add number bot even also when stream stops created a
    * new conversatoin object */
    if (listOfWords != "") {
        if (chatOwner == "user" && messages.size % 2 == 0) {

            messages.add(ConversationObj(listOfWords, chatOwner))
            streamingStopped = false

        } else if (chatOwner == "bot" && messages.size % 2 != 0) {
            val newListOfWords = listOfWords
            messages.add(ConversationObj(newListOfWords, chatOwner))

        } else if (chatOwner == "bot") {
            messages[messages.size - 1] = ConversationObj(listOfWords, chatOwner)
        }
    }
    LaunchedEffect(messages) {
        listState.animateScrollToItem(messages.size - 1)
    }

    LazyColumn(
        state = LazyListState()
    )
    {
        itemsIndexed(messages) { index, message ->
            MessageBox(msg = messages[index])
        }
    }

}
