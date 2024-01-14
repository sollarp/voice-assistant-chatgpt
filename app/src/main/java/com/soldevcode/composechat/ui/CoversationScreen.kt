package com.soldevcode.composechat.ui

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.soldevcode.composechat.R
import com.soldevcode.composechat.models.Message.Question
import com.soldevcode.composechat.models.platform.audio.rememberRecordingManager
import com.soldevcode.composechat.presentation.MainViewModel
import com.soldevcode.composechat.ui.components.AppDrawer
import com.soldevcode.composechat.ui.components.ErrorDialog
import com.soldevcode.composechat.ui.components.PermissionAlertDialog
import com.soldevcode.composechat.ui.components.RecordingIndicator
import com.soldevcode.composechat.ui.components.TextInput
import com.soldevcode.composechat.ui.components.goToAppSetting
import com.soldevcode.composechat.ui.navigation.AppScreen
import com.soldevcode.composechat.util.NeededPermission
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun ConversationScreen(
    viewModel: MainViewModel = koinViewModel(),
    navController: NavHostController
)
{
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val permissionDialog = remember {
        mutableStateListOf<NeededPermission>()
    }

    val uiState = viewModel.uiState.collectAsState()
    val textFieldText = rememberSaveable { mutableStateOf("") }
    val recordingManager = rememberRecordingManager(speechToTextState = textFieldText)
    var recording by recordingManager.showRecording
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.Start.name
    )

    when {
        uiState.value.isErrorDialog -> {
            ErrorDialog(
                onDismissRequest = { viewModel.clearErrorDialog() },
                dialogTitle = "ERROR",
                dialogText = uiState.value.errorMessage
            )
        }
    }

    val microphonePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted)
                permissionDialog.add(NeededPermission.RECORD_AUDIO)
        }
    )

    permissionDialog.forEach { permission ->
        val shouldShowRequestPermission =
            (context as? Activity)?.shouldShowRequestPermissionRationale(
                permission.permission
            ) ?: false

        PermissionAlertDialog(
            neededPermission = permission,
            onDismiss = { permissionDialog.remove(permission) },
            onOkClick = {
                permissionDialog.remove(permission)
                microphonePermissionLauncher.launch(NeededPermission.RECORD_AUDIO.permission)
            },
            onGoToAppSettingsClick = {
                permissionDialog.remove(permission)
                context.goToAppSetting()
            },
            isPermissionDeclined = shouldShowRequestPermission,
        )
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
                AppDrawer(navController)

        },
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(
                            "AI Chat Assistant",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate(AppScreen.Language.name) }) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
        ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                ConversationList()
                                Box(modifier = Modifier.fillMaxSize()) {
                                    FloatingActionButton(
                                        modifier = Modifier
                                            .padding(bottom = 10.dp, end = 10.dp)
                                            .align(alignment = Alignment.BottomEnd),
                                        containerColor = Color.LightGray,
                                        onClick = {
                                            recording = if (recording) {
                                                recordingManager.stopRecording()
                                                false
                                            } else {
                                                microphonePermissionLauncher.launch(
                                                    NeededPermission.RECORD_AUDIO.permission
                                                )
                                                recordingManager.startRecording()
                                                true
                                            }
                                        }
                                    ) {
                                        if (recording) {
                                            Icon(
                                                painter = painterResource(R.drawable.ic_stop),
                                                contentDescription = ""
                                            )
                                        } else {
                                            Icon(
                                                painter = painterResource(R.drawable.ic_mic),
                                                contentDescription = ""
                                            )
                                        }
                                    }
                                }
                            }
                            // Pushes TextInput to the bottom
                            Spacer(modifier = Modifier.weight(.01f))
                            // This will be at the bottom because of the weight modifier applied to the Box above
                            TextInput(
                                textFieldText = textFieldText,
                                onSendMessage = { text ->
                                    viewModel.jsonRequestBody(Question(text))
                                    viewModel.updateMessageUiState(Question(text))
                                }
                            )
                        }
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 10.dp, end = 10.dp)
                                .size(50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (recording) {
                                RecordingIndicator()
                            }
                        }
                    }
                }
            }
}

/*
@Preview(showBackground = true)
@Composable
fun PreviewConversationScreen() {
    ConversationScreen()
}*/
