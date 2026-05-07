package demo.terrific.compose.compose.vertical

import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ThumbUpOffAlt
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import demo.terrific.compose.compose.common.VideoProgressBar
import demo.terrific.compose.model.AssetDto
import demo.terrific.compose.model.PollOptionDto
import demo.terrific.compose.style.VideoFeatureStyle
import demo.terrific.compose.style.withSdkFont
import kotlinx.coroutines.delay


@Composable
fun PollScreen(
    asset: AssetDto,
    selectedOptionText: String?,
    onOptionClick: (String) -> Unit,
    onBackClicked: () -> Unit,
    isLiked: Boolean,
    onLikeClick: (String) -> Unit,
    style: VideoFeatureStyle
) {
    val hasVoted = selectedOptionText != null
    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(asset.id) {
        val duration = 3000L // 3 секунди
        val startTime = System.currentTimeMillis()

        while (true) {
            val elapsed = System.currentTimeMillis() - startTime
            progress = (elapsed.toFloat() / duration).coerceIn(0f, 1f)

            if (progress >= 1f) break

            delay(16) // ~60fps
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF8E123C),
                        Color(0xFF4E2D72)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(90.dp))

            asset.pollData?.question?.let {
                Text(
                    text = it,
                    color = Color.White,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    style = style.titleTextStyle.withSdkFont(style.fontFamily)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                asset.pollData?.options?.forEach { option ->
                    if (hasVoted) {
                        PollResultOption(
                            option = option,
                            options = asset.pollData.options,
                            isSelected = option.text == selectedOptionText,
                            onClick = { onOptionClick(option.text) },
                            style = style
                        )
                    } else {
                        PollOptionButton(
                            text = option.text,
                            onClick = { onOptionClick(option.text) },
                            style = style
                        )
                    }
                }
            }
        }

//        LinearProgressIndicator(
//            progress = { progress },
//            color = Color.Blue,
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp, vertical = 32.dp)
//                .height(4.dp),
//        )


        VideoProgressBar(
            progress = progress,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(start = 24.dp, top = 0.dp, end = 24.dp, bottom = 32.dp),
            height = 8.dp,
            trackColor = Color.White.copy(alpha = 0.28f),
            progressColor = Color(0xFF36C3FF)
        )

        PollOverlay(
            asset = asset,
            isLiked = isLiked,
            onLikeClick = onLikeClick,
            onBackClicked = onBackClicked,
            style = style
        )
    }
}

@Composable
private fun PollOptionButton(
    text: String,
    onClick: () -> Unit,
    style: VideoFeatureStyle
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFF0F0F0),
        shadowElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(82.dp)
                .padding(horizontal = 22.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = text,
                color = Color(0xFF1C1C1C),
                style = style.subtitleTextStyle.withSdkFont(style.fontFamily)
            )
        }
    }
}

@Composable
private fun PollResultOption(
    option: PollOptionDto,
    options: List<PollOptionDto>,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    style: VideoFeatureStyle
) {
    val totalVotes = options.sumOf { it.numberOfVotes }.coerceAtLeast(1)
    val targetProgress = (option.numberOfVotes.toFloat() / totalVotes).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        label = "poll_progress"
    )

    val percent = (targetProgress * 100).toInt()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(82.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFEDEDED))
            .clickable { onClick() }
    ) {

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(
                    when {
                        percent == 0 -> 0f
                        else -> animatedProgress
                    }
                )
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (isSelected) {
                        Color(0xFFB8B8B8)
                    }
                    else {
                        Color(0xFFD7D7D7)
                    }
                )
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = option.text,
                modifier = Modifier.weight(1f),
                color = Color(0xFF1F1F1F),
                fontWeight = FontWeight.Medium,
                style = style.subtitleTextStyle.withSdkFont(style.fontFamily)
            )

            Text(
                text = "$percent%",
                color = Color(0xFF1F1F1F),
                fontWeight = FontWeight.Bold,
                style = style.subtitleTextStyle.withSdkFont(style.fontFamily)
            )
        }
    }
}

@Composable
fun PollOverlay(
    asset: AssetDto,
    isLiked: Boolean,
    onLikeClick: (String) -> Unit,
    onBackClicked: () -> Unit,
    style: VideoFeatureStyle
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 32.dp, end = 32.dp, top = 32.dp, bottom = 48.dp)
            .zIndex(1f)
    ) {

        // CLOSE BUTTON
        IconButton(
            onClick = { onBackClicked() },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
        }


//        val formatted = remember(asset.timestamp) {
//            asset.timestamp?.toFormatted()
//        }

        // DATE

//        if (formatted?.isNotEmpty() == true) {
//            Text(
//                text = formatted,
//                modifier = Modifier
//                    .align(Alignment.TopStart)
//                    .background(Color.White.copy(alpha = 0.8f))
//                    .padding(6.dp)
//            )
//        }

        // RIGHT ACTIONS
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            IconButton(onClick = { onLikeClick(asset.id) }) {
                Icon(imageVector = if (isLiked) Icons.Default.ThumbUp else Icons.Default.ThumbUpOffAlt,
                    contentDescription = "Like",
                    tint = if (isLiked) Color.White else Color.White)
            }

            Spacer(Modifier.height(12.dp))

            val context = LocalContext.current

            IconButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, asset.media?.mobileUrl)
                    }
                    context.startActivity(Intent.createChooser(intent, "Share"))
                }
            ) {
                Icon(Icons.Outlined.Share, contentDescription = "Share", tint = Color.White)
            }
        }

        // TITLE + DESCRIPTION
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(end = 80.dp)
        ) {

            Text(
                text = asset.title ?: "",
                style = style.titleTextStyle.withSdkFont(style.fontFamily),
                color = Color.White
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = asset.description ?: "",
                style = style.subtitleTextStyle.withSdkFont(style.fontFamily),
                color = Color.White
            )
        }
    }
}
