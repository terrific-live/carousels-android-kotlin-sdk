package demo.terrific.compose.compose.common

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SwipeHintOverlay(
    modifier: Modifier = Modifier,
    visibleDuration: Long = 2000L,
    onFinished: () -> Unit = {}
) {
    var visible by remember { mutableStateOf(true) }

    val transition = rememberInfiniteTransition(label = "swipe_hint")

    val arrow1Offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = -18f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "arrow1"
    )

    val arrow2Offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = -18f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 900,
                delayMillis = 120,
                easing = EaseInOut
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "arrow2"
    )

    val arrow3Offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = -18f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 900,
                delayMillis = 240,
                easing = EaseInOut
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "arrow3"
    )

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(500),
        label = "overlay_alpha"
    )

    LaunchedEffect(Unit) {
        delay(visibleDuration)
        visible = false
        delay(500)
        onFinished()
    }

    if (alpha > 0.01f) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.22f))
                .graphicsLayer {
                    this.alpha = alpha
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                SwipeArrow(offsetY = arrow1Offset.dp)
                SwipeArrow(offsetY = (arrow2Offset - 20).dp)
                SwipeArrow(offsetY = (arrow3Offset - 40).dp)

                Text(
                    text = "Défilez",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    style = androidx.compose.ui.text.TextStyle(
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.55f),
                            blurRadius = 8f
                        )
                    )
                )
            }
        }
    }
}

@Composable
private fun SwipeArrow(
    offsetY: Dp
) {
    Icon(
        imageVector = Icons.Default.KeyboardArrowUp,
        contentDescription = null,
        tint = Color.White,
        modifier = Modifier
            .offset(y = offsetY)
            .size(52.dp)
    )
}