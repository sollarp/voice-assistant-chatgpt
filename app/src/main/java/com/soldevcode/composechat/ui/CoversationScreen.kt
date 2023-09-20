package com.soldevcode.composechat.ui

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.soldevcode.composechat.presentation.MainViewModel
import com.soldevcode.composechat.R
import com.soldevcode.composechat.ui.components.PermissionAlertDialog
import com.soldevcode.composechat.ui.components.TextInput
import com.soldevcode.composechat.ui.components.goToAppSetting
import com.soldevcode.composechat.util.NeededPermission

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

fun ConversationScreen(viewModel: MainViewModel = viewModel()) {

    val activity = LocalContext.current as Activity

    val permissionDialog = remember {
        mutableStateListOf<NeededPermission>()
    }

    val microphonePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted)
                permissionDialog.add(NeededPermission.RECORD_AUDIO)
        }
    )

    permissionDialog.forEach { permission ->
        PermissionAlertDialog(
            neededPermission = permission,
            onDismiss = { permissionDialog.remove(permission) },
            onOkClick = {
                permissionDialog.remove(permission)
                microphonePermissionLauncher.launch(NeededPermission.RECORD_AUDIO.permission)
            },
            onGoToAppSettingsClick = {
                permissionDialog.remove(permission)
                activity.goToAppSetting()
            },
            isPermissionDeclined = !activity.shouldShowRequestPermissionRationale(permission.permission)
        )
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chat with GPT") },
                modifier = Modifier
                    .padding(start = 2.dp, end = 2.dp)
                    .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(4.dp)
                ),
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween // Ensure items are spaced out, with `TextInput` at the bottom
            ) {
                    Box(
                        modifier = Modifier
                            .weight(1f), // Occupy all available space, pushing the TextInput to the bottom
                        contentAlignment = Alignment.TopCenter // Center the ConversationList within the Box
                    ) {
                        ConversationList()
                        Box(modifier = Modifier.fillMaxSize()) {
                            FloatingActionButton(
                                modifier = Modifier
                                    .padding(bottom = 10.dp, end = 10.dp)
                                    .align(alignment = Alignment.BottomEnd),
                                containerColor = Color.LightGray,
                                onClick = {
                                    microphonePermissionLauncher.launch(NeededPermission.RECORD_AUDIO.permission)
                                    viewModel.setRecording()
                                    viewModel.startRecording()
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_mic),
                                    contentDescription = stringResource(id = R.string.bus_content_description)
                                )
                            }
                        }
                    }
                Spacer(modifier = Modifier.weight(.01f)) // Pushes TextInput to the bottom
                TextInput() // This will be at the bottom because of the weight modifier applied to the Box above
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConversationScreen() {
    ConversationScreen()
}