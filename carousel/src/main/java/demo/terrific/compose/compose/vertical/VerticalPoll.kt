package demo.terrific.compose.compose.vertical

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import demo.terrific.compose.model.PollDataDto
import demo.terrific.compose.model.PollOptionDto


@Composable
fun PollScreen(
    pollData: PollDataDto,
    selectedOptionText: String?,
    onOptionClick: (String) -> Unit,
    onBackClicked: () -> Unit,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
) {
    val hasVoted = selectedOptionText != null

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
        IconButton(
            onClick = onBackClicked,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(90.dp))

            Text(
                text = pollData.question,
                color = Color.White,
                fontSize = 24.sp,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                pollData.options.forEach { option ->
                    if (hasVoted) {
                        PollResultOption(
                            option = option,
                            options = pollData.options,
                            isSelected = option.text == selectedOptionText
                        )
                    } else {
                        PollOptionButton(
                            text = option.text,
                            onClick = { onOptionClick(option.text) }
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 140.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            IconButton(onClick = onLikeClick) {
                Icon(
                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Outlined.ThumbUp,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Send,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun PollActionsColumn(
    liked: Boolean,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = onLikeClick) {
            Icon(
                imageVector = if (liked) Icons.Default.Favorite else Icons.Outlined.ThumbUp,
                contentDescription = "Like",
                tint = Color.White
            )
        }

        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Send,
                contentDescription = "Share",
                tint = Color.White
            )
        }
    }
}

@Composable
private fun PollBottomBranding(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.86f)
                .height(10.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(Color.White.copy(alpha = 0.28f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.78f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color(0xFF1FB2FF))
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "POWERED BY ",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 12.sp
            )
            Text(
                text = "terrific",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PollQuestionSection(
    question: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFFECECEC),
            shadowElevation = 4.dp
        ) {
            Box(
                modifier = Modifier.size(64.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.BarChart,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = question,
            color = Color.White,
            fontSize = 24.sp,
//            fontStyle = FontStyle.FONT_WEIGHT_SEMI_BOLD,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PollOptionButton(
    text: String,
    onClick: () -> Unit
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
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun PollResultOption(
    option: PollOptionDto,
    options: List<PollOptionDto>,
    isSelected: Boolean,
    modifier: Modifier = Modifier
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
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "$percent%",
                color = Color(0xFF1F1F1F),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}