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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import demo.terrific.compose.VideoSdk
import demo.terrific.compose.analytics.AnalyticsEvent
import demo.terrific.compose.model.AssetDto
import demo.terrific.compose.model.analytics.AuxData

@Composable
fun PollCarouselItem(
    asset: AssetDto,
    assetId: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {


        AsyncImage(
            model = asset.background?.imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        val backgroundModifier = if (asset.background == null) {
            Modifier.background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFA61E2C),
                        Color(0xFF233B7B)
                    )
                )
            )
        } else {
            Modifier
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .aspectRatio(9f / 16f)
                .clip(RoundedCornerShape(28.dp))
                .then(backgroundModifier)
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .clickable {
                    onClick(assetId)
                    VideoSdk.analytics().trackEvent(
                        event = AnalyticsEvent.TimelineCarouselClicked,
                        auxData = AuxData(
                            assetType = "poll",
                        )
                    )
                },
            contentAlignment = Alignment.Center
        ) {


            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                asset.pollData?.question?.let {
                    Text(
                        text = it,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    asset.pollData?.options?.forEach { option ->
                        PollOptionStatic(text = option.text)
                    }
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
            .height(48.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF1ECEF)),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            color = Color(0xFF303030),
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 28.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}