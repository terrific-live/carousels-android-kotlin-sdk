package demo.terrific.compose.compose

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import demo.terrific.compose.model.AssetDto
import demo.terrific.compose.model.PollDataDto
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalScreen(
    assets: List<AssetDto>,
    onPollOptionClick: (questionId: String, optionText: String) -> Unit,
    likedVideos: Set<String>,
    videoId: String,
    onLikeClick: (String) -> Unit,
    onBackClicked: () -> Unit,
) {
    HideSystemBars()

    if (assets.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val startIndex = remember(assets, videoId) {
        val index = assets.indexOfFirst { it.id == videoId }
        if (index >= 0) index else 0
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
        val isLiked = likedVideos.contains(asset.id)

        VerticalScreenPage(
            asset = asset,
            isLiked = isLiked,
            onLikeClick = onLikeClick,
            onPollOptionClick = onPollOptionClick,
            onBackClicked = onBackClicked
        )
    }
}

@Composable
private fun VerticalScreenPage(
    asset: AssetDto,
    isLiked: Boolean,
    onLikeClick: (String) -> Unit,
    onPollOptionClick: (questionId: String, optionText: String) -> Unit,
    onBackClicked: () -> Unit,
) {
    when {
        asset.pollData != null -> {
            PollScreen(
                pollData = asset.pollData,
                isLiked = isLiked,
                onLikeClick = { onLikeClick(asset.id) },
                onBackClicked = onBackClicked,
                onPollOptionClick = onPollOptionClick
            )
        }

        asset.media != null -> {
            FullscreenVideoScreen(
                video = asset,
                likedVideoIds = if (isLiked) setOf(asset.id) else emptySet(),
                onLikeClick = onLikeClick,
                onBackClicked = onBackClicked
            )
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

@Composable
fun PollScreen(
    pollData: PollDataDto,
    isLiked: Boolean,
    onBackClicked: () -> Unit,
    onLikeClick: () -> Unit,
    onPollOptionClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF6D500),
                        Color(0xFFE9B684),
                        Color(0xFF9A7680)
                    )
                )
            )
    ) {
        IconButton(
            onClick = onBackClicked,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            PollQuestionSection(
                question = pollData.question
            )

            Spacer(modifier = Modifier.height(28.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                pollData.options.forEach { option ->
                    PollOptionButton(
                        text = option.text,
                        onClick = {
                            onPollOptionClick(pollData.id, option.text) }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        PollActionsColumn(
            liked = isLiked,
            onLikeClick = onLikeClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 140.dp)
        )

        PollBottomBranding(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 28.dp)
        )
    }
}

@Composable
private fun PollActionsColumn(
    liked: Boolean,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = onLikeClick) {
            Icon(
                imageVector = if (liked) Icons.Default.Favorite else Icons.Outlined.ThumbUp,
                contentDescription = "Like",
                tint = Color.White
            )
        }

        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Send,
                contentDescription = "Share",
                tint = Color.White
            )
        }
    }
}

@Composable
private fun PollBottomBranding(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.86f)
                .height(10.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(Color.White.copy(alpha = 0.28f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.78f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color(0xFF1FB2FF))
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "POWERED BY ",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 12.sp
            )
            Text(
                text = "terrific",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PollQuestionSection(
    question: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFFECECEC),
            shadowElevation = 4.dp
        ) {
            Box(
                modifier = Modifier.size(64.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.BarChart,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = question,
            color = Color.White,
            fontSize = 24.sp,
//            fontStyle = FontStyle.FONT_WEIGHT_SEMI_BOLD,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PollOptionButton(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFF0F0F0),
        shadowElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(82.dp)
                .padding(horizontal = 22.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = text,
                color = Color(0xFF1C1C1C),
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun FullscreenVideoScreen(
    video: AssetDto,
    likedVideoIds: Set<String>,
    onLikeClick: (String) -> Unit,
    onBackClicked: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // VIDEO
        FullscreenVideoPlayer(video, likedVideoIds, onLikeClick, onBackClicked)
        // OTHER UI

    }
}


@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun FullscreenVideoPlayer(video: AssetDto,
                          likedVideoIds: Set<String>,
                          onLikeClick: (String) -> Unit,
                          onBackClicked: () -> Unit) {

    val context = LocalContext.current


    val player = remember {

        ExoPlayer.Builder(context).build().apply {
            video.media?.mobileUrl?.let { setMediaItem(MediaItem.fromUri(it)) }
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

    VideoOverlay(video, likedVideoIds, onLikeClick, onBackClicked = onBackClicked, player)
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
    likedVideoIds: Set<String>,
    onLikeClick: (String) -> Unit,
    onBackClicked: () -> Unit,
    player: ExoPlayer
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .zIndex(1f)
    ) {

        // CLOSE BUTTON
        IconButton(
            onClick = { onBackClicked() },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close")
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

        val isLiked = likedVideoIds.contains(video.id)

        // RIGHT ACTIONS
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            IconButton(onClick = { onLikeClick(video.id) }) {
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
    } catch (e: Exception) {
        this
    }
}
