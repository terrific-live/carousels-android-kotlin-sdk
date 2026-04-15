package demo.terrific.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import demo.terrific.viewmodel.FeedViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalScreen(viewModel: FeedViewModel) {
    val context = LocalContext.current

    val state by viewModel.uiState.collectAsState()

    val pagerState = rememberPagerState(
        initialPage = state.currentIndex
    ) { state.videos.size }

    val players = remember {
        state.videos.map { video ->
            ExoPlayer.Builder(context).build().apply {
                setMediaItem(MediaItem.fromUri(video.videoUrl))
                prepare()
                playWhenReady = false
                repeatMode = Player.REPEAT_MODE_ONE
            }
        }
    }

    // 🔥 Autoplay
    LaunchedEffect(pagerState.currentPage) {
        players.forEachIndexed { index, player ->
            player.playWhenReady = index == pagerState.currentPage
        }
    }

    VerticalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        FullscreenVideoItem(player = players[page])
    }

    DisposableEffect(Unit) {
        onDispose {
            players.forEach { it.release() }
        }
    }
}

@Composable
fun VerticalPollItem() {

}

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun FullscreenVideoItem(player: ExoPlayer?) {

    var liked by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    this.player = player
                    useController = false
                    resizeMode =
                        AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        ActionButtons(
            onLikeClick = { liked = !liked },
            onShareClick = { /* логіка шарінгу */ },
            onCloseClick = { /* логіка закриття */ }
        )
    }
}

@Composable
fun ActionButtons(
    onLikeClick: () -> Unit,
    onShareClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        IconButton(
            onClick = onCloseClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = MaterialTheme.colorScheme.error
            )
        }

        // Кнопки лайку та шерінгу внизу справа
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(onClick = onLikeClick) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Like",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onShareClick) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
