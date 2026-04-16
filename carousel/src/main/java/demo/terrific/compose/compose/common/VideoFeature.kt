package demo.terrific.compose.compose.common

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import demo.terrific.compose.VideoSdk
import demo.terrific.compose.compose.horizontal.VideoCarousel
import demo.terrific.compose.compose.vertical.VerticalScreen
import demo.terrific.compose.controller.VideoFeatureController
import demo.terrific.compose.style.VideoFeatureStyle

@Composable
fun AssetCarousel(
    storeId: String,
    carouselId: String,
    modifier: Modifier = Modifier,
    style: VideoFeatureStyle = VideoFeatureStyle()
) {
    val controller = rememberVideoFeatureController(storeId = storeId)
    val state by controller.state.collectAsState()

    LaunchedEffect(storeId) {
        controller.load(storeId, carouselId)
    }

    when {
        state.isLoading -> {
            Box(modifier = modifier.fillMaxSize()) {
//                CircularProgressIndicator()
            }
        }

        state.error != null -> {
//            Box(modifier = modifier.fillMaxSize()) {
//                Text(text = state.error ?: "Unknown error")
//            }
        }

        state.screen is VideoScreen.Carousel -> {
            VideoCarousel(
                assets = state.assets,
                config = state.configDto,
                style = style,
                onVideoClick = controller::onVideoClick,
                onProductClick = controller::onProductClick
            )
        }

        state.screen is VideoScreen.Feed -> {
            val selectedVideoId = state.assets
                .find { state.selectedId == it.id }
                ?.id
                .orEmpty()

            VerticalScreen(
                assets = state.assets,
                likedVideos = state.likedVideoIds,
                selectedPollAnswers = state.selectedPollAnswers,
                videoId = selectedVideoId,
                onLikeClick = { id ->
                    controller.onLikeClick(id)
                },
                onPollOptionClick = controller::onPollOptionClick,
                onBackClicked = controller::onBack,
                onProductClick = controller::onProductClick
            )
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

    return remember(repository, likesStorage, pollStorage,scope) {
        VideoFeatureController(
            repository = repository,
            likesStorage = likesStorage,
            pollStorage = pollStorage,
            scope = scope
        )
    }
}