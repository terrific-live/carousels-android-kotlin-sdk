package demo.terrific.compose.controller

import demo.terrific.compose.compose.VideoScreen
import demo.terrific.compose.model.AssetDto
import demo.terrific.compose.repository.VideoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class VideoFeatureController(
    private val repository: VideoRepository,
    private val scope: CoroutineScope
) {
    private val _state = MutableStateFlow(VideoFeatureState())
    val state: StateFlow<VideoFeatureState> = _state

    fun load(
        storeId: String,
        carouselId: String
        ) {
        scope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            runCatching {
                repository.getFeed(storeId, carouselId)
            }.onSuccess { feed ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        assets = feed
                    )
                }
            }.onFailure { throwable ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message
                    )
                }
            }
        }
    }

    fun onVideoClick(id: String) {
        _state.update {
            it.copy(
                screen = VideoScreen.Feed,
                selectedId = id
            )
        }
    }

    fun onBack() {
        _state.update { it.copy(screen = VideoScreen.Carousel) }
    }

    fun onLikeClick(videoId: String) {

    }
}

internal data class VideoFeatureState(
    val isLoading: Boolean = false,
    val assets: List<AssetDto> = emptyList(),
//    val likedVideoIds: Set<String> = emptySet(),
    val selectedId: String = "",
    val screen: VideoScreen = VideoScreen.Carousel,
    val error: String? = null
)

