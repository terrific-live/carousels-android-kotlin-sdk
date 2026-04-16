@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package demo.terrific.compose.compose.vertical

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import demo.terrific.R
import demo.terrific.compose.model.AssetDto
import demo.terrific.compose.model.AssetType
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import kotlin.time.ExperimentalTime

@Composable
fun VerticalScreen(
    assets: List<AssetDto>,
    onPollOptionClick: (questionId: String, optionText: String) -> Unit,
    likedVideos: Set<String>,
    selectedPollAnswers: Map<String, String>,
    videoId: String,
    onLikeClick: (String) -> Unit,
    onBackClicked: () -> Unit,
    onProductClick: (String) -> Unit
) {
    HideSystemBars()

    val startIndex = remember(assets, videoId) {
        assets.indexOfFirst { it.id == videoId }.takeIf { it >= 0 } ?: 0
    }

    val pagerState = rememberPagerState(
        initialPage = startIndex,
        pageCount = { assets.size }
    )

    VerticalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        val asset = assets[page]

        VerticalScreenPage(
            asset = asset,
            isLiked = asset.id in likedVideos,
            selectedPollAnswer = asset.pollData?.questionId?.let(selectedPollAnswers::get),
            onLikeClick = onLikeClick,
            onPollOptionClick = onPollOptionClick,
            onBackClicked = onBackClicked,
            onProductClick = onProductClick
        )
    }
}

@Composable
private fun VerticalScreenPage(
    asset: AssetDto,
    isLiked: Boolean,
    selectedPollAnswer: String?,
    onLikeClick: (String) -> Unit,
    onPollOptionClick: (questionId: String, optionText: String) -> Unit,
    onBackClicked: () -> Unit,
    onProductClick: (String) -> Unit,
) {
    when (asset.type) {
        AssetType.POLL.type -> {
            asset.pollData?.let { poll ->
                PollScreen(
                    asset = asset,
                    isLiked = isLiked,
                    onLikeClick = { onLikeClick(asset.id) },
                    onBackClicked = onBackClicked,
                    selectedOptionText = selectedPollAnswer,
                    onOptionClick = { optionText ->
                        onPollOptionClick(poll.questionId, optionText)
                    }
                )
            }
        }

        AssetType.VIDEO.type -> {
            FullscreenVideoPlayer(
                video = asset,
                isLiked = isLiked,
                onLikeClick = { onLikeClick(asset.id) },
                onBackClicked = onBackClicked,
                onProductClick = onProductClick
            )
        }

        AssetType.IMAGE.type -> {
            asset.media?.mobileUrl?.let {
                ImageAsset(
                    asset = asset,
                    isLiked = isLiked,
                    onLikeClick = { onLikeClick(asset.id) },
                    onBackClicked = onBackClicked,
                    onProductClick = onProductClick
                )
            }
        }

        else -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Unsupported asset")
            }
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun FullscreenVideoPlayer(
    video: AssetDto,
    isLiked: Boolean,
    onLikeClick: (String) -> Unit,
    onBackClicked: () -> Unit,
    onProductClick: (String) -> Unit
) {
    val context = LocalContext.current
    val products = video.products.orEmpty()
    val hasProducts = products.isNotEmpty()
    var progress by remember { mutableFloatStateOf(0f) }

    val player = remember(video.id) {
        ExoPlayer.Builder(context).build().apply {
            video.media?.mobileUrl?.let { setMediaItem(MediaItem.fromUri(it)) }
            prepare()
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    DisposableEffect(player) {
        onDispose { player.release() }
    }

    LaunchedEffect(player) {
        while (true) {
            val duration = player.duration
            val position = player.currentPosition

            progress = if (duration > 0) {
                (position.toFloat() / duration.toFloat()).coerceIn(0f, 1f)
            } else {
                0f
            }

            delay(200)
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
//                    .aspectRatio(9f / 16f)
            ) {

                AndroidView(
                    factory = {
                        PlayerView(it).apply {
                            this.player = player
                            useController = false
                            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                VideoOverlay(
                    video = video,
                    isLiked = isLiked,
                    onLikeClick = onLikeClick,
                    onBackClicked = onBackClicked,
                    player = player
                )

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(4.dp)
                        .height(6.dp),
                )
            }

            if (hasProducts) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.35f)
                                )
                            )
                        )
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                ) {
                    TimelineProductsRow(
                        products = products,
                        onProductClick = onProductClick
                    )
                }
            }
        }
    }
}

@Composable
fun VideoOverlay(
    video: AssetDto,
    isLiked: Boolean,
    onLikeClick: (String) -> Unit,
    onBackClicked: () -> Unit,
    player: ExoPlayer
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 32.dp, horizontal = 32.dp)
            .zIndex(1f)
    ) {

        // CLOSE BUTTON
        IconButton(
            onClick = { onBackClicked() },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
        }


        val formatted = remember(video.timestamp) {
            video.timestamp?.toFormatted()
        }

        // DATE

        if (formatted?.isNotEmpty() == true) {
            Text(
                text = formatted,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .background(Color.White.copy(alpha = 0.8f))
                    .padding(6.dp)
            )
        }

        var isMuted by remember { mutableStateOf(false) }

        // RIGHT ACTIONS
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            IconButton(onClick = { onLikeClick(video.id) }) {
                Icon(
                    imageVector = if (isLiked) {
                        Icons.Filled.ThumbUp
                    } else {
                        Icons.Outlined.ThumbUp
                    },
                    contentDescription = "Like",
                    tint = Color.White
                )
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
                Icon(
                    painter = painterResource(R.drawable.ic_share),
                    contentDescription = "Share",
                    tint = Color.White
                )
            }


            Spacer(Modifier.height(12.dp))

            IconButton(
                onClick = {
                    isMuted = !isMuted
                    player.volume = if (isMuted) 0f else 1f
                }
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

            Spacer(Modifier.height(6.dp))

            video.description?.let {
                Text(
                    text = it,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 24.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
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

@OptIn(ExperimentalTime::class)
fun String.toFormatted(): String {
    return try {
        val inputFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.getDefault()
        ).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

        val outputFormat = SimpleDateFormat(
            "dd/MM/yyyy - HH'h'mm",
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
