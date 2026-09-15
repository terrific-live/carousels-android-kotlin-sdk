@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package demo.terrific.compose.compose.vertical

import android.content.Context
import android.content.Intent
import androidx.annotation.OptIn
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import demo.terrific.R
import demo.terrific.compose.VideoSdk
import demo.terrific.compose.analytics.TimelineEvent
import demo.terrific.compose.compose.common.DateTimeBadge
import demo.terrific.compose.compose.common.SwipeHintOverlay
import demo.terrific.compose.compose.common.VideoProgressBar
import demo.terrific.compose.compose.common.sharePayload
import demo.terrific.compose.compose.common.rememberIsLifecycleResumed
import demo.terrific.compose.compose.common.shouldPreloadVideo
import demo.terrific.compose.compose.common.toFormatted
import demo.terrific.compose.compose.horizontal.toComposeColorOrNullSafe
import demo.terrific.compose.model.AssetDto
import demo.terrific.compose.model.AssetType
import demo.terrific.compose.model.SponsorshipDto
import demo.terrific.compose.style.VideoFeatureStyle
import demo.terrific.compose.style.withSdkFont
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun VerticalScreen(
    assets: List<AssetDto>,
    timestampFormat: String?,
    onPollOptionClick: (assetId: String, questionId: String, optionText: String) -> Unit,
    likedVideos: Set<String>,
    selectedPollAnswers: Map<String, String>,
    videoId: String,
    onLikeClick: (String) -> Unit,
    onBackClicked: () -> Unit,
    sponsorship: SponsorshipDto?,
    style: VideoFeatureStyle
) {
    val startIndex = remember(assets, videoId) {
        assets.indexOfFirst { it.id == videoId }.takeIf { it >= 0 } ?: 0
    }

    val pagerState = rememberPagerState(
        initialPage = startIndex,
        pageCount = { assets.size }
    )
    val isLifecycleResumed = rememberIsLifecycleResumed()

    var hasShownSwipeHint by rememberSaveable {
        mutableStateOf(false)
    }

    var isMuted by rememberSaveable {
        mutableStateOf(false)
    }

    var activeAssetIndex by remember {
        mutableIntStateOf(pagerState.settledPage)
    }

    var assetViewStartedAt by remember {
        mutableLongStateOf(System.currentTimeMillis())
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }
            .distinctUntilChanged()
            .collect { newPage ->

                val previousPage = activeAssetIndex

                if (newPage == previousPage) {
                    return@collect
                }

                val previousAsset = assets.getOrNull(previousPage)
                val now = System.currentTimeMillis()

                previousAsset?.let { asset ->
                    VideoSdk.analytics.sendEvent(
                        TimelineEvent.TimelineAssetViewEndedEvent(
                            assetType = asset.type,
                            parentUrl = "",
                            netoAssetWatchTimeMs = now - assetViewStartedAt,
                            viewDurationMs = now - assetViewStartedAt,
                            drawerOpenDurationMs = 0,
                            position = asset.position,
                            customProducts = emptyList(),
                            products = emptyList(),
                            assetId = asset.id
                        )
                    )
                }

                activeAssetIndex = newPage
                assetViewStartedAt = now
            }
    }


    VerticalPager(
        state = pagerState,
        beyondViewportPageCount = 1,
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) { page ->
        val asset = assets[page]

        VerticalScreenPage(
            asset = asset,
            timestampFormat = timestampFormat,
            isLiked = asset.id in likedVideos,
            isActive = pagerState.settledPage == page &&
                    !pagerState.isScrollInProgress,
            isLifecycleResumed = isLifecycleResumed,
            shouldPrepareVideo = shouldPreloadVideo(
                page = page,
                currentPage = pagerState.currentPage,
                isVideo = asset.type == AssetType.VIDEO.type,
                isLifecycleResumed = isLifecycleResumed
            ),
            isMuted = isMuted,
            onMuteToggle = {
                isMuted = !isMuted
            },
            showSwipeHint = !hasShownSwipeHint && page == pagerState.settledPage,
            onSwipeHintFinished = {
                hasShownSwipeHint = true
            },
            selectedPollAnswer = asset.pollData?.questionId?.let(selectedPollAnswers::get),
            onLikeClick = onLikeClick,
            onPollOptionClick = onPollOptionClick,
            sponsorship = sponsorship,
            onBackClicked = onBackClicked,
            style = style
        )
    }
}

@Composable
private fun VerticalScreenPage(
    asset: AssetDto,
    timestampFormat: String?,
    isLiked: Boolean,
    isActive: Boolean,
    isLifecycleResumed: Boolean,
    shouldPrepareVideo: Boolean,
    isMuted: Boolean,
    onMuteToggle: () -> Unit,
    showSwipeHint: Boolean,
    onSwipeHintFinished: () -> Unit,
    selectedPollAnswer: String?,
    onLikeClick: (String) -> Unit,
    onPollOptionClick: (assetId: String, questionId: String, optionText: String) -> Unit,
    onBackClicked: () -> Unit,
    sponsorship: SponsorshipDto?,
    style: VideoFeatureStyle
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
                    sponsorship = sponsorship,
                    onOptionClick = { optionText ->
                        onPollOptionClick(asset.id, poll.questionId, optionText)
                    },
                    style = style
                )
            }
        }

        AssetType.VIDEO.type -> {
            PreloadedFullscreenVideoPlayer(
                video = asset,
                timestampFormat = timestampFormat,
                isLiked = isLiked,
                isActive = isActive,
                isLifecycleResumed = isLifecycleResumed,
                shouldPrepare = shouldPrepareVideo,
                isMuted = isMuted,
                onMuteToggle = onMuteToggle,
                onLikeClick = { onLikeClick(asset.id) },
                onBackClicked = onBackClicked,
                style = style,
                onSwipeHintFinished = onSwipeHintFinished,
                sponsorship = sponsorship,
                showSwipeHint = showSwipeHint
            )
        }

        AssetType.IMAGE.type -> {
            asset.media?.mobileUrl?.let {
                ImageAsset(
                    asset = asset,
                    timestampFormat = timestampFormat,
                    isLiked = isLiked,
                    onLikeClick = { onLikeClick(asset.id) },
                    onBackClicked = onBackClicked,
                    style = style
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

@OptIn(UnstableApi::class)
@Composable
fun FullscreenVideoPlayer(
    video: AssetDto,
    timestampFormat: String?,
    isLiked: Boolean,
    isActive: Boolean,
    onSwipeHintFinished: () -> Unit,
    onLikeClick: (String) -> Unit,
    onBackClicked: () -> Unit,
    style: VideoFeatureStyle,
    sponsorship: SponsorshipDto?,
    showSwipeHint: Boolean
) {
    val isLifecycleResumed = rememberIsLifecycleResumed()
    var isMuted by rememberSaveable(video.id) {
        mutableStateOf(false)
    }

    PreloadedFullscreenVideoPlayer(
        video = video,
        timestampFormat = timestampFormat,
        isLiked = isLiked,
        isActive = isActive,
        isLifecycleResumed = isLifecycleResumed,
        shouldPrepare = true,
        isMuted = isMuted,
        onMuteToggle = { isMuted = !isMuted },
        onSwipeHintFinished = onSwipeHintFinished,
        onLikeClick = onLikeClick,
        onBackClicked = onBackClicked,
        style = style,
        sponsorship = sponsorship,
        showSwipeHint = showSwipeHint
    )
}

@Composable
private fun PreloadedFullscreenVideoPlayer(
    video: AssetDto,
    timestampFormat: String?,
    isLiked: Boolean,
    isActive: Boolean,
    isLifecycleResumed: Boolean,
    shouldPrepare: Boolean,
    isMuted: Boolean,
    onMuteToggle: () -> Unit,
    onSwipeHintFinished: () -> Unit,
    onLikeClick: (String) -> Unit,
    onBackClicked: () -> Unit,
    style: VideoFeatureStyle,
    sponsorship: SponsorshipDto?,
    showSwipeHint: Boolean
) {
    val context = LocalContext.current
    val videoUrl = video.media?.mobileUrl?.takeIf { it.isNotBlank() }

    if (!shouldPrepare || !isLifecycleResumed || videoUrl == null) {
        FullscreenVideoPlayerContent(
            video = video,
            timestampFormat = timestampFormat,
            isLiked = isLiked,
            isActive = isActive,
            player = null,
            isMuted = isMuted,
            onMuteToggle = onMuteToggle,
            onSwipeHintFinished = onSwipeHintFinished,
            onLikeClick = onLikeClick,
            onBackClicked = onBackClicked,
            style = style,
            sponsorship = sponsorship,
            showSwipeHint = showSwipeHint
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

    DisposableEffect(player) {
        onDispose {
            player.pause()
            player.release()
        }
    }

    LaunchedEffect(player, isActive) {
        if (!isActive) {
            player.pause()
            return@LaunchedEffect
        }
        player.play()
    }

    LaunchedEffect(player, isMuted) {
        player.volume = if (isMuted) 0f else 1f
    }

    FullscreenVideoPlayerContent(
        video = video,
        timestampFormat = timestampFormat,
        isLiked = isLiked,
        isActive = isActive,
        player = player,
        isMuted = isMuted,
        onMuteToggle = onMuteToggle,
        onSwipeHintFinished = onSwipeHintFinished,
        onLikeClick = onLikeClick,
        onBackClicked = onBackClicked,
        style = style,
        sponsorship = sponsorship,
        showSwipeHint = showSwipeHint
    )
}

@OptIn(UnstableApi::class)
@Composable
private fun FullscreenVideoPlayerContent(
    video: AssetDto,
    timestampFormat: String?,
    isLiked: Boolean,
    isActive: Boolean,
    player: ExoPlayer?,
    isMuted: Boolean,
    onMuteToggle: () -> Unit,
    onSwipeHintFinished: () -> Unit,
    onLikeClick: (String) -> Unit,
    onBackClicked: () -> Unit,
    style: VideoFeatureStyle,
    sponsorship: SponsorshipDto?,
    showSwipeHint: Boolean
) {
    val context = LocalContext.current
    val products = video.products.orEmpty()
    val hasProducts = products.isNotEmpty()
    var progress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(video.id, isActive) {
        if (!isActive) return@LaunchedEffect

        VideoSdk.analytics.sendEvent(
            event = TimelineEvent.TimelineAssetViewStartedEvent(
                assetType = video.type,
                parentUrl = "",
                fixedPosition = video.position,
                position = video.position,
                products = emptyList(),
                customProducts = emptyList(),
                assetId = video.id
            )
        )
    }

    var isLoading by remember(video.id) {
        mutableStateOf(true)
    }

    var errorMessage by remember(video.id) {
        mutableStateOf<String?>(null)
    }

    DisposableEffect(player, video.id) {
        if (player == null) {
            return@DisposableEffect onDispose { }
        }

        isLoading = player.playbackState != Player.STATE_READY
        errorMessage = player.playerError?.message

        val listener = object : Player.Listener {

            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        isLoading = true
                    }

                    Player.STATE_READY -> {
                        isLoading = false
                        errorMessage = null
                    }

                    Player.STATE_ENDED,
                    Player.STATE_IDLE -> {
                        isLoading = false
                    }
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                isLoading = false
                errorMessage = error.message ?: "Video loading error"
            }
        }

        player.addListener(listener)

        onDispose {
            player.removeListener(listener)
        }
    }

    LaunchedEffect(player, isActive) {
        while (isActive && player != null) {
            val duration = player.duration
            val position = player.currentPosition

            progress = if (duration > 0) {
                (position.toFloat() / duration.toFloat()).coerceIn(0f, 1f)
            } else {
                0f
            }

            delay(200)
        }

        progress = 0f
    }


    Box(modifier = Modifier.fillMaxSize()) {

        video.background?.let {
            AsyncImage(
                model = it.imageUrl,
                contentDescription = "background",
                modifier = Modifier
                    .fillMaxSize(),
//                            .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
//                    .aspectRatio(9f / 16f)
            ) {

                if (player != null && (isLoading || errorMessage != null)) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 3.dp
                        )
                    }
                }


                val sponsorshipBanner = sponsorship
                    ?.takeIf { it.enabled && it.adPlacementType == "banner" }
                    ?.banner

                val bannerPosition = sponsorshipBanner?.position?.lowercase()

                val showTopSponsorBanner =
                    sponsorshipBanner != null &&
                            (bannerPosition == "top" || bannerPosition == "top-bottom")

                val showBottomSponsorBanner =
                    sponsorshipBanner != null &&
                            (bannerPosition == "bottom" || bannerPosition == "top-bottom")


                val videoContainerModifier = if (video.background != null) {
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                } else {
                    Modifier.fillMaxSize()
                }

                Box(
                    modifier = videoContainerModifier
                ) {
                    if (player != null) {
                        AndroidView(
                            factory = {
                                PlayerView(it).apply {
                                    this.player = player
                                    useController = false
                                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                                }
                            },
                            update = {
                                it.player = player
                            },
                            onRelease = {
                                it.player = null
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        AsyncImage(
                            model = video.media?.coverUrl ?: video.background?.imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }

                    if (showTopSponsorBanner) {
                        SponsorshipBanner(
                            banner = sponsorshipBanner,
                            modifier = Modifier.align(Alignment.TopCenter),
                            onClick = { url ->
                                openUrl(context, url)
                            }
                        )
                    }

                    if (showBottomSponsorBanner) {
                        SponsorshipBanner(
                            banner = sponsorshipBanner,
                            modifier = Modifier.align(Alignment.BottomCenter),
                            onClick = { url ->
                                openUrl(context, url)
                            }
                        )
                    }

                    sponsorship?.badge?.let {
                        val alignment = when (it.position) {
                            "top-left" -> {
                                Alignment.TopStart
                            }
                            "top-center" -> {
                                Alignment.TopCenter
                            }
                            else -> {
                                Alignment.TopEnd
                            }
                        }
                        SponsorshipBadge(
                            title = it.title,
                            logoUrl = it.logoUrl,
                            link = it.clickRedirect,
                            backgroundColor = sponsorship.badge.backgroundColor?.toComposeColorOrNullSafe()
                                ?: Color(0xFFF96544),
                            modifier = Modifier
                                .align(alignment),
                            style = style,
                            onClick = { url ->
                                openUrl(context, url)
                            }

                        )
                    }
                    VideoOverlayContent(
                        video = video,
                        timestampFormat = timestampFormat,
                        isLiked = isLiked,
                        onLikeClick = onLikeClick,
                        onBackClicked = onBackClicked,
                        player = player,
                        isMuted = isMuted,
                        onMuteToggle = onMuteToggle,
                        style = style
                    )

                    VideoProgressBar(
                        progress = progress,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(
                                start = 24.dp,
                                end = 24.dp,
                                bottom = if (showBottomSponsorBanner) {
                                    48.dp
                                } else {
                                    0.dp
                                }
                            ),
                        height = 8.dp,
                        trackColor = Color.White.copy(alpha = 0.28f),
                        progressColor = Color.White
                    )

                    if (showSwipeHint) {
                        SwipeHintOverlay(
                            modifier = Modifier.align(Alignment.Center),
                            onFinished = onSwipeHintFinished
                        )
                    }
                }
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
                        .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 16.dp)
                ) {
                    TimelineProductsRow(
                        products = products,
                        style = style
                    )
                }
            }
        }
    }
}

@Composable
fun VideoOverlay(
    video: AssetDto,
    timestampFormat: String?,
    isLiked: Boolean,
    onLikeClick: (String) -> Unit,
    onBackClicked: () -> Unit,
    player: ExoPlayer,
    style: VideoFeatureStyle
) {
    var isMuted by remember(video.id, player) {
        mutableStateOf(player.volume == 0f)
    }

    VideoOverlayContent(
        video = video,
        timestampFormat = timestampFormat,
        isLiked = isLiked,
        onLikeClick = onLikeClick,
        onBackClicked = onBackClicked,
        player = player,
        isMuted = isMuted,
        onMuteToggle = {
            isMuted = !isMuted
            player.volume = if (isMuted) 0f else 1f
        },
        style = style
    )
}

@Composable
private fun VideoOverlayContent(
    video: AssetDto,
    timestampFormat: String?,
    isLiked: Boolean,
    onLikeClick: (String) -> Unit,
    onBackClicked: () -> Unit,
    player: ExoPlayer?,
    isMuted: Boolean,
    onMuteToggle: () -> Unit,
    style: VideoFeatureStyle
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 32.dp, end = 16.dp, top = 48.dp, bottom = 72.dp)
            .zIndex(1f)
    ) {

        // CLOSE BUTTON
        IconButton(
            onClick = {
                onBackClicked()
            },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = "Close",
                tint = Color.White
            )
        }

        val formatted = remember(video.timestamp) {
            timestampFormat?.let { video.timestamp?.toFormatted(it) }
        }

        if (formatted?.isNotEmpty() == true) {
            DateTimeBadge(formatted)
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            IconButton(onClick = {
                VideoSdk.analytics.sendEvent(
                    TimelineEvent.TimelineAssetLikedEvent(
                        parentUrl = "",
                        customProducts = emptyList(),
                        position = video.position,
                        assetId = video.id
                    )
                )
                onLikeClick(video.id)
            }) {
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
            val sharePayload = remember(video) {
                video.sharePayload()
            }

            if (sharePayload != null) {
                IconButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, sharePayload.intentText)
                        }

                        runCatching {
                            context.startActivity(Intent.createChooser(intent, "Share"))
                        }.onSuccess {
                            VideoSdk.analytics.sendEvent(
                                TimelineEvent.TimelineAssetSharedEvent(
                                    parentUrl = sharePayload.url.orEmpty(),
                                    customProducts = emptyList(),
                                    position = video.position,
                                    assetId = video.id
                                )
                            )
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_share),
                        contentDescription = "Share",
                        tint = Color.White
                    )
                }

                Spacer(Modifier.height(12.dp))
            }

            IconButton(
                enabled = player != null,
                onClick = {
                    onMuteToggle()
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
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 24.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = style.mainTitleTextStyle.withSdkFont(style.fontFamily)
                )
            }

            Spacer(Modifier.height(6.dp))

            video.description?.let {
                Text(
                    text = it,
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 24.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = style.subtitleTextStyle.withSdkFont(style.fontFamily)
                )
            }
        }
    }
}

fun openUrl(
    context: Context,
    url: String
) {
    runCatching {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}
