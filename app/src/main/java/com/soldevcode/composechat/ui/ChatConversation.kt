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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.common.collect.ImmutableList
import com.soldevcode.composechat.models.ConversationModel
import com.soldevcode.composechat.presentation.MainViewModel
import com.soldevcode.composechat.ui.components.MessageBox
import okhttp3.internal.immutableListOf


/** Referring to Google documentation
https://developer.android.com/jetpack/compose/tooling/previews#best-practices
 * if you want to preview a composable that uses a ViewModel,
 * you should create another composable with the parameters
 * from ViewModel passed as arguments of the composable.
 * This way, you don't need to preview the composable that uses the ViewModel.
 */

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
    @PreviewParameter(
        ConversationPreviewParameterProvider::class
    )
    conversations: MutableList<ConversationModel>
) {
    ConversationList(
        conversations = conversations
    ) {}
}
/** Referring to Google documentation
 * where you must pass a large dataset to your composable preview.
 * https://developer.android.com/jetpack/compose/tooling/previews#preview-data
 * This is a demonstration how to use ParameterProvider, can be just hardcode
 * the parameters in the preview.
 */
private class ConversationPreviewParameterProvider :
    PreviewParameterProvider<List<ConversationModel>> {
    override val values: Sequence<List<ConversationModel>> = sequenceOf(
        immutableListOf(
            ConversationModel(
                chatOwner = "user",
                question = "user question in here"
            ),
            ConversationModel(
                chatOwner = "system",
                answer = "bot response in here"
            )
        )
    )
}
