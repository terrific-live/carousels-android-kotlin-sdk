package demo.terrific.compose.compose.horizontal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import demo.terrific.compose.VideoSdk
import demo.terrific.compose.analytics.AnalyticsEvent
import demo.terrific.compose.compose.common.DateTimeBadgeCarousel
import demo.terrific.compose.compose.common.toFormatted
import demo.terrific.compose.model.AssetDto
import demo.terrific.compose.model.analytics.AuxData
import demo.terrific.compose.style.VideoFeatureStyle
import demo.terrific.compose.style.withSdkFont

@Composable
fun CarouselImage(
    asset: AssetDto,
    timestampFormat: String?,
    modifier: Modifier = Modifier,
    onVideoClick: (String) -> Unit,
    textBottomPadding: Dp = 68.dp,
    style: VideoFeatureStyle
) {
    Box(
        modifier = modifier
            .aspectRatio(9f / 16f)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Black)
            .clickable {
                onVideoClick(asset.id)
                VideoSdk.analytics().trackEvent(
                    event = AnalyticsEvent.TimelineCarouselClicked,
                    auxData = AuxData(assetType = "video")
                )
            }
    ) {
        AsyncImage(
            model = asset.media?.mobileUrl,
            contentDescription = "image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        val formatted = remember(asset.timestamp) {
            timestampFormat?.let { asset.timestamp?.toFormatted(it) }
        }

        if (formatted?.isNotEmpty() == true) {
            DateTimeBadgeCarousel(
                text = formatted,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.25f),
                            Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = textBottomPadding
                )
        ) {
            asset.title?.let {
                Text(
                    text = it,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 24.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = style.subtitleTextStyle.withSdkFont(style.fontFamily)
                )
            }

        }
    }
}