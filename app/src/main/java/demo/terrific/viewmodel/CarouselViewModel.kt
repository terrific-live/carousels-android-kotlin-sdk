package demo.terrific.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import demo.terrific.model.AssetDto
import demo.terrific.repository.VideoRepository
import demo.terrific.state.CarouselUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarouselViewModel @Inject constructor(
    private val repository: VideoRepository
) : ViewModel() {

    private val _assets = MutableStateFlow<List<AssetDto>>(emptyList())
    val assets: StateFlow<List<AssetDto>> = _assets

    init {
        loadAssets()
    }

    private fun loadAssets() {
        viewModelScope.launch {
            _assets.value = repository.loadAssets()
        }
    }
}