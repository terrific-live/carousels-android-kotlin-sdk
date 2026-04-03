package demo.terrific.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import demo.terrific.repository.VideoRepository
import demo.terrific.state.CarouselUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CarouselViewModel @Inject constructor(
    private val repository: VideoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CarouselUiState(videos = emptyList())
    )
    val uiState: StateFlow<CarouselUiState> = _uiState

    init {
        loadVideos()
    }

    private fun loadVideos() {
        _uiState.value = CarouselUiState(
            videos = repository.getVideos()
        )
    }
}