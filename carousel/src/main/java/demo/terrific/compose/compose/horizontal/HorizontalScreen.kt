@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package demo.terrific.compose.compose.horizontal

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import demo.terrific.compose.VideoSdk
import demo.terrific.compose.analytics.AnalyticsEvent
import demo.terrific.compose.compose.TimelineProductCard
import demo.terrific.compose.model.AssetDto
import demo.terrific.compose.model.CarouselConfigDto
import demo.terrific.compose.model.analytics.AuxData
import demo.terrific.compose.style.VideoFeatureStyle
import kotlin.math.absoluteValue

@SuppressLint("FrequentlyChangingValue")
@Composable
fun VideoCarousel(
    assets: List<AssetDto>,
    config: CarouselConfigDto?,
    style: VideoFeatureStyle,
    onVideoClick: (String) -> Unit,
    onProductClick: (String) -> Unit
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { assets.size })

    val players = remember(assets) {
        assets.map { asset ->
            ExoPlayer.Builder(context).build().apply {
                asset.media?.videoPreviewUrl?.let { url ->
                    setMediaItem(MediaItem.fromUri(url))
                }
                prepare()
                playWhenReady = false
                repeatMode = Player.REPEAT_MODE_ONE
            }
        }
    }

    LaunchedEffect(pagerState.currentPage, players) {
        players.forEachIndexed { index, player ->
            player.playWhenReady = index == pagerState.currentPage
        }
    }

    DisposableEffect(players) {
        onDispose {
            players.forEach { it.release() }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = style.pagerHorizontalPadding),
            pageSpacing = style.pagerPageSpacing,
            modifier = Modifier
                .fillMaxWidth()
                .height(style.pageHeight)
        ) { page ->
            val asset = assets[page]
            val firstProduct = asset.products?.firstOrNull()

            val pageOffset =
                ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction)
                    .absoluteValue

            val scale = lerp(
                start = 0.9f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            )

            val productHeight = style.pageHeight * style.productHeightFraction
            val hasProduct = firstProduct != null

            val assetHeight = if (hasProduct) {
                style.pageHeight - productHeight - style.productSpacing
            } else {
                style.pageHeight
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                config?.name?.let {
                    Text(
                        text = it,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 24.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(assetHeight)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .clip(RoundedCornerShape(style.cornerRadius))
                ) {
                    when {
                        asset.pollData != null -> {
                            PollCarouselItem(
                                pollData = asset.pollData,
                                assetId = asset.id,
                                onClick = onVideoClick,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        asset.media != null -> {
                            VideoCard(
                                video = asset,
                                modifier = Modifier.fillMaxSize().height(assetHeight),
                                player = players[page],
                                onVideoClick = onVideoClick,
                                textBottomPadding = if (hasProduct) 68.dp else 20.dp
                            )
                        }

                    }
                }

                if (firstProduct != null) {
                    Spacer(modifier = Modifier.height(style.productSpacing))

                    TimelineProductCard(
                        product = firstProduct,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(productHeight),
                        onClick = onProductClick
                    )
                }
            }
        }
    }
}

@Composable
fun VideoCard(
    video: AssetDto,
    modifier: Modifier = Modifier,
    player: ExoPlayer,
    onVideoClick: (String) -> Unit,
    textBottomPadding: Dp = 68.dp
) {
    Box(
        modifier = modifier
            .aspectRatio(9f / 16f)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Black)
            .clickable {
                onVideoClick(video.id)
                VideoSdk.analytics().trackEvent(
                    event = AnalyticsEvent.TimelineCarouselClicked,
                    auxData = AuxData(assetType = "video")
                )
            }
    ) {
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    this.player = player
                    useController = false
                }
            },
            modifier = Modifier.fillMaxSize()
        )

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
            video.title?.let {
                Text(
                    text = it,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 24.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
    }
}