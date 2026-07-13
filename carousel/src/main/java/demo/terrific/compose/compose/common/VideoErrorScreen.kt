package demo.terrific.compose.compose.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VideoErrorScreen(
    modifier: Modifier = Modifier,
    title: String = "We couldn’t load\nthis content",
    message: String = "Please try again in a moment.",
    buttonText: String = "Try Again",
    onRetryClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .semantics {
                contentDescription = "$title. $message"
            }
    ) {
        IconButton(
            onClick = onCloseClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(20.dp)
                .size(48.dp)
                .semantics {
                    contentDescription = "Close"
                }
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = Color(0xFF586174),
                modifier = Modifier.size(36.dp)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ErrorIllustration(
                modifier = Modifier
                    .fillMaxWidth(0.72f)
                    .aspectRatio(0.75f)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = title,
                color = Color(0xFF111B35),
                fontSize = 30.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = message,
                color = Color(0xFF667085),
                fontSize = 17.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onRetryClick,
                modifier = Modifier
                    .fillMaxWidth(0.72f)
                    .height(58.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF7A2F),
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = buttonText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ErrorIllustration(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PlaceholderCard(
                modifier = Modifier
                    .fillMaxWidth(0.68f)
                    .weight(1f),
                alpha = 0.35f
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.74f)
                    .weight(1.2f)
            ) {
                PlaceholderCard(
                    modifier = Modifier.fillMaxSize(),
                    alpha = 0.75f,
                    showErrorFace = true
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 12.dp, y = (-12).dp)
                        .size(54.dp)
                        .background(
                            color = Color(0xFFFF7A2F),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "!",
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            PlaceholderCard(
                modifier = Modifier
                    .fillMaxWidth(0.68f)
                    .weight(1f),
                alpha = 0.35f
            )
        }
    }
}

@Composable
private fun PlaceholderCard(
    modifier: Modifier = Modifier,
    alpha: Float,
    showErrorFace: Boolean = false
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFFF5F6F8).copy(alpha = alpha),
        border = BorderStroke(
            1.dp,
            Color(0xFFD9DDE5).copy(alpha = alpha)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (showErrorFace) {
                Text(
                    text = "☹",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 52.sp,
                    color = Color(0xFF596174)
                )
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(
                                    Color(0xFFE6E8ED),
                                    CircleShape
                                )
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Box(
                            modifier = Modifier
                                .width(56.dp)
                                .height(8.dp)
                                .background(
                                    Color(0xFFE6E8ED),
                                    RoundedCornerShape(100.dp)
                                )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(
                                Color(0xFFE9EBF0),
                                RoundedCornerShape(10.dp)
                            )
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.58f)
                            .height(8.dp)
                            .background(
                                Color(0xFFE6E8ED),
                                RoundedCornerShape(100.dp)
                            )
                    )
                }
            }
        }
    }
}