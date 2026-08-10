package demo.terrific.compose.compose.common

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import demo.terrific.compose.VideoSdk
import demo.terrific.compose.compose.horizontal.VideoCarousel
import demo.terrific.compose.compose.vertical.VerticalScreen
import demo.terrific.compose.controller.VideoFeatureController
import demo.terrific.compose.style.VideoFeatureStyle
import demo.terrific.compose.style.withSdkFont

@Composable
fun AssetCarousel(
    storeId: String,
    carouselId: String,
    modifier: Modifier = Modifier,
    style: VideoFeatureStyle = VideoFeatureStyle()
) {
    val controller = rememberVideoFeatureController(storeId = storeId)
    val state by controller.state.collectAsState()

    LaunchedEffect(storeId, carouselId) {
        controller.load(storeId, carouselId)
    }

    when {
        state.isLoading -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(style.carouselHeight)
            )
        }

        state.error != null -> {
            VideoErrorScreen(
                modifier = modifier
                    .fillMaxWidth()
                    .height(style.carouselHeight),
                onRetryClick = controller::retry,
                onCloseClick = controller::onBack
            )
        }

        state.screen is VideoScreen.Carousel -> {

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = state.configDto?.name.orEmpty(),
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 32.dp,
                    bottom = 0.dp
                ),
                fontWeight = FontWeight.SemiBold,
                style = style.titleTextStyle.withSdkFont(style.fontFamily)
            )

            VideoCarousel(
                assets = state.assets,
                timestampFormat = state.timestampFormat,
                config = state.configDto,
                style = style,
                onVideoClick = controller::onVideoClick
            )
        }

        state.screen is VideoScreen.Feed -> {
            val selectedVideoId = state.assets
                .find { state.selectedId == it.id }
                ?.id
                .orEmpty()

            Dialog(
                onDismissRequest = controller::onBack,
                properties = DialogProperties(
                    usePlatformDefaultWidth = false,
                    decorFitsSystemWindows = false
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    VerticalScreen(
                        assets = state.assets,
                        timestampFormat = state.timestampFormat,
                        likedVideos = state.likedVideoIds,
                        selectedPollAnswers = state.selectedPollAnswers,
                        videoId = selectedVideoId,
                        onLikeClick = controller::onLikeClick,
                        onPollOptionClick = controller::onPollOptionClick,
                        onBackClicked = controller::onBack,
                        sponsorship = state.configDto?.sponsorship,
                        style = style
                    )
                }
            }
        }
    }
}

@SuppressLint("RememberReturnType")
@Composable
internal fun rememberVideoFeatureController(storeId: String): VideoFeatureController {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    remember(context) {
        VideoSdk.ensureInitialized(context, storeId)
    }

    val repository = remember { VideoSdk.repository() }
    val likesStorage = remember { VideoSdk.likesStorage() }
    val pollStorage = remember { VideoSdk.pollStorage() }

    return remember(
        storeId,
        repository,
        likesStorage,
        pollStorage,
        scope
    ) {
        VideoFeatureController(
            repository = repository,
            likesStorage = likesStorage,
            pollStorage = pollStorage,
            scope = scope
        )
    }
}