package com.soldevcode.composechat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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


@Composable
fun MessageBox(
    msg: ConversationModel,
    onDeleteClicked: () -> Unit
) {
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
            if (msg.chatOwner == "user") {
                UserMessageCard(msg = msg.question)
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                BotMessageCard(msg = msg.answer)
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
            if (msg.chatOwner == "bot") {
                Image(
                    painter = painterResource(id = R.drawable.icon_speaker),
                    contentDescription = "Contact profile picture",
                    modifier = Modifier.clickable { onDeleteClicked() }
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

/*
@Preview(showBackground = true)
@Composable
fun PreviewMessageBox() {
    MessageBox(
        msg = ConversationModel(
            answer = "user request field",
            chatOwner = "bot")
    )
}
*/

