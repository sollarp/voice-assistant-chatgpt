package com.soldevcode.composechat.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RecordButton(onClick: () -> Unit) {
    SmallFloatingActionButton(
        onClick = { onClick() },
        shape = CircleShape,
        modifier = Modifier
            .padding(bottom = 10.dp),
        containerColor = Color.LightGray
    ) {
        Icon(Icons.Filled.Add,
            "Large floating action button")
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewRecordButton() {
    RecordButton {

    }
}

