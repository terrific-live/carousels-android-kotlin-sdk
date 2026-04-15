package demo.terrific.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import demo.terrific.repository.VideoRepository
import demo.terrific.state.FeedUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    repository: VideoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val startIndex: Int =
        savedStateHandle["startIndex"] ?: 0

    private val _uiState = MutableStateFlow(
        FeedUiState(
            videos = repository.getVideos(),
            currentIndex = startIndex
        )
    )

    val uiState: StateFlow<FeedUiState> = _uiState
}