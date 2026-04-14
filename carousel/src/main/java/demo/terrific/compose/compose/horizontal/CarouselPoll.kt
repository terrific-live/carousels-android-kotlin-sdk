package demo.terrific.compose.compose.horizontal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import demo.terrific.compose.VideoSdk
import demo.terrific.compose.analytics.AnalyticsEvent
import demo.terrific.compose.model.PollDataDto
import demo.terrific.compose.model.analytics.AuxData

@Composable
fun PollCarouselItem(
    pollData: PollDataDto,
    assetId: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(9f / 16f)
            .clip(RoundedCornerShape(28.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFA61E2C),
                        Color(0xFF233B7B)
                    )
                )
            )
            .padding(horizontal = 24.dp, vertical = 32.dp)
            .clickable {
                onClick(assetId)
                VideoSdk.analytics().trackEvent(
                    event = AnalyticsEvent.TimelineCarouselClicked,
                    auxData = AuxData(
                        assetType = "poll",
                    )
                )
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Text(
                text = pollData.question,
                color = Color.White,
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(36.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                pollData.options.forEach { option ->
                    PollOptionStatic(text = option.text)
                }
            }
        }
    }
}

@Composable
private fun PollOptionStatic(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(74.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF1ECEF)),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            color = Color(0xFF303030),
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 28.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}