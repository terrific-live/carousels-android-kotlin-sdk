package demo.terrific.compose.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import demo.terrific.compose.VideoSdk
import demo.terrific.compose.controller.VideoFeatureController
import demo.terrific.compose.style.VideoFeatureStyle

@Composable
fun AssetCarousel(
    storeId: String,
    carouselId: String,
    modifier: Modifier = Modifier,
    style: VideoFeatureStyle = VideoFeatureStyle()
) {
    val controller = rememberVideoFeatureController()
    val state by controller.state.collectAsState()

    LaunchedEffect(storeId) {
        controller.load(storeId, carouselId)
    }

    when {
        state.isLoading -> {
            Box(modifier = modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }

        state.error != null -> {
            Box(modifier = modifier.fillMaxSize()) {
                Text(text = state.error ?: "Unknown error")
            }
        }

        state.screen is VideoScreen.Carousel -> {
            VideoCarousel(
                assets = state.assets,
                style = style,
                onVideoClick = controller::onVideoClick
            )
        }

        state.screen is VideoScreen.Feed -> {
            val selectedVideoId = state.assets
                .find { state.selectedId == it.id }/*(state.selectedId)*/
                ?.id
                .orEmpty()

            VerticalScreen(
                assets = state.assets,
                videoId = selectedVideoId,
                style = style,
                onLikeClick = { id ->
                    controller.onLikeClick(id)
                }/*,
                onBack = controller::onBack*/
            )
        }
    }
}

@Composable
internal fun rememberVideoFeatureController(): VideoFeatureController {
    val scope = rememberCoroutineScope()
    val repository = remember { VideoSdk.repository() }

    return remember(repository, scope) {
        VideoFeatureController(
            repository = repository,
            scope = scope
        )
    }
}