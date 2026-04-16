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
import demo.terrific.compose.VideoSdk
import demo.terrific.compose.analytics.AnalyticsEvent
import demo.terrific.compose.compose.TimelineProductCard
import demo.terrific.compose.model.AssetDto
import demo.terrific.compose.model.AssetType
import demo.terrific.compose.model.CarouselConfigDto
import demo.terrific.compose.model.analytics.AuxData
import demo.terrific.compose.style.VideoFeatureStyle

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

//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        HorizontalPager(
//            state = pagerState,
//            contentPadding = PaddingValues(horizontal = style.pagerHorizontalPadding),
//            pageSpacing = style.pagerPageSpacing,
//            modifier = Modifier
//                .fillMaxSize()
//                .height(style.pageHeight)
//        ) { page ->

//    Column() {
//
//        config?.name?.let {
//            Text(
//                text = it,
//                color = Color.White,
//                fontSize = 20.sp,
//                fontWeight = FontWeight.SemiBold,
//                lineHeight = 24.sp,
//                maxLines = 3,
//                overflow = TextOverflow.Ellipsis
//            )
//        }
//

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 32.dp),
        pageSpacing = 8.dp,
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) { page ->
        val asset = assets[page]
        val firstProduct = asset.products?.firstOrNull()

        val productHeight = style.pageHeight * style.productHeightFraction
        val hasProduct = firstProduct != null

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
                    .clip(RoundedCornerShape(style.cornerRadius))
            ) {

                when (asset.type) {
                    AssetType.POLL.type -> {
                        PollCarouselItem(
                            asset = asset,
                            assetId = asset.id,
                            onClick = onVideoClick,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    AssetType.VIDEO.type -> {
                        VideoCard(
                            video = asset,
                            player = players[page],
                            onVideoClick = onVideoClick,
                            textBottomPadding = if (hasProduct) 68.dp else 20.dp
                        )
                    }

                    AssetType.IMAGE.type -> {
                        CarouselImage(
                            asset = asset,
                            onVideoClick = onVideoClick
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