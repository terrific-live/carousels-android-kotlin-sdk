package demo.terrific.compose.storage.likes

interface LikesStorage {
    fun getLikedVideoIds(carouselId: String): Set<String>
    fun toggleLike(carouselId: String, videoId: String): Set<String>
    fun isLiked(carouselId: String, videoId: String): Boolean
}