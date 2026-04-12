package demo.terrific.compose.controller

import demo.terrific.compose.compose.VideoScreen
import demo.terrific.compose.model.AssetDto
import demo.terrific.compose.repository.VideoRepository
import demo.terrific.compose.storage.likes.LikesStorage
import demo.terrific.compose.storage.storage.PollStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class VideoFeatureController(
    private val repository: VideoRepository,
    private val likesStorage: LikesStorage,
    private val pollStorage: PollStorage,
    private val scope: CoroutineScope
) {
    private val _state = MutableStateFlow(VideoFeatureState())
    val state: StateFlow<VideoFeatureState> = _state

    private var carouselId: String = ""

    fun load(
        storeId: String,
        carouselId: String
        ) {
        this.carouselId = carouselId
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


    fun onLikeClick(videoId: String) {
        val updatedLikes = likesStorage.toggleLike(
            carouselId = carouselId,
            videoId = videoId
        )

        _state.update {
            it.copy(likedVideoIds = updatedLikes)
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

    fun onPollOptionClick(questionId: String, optionText: String) {
        pollStorage.saveVote(questionId, optionText)

        _state.update {
            it.copy(
                selectedPollAnswers = it.selectedPollAnswers + (questionId to optionText)
            )
        }
    }
}

internal data class VideoFeatureState(
    val isLoading: Boolean = false,
    val assets: List<AssetDto> = emptyList(),
    val likedVideoIds: Set<String> = emptySet(),
    val selectedId: String = "",
    val screen: VideoScreen = VideoScreen.Carousel,
    val error: String? = null,
    val selectedPollAnswers: Map<String, String> = emptyMap()
)

