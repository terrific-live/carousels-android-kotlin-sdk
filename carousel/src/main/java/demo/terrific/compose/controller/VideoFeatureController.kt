package demo.terrific.compose.controller

import demo.terrific.compose.VideoSdk
import demo.terrific.compose.analytics.AnalyticsEvent
import demo.terrific.compose.compose.common.VideoScreen
import demo.terrific.compose.model.AssetDto
import demo.terrific.compose.model.CarouselConfigDto
import demo.terrific.compose.model.PollOptionDto
import demo.terrific.compose.model.analytics.AuxData
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
            }.onSuccess { resp ->
                val restoredAssets = resp.assets.map { asset ->
                    val pollData = asset.pollData

                    if (pollData != null) {
                        val savedPollState = pollStorage.getSavedPollState(pollData.questionId)

                        if (savedPollState != null) {
                            val restoredOptions = pollData.options.map { option ->
                                val savedOption = savedPollState.options.firstOrNull { saved ->
                                    saved.text == option.text
                                }

                                if (savedOption != null) {
                                    option.copy(numberOfVotes = savedOption.numberOfVotes)
                                } else {
                                    option
                                }
                            }

                            asset.copy(
                                pollData = pollData.copy(
                                    options = restoredOptions
                                )
                            )
                        } else {
                            asset
                        }
                    } else {
                        asset
                    }
                }

                val restoredPollAnswers = restoredAssets
                    .mapNotNull { asset ->
                        val pollData = asset.pollData ?: return@mapNotNull null
                        val savedPollState = pollStorage.getSavedPollState(pollData.questionId)

                        if (savedPollState != null) {
                            pollData.questionId to savedPollState.selectedOptionText
                        } else {
                            null
                        }
                    }
                    .toMap()

                val likedVideos = likesStorage.getLikedVideoIds(carouselId)

                _state.update {
                    it.copy(
                        isLoading = false,
                        assets = restoredAssets,
                        likedVideoIds = likedVideos,
                        selectedPollAnswers = restoredPollAnswers,
                        timestampFormat = resp.carouselConfig.timestampFormat
                    )
                }

                VideoSdk.analytics().trackEvent(
                    AnalyticsEvent.TimelineCarouselLoaded,
                    AuxData(
                        assets = resp.assets
                    )
                )
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
        val updatedAssets = _state.value.assets.map { asset ->
            val pollData = asset.pollData
            if (pollData?.questionId == questionId) {
                val updatedOptions = pollData.options.map { option ->
                    if (option.text == optionText) {
                        option.copy(numberOfVotes = option.numberOfVotes + 1)
                    } else {
                        option
                    }
                }

                pollStorage.savePollState(
                    questionId = questionId,
                    selectedOptionText = optionText,
                    options = updatedOptions
                )

                asset.copy(
                    pollData = pollData.copy(options = updatedOptions)
                )
            } else {
                asset
            }
        }

        _state.update {
            it.copy(
                assets = updatedAssets,
                selectedPollAnswers = it.selectedPollAnswers + (questionId to optionText)
            )
        }
    }
    fun onPollOptionClick(
        assetId: String,
        questionId: String,
        selectedOptionText: String
    ) {
        _state.update { currentState ->

            val previousAnswer = currentState.selectedPollAnswers[questionId]

            if (previousAnswer == selectedOptionText) return@update currentState

            var updatedOptionsForSave: List<PollOptionDto> = emptyList()

            val updatedAssets = currentState.assets.map { asset ->
                if (asset.id != assetId) return@map asset

                val poll = asset.pollData ?: return@map asset

                val updatedOptions = poll.options.map { option ->
                    when (option.text) {
                        previousAnswer -> option.copy(
                            numberOfVotes = (option.numberOfVotes - 1).coerceAtLeast(0)
                        )

                        selectedOptionText -> option.copy(
                            numberOfVotes = option.numberOfVotes + 1
                        )

                        else -> option
                    }
                }

                updatedOptionsForSave = updatedOptions

                asset.copy(
                    pollData = poll.copy(options = updatedOptions)
                )
            }

            val updatedSelectedAnswers =
                currentState.selectedPollAnswers.toMutableMap().apply {
                    this[questionId] = selectedOptionText
                }

            pollStorage.savePollState(questionId, selectedOptionText, updatedOptionsForSave)

            currentState.copy(
                assets = updatedAssets,
                selectedPollAnswers = updatedSelectedAnswers
            )
        }
    }
}

internal data class VideoFeatureState(
    val isLoading: Boolean = false,
    val assets: List<AssetDto> = emptyList(),
    val configDto: CarouselConfigDto? = null,
    val likedVideoIds: Set<String> = emptySet(),
    val selectedId: String = "",
    val screen: VideoScreen = VideoScreen.Carousel,
    val error: String? = null,
    val selectedPollAnswers: Map<String, String> = emptyMap(),
    val timestampFormat: String? = ""
)

