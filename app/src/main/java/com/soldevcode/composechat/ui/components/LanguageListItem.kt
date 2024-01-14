package com.soldevcode.composechat.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LanguageListItem(
    language: String,
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit
) {
    Column {
        ListItem(
            headlineContent = { Text(language) },
            leadingContent = {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Localized description",
                )
            },
            trailingContent = { CheckBox(isSelected, onSelectionChanged) }
        )
        HorizontalDivider()
    }
}

@Composable
fun CheckBox(isSelected: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Checkbox(
        checked = isSelected,
        onCheckedChange = onCheckedChange
    )
}

/*@Preview(showBackground = true)
@Composable
fun PreviewLanguageList() {
    LanguageListItem(countries.map { it.languages })
}*/

