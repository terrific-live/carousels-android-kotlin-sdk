package demo.terrific.state

import demo.terrific.model.VideoItem

data class FeedUiState(
    val videos: List<VideoItem>,
    val currentIndex: Int
)