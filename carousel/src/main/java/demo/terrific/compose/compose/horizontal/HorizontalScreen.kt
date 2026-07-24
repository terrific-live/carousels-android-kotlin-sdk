@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package demo.terrific.compose.compose.horizontal

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import demo.terrific.compose.VideoSdk
import demo.terrific.compose.analytics.AnalyticsEvent
import demo.terrific.compose.compose.common.DateTimeBadgeCarousel
import demo.terrific.compose.compose.common.toFormatted
import demo.terrific.compose.model.AssetDto
import demo.terrific.compose.model.AssetType
import demo.terrific.compose.model.CarouselConfigDto
import demo.terrific.compose.model.analytics.AuxData
import demo.terrific.compose.style.VideoFeatureStyle
import demo.terrific.compose.style.withSdkFont
import kotlinx.coroutines.delay

@SuppressLint("FrequentlyChangingValue")
@Composable
fun VideoCarousel(
    assets: List<AssetDto>,
    timestampFormat: String?,
    style: VideoFeatureStyle,
    onVideoClick: (String) -> Unit
) {
    val pagerState = rememberPagerState(
        pageCount = { assets.size }
    )

    LaunchedEffect(pagerState.currentPage, assets) {

        VideoSdk.analytics().trackTimelineAssetViewStarted(
            assetType = "video",
            position = pagerState.currentPage,
            fixedPosition = pagerState.currentPage,
            emptyList(),
            emptyList()
        )
    }

    LaunchedEffect(assets.size) {
        if (assets.size <= 1) return@LaunchedEffect

        while (true) {
            delay(2_000)

            if (!pagerState.isScrollInProgress) {
                val nextPage =
                    if (pagerState.currentPage == assets.lastIndex) {
                        0
                    } else {
                        pagerState.currentPage + 1
                    }

                // pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 32.dp),
        pageSpacing = 8.dp,
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        beyondViewportPageCount = 1
    ) { page ->
        val asset = assets[page]
        val hasProducts = asset.products?.isNotEmpty() == true

        val shouldPrepareVideo =
            asset.type == AssetType.VIDEO.type &&
                    kotlin.math.abs(page - pagerState.currentPage) <= 1


        val isCurrentPage =
            page == pagerState.currentPage &&
                    !pagerState.isScrollInProgress

        Column(
            modifier = Modifier
                .height(style.carouselHeight)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(9f / 16f)
                    .clip(
                        RoundedCornerShape(
                            style.cornerRadius
                        )
                    )
            ) {
                when (asset.type) {
                    AssetType.POLL.type -> {
                        PollCarouselItem(
                            asset = asset,
                            timestampFormat = timestampFormat,
                            assetId = asset.id,
                            onClick = onVideoClick,
                            modifier = Modifier.fillMaxSize(),
                            style = style
                        )
                    }

                    AssetType.VIDEO.type -> {
                        VideoCard(
                            video = asset,
                            timestampFormat = timestampFormat,
                            shouldPrepare = shouldPrepareVideo,
                            isActive = isCurrentPage,
                            onVideoClick = onVideoClick,
                            textBottomPadding =
                                if (hasProducts) 68.dp else 20.dp,
                            style = style
                        )
                    }

                    AssetType.IMAGE.type -> {
                        CarouselImage(
                            asset = asset,
                            timestampFormat = timestampFormat,
                            onVideoClick = onVideoClick,
                            style = style
                        )
                    }
                }
            }

            if (hasProducts) {
                Spacer(
                    modifier = Modifier.height(
                        style.productSpacing
                    )
                )

                TimelineProductsRowCarousel(
                    products = asset.products,
                    modifier = Modifier.fillMaxWidth(),
                    style = style
                )
            }
        }
    }
}

@Composable
fun VideoCard(
    video: AssetDto,
    timestampFormat: String?,
    modifier: Modifier = Modifier,
    shouldPrepare: Boolean,
    isActive: Boolean,
    onVideoClick: (String) -> Unit,
    textBottomPadding: Dp = 68.dp,
    style: VideoFeatureStyle
) {
    val context = LocalContext.current
    val videoUrl = video.media?.videoPreviewUrl

    if (!shouldPrepare || videoUrl.isNullOrBlank()) {
        VideoCardContent(
            video = video,
            timestampFormat = timestampFormat,
            modifier = modifier,
            player = null,
            onVideoClick = onVideoClick,
            textBottomPadding = textBottomPadding,
            style = style
        )

        return
    }

    val player = remember(video.id, videoUrl) {
        ExoPlayer.Builder(context.applicationContext)
            .build()
            .apply {
                setMediaItem(MediaItem.fromUri(videoUrl))
                repeatMode = Player.REPEAT_MODE_ONE
                playWhenReady = false
                prepare()
            }
    }

    LaunchedEffect(player, isActive) {
        if (isActive) {
            player.playWhenReady = true
            player.play()
        } else {
            player.playWhenReady = false
            player.pause()
        }
    }

    DisposableEffect(player) {
        onDispose {
            player.playWhenReady = false
            player.stop()
            player.release()
        }
    }

    VideoCardContent(
        video = video,
        timestampFormat = timestampFormat,
        modifier = modifier,
        player = player,
        onVideoClick = onVideoClick,
        textBottomPadding = textBottomPadding,
        style = style
    )
}

@Composable
private fun VideoCardContent(
    video: AssetDto,
    timestampFormat: String?,
    modifier: Modifier,
    player: ExoPlayer?,
    onVideoClick: (String) -> Unit,
    textBottomPadding: Dp,
    style: VideoFeatureStyle
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
        if (player != null) {
            AndroidView(
                factory = { viewContext ->
                    PlayerView(viewContext).apply {
                        this.player = player
                        useController = false
                    }
                },
                update = { playerView ->
                    if (playerView.player !== player) {
                        playerView.player = player
                    }
                },
                onRelease = { playerView ->
                    playerView.player = null
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            /*
             * Тут бажано показати preview image,
             * thumbnail або background asset.
             */
            video.background?.imageUrl?.let { previewUrl ->
                AsyncImage(
                    model = previewUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
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

        val formatted = remember(
            video.timestamp,
            timestampFormat
        ) {
            timestampFormat?.let {
                video.timestamp?.toFormatted(it)
            }
        }

        if (!formatted.isNullOrEmpty()) {
            DateTimeBadgeCarousel(
                text = formatted,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            )
        }

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
            video.title?.let { title ->
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 24.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = style.subtitleTextStyle.withSdkFont(
                        style.fontFamily
                    )
                )
            }

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            video.description?.let { description ->
                Text(
                    text = description,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 24.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = style.bodyTextStyle.withSdkFont(
                        style.fontFamily
                    )
                )
            }
        }
    }
}