package com.soldevcode.composechat.ui.components

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RecordingIndicator() {
    Column {
        val infiniteTransition = rememberInfiniteTransition(label = "")
        val size by infiniteTransition.animateValue(
            initialValue = 36.dp,
            targetValue = 50.dp,
            Dp.VectorConverter,
            animationSpec = infiniteRepeatable(
                animation = tween(500, easing = FastOutLinearInEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )
        val smallCircle by infiniteTransition.animateValue(
            initialValue = 32.dp,
            targetValue = 25.dp,
            Dp.VectorConverter,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutLinearInEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            contentAlignment = Alignment.Center
        ) {
            SimpleCircleShape(
                size = size,
                color = Color.Red.copy(alpha = 0.25f)
            )
            SimpleCircleShape(
                size = smallCircle,
                color = Color.Red.copy(alpha = 0.25f)
            )
            SimpleCircleShape(
                size = 18.dp,
                color = Color.Red
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConversationScreen() {
    RecordingIndicator()
}

