package demo.terrific.compose.compose.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun String.toFormatted(
    format: String = "dd/MM/yyyy - HH'h'mm"
): String {
    return try {
        val inputFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.getDefault()
        ).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

        val outputFormat = SimpleDateFormat(
            formatTimestamp(format),
            Locale.getDefault()
        ).apply {
            timeZone = TimeZone.getDefault()
        }

        val date = inputFormat.parse(this) ?: return this
        outputFormat.format(date)
    } catch (_: Exception) {
        this
    }
}

@Composable
fun VideoProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    trackColor: Color = Color.White.copy(alpha = 0.35f),
    progressColor: Color = Color.White,
    height: Dp = 10.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(100.dp))
            .background(trackColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .clip(RoundedCornerShape(100.dp))
                .background(progressColor)
        )
    }
}

@Composable
fun DateTimeBadge(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFFF4F4F4))
            .padding(horizontal = 18.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            color = Color(0xFF111111),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DateTimeBadgeCarousel(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFFF4F4F4))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = Color(0xFF111111),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun formatTimestamp(
    pattern: String
): String {
    return pattern
        .replace("{hh}", "hh")
        .replace("{mm}", "mm")
        .replace("{DD}", "dd")
        .replace("{MM}", "MM")
        .replace("{YY}", "YY")
        .replace("{YYYY}", "YYYY")
        .replace("{hh}h{mm}", "hhhmm")
}