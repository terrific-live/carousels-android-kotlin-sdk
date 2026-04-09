package demo.terrific.compose.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import demo.terrific.compose.model.AssetDto
import demo.terrific.compose.model.VideoItem
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VideoCarousel(
//    viewModel: CarouselViewModel,
    assets: List<AssetDto>,
    onVideoClick: (String) -> Unit
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState { assets.size }


    if (assets.isEmpty()) {
        CircularProgressIndicator()
        return
    }

    val players = remember {
        assets.map { video ->
            ExoPlayer.Builder(context).build().apply {
                video.media?.videoPreviewUrl?.let { setMediaItem(MediaItem.fromUri(it)) }
                prepare()
                playWhenReady = false
                repeatMode = Player.REPEAT_MODE_ONE
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 48.dp),
            pageSpacing = 16.dp,
            modifier = Modifier
                .fillMaxSize()
        ) { page ->

            val pageOffset =
                ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction)
                    .absoluteValue

            val scale = lerp(
                start = 0.9f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            )

//            if (state[page].question.isNullOrEmpty()) {
            VideoCard(
                video = assets[page],
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    },
                player = players[page],
                onVideoClick = onVideoClick
            )
//            } else {
//                HorizontalPollItem(
//                    video = state.videos[page],
//                    modifier = Modifier
//                        .graphicsLayer {
//                            scaleX = scale
//                            scaleY = scale
//                        },
//                    player = players[page],
//                    onVideoClick = onVideoClick
//                )
//            }

        }
    }

    LaunchedEffect(pagerState.currentPage) {
        players.forEachIndexed { index, player ->
            player.playWhenReady = index == pagerState.currentPage
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            players.forEach { it.release() }
        }
    }

}

@Composable
private fun HorizontalPollItem(
    video: VideoItem,
    modifier: Modifier = Modifier,
    player: ExoPlayer,
    onVideoClick: (Int) -> Unit
) {

    // Створюємо "коротке відео" як MediaItem
    val mediaItem = MediaItem.fromUri(
        "android.resource://\${context.packageName}/raw/black".toUri() // маленький mp4
    )

    DisposableEffect(Unit) {
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true

        onDispose { player.release() }
    }

    // Таймер, щоб закінчити відтворення через durationSeconds
    LaunchedEffect(Unit) {
        delay(3 * 1000L)
    }

    Box(
        modifier = modifier
            .aspectRatio(9f / 16f)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Black)
            .clickable {
                onVideoClick(video.id) // 🔥
            }
    ) {
        // Картинка поверх відео
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Blue.copy(alpha = 0.4f),
                            Color.Yellow.copy(alpha = 0.3f)
                        ),
                        startY = 0f,
                        endY = 600f
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize(), // займає весь екран
            contentAlignment = Alignment.Center // центрування по горизонталі та вертикалі
        ) {
            // Кнопки голосування поверх фону
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp), // відступи між кнопками
                horizontalAlignment = Alignment.CenterHorizontally // центр по горизонталі
            ) {

                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 0.dp),
                    text = video.question ?: "",
                    fontSize = 24.sp,
                    color = Color.White
                )

                video.options?.forEach { option ->
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .size(width = 160.dp, height = 40.dp), // прямокутна форма
                        shape = RoundedCornerShape(8.dp), // легке заокруглення
                    ) {
                        Text(option.answerText)
                    }
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
    onVideoClick: (String) -> Unit
) {
    Box(
        modifier = modifier
            .aspectRatio(9f / 16f)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Black)
            .clickable {
                onVideoClick(video.id) // 🔥
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
    }
}