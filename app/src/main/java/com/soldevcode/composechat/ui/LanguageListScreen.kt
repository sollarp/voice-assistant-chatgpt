package com.soldevcode.composechat.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.soldevcode.composechat.presentation.MainViewModel
import com.soldevcode.composechat.ui.components.LanguageListItem
import com.soldevcode.composechat.util.getLanguages
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageListScreen(
    viewModel: MainViewModel = koinViewModel(), navController: NavHostController
) {
    val countries = remember { getLanguages() }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedLanguage = uiState.languages?.languages

    Column {
        TopAppBar(
            title = { Text("Languages") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                // Your action buttons here
            }
        )

        LazyColumn {
            items(countries.size) { i ->
                LanguageListItem(
                    countries[i].languages,
                    isSelected = countries[i].languages == selectedLanguage,
                    onSelectionChanged = { isSelected ->
                        if (isSelected) viewModel.setSelectedLanguage(countries[i])
                        else if (selectedLanguage == countries[i].languages)
                            selectedLanguage = null
                    }
                )
            }
        }
    }
}
