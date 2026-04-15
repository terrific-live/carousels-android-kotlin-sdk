package demo.terrific.compose.storage.likes

import android.content.Context
import androidx.core.content.edit

class SharedPrefsLikesStorage(
    context: Context
) : LikesStorage {

    private val prefs = context.getSharedPreferences(
        "video_feature_prefs",
        Context.MODE_PRIVATE
    )

    override fun getLikedVideoIds(carouselId: String): Set<String> {
        return prefs.getStringSet(key(carouselId), emptySet())?.toSet().orEmpty()
    }

    override fun toggleLike(carouselId: String, videoId: String): Set<String> {
        val current = getLikedVideoIds(carouselId).toMutableSet()

        if (current.contains(videoId)) {
            current.remove(videoId)
        } else {
            current.add(videoId)
        }

        prefs.edit {
            putStringSet(key(carouselId), current)
        }

        return current.toSet()
    }

    override fun isLiked(carouselId: String, videoId: String): Boolean {
        return getLikedVideoIds(carouselId).contains(videoId)
    }

    private fun key(carouselId: String): String = "liked_videos_$carouselId"
}