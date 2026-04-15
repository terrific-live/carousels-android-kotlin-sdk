package demo.terrific.compose.style

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class VideoFeatureStyle(
    val pageHeight: Dp = 700.dp,
    val cornerRadius: Dp = 24.dp,
    val pagerHorizontalPadding: Dp = 48.dp,
    val pagerPageSpacing: Dp = 16.dp,
    val productSpacing: Dp = 12.dp,
    val productHeightFraction: Float = 0.16f
)