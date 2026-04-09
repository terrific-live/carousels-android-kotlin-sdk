package demo.terrific.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import demo.terrific.compose.model.AssetDto
import demo.terrific.repository.VideoRepository
import demo.terrific.storage.LocalStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    val repository: VideoRepository,
    private val storage: LocalStorage
) : ViewModel() {

    private val _videos = MutableStateFlow<List<AssetDto>>(emptyList())
    val videos = _videos.asStateFlow()

    private val _likedVideos = MutableStateFlow(storage.getLikedVideos())
    val likedVideos = _likedVideos.asStateFlow()

    private val _isMuted = MutableStateFlow(storage.isMuted())
    val isMuted = _isMuted.asStateFlow()

    fun toggleLike(videoId: String) {

        val current = _likedVideos.value.toMutableSet()

        if (current.contains(videoId)) {
            current.remove(videoId)
        } else {
            current.add(videoId)
        }

        _likedVideos.value = current
        storage.saveLikedVideos(current)
    }

    fun toggleMute() {

        val newValue = !_isMuted.value

        _isMuted.value = newValue
        storage.setMuted(newValue)
    }

    fun loadFeed() {

        viewModelScope.launch {

            val response = repository.loadVerticalAssets()

            _videos.value = response
        }
    }

}