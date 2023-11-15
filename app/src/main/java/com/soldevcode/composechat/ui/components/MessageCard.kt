package com.soldevcode.composechat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soldevcode.composechat.R
import com.soldevcode.composechat.models.ConversationModel
import com.soldevcode.composechat.models.Message
import com.soldevcode.composechat.models.Message.Answer
import com.soldevcode.composechat.models.Message.Question


@Composable
fun MessageBox(
    msg: Message,
    onSpeakerClicked: () -> Unit,
    onStopClicked: () -> Unit
) {
    val isPlaying = remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 5.dp, horizontal = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .widthIn(0.dp, max = 320.dp) //mention max width here
                .background(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.LightGray
                )
        )
        {
            when (msg) {
                is Question -> {
                    UserMessageCard(msg = msg.text)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                is Answer -> {
                    BotMessageCard(msg = msg.text)
                }
            }
        }

        //Add a 5dp spacer between the two boxes
        Spacer(modifier = Modifier.width(10.dp))

        //Add a box to center the speaker icon
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
        ) {
            if (msg is Answer) {
                Image(
                    painter = painterResource(
                        id = if (isPlaying.value) R.drawable.ic_stop else R.drawable.icon_speaker
                    ), contentDescription = "Contact profile picture",
                    modifier = Modifier.clickable {
                        if (isPlaying.value) {
                            onStopClicked() // Function called when the stop icon is clicked
                        } else {
                            onSpeakerClicked() // Function called when the speaker icon is clicked
                        }
                        isPlaying.value = !isPlaying.value // Toggle the playing state
                    }
                )
            }
        }
    }
}


@Composable
fun UserMessageCard(msg: String) {
    Row(
        modifier = Modifier.padding(start = 5.dp, top = 5.dp, bottom = 5.dp, end = 5.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_user),
            contentDescription = "Contact profile picture",
        )
        Text(
            text = msg,
            style = TextStyle(fontSize = 16.sp)
        )
    }
}

@Composable
fun BotMessageCard(
    msg: String,
) {

    Row(
        modifier = Modifier.padding(start = 5.dp, top = 5.dp, bottom = 5.dp, end = 5.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.icons_robot),
            contentDescription = "Contact profile picture",
        )
        SelectionContainer(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = msg,
                style = TextStyle(fontSize = 16.sp)
            )
        }

    }

}

@Preview(showBackground = true)
@Composable
fun PreviewMessageBox() {
    MessageBox(
        msg = Answer(
            text = "bot request field",
        ),
        onSpeakerClicked = {},
        onStopClicked = {}

    )

    MessageBox(
        msg = Question(
            text = "user request field",
        ),
        onSpeakerClicked = {},
        onStopClicked = {}

    )
}

