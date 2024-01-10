package com.soldevcode.composechat.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.google.relay.compose.RelayContainer
import com.google.relay.compose.RelayContainerScope
import com.google.relay.compose.RelayText
import com.google.relay.compose.RelayVector
import com.google.relay.compose.relayDropShadow
import com.soldevcode.composechat.R

/**
 * try
 *
 * This composable was generated from the UI Package 'setting_screen'.
 * Generated code; do not edit directly
 */
@Composable
fun SettingScreen(modifier: Modifier = Modifier) {
    TopLevel(modifier = modifier) {
        SpeechRectangle(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 13.0.dp,
                    y = 151.0.dp
                )
            )
        )
        SpeechVector(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 360.999755859375.dp,
                    y = 171.99978637695312.dp
                )
            )
        )
        Speechlanguage(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 35.0.dp,
                    y = 166.0.dp
                )
            )
        )
        MicRectangle(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 13.0.dp,
                    y = 221.0.dp
                )
            )
        )
        Vector5(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 360.9998779296875.dp,
                    y = 239.99981689453125.dp
                )
            )
        )
        Mic(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 35.0.dp,
                    y = 234.0.dp
                )
            )
        )
        ModelRectangle(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 13.0.dp,
                    y = 290.0.dp
                )
            )
        )
        Vector6(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 360.9998779296875.dp,
                    y = 309.9998321533203.dp
                )
            )
        )
        ModelPreference(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 35.0.dp,
                    y = 304.0.dp
                )
            )
        )
        ChatGroup(
            modifier = Modifier.boxAlign(
                alignment = Alignment.TopStart,
                offset = DpOffset(
                    x = 17.0.dp,
                    y = 81.0.dp
                )
            )
        ) {
            ChatRectangle(
                modifier = Modifier.boxAlign(
                    alignment = Alignment.TopStart,
                    offset = DpOffset(
                        x = -4.0.dp,
                        y = 0.0.dp
                    )
                )
            )
            Chatmodel(
                modifier = Modifier.boxAlign(
                    alignment = Alignment.TopStart,
                    offset = DpOffset(
                        x = 18.01171875.dp,
                        y = 15.888946533203125.dp
                    )
                )
            )
            ChatVector(
                modifier = Modifier.boxAlign(
                    alignment = Alignment.TopStart,
                    offset = DpOffset(
                        x = 343.4736328125.dp,
                        y = 21.335723876953125.dp
                    )
                )
            )
        }
        TopAppBarGroup {
            Rectangle1()
            Settings(
                modifier = Modifier.boxAlign(
                    alignment = Alignment.TopStart,
                    offset = DpOffset(
                        x = 160.0555419921875.dp,
                        y = 16.0.dp
                    )
                )
            )
            IconsArrowBack24px(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f)) {
                ArrowIcon(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
            }
        }
    }
}

@Preview(widthDp = 430, heightDp = 932)
@Composable
private fun SettingScreenPreview() {
    MaterialTheme {
        RelayContainer {
            SettingScreen(modifier = Modifier.rowWeight(1.0f).columnWeight(1.0f))
        }
    }
}

@Composable
fun SpeechRectangle(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.setting_screen_speech_rectangle),
        modifier = modifier.requiredWidth(385.0.dp).requiredHeight(57.0.dp).relayDropShadow(
            color = Color(
                alpha = 63,
                red = 0,
                green = 0,
                blue = 0
            ),
            borderRadius = 8.000000953674316.dp,
            blur = 4.000000476837158.dp,
            offsetX = 0.0.dp,
            offsetY = 4.000000476837158.dp,
            spread = 0.0.dp
        )
    )
}

@Composable
fun SpeechVector(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.setting_screen_speech_vector_),
        modifier = modifier.requiredWidth(12.0.dp).requiredHeight(13.0.dp)
    )
}

@Composable
fun Speechlanguage(modifier: Modifier = Modifier) {
    RelayText(
        content = "Speech Language",
        fontSize = 22.0.sp,
        color = MaterialTheme.colorScheme.onSurface,
        height = 1.2727273559570313.em,
        textAlign = TextAlign.Left,
        maxLines = -1,
        shadow = Shadow(
            color = Color(
                alpha = 63,
                red = 0,
                green = 0,
                blue = 0
            ),
            offset = Offset(
                x = 0.0f,
                y = 4.000000476837158f
            ),
            blurRadius = 4.000000476837158f
        ),
        modifier = modifier.requiredWidth(198.0.dp).requiredHeight(28.0.dp).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun MicRectangle(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.setting_screen_mic_rectangle_),
        modifier = modifier.requiredWidth(385.0.dp).requiredHeight(56.0.dp).relayDropShadow(
            color = Color(
                alpha = 63,
                red = 0,
                green = 0,
                blue = 0
            ),
            borderRadius = 8.000000953674316.dp,
            blur = 4.000000476837158.dp,
            offsetX = 0.0.dp,
            offsetY = 4.000000476837158.dp,
            spread = 0.0.dp
        )
    )
}

@Composable
fun Vector5(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.setting_screen_vector_5),
        modifier = modifier.requiredWidth(12.0.dp).requiredHeight(15.0.dp)
    )
}

@Composable
fun Mic(modifier: Modifier = Modifier) {
    RelayText(
        content = "Mic Language",
        fontSize = 22.0.sp,
        color = MaterialTheme.colorScheme.onSurface,
        height = 1.2727273559570313.em,
        textAlign = TextAlign.Left,
        maxLines = -1,
        shadow = Shadow(
            color = Color(
                alpha = 63,
                red = 0,
                green = 0,
                blue = 0
            ),
            offset = Offset(
                x = 0.0f,
                y = 4.000000476837158f
            ),
            blurRadius = 4.000000476837158f
        ),
        modifier = modifier.requiredWidth(198.0.dp).requiredHeight(29.0.dp).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun ModelRectangle(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.setting_screen_model_rectangle_),
        modifier = modifier.requiredWidth(385.0.dp).requiredHeight(57.0.dp).relayDropShadow(
            color = Color(
                alpha = 63,
                red = 0,
                green = 0,
                blue = 0
            ),
            borderRadius = 8.000000953674316.dp,
            blur = 4.000000476837158.dp,
            offsetX = 0.0.dp,
            offsetY = 4.000000476837158.dp,
            spread = 0.0.dp
        )
    )
}

@Composable
fun Vector6(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.setting_screen_vector_6),
        modifier = modifier.requiredWidth(12.0.dp).requiredHeight(15.0.dp)
    )
}

@Composable
fun ModelPreference(modifier: Modifier = Modifier) {
    RelayText(
        content = "Model Preferences",
        fontSize = 22.0.sp,
        color = MaterialTheme.colorScheme.onSurface,
        height = 1.2727273559570313.em,
        textAlign = TextAlign.Left,
        maxLines = -1,
        shadow = Shadow(
            color = Color(
                alpha = 63,
                red = 0,
                green = 0,
                blue = 0
            ),
            offset = Offset(
                x = 0.0f,
                y = 4.000000476837158f
            ),
            blurRadius = 4.000000476837158f
        ),
        modifier = modifier.requiredWidth(221.0.dp).requiredHeight(29.0.dp).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun ChatRectangle(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.setting_screen_chat_rectangle),
        modifier = modifier.requiredWidth(385.0.dp).requiredHeight(56.8549690246582.dp).relayDropShadow(
            color = Color(
                alpha = 63,
                red = 0,
                green = 0,
                blue = 0
            ),
            borderRadius = 8.000000953674316.dp,
            blur = 4.000000476837158.dp,
            offsetX = 0.0.dp,
            offsetY = 4.000000476837158.dp,
            spread = 0.0.dp
        )
    )
}

@Composable
fun Chatmodel(modifier: Modifier = Modifier) {
    RelayText(
        content = "Chat Model",
        fontSize = 22.0.sp,
        color = MaterialTheme.colorScheme.onSurface,
        height = 1.2727273559570313.em,
        textAlign = TextAlign.Left,
        maxLines = -1,
        shadow = Shadow(
            color = Color(
                alpha = 63,
                red = 0,
                green = 0,
                blue = 0
            ),
            offset = Offset(
                x = 0.0f,
                y = 4.000000476837158f
            ),
            blurRadius = 4.000000476837158f
        ),
        modifier = modifier.requiredWidth(189.93905639648438.dp).requiredHeight(29.4427490234375.dp).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun ChatVector(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.setting_screen_chat_vector),
        modifier = modifier.requiredWidth(11.25731086730957.dp).requiredHeight(14.21374225616455.dp)
    )
}

@Composable
fun ChatGroup(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        isStructured = false,
        clipToParent = false,
        content = content,
        modifier = modifier.requiredWidth(385.0.dp).requiredHeight(56.8549690246582.dp)
    )
}

@Composable
fun Rectangle1(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.setting_screen_rectangle_1),
        modifier = modifier.requiredWidth(430.0.dp).requiredHeight(58.0.dp)
    )
}

@Composable
fun Settings(modifier: Modifier = Modifier) {
    RelayText(
        content = "Settings",
        fontSize = MaterialTheme.typography.titleLarge.fontSize,
        fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
        color = MaterialTheme.colorScheme.onSurface,
        height = MaterialTheme.typography.titleLarge.lineHeight,
        letterSpacing = MaterialTheme.typography.titleLarge.letterSpacing,
        fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
        maxLines = -1,
        modifier = modifier.requiredWidth(96.75.dp).requiredHeight(28.0.dp).wrapContentHeight(
            align = Alignment.CenterVertically,
            unbounded = true
        )
    )
}

@Composable
fun ArrowIcon(modifier: Modifier = Modifier) {
    RelayVector(
        vector = painterResource(R.drawable.setting_screen_arrow_icon),
        modifier = modifier.padding(
            paddingValues = PaddingValues(
                start = 4.77783203125.dp,
                top = 4.0.dp,
                end = 4.77772331237793.dp,
                bottom = 4.0.dp
            )
        ).fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun IconsArrowBack24px(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        isStructured = false,
        clipToParent = false,
        content = content,
        modifier = modifier.padding(
            paddingValues = PaddingValues(
                start = 14.3333740234375.dp,
                top = 20.0.dp,
                end = 386.9999599456787.dp,
                bottom = 14.0.dp
            )
        ).fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}

@Composable
fun TopAppBarGroup(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        isStructured = false,
        clipToParent = false,
        content = content,
        modifier = modifier.requiredWidth(430.0.dp).requiredHeight(58.0.dp)
    )
}

@Composable
fun TopLevel(
    modifier: Modifier = Modifier,
    content: @Composable RelayContainerScope.() -> Unit
) {
    RelayContainer(
        backgroundColor = Color(
            alpha = 255,
            red = 255,
            green = 255,
            blue = 255
        ),
        isStructured = false,
        content = content,
        modifier = modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f)
    )
}
