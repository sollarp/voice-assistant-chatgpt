package com.soldevcode.composechat.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ErrorDialog(
    onDismissRequest: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    AlertDialog(
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

/*
@Preview(showBackground = true)
@Composable
fun PreviewErrorDialog() {
    ErrorDialog(
        onConfirmation = {  },
        dialogTitle = "Dialog Title",
        dialogText = "Dialog Text"
    )

}*/
