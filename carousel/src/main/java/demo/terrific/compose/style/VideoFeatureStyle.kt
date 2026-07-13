package demo.terrific.compose.style

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import demo.terrific.compose.config.DefaultSdkFontFamily

data class VideoFeatureStyle(
    val carouselHeight: Dp = 500.dp,
    val pageHeight: Dp = 700.dp,
    val cornerRadius: Dp = 24.dp,
    val pagerHorizontalPadding: Dp = 48.dp,
    val pagerPageSpacing: Dp = 16.dp,
    val productSpacing: Dp = 12.dp,
    val productHeightFraction: Float = 0.16f,
    val fontFamily: FontFamily = DefaultSdkFontFamily,

    val titleTextStyle: TextStyle = TextStyle(
        fontSize = 20.sp
    ),

    val subtitleTextStyle: TextStyle = TextStyle(
        fontSize = 16.sp
    ),

    val bodyTextStyle: TextStyle = TextStyle(
        fontSize = 14.sp
    ),

    val badgeTextStyle: TextStyle = TextStyle(
        fontSize = 12.sp
    ),

    val smallTextStyle: TextStyle = TextStyle(
        fontSize = 8.sp
    )
)

fun TextStyle.withSdkFont(fontFamily: FontFamily): TextStyle {
    return copy(fontFamily = fontFamily)
}