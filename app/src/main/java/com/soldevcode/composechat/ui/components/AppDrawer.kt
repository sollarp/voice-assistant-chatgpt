package com.soldevcode.composechat.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soldevcode.composechat.ui.navigation.AppScreen

@Composable
fun AppDrawer(navController: NavHostController) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // App drawer title
            Text(
                text = "App Drawer",
                style = MaterialTheme.typography.bodyLarge
            )

            // Divider between title and new chat button
            Divider(thickness = 1.dp)

            // Rounded new chat button
            Button(
                onClick = { },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.padding(vertical = 16.dp)
            ) {

                Text(text = "+ New Chat")
            }

            // Divider between new chat button and list of items
            Divider(thickness = 1.dp)
            DrawerItemHeader("Recent")
            NavigationDrawerItem(
                label = { Text(text = "Drawer Item") },
                selected = false,
                onClick = { }
            )
            // Divider between list of items and settings section
            Divider(thickness = 1.dp)

            // Settings section
            Box(
                modifier = Modifier
                    .width(140.dp),
            )
            {
                NavigationDrawerItem(
                    label = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Settings,
                                contentDescription = "Settings"
                            )
                            Text(
                                text = "Settings",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp),
                            )
                        }
                    },
                    selected = false,
                    onClick = { navController.navigate(AppScreen.Settings.name)  }
                )
            }
        }
    }
}
/*ModalDrawerSheet {
    Text("Drawer title", modifier = Modifier.padding(16.dp))
    Divider()
    NavigationDrawerItem(
        label = { Text(text = "Drawer Item") },
        selected = false,
        onClick = { *//*TODO*//* }
            )
        }*/

@Composable
private fun DrawerItemHeader(text: String) {
    Box(
        modifier = Modifier
            .heightIn(min = 40.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = CenterStart
    ) {
        Text(
            text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
/*
@Preview(showBackground = true)
@Composable
fun PreviewAppDrawer() {
    AppDrawer(navController)
}*/
