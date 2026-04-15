@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package demo.terrific.compose

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import demo.terrific.model.AssetDto
import demo.terrific.viewmodel.FeedViewModel

//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun VerticalScreen(
//    viewModel: FeedViewModel,
//    videoId: String) {
//    val context = LocalContext.current
//
//
//    val videos by viewModel.videos.collectAsState()
//
////    val pagerState = rememberPagerState(
////        initialPage = state.currentIndex
////    ) { state.videos.size }
//
//    val players = remember {
////        state.videos.map { video ->
//            ExoPlayer.Builder(context).build().apply {
//                state?.media?.videoPreviewUrl?.let { setMediaItem(MediaItem.fromUri(it)) }
//                prepare()
//                playWhenReady = false
//                repeatMode = Player.REPEAT_MODE_ONE
//            }
////        }
//    }
//
//    // 🔥 Autoplay
////    LaunchedEffect(pagerState.currentPage) {
////        players.forEachIndexed { index, player ->
////            player.playWhenReady = index == pagerState.currentPage
////        }
////    }
//
////    VerticalPager(
////        state = pagerState,
////        modifier = Modifier.fillMaxSize()
////    ) { page ->
////        FullscreenVideoItem(player = players[page])
////    }
////
////    DisposableEffect(Unit) {
////        onDispose {
////            players.forEach { it.release() }
////        }
////    }
//}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalScreen(
    viewModel: FeedViewModel,
    videoId: String,
    navController: NavController
) {
    val videos by viewModel.videos.collectAsState()

    HideSystemBars()

    LaunchedEffect(Unit) {
        viewModel.loadFeed()
    }

    if (videos.isEmpty()) {
        CircularProgressIndicator()
        return
    }

    val startIndex = videos.indexOfFirst { it.id == videoId }

    val pagerState = rememberPagerState(
        initialPage = if (startIndex >= 0) startIndex else 0,
        pageCount = { videos.size }
    )


    VerticalPager(
        state = pagerState
    ) { page ->

        val video = videos[page]

        FullscreenVideoScreen(video, viewModel, navController)
    }
}

@Composable
fun FullscreenVideoScreen(
    video: AssetDto,
    viewModel: FeedViewModel,
    navController: NavController
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // VIDEO
        FullscreenVideoPlayer(video)
        // OTHER UI
        VideoOverlay(video, viewModel, navController)
    }
}


@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun FullscreenVideoPlayer(video: AssetDto) {

    val context = LocalContext.current


    val player = remember {

        ExoPlayer.Builder(context).build().apply {

            video.media?.videoPreviewUrl?.let { setMediaItem(MediaItem.fromUri(it)) }
            prepare()
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    DisposableEffect(Unit) {
        onDispose { player.release() }
    }

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
    }
}

@Composable
fun HideSystemBars() {

    val activity = LocalContext.current as Activity

    DisposableEffect(Unit) {

        val controller = WindowCompat.getInsetsController(
            activity.window,
            activity.window.decorView
        )

        controller.hide(WindowInsetsCompat.Type.statusBars())

        onDispose {
            controller.show(WindowInsetsCompat.Type.statusBars())
        }
    }
}

@Composable
fun VideoOverlay(
    video: AssetDto,
    viewModel: FeedViewModel,
    navController: NavController
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .zIndex(1f)
    ) {

        // CLOSE BUTTON
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close")
        }



        // DATE
        Text(
            text = video.timestamp ?: "",
            modifier = Modifier
                .align(Alignment.TopStart)
                .background(Color.White.copy(alpha = 0.8f))
                .padding(6.dp)
        )

        val likedVideos by viewModel.likedVideos.collectAsState()
        val isMuted by viewModel.isMuted.collectAsState()

        val isLiked = likedVideos.contains(video.id)

        // RIGHT ACTIONS
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            IconButton(onClick = { viewModel.toggleLike(video.id) }) {
                Icon(imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (isLiked) Color.Red else Color.White)
            }

            Spacer(Modifier.height(12.dp))

            val context = LocalContext.current

            IconButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, video.media?.mobileUrl)
                    }
                    context.startActivity(Intent.createChooser(intent, "Share"))
                }
            ) {
                Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
            }


            Spacer(Modifier.height(12.dp))

            IconButton(
                onClick = { viewModel.toggleMute() }
            ) {
                Icon(
                    imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                    contentDescription = "Mute",
                    tint = Color.White
                )
            }
        }

        // TITLE + DESCRIPTION
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(end = 80.dp)
        ) {

            Text(
                text = video.title ?: "",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = video.description ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }
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
