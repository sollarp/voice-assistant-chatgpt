package com.soldevcode.composechat.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInput(
    textFieldText: MutableState<String> = rememberSaveable { mutableStateOf("") },
    onSendMessage: (String) -> Unit,
) {

    Box(
        // Use navigationBarsPadding() imePadding() and , to move the input panel above both the
        // navigation bar, and on-screen keyboard (IME)
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 10.dp, top = 10.dp, bottom = 10.dp)
                    .imePadding()
                    .weight(.8f),
            ) {
                TextField(
                    value = textFieldText.value,
                    onValueChange = {textFieldText.value = it},
                    label = null,
                    placeholder = { Text("Ask me anything", fontSize = 16.sp) },
                    shape = RoundedCornerShape(25.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp)
                        .border(2.dp, Color.Black, shape = RoundedCornerShape(12.dp)),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                    ),
                )
            }
            Box(
                modifier = Modifier
                    .padding(end = 30.dp, top = 10.dp, bottom = 10.dp)
                    .weight(.1f)
                    .sizeIn(minHeight = 48.dp), // Ensure IconButton maintains the desired height
            ) {
                IconButton(
                    modifier = Modifier
                        .border(2.dp, Color.Black, shape = RoundedCornerShape(12.dp)),
                    onClick = {
                        onSendMessage(textFieldText.value)
                        textFieldText.value = ""
                    }
                ) {
                    Icon(
                        Icons.Filled.Send,
                        "sendMessage",
                        modifier = Modifier,
                    )

                }
            }
        }
    }
}

/*@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewInputText(
) {
    MaterialTheme {
        TextInput(
            onSendMessage = {},
            speechToText = remember { mutableStateOf("") }
        )
    }
}*/
