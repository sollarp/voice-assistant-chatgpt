package com.soldevcode.composechat.ui

import android.view.MenuItem
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DrawerBody(
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick: (Int) -> Unit
) {
    LazyColumn(modifier) {
        items(listMenu.size) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(item)
                    }
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "valami",
                    style = itemTextStyle,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDrawerBody() {
    DrawerBody(
        modifier = Modifier.fillMaxSize(),
        itemTextStyle = TextStyle(fontSize = 18.sp),
        onItemClick = { index ->
            // Handle item click
        }
    )
}

 var listMenu = listOf(
     MenuItemE(
     id = "21",
     title = "title top",
     contentDescription = "something content",
     icon = Icons.Filled.Star),
     MenuItemE(
         id = "22",
         title = "title top2",
         contentDescription = "something content2",
         icon = Icons.Filled.AccountBox)


 )

data class MenuItemE(
    val id: String,
    val title: String,
    val contentDescription: String,
    val icon: ImageVector
)