package com.soldevcode.composechat.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.soldevcode.composechat.ui.components.LanguageListItem
import com.soldevcode.composechat.util.getLanguages

@Composable
fun LanguageListScreen(){
    val countries = remember { getLanguages() }
    var selectedLanguage by remember { mutableStateOf("") }

    LazyColumn {
        items(countries.size) {i ->
            LanguageListItem(
                countries[i].languages,
                isSelected = countries[i].languages == selectedLanguage,
                onSelectionChanged = { isSelected ->
                    if (isSelected) selectedLanguage = countries[i].languages
                    else if (selectedLanguage == countries[i].languages) selectedLanguage = null.toString()
                }
            )
        }
    }

}